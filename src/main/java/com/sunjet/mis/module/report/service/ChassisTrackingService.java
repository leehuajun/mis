package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.repository.ChassisTrackingRepository;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.ProductionPlanTrackingReportRepository;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.warehouse.entity.*;
import com.sunjet.mis.module.warehouse.repository.*;
import com.sunjet.mis.module.warehouse.service.DistributionVehicleService;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.SaslServer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisTrackingService")
public class ChassisTrackingService {

    @Autowired
    private ChassisTrackingRepository chassisTrackingRepository;

    @Autowired
    private DistributionVehicleRepository distributionVehicleRepository;

    @Autowired
    private ChassisInventoryRepository chassisInventoryRepository;

    @Autowired
    private ProductionWarehouseRepository productionWarehouseRepository;

    @Autowired
    private ChassisWarehouseQingDaoRepository chassisWarehouseQingDaoRepository;

    @Autowired
    private ChassisOutboundQingDaoRepository chassisOutboundQingDaoRepository;

    @Autowired
    private RailwaySendKunMingRepository railwaySendKunMingRepository; //中铁发昆明

    @Autowired
    private RailwaySendLIUZHOURepository railwaySendLIUZHOURepository;//中铁发柳州

    @Autowired
    private ChongQingSendLiuZhouRepository chongQingSendLiuZhouRepository;//重庆发柳州

    @Autowired
    private ChassisOutboundLiuZhouRepository chassisOutboundLiuZhouRepository;

    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public PageResult<ChassisTrackingView> getPageList(PageParam<ChassisTrackingView> pageParam) {
        //1
        ChassisTrackingView  chassisTrackingView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisTrackingView> specification = null;
        Specification<ChassisTrackingView> specification2 = null;
        Specification<ChassisTrackingView> specification3 = null;
        System.out.println(chassisTrackingView.getYear() + "-" + chassisTrackingView.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (chassisTrackingView != null && StringUtils.isNotBlank(chassisTrackingView.getBatchNumber())) {
                specification2 = Specifications.<ChassisTrackingView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(chassisTrackingView.getYear()!=null, "year", chassisTrackingView.getYear())
                        .eq(chassisTrackingView.getMonth()!=null,"month", chassisTrackingView.getMonth())
                        .eq(chassisTrackingView.getVehicleModel()!=null, "vehicleModel", chassisTrackingView.getVehicleModel())
                        .eq(chassisTrackingView.getVariety()!=null,"variety", chassisTrackingView.getVariety())
                        .build();
            }
        } else {
            if (chassisTrackingView != null ) {
                specification2 = Specifications.<ChassisTrackingView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(chassisTrackingView.getYear()!=null, "year", chassisTrackingView.getYear())
                        .eq(chassisTrackingView.getMonth()!=null,"month", chassisTrackingView.getMonth())
                        .eq(chassisTrackingView.getVehicleModel()!=null, "vehicleModel", chassisTrackingView.getVehicleModel())
                        .eq(chassisTrackingView.getVariety()!=null,"variety", chassisTrackingView.getVariety())
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<ChassisTrackingView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ChassisTrackingView> pages = chassisTrackingRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        if (pages.getContent().size() > 0) {
            for(ChassisTrackingView ctv:pages.getContent()) {
                //股份已分车
                List<DistributionVehicleEntity> dvlist = distributionVehicleRepository.findByVehicleModel(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear(), ctv.getMonth());
                int amount = 0;
                for (DistributionVehicleEntity dv : dvlist) {
                    amount += dv.getAmount();
                    ctv.setSharesAlready(amount);
                }
//                int amount = 0;
//                List<DistributionVehicleEntity> disList =  distributionVehicleRepository.findAll();
//                for(DistributionVehicleEntity dv:disList){
//                    if(ctv.getVehicleModel().equals(dv.getVehicleModel()) && dv.getVariety().equals(dv.getVariety()) && ctv.getBatchNumber().equals(dv.getBatchNumber())){
//                        amount += dv.getAmount();
//                    }
//                }
//                ctv.setSharesAlready(amount);
                //股份待分车
                ctv.setSharesWaiting(ctv.getProcurementPlan() - ctv.getSharesAlready());
                //已入底盘库
                List<ChassisInventoryEntity> chlist = chassisInventoryRepository.findByVehicleModel(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
                if (chlist != null && chlist.size() > 0) {
                    ctv.setAlreadyWarehouse(chlist.size());
                }
//                int alreadyWarehouse = 0;
//                List<ChassisInventoryEntity> ciList = chassisInventoryRepository.findAll();
//                for(ChassisInventoryEntity ci:ciList){
//                    if(ctv.getVehicleModel().equals(ci.getVehicleModel()) && ctv.getVariety().equals(ci.getVariety()) && ctv.getBatchNumber().equals(ci.getBatchNumber())){
//                        alreadyWarehouse=alreadyWarehouse+1;
//                    }
//                }
//                ctv.setAlreadyWarehouse(alreadyWarehouse);

                //已分车待提 dvlist - chlist
                ctv.setWaitingExtract(ctv.getSharesAlready() - ctv.getAlreadyWarehouse());
                //重庆底盘库库存 重庆底盘入库-重庆成品入库
                List<ChassisInventoryEntity> cqinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%重庆%");
                List<ProductionWarehouseEntity> cqwarehouse = new ArrayList<>();
                if(cqinventory!=null && cqinventory.size()>0) {
                    for (ChassisInventoryEntity ci : cqinventory) {
                        ProductionWarehouseEntity pwentity = productionWarehouseRepository.findByVinAndVsn(ci.getVin(), ci.getVsn(), "%重庆%");
                        cqwarehouse.add(pwentity);
                    }
                }
                if (cqinventory != null && cqinventory.size() > 0 && cqwarehouse.size() > 0) {
                    ctv.setCHONGQINGInventory(cqinventory.size() - cqwarehouse.size());
                }
//                int cqsum = 0;
//                List<ProductionWarehouseEntity> cqwarehouselist = new ArrayList<>();
//                List<ChassisInventoryEntity> cqList = chassisInventoryRepository.findAll();
//                for(ChassisInventoryEntity ci:cqList){
//                    if(ctv.getVehicleModel().equals(ci.getVehicleModel()) && ctv.getVariety().equals(ci.getVariety()) && ctv.getBatchNumber().equals(ci.getBatchNumber()) && ctv.getYear()==Integer.parseInt(ci.getShipmentData().substring(0,4)) && ctv.getMonth()==Integer.parseInt(ci.getShipmentData().substring(4,6)) && ci.getDataSource().equals("重庆底盘入库")){
//                        cqsum=cqsum+1;
//                        ProductionWarehouseEntity pwentity = productionWarehouseRepository.findByVinAndVsn(ci.getVin(), ci.getVsn(), "%重庆%");
//                        cqwarehouselist.add(pwentity);
//                    }
//                }
//                if (cqwarehouselist != null && cqwarehouselist.size() > 0 && cqsum > 0) {
//                    ctv.setCHONGQINGInventory(cqsum - cqwarehouselist.size());
//                }



                //昆明前置库库存
                List<RailwaySendKunMingEntity> kunMingEntities = railwaySendKunMingRepository.findAll();
                List<AcceptanceDetailEntity> allList = acceptanceDetailRepository.findByVehicleModel(ctv.getVehicleModel(),ctv.getVariety(),ctv.getBatchNumber(),ctv.getYear().toString(),ctv.getMonth().toString());
                List<RailwaySendKunMingEntity> kunMingsum = new ArrayList<>();
                for(AcceptanceDetailEntity ci:allList){
                    for(RailwaySendKunMingEntity km:kunMingEntities){
                        if(ci.getVin().equals(km.getVin())){
                            kunMingsum.add(km);
                        }
                    }
                }
                ctv.setKUNMINGInventory(kunMingsum.size());

//                List<RailwaySendKunMingEntity> railwaySendKunMingEntities = railwaySendKunMingRepository.findAll();
//                List<AcceptanceDetailEntity> adList = acceptanceDetailRepository.findAll();
//                List<RailwaySendKunMingEntity> kunMingList = new ArrayList<>();
//                List<AcceptanceDetailEntity> allList = new ArrayList<>();
//                for(AcceptanceDetailEntity ad:adList){
//                    if(ctv.getVehicleModel().equals(ad.getVehicleModel()) && ctv.getVariety().equals(ad.getVariety()) && ctv.getBatchNumber().equals(ad.getBatchNumber()) && ctv.getYear()==Integer.parseInt(ad.getShipmentData().substring(0,4)) &&  ctv.getMonth()==Integer.parseInt(ad.getShipmentData().substring(4,6))) {
//                        allList.add(ad);
//                        for(RailwaySendKunMingEntity km:railwaySendKunMingEntities){
//                            if(ad.getVin().equals(km.getVin())){
//                                kunMingList.add(km);
//                            }
//                        }
//                    }
//                }
//                ctv.setKUNMINGInventory(kunMingList.size());

                //青岛底盘库存
                List<ChassisInventoryEntity> qdinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%青岛%");
                List<ChassisWarehouseQingDaoEntity> qdwarehouse = new ArrayList<>();
                if(qdinventory!=null && qdinventory.size()>0) {
                    for (ChassisInventoryEntity ci : qdinventory) {
                        ChassisWarehouseQingDaoEntity cwentity = chassisWarehouseQingDaoRepository.findByVinAndVsn(ci.getVin(), ci.getVsn(), "%千业物流（青岛底盘）%");
                        qdwarehouse.add(cwentity);
                    }
                    List<ChassisOutboundQINGDAOEntity> qdoutlist = new ArrayList<>();
                    if(qdwarehouse!=null && qdwarehouse.size()>0) {
                        for (ChassisWarehouseQingDaoEntity qd : qdwarehouse) {
                            if(qd!=null && StringUtils.isNotBlank(qd.getVin()) && StringUtils.isNotBlank(qd.getVsn())) {
                                ChassisOutboundQINGDAOEntity qdout = chassisOutboundQingDaoRepository.findByVinAndVsn(qd.getVin(), qd.getVsn(), "%千业物流（青岛底盘）%");
                                qdoutlist.add(qdout);
                            }
                        }
                    }
                    ctv.setQINGDAOInventory(qdwarehouse.size() - qdoutlist.size());
                }
                //发柳州在途 数据=青岛底盘出库（柳州用底盘）+中铁发运回柳州+重庆发运回柳州-柳州底盘入库
                //青岛出库
                List<ChassisOutboundQINGDAOEntity> qingdaooutlist = chassisOutboundQingDaoRepository.findBVehicleModel(ctv.getVehicleModel(),ctv.getBatchNumber(),ctv.getVariety(),"%千业物流（回柳底盘）%",ctv.getYear().toString(),ctv.getMonth().toString());
                //中铁发柳州
                List<RailwaySendLIUZHOUEntity> liuzhouEntities = railwaySendLIUZHOURepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
                //重庆发柳州
                List<ChongQingSendLiuZhouEntity> chongQingSendLiuZhouEntities = chongQingSendLiuZhouRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
                //柳州入库
                List<ChassisInventoryEntity> LIUZHOUinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%柳州底盘入库%");
                List<ChassisInventoryEntity> sendLIUZHOU = new ArrayList<>();
                for(ChassisInventoryEntity lz:LIUZHOUinventory){
                    //青岛出库
                    for(ChassisOutboundQINGDAOEntity qd:qingdaooutlist){
                        if(lz.getVin().equals(qd.getVin())){
                            sendLIUZHOU.add(lz);
                        }
                    }
                    //中铁发柳州
                    for(RailwaySendLIUZHOUEntity zt:liuzhouEntities){
                        if(lz.getVin().equals(zt.getVin())){
                            sendLIUZHOU.add(lz);
                        }
                    }
                    //重庆发柳州
                    for(ChongQingSendLiuZhouEntity cq:chongQingSendLiuZhouEntities){
                        if(lz.getVin().equals(cq.getVin())){
                            sendLIUZHOU.add(lz);
                        }
                    }
//                    ctv.setLIUZHOUOnTheWay(qingdaooutlist.size()+liuzhouEntities.size()+chongQingSendLiuZhouEntities.size()-sendLIUZHOU.size());
                    ctv.setLIUZHOUOnTheWay(sendLIUZHOU.size()-LIUZHOUinventory.size());
                }
                //青岛缓冲区 青岛底盘出库-成品入库
                List<ChassisOutboundQINGDAOEntity> qingdao = chassisOutboundQingDaoRepository.findBVehicleModel(ctv.getVehicleModel(),ctv.getBatchNumber(),ctv.getVariety(),"%千业物流（青岛底盘）%",ctv.getYear().toString(),ctv.getMonth().toString());
                List<ProductionWarehouseEntity> pwlist = productionWarehouseRepository.findByInboundDate(ctv.getYear().toString(),ctv.getMonth().toString());
                List<ChassisOutboundQINGDAOEntity> buffer = new ArrayList<>();
                for(ChassisOutboundQINGDAOEntity out:qingdao){
                    for(ProductionWarehouseEntity pw:pwlist){
                        if(out.getVin().equals(pw.getVin())){
                            buffer.add(out);
                        }
                    }
                }
                ctv.setQINGDAOBufferInventory(qingdao.size()-buffer.size());
                //柳州底盘库 柳州底盘入库-柳州底盘出库
//                List<ChassisInventoryEntity> LIUZHOUinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%柳州底盘入库%");
                List<ChassisOutboundLiuZhouEntity> lzlist =  chassisOutboundLiuZhouRepository.findByStandingTime(ctv.getYear().toString(),ctv.getMonth().toString());
                List<ChassisInventoryEntity> liuzhoulist = new ArrayList<>();
                for(ChassisInventoryEntity lz:LIUZHOUinventory){
                    for(ChassisOutboundLiuZhouEntity out:lzlist){
                        if(lz.getVin().equals(out.getVin())){
                            liuzhoulist.add(lz);
                        }
                    }
                }
                List<String> list = liuzhoulist.stream().map(e -> e.getVin()).collect(Collectors.toList());
                ctv.setLIUZHOUInventory(LIUZHOUinventory.size()-list.size());
                //柳州在制 柳州底盘出库-成品入库 没筛选
//                List<ChassisOutboundLiuZhouEntity> lzlist =  chassisOutboundLiuZhouRepository.findByStandingTime(ctv.getYear().toString(),ctv.getMonth().toString());
//                List<ProductionWarehouseEntity> pwlist = productionWarehouseRepository.findByInboundDate(ctv.getYear().toString(),ctv.getMonth().toString());
                //List<AcceptanceDetailEntity> allList = acceptanceDetailRepository.findByVehicleModel(ctv.getVehicleModel(),ctv.getVariety(),ctv.getBatchNumber(),ctv.getYear().toString(),ctv.getMonth().toString());
                List<ChassisOutboundLiuZhouEntity> outAlllist = new ArrayList<>();
                for(AcceptanceDetailEntity ad:allList){
                    for(ChassisOutboundLiuZhouEntity outlz:lzlist){
                        if(ad.getVin().equals(outlz.getVin())){
                            outAlllist.add(outlz);
                        }
                    }
                }

                List<ChassisOutboundLiuZhouEntity> LIUZHOUMakinglist = new ArrayList<>();
                for(ChassisOutboundLiuZhouEntity out:outAlllist){
                    for(ProductionWarehouseEntity pw:pwlist){
                        if(!(out.getVin().equals(pw.getVin()))){
                            LIUZHOUMakinglist.add(out);
                        }
                    }
                }
                List<String> liuzhouMaking = LIUZHOUMakinglist.stream().map(e -> e.getVin()).collect(Collectors.toList());
                ctv.setLIUZHOUMaking(outAlllist.size()-liuzhouMaking.size());
                //实车库存小计 已分待车+重庆底盘库存+昆明底盘库存+青岛底盘库存+发柳州在途+青岛缓冲+柳州底盘库存+柳州在制
                ctv.setVehicleSum(ctv.getWaitingExtract()+ctv.getCHONGQINGInventory()+ctv.getKUNMINGInventory()+ctv.getQINGDAOInventory()+ctv.getLIUZHOUOnTheWay()+ctv.getQINGDAOBufferInventory()+ctv.getLIUZHOUInventory()+ctv.getLIUZHOUMaking());
                //资源合计 实车库存小计+待股份分车
                ctv.setResourcesSum(ctv.getVehicleSum()+ctv.getSharesWaiting());
            }
        }


        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}
