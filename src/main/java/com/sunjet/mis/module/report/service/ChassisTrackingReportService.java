package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.repository.ChassisTrackingReportRepository;
import com.sunjet.mis.module.report.repository.ChassisTrackingRepository;
import com.sunjet.mis.module.report.view.ChassisTrackReportView;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.warehouse.entity.*;
import com.sunjet.mis.module.warehouse.repository.*;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisTrackingReportService")
public class ChassisTrackingReportService {

    @Autowired
    private ChassisTrackingReportRepository chassisTrackingReportRepository;

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
    private AcceptanceDetailRepository acceptanceDetailRepository;

    @Autowired
    private RailwaySendLIUZHOURepository railwaySendLIUZHOURepository;
    @Autowired
    private ChongQingSendLiuZhouRepository chongQingSendLiuZhouRepository;
    @Autowired
    private ChassisOutboundLiuZhouRepository chassisOutboundLiuZhouRepository;
    @Autowired
    private ChassisProcurementRepository chassisProcurementRepository;
    @Autowired
    private ChassisTrackingService chassisTrackingService;

    public PageResult<ChassisTrackReportView> getPageList(PageParam<ChassisTrackReportView> pageParam) {
        //1
        ChassisTrackReportView  chassisTrackReportView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisTrackReportView> specification = null;
        Specification<ChassisTrackReportView> specification2 = null;
        Specification<ChassisTrackReportView> specification3 = null;
        System.out.println(chassisTrackReportView.getYear() + "-" + chassisTrackReportView.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (chassisTrackReportView != null && StringUtils.isNotBlank(chassisTrackReportView.getVariety())) {
                specification2 = Specifications.<ChassisTrackReportView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(chassisTrackReportView.getVehicleModel()!=null, "vehicleModel", chassisTrackReportView.getVehicleModel())
                        .eq(chassisTrackReportView.getVariety()!=null,"variety", chassisTrackReportView.getVariety())
                        .eq(chassisTrackReportView.getYear()!=null, "year", chassisTrackReportView.getYear())
                        .eq(chassisTrackReportView.getMonth()!=null,"month", chassisTrackReportView.getMonth())
                        .build();
            }
        } else {
            if (chassisTrackReportView != null ) {
                specification2 = Specifications.<ChassisTrackReportView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(chassisTrackReportView.getVehicleModel()!=null, "vehicleModel", chassisTrackReportView.getVehicleModel())
                        .eq(chassisTrackReportView.getVariety()!=null,"variety", chassisTrackReportView.getVariety())
                        .eq(chassisTrackReportView.getYear()!=null, "year", chassisTrackReportView.getYear())
                        .eq(chassisTrackReportView.getMonth()!=null,"month", chassisTrackReportView.getMonth())
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<ChassisTrackReportView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ChassisTrackReportView> pages = chassisTrackingReportRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        List<ChassisTrackReportView> chassisTrackReportViews = new ArrayList<>();
        if (pages.getContent().size() > 0) {
//            //柳州底盘出库
//            List<ChassisOutboundLiuZhouEntity> liuZhouEntityList =  chassisOutboundLiuZhouRepository.findAll();
//            //柳州成品入库
//            List<ProductionWarehouseEntity> lzWarehouseList = productionWarehouseRepository.findByWarehouseArea("%柳州%");
//            SimpleDateFormat year = new SimpleDateFormat("yyyy");
//            SimpleDateFormat month = new SimpleDateFormat("MM");
//
//            List<Object[]> chassisProcurementEntities = chassisProcurementRepository.findDistinct();
//            for(ChassisTrackReportView ctv:pages.getContent()) {
//                //股份已分车
//                List<DistributionVehicleEntity> dvlist = distributionVehicleRepository.findByVehicleModel(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear(), ctv.getMonth());
//                int amount = 0;
//                for (DistributionVehicleEntity dv : dvlist) {
//                    amount += dv.getAmount();
//                    ctv.setSharesAlready(amount);
//                }
//                //股份待分车
//                ctv.setSharesWaiting(ctv.getProcurementPlan() - ctv.getSharesAlready());
//                //已入底盘库
//                List<ChassisInventoryEntity> chlist = chassisInventoryRepository.findByVehicleModel(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
//                if (chlist != null && chlist.size() > 0) {
//                    ctv.setAlreadyWarehouse(chlist.size());
//                }
//                //已分车待提 dvlist - chlist
//                ctv.setWaitingExtract(ctv.getSharesAlready() - ctv.getAlreadyWarehouse());
//                //重庆底盘库库存 重庆底盘入库-重庆成品入库
//                List<ChassisInventoryEntity> cqinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%重庆%");
//                List<ProductionWarehouseEntity> cqwarehouse = new ArrayList<>();
//                if(cqinventory!=null && cqinventory.size()>0) {
//                    for (ChassisInventoryEntity ci : cqinventory) {
//                        ProductionWarehouseEntity pwentity = productionWarehouseRepository.findByVinAndVsn(ci.getVin(), ci.getVsn(), "%重庆%");
//                        cqwarehouse.add(pwentity);
//                    }
//                }
//                if (cqinventory != null && cqinventory.size() > 0 && cqwarehouse.size() > 0) {
//                    ctv.setCHONGQINGInventory(cqinventory.size() - cqwarehouse.size());
//                }
//                //昆明前置库库存
//                List<RailwaySendKunMingEntity> kunMingEntities = railwaySendKunMingRepository.findAll();
//                List<AcceptanceDetailEntity> allList = acceptanceDetailRepository.findByVehicleModel(ctv.getVehicleModel(),ctv.getVariety(),ctv.getBatchNumber(),ctv.getYear().toString(),ctv.getMonth().toString());
//                List<RailwaySendKunMingEntity> kunMingsum = new ArrayList<>();
//                for(AcceptanceDetailEntity ci:allList){
//                    for(RailwaySendKunMingEntity km:kunMingEntities){
//                        if(ci.getVin().equals(km.getVin())){
//                            kunMingsum.add(km);
//                        }
//                    }
//                }
//                ctv.setKUNMINGInventory(kunMingsum.size());
//                //青岛底盘库存
//                List<ChassisInventoryEntity> qdinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%青岛%");
//                List<ChassisWarehouseQingDaoEntity> qdwarehouse = new ArrayList<>();
//                if(qdinventory!=null && qdinventory.size()>0) {
//                    for (ChassisInventoryEntity ci : qdinventory) {
//                        ChassisWarehouseQingDaoEntity cwentity = chassisWarehouseQingDaoRepository.findByVinAndVsn(ci.getVin(), ci.getVsn(), "%千业物流（青岛底盘）%");
//                        qdwarehouse.add(cwentity);
//                    }
//                    List<ChassisOutboundQINGDAOEntity> qdoutlist = new ArrayList<>();
//                    if(qdwarehouse!=null && qdwarehouse.size()>0) {
//                        for (ChassisWarehouseQingDaoEntity qd : qdwarehouse) {
//                            if(qd!=null && StringUtils.isNotBlank(qd.getVin()) && StringUtils.isNotBlank(qd.getVsn())) {
//                                ChassisOutboundQINGDAOEntity qdout = chassisOutboundQingDaoRepository.findByVinAndVsn(qd.getVin(), qd.getVsn(), "%千业物流（青岛底盘）%");
//                                qdoutlist.add(qdout);
//                            }
//                        }
//                    }
//                    ctv.setQINGDAOInventory(qdwarehouse.size() - qdoutlist.size());
//                }
//                //发柳州在途
//                //青岛出库
//                List<ChassisOutboundQINGDAOEntity> qingdaooutlist = chassisOutboundQingDaoRepository.findBVehicleModel(ctv.getVehicleModel(),ctv.getBatchNumber(),ctv.getVariety(),"%千业物流（回柳底盘）%",ctv.getYear().toString(),ctv.getMonth().toString());
//                //中铁发柳州
//                List<RailwaySendLIUZHOUEntity> liuzhouEntities = railwaySendLIUZHOURepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
//                //重庆发柳州
//                List<ChongQingSendLiuZhouEntity> chongQingSendLiuZhouEntities = chongQingSendLiuZhouRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString());
//                //柳州入库
//                List<ChassisInventoryEntity> LIUZHOUinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%柳州底盘入库%");
//                List<ChassisInventoryEntity> sendLIUZHOU = new ArrayList<>();
//                for(ChassisInventoryEntity lz:LIUZHOUinventory){
//                    //青岛出库
//                    for(ChassisOutboundQINGDAOEntity qd:qingdaooutlist){
//                        if(lz.getVin().equals(qd.getVin())){
//                            sendLIUZHOU.add(lz);
//                        }
//                    }
//                    //中铁发柳州
//                    for(RailwaySendLIUZHOUEntity zt:liuzhouEntities){
//                        if(lz.getVin().equals(zt.getVin())){
//                            sendLIUZHOU.add(lz);
//                        }
//                    }
//                    //重庆发柳州
//                    for(ChongQingSendLiuZhouEntity cq:chongQingSendLiuZhouEntities){
//                        if(lz.getVin().equals(cq.getVin())){
//                            sendLIUZHOU.add(lz);
//                        }
//                    }
//                    ctv.setLIUZHOUOnTheWay(sendLIUZHOU.size()-LIUZHOUinventory.size());
//                }
//                //青岛缓冲区 青岛底盘出库-成品入库
//                List<ChassisOutboundQINGDAOEntity> qingdao = chassisOutboundQingDaoRepository.findBVehicleModel(ctv.getVehicleModel(),ctv.getBatchNumber(),ctv.getVariety(),"%千业物流（青岛底盘）%",ctv.getYear().toString(),ctv.getMonth().toString());
//                List<ProductionWarehouseEntity> pwlist = productionWarehouseRepository.findByInboundDate(ctv.getYear().toString(),ctv.getMonth().toString());
//                List<ChassisOutboundQINGDAOEntity> buffer = new ArrayList<>();
//                for(ChassisOutboundQINGDAOEntity out:qingdao){
//                    for(ProductionWarehouseEntity pw:pwlist){
//                        if(out.getVin().equals(pw.getVin())){
//                            buffer.add(out);
//                        }
//                    }
//                }
//                ctv.setQINGDAOBufferInventory(qingdao.size()-buffer.size());
//                //柳州底盘库 柳州底盘入库-柳州底盘出库
////                List<ChassisInventoryEntity> LIUZHOUinventory = chassisInventoryRepository.findByDataSource(ctv.getVehicleModel(), ctv.getVariety(), ctv.getBatchNumber(), ctv.getYear().toString(), ctv.getMonth().toString(), "%柳州底盘入库%");
//                List<ChassisOutboundLiuZhouEntity> lzlist =  chassisOutboundLiuZhouRepository.findByStandingTime(ctv.getYear().toString(),ctv.getMonth().toString());
//                List<ChassisInventoryEntity> liuzhoulist = new ArrayList<>();
//                for(ChassisInventoryEntity lz:LIUZHOUinventory){
//                    for(ChassisOutboundLiuZhouEntity out:lzlist){
//                        if(lz.getVin().equals(out.getVin())){
//                            liuzhoulist.add(lz);
//                        }
//                    }
//                }
//                List<String> list = liuzhoulist.stream().map(e -> e.getVin()).collect(Collectors.toList());
//                ctv.setLIUZHOUInventory(LIUZHOUinventory.size()-list.size());
//                //柳州在制 柳州底盘出库-成品入库
//                int LIUZHOUOut = 0;
//                int LIUZHOUProduction = 0;
//
//                for(ChassisOutboundLiuZhouEntity lzout:liuZhouEntityList){
//                    if(ctv.getVehicleModel().equals(lzout.getVehicleModel()) && ctv.getVariety().equals(lzout.getVariety()) && ctv.getBatchNumber().equals(lzout.getBatchNumber()) && ctv.getYear()==Integer.parseInt(year.format(lzout.getStandingTime())) && ctv.getMonth()==Integer.parseInt(month.format(lzout.getStandingTime()))){
//                        LIUZHOUOut=LIUZHOUOut+1;
//                    }
//                }
//
//                for(ProductionWarehouseEntity pwlz:lzWarehouseList){
//                    if(ctv.getVehicleModel().equals(pwlz.getVehicleModel()) && ctv.getVariety().equals(pwlz.getVariety()) && ctv.getBatchNumber().equals(pwlz.getBatchNumber()) && ctv.getYear()==Integer.parseInt(year.format(pwlz.getInboundDate())) && ctv.getMonth()==Integer.parseInt(month.format(pwlz.getInboundDate()))){
//                        LIUZHOUProduction = LIUZHOUProduction+1;
//                    }
//                }
//                ctv.setLIUZHOUMaking(LIUZHOUOut-LIUZHOUProduction);
//                //实车库存小计 已分待车+重庆底盘库存+昆明底盘库存+青岛底盘库存+发柳州在途+青岛缓冲+柳州底盘库存+柳州在制
//                ctv.setVehicleSum(ctv.getWaitingExtract()+ctv.getCHONGQINGInventory()+ctv.getKUNMINGInventory()+ctv.getQINGDAOInventory()+ctv.getLIUZHOUOnTheWay()+ctv.getQINGDAOBufferInventory()+ctv.getLIUZHOUInventory()+ctv.getLIUZHOUMaking());
//                //资源合计 实车库存小计+待股份分车
//                ctv.setResourcesSum(ctv.getVehicleSum()+ctv.getSharesWaiting());


            List<Object[]> chassisProcurementEntities = chassisProcurementRepository.findDistinct();
            //股份已分车
            List<DistributionVehicleEntity> distributionVehicleEntityList = distributionVehicleRepository.findAll();
            //已入底盘库
            List<ChassisInventoryEntity> chassisInventoryEntityList = chassisInventoryRepository.findAll();
            //重庆底盘入库
            List<ChassisInventoryEntity> cqInventoryList = chassisInventoryRepository.findDataSource("%重庆%");
            //重庆成品入库
            List<ProductionWarehouseEntity> cqWarehouseList = productionWarehouseRepository.findByWarehouseArea("%重庆%");
            //中铁发昆明
            List<RailwaySendKunMingEntity> kunMingEntities = railwaySendKunMingRepository.findAll();
            //sgmw验收明细
            List<AcceptanceDetailEntity> acceptanceDetailEntityList = acceptanceDetailRepository.findAll();
            //青岛底盘入库
            List<ChassisInventoryEntity> qdInventoryList = chassisInventoryRepository.findDataSource("%青岛%");
            List<ChassisWarehouseQingDaoEntity> chassisWarehouseQingDaoEntityList = chassisWarehouseQingDaoRepository.findByScanMember("%青岛底盘%");
            //青岛底盘出库（青岛用底盘）
            List<ChassisOutboundQINGDAOEntity> chassisOutboundQINGDAOEntityList = chassisOutboundQingDaoRepository.findByScanMember("%青岛底盘%");
            //青岛底盘出库（回柳底盘）
            List<ChassisOutboundQINGDAOEntity> chassisOutboundQINGDAOSendLiuZhouList = chassisOutboundQingDaoRepository.findByScanMember("%回柳底盘%");
            //中铁发柳州
            List<RailwaySendLIUZHOUEntity> liuzhouEntities = railwaySendLIUZHOURepository.findAll();
            //重庆发柳州
            List<ChongQingSendLiuZhouEntity> chongQingSendLiuZhouEntities = chongQingSendLiuZhouRepository.findAll();
            //柳州底盘入库
            List<ChassisInventoryEntity> lzInventoryList = chassisInventoryRepository.findDataSource("%柳州%");
            //青岛成品入库
            List<ProductionWarehouseEntity> qdWarehouseList = productionWarehouseRepository.findByWarehouseArea("%青岛%");
            //柳州底盘出库
            List<ChassisOutboundLiuZhouEntity> liuZhouEntityList =  chassisOutboundLiuZhouRepository.findAll();
            //柳州成品入库
            List<ProductionWarehouseEntity> lzWarehouseList = productionWarehouseRepository.findByWarehouseArea("%柳州%");
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            for (ChassisTrackReportView ctv : pages.getContent()) {
                //股份已分车
                int sharesAlready = 0;
                for (DistributionVehicleEntity dv : distributionVehicleEntityList) {
                    if (ctv.getVehicleModel().equals(dv.getVehicleModel()) && ctv.getVariety().equals(dv.getVariety()) && ctv.getBatchNumber().equals(dv.getBatchNumber())) {
                        sharesAlready += dv.getAmount();
                    }
                }
                ctv.setSharesAlready(sharesAlready);
                //股份待分车 数据=底盘采购数-股份已分车
                ctv.setSharesWaiting(ctv.getProcurementPlan() - ctv.getSharesAlready());
                //已入底盘库
                int alreadyWarehouse = 0;
                for (ChassisInventoryEntity ci : chassisInventoryEntityList) {
                    if (ctv.getVehicleModel().equals(ci.getVehicleModel()) && ctv.getVariety().equals(ci.getVariety()) && ctv.getBatchNumber().equals(ci.getBatchNumber())) {
                        alreadyWarehouse = alreadyWarehouse + 1;
                    }
                }
                ctv.setAlreadyWarehouse(alreadyWarehouse);
                //已分车待提 股份已分车-已入库底盘
                ctv.setWaitingExtract(ctv.getSharesAlready() - ctv.getAlreadyWarehouse());
                //重庆底盘库库存 重庆底盘入库-重庆成品入库
                int cqWarehouseSum = 0;
                for (ChassisInventoryEntity ci : cqInventoryList) {
                    if (ctv.getVehicleModel().equals(ci.getVehicleModel()) && ctv.getVariety().equals(ci.getVariety()) && ctv.getBatchNumber().equals(ci.getBatchNumber()) && ctv.getYear() == Integer.parseInt(ci.getShipmentData().substring(0, 4)) && ctv.getMonth() == Integer.parseInt(ci.getShipmentData().substring(4, 6))) {
                        for (ProductionWarehouseEntity pw : cqWarehouseList) {
                            if (ci.getVin().equals(pw.getVin())) {
                                cqWarehouseSum = cqWarehouseSum + 1;
                            }
                        }
                        ctv.setCHONGQINGInventory(cqInventoryList.size() - cqWarehouseSum);
                    }
                }

                //昆明前置库库存 将中铁发运昆明的信息列为前置库资源
                int kunMingInventory = 0;
                for (RailwaySendKunMingEntity rskm : kunMingEntities) {
//                   && ctv.getYear()==Integer.parseInt(rskm.getShipmentData().substring(0,4))    只查了月份 年不知道查哪里
                    if (ctv.getVehicleModel().equals(rskm.getVehicleModel()) && ctv.getVariety().equals(rskm.getVariety()) && ctv.getBatchNumber().equals(rskm.getBatchNumber()) && ctv.getMonth() == Integer.parseInt(rskm.getTrainlNumber().substring(0, 2))) {
                        kunMingInventory = kunMingInventory+1;
                    }
                }
                ctv.setKUNMINGInventory(kunMingInventory);
                //青岛底盘库存 数据=青岛底盘入库-青岛底盘出库（青岛用底盘） VIN码（只匹配扫描员为“千业物流（青岛底盘）”的）批次号、
                List<ChassisWarehouseQingDaoEntity> qdwarehouse = new ArrayList<>();
                int qdInventory= 0;
                for(ChassisInventoryEntity qd:qdInventoryList){
                    if (ctv.getVehicleModel().equals(qd.getVehicleModel()) && qd.getVariety().equals(qd.getVariety()) && ctv.getBatchNumber().equals(qd.getBatchNumber()) && ctv.getYear()==Integer.parseInt(qd.getShipmentData().substring(0,4)) && ctv.getMonth()==Integer.parseInt(qd.getShipmentData().substring(4,6))) {
                        for(ChassisWarehouseQingDaoEntity cwqd:chassisWarehouseQingDaoEntityList){
                            if(qd.getVin().equals(cwqd.getVin())){
                                qdwarehouse.add(cwqd);
                            }
                        }
                    }
                }
                if(qdwarehouse!=null && qdwarehouse.size()>0) {
                    for (ChassisWarehouseQingDaoEntity qdkc : qdwarehouse) {
                        for(ChassisOutboundQINGDAOEntity outqd:chassisOutboundQINGDAOEntityList){
                            if(qdkc.getVin().equals(outqd.getVin())){
                                qdInventory = qdInventory+1;
                            }
                        }
                    }
                }
                ctv.setQINGDAOInventory(qdwarehouse.size() - qdInventory);
                //发柳州在途 数据=青岛底盘出库（柳州用底盘）+中铁发运回柳州+重庆发运回柳州-柳州底盘入库
                //青岛底盘出库（柳州用底盘） chassisOutboundQINGDAOSendLiuZhouList
                //中铁发柳州 liuzhouEntities
                //重庆发柳州 chongQingSendLiuZhouEntities
                //柳州入库 lzInventoryList
                int lzOnTheWay = 0;
                int sendlz = 0;
                for(ChassisInventoryEntity lz:lzInventoryList){
                    if (ctv.getVehicleModel().equals(lz.getVehicleModel()) && ctv.getVariety().equals(lz.getVariety()) && ctv.getBatchNumber().equals(lz.getBatchNumber()) && ctv.getYear() == Integer.parseInt(lz.getShipmentData().substring(0, 4)) && ctv.getMonth() == Integer.parseInt(lz.getShipmentData().substring(4, 6))) {
                        lzOnTheWay = lzOnTheWay+1;
                        for(ChassisOutboundQINGDAOEntity qdout:chassisOutboundQINGDAOSendLiuZhouList){
                            if(lz.getVin().equals(qdout.getVin())){
                                sendlz=sendlz+1;
                            }
                        }
                        for(RailwaySendLIUZHOUEntity rsl:liuzhouEntities){
                            if(lz.getVin().equals(rsl.getVin())){
                                sendlz=sendlz+1;
                            }
                        }
                        for(ChongQingSendLiuZhouEntity qdslz:chongQingSendLiuZhouEntities){
                            if(lz.getVin().equals(qdslz.getVin())){
                                sendlz=sendlz+1;
                            }
                        }
                    }
                }
                ctv.setLIUZHOUOnTheWay(sendlz-lzOnTheWay);
                //青岛缓冲区  青岛底盘出库-成品入库 chassisOutboundQINGDAOEntityList  qdWarehouseList
                int QingDaoOut = 0;
                int QingDaoWarehouse = 0;
                for(ChassisOutboundQINGDAOEntity qdout:chassisOutboundQINGDAOEntityList){
                    if(ctv.getVehicleModel().equals(qdout.getVehicleModel()) && ctv.getVariety().equals(qdout.getVariety()) && ctv.getBatchNumber().equals(qdout.getSharesProduceNumber()) && ctv.getYear()==Integer.parseInt(year.format(qdout.getStandingTime())) && ctv.getMonth()==Integer.parseInt(month.format(qdout.getStandingTime()))){
                        QingDaoOut = QingDaoOut+1;
                    }
                }
                for(ProductionWarehouseEntity qdwh:qdWarehouseList){
                    if(ctv.getVehicleModel().equals(qdwh.getVehicleModel()) && ctv.getVariety().equals(qdwh.getVariety()) && ctv.getBatchNumber().equals(qdwh.getBatchNumber()) && ctv.getYear()==Integer.parseInt(year.format(qdwh.getInboundDate())) && ctv.getMonth()==Integer.parseInt(month.format(qdwh.getInboundDate()))){
                        QingDaoWarehouse =QingDaoWarehouse+1;
                    }
                }
                ctv.setQINGDAOBufferInventory(QingDaoOut-QingDaoWarehouse);
                //柳州底盘 柳州底盘入库-柳州底盘出库
                int LIUZHOUInventory =0;
                List<ChassisInventoryEntity> lzInventoryLists = new ArrayList<>();
                for(ChassisInventoryEntity lz:lzInventoryList){
                    if (ctv.getVehicleModel().equals(lz.getVehicleModel()) && ctv.getVariety().equals(lz.getVariety()) && ctv.getBatchNumber().equals(lz.getBatchNumber()) && ctv.getYear() == Integer.parseInt(lz.getShipmentData().substring(0, 4)) && ctv.getMonth() == Integer.parseInt(lz.getShipmentData().substring(4, 6))) {
                        lzInventoryLists.add(lz);
                        for(ChassisOutboundLiuZhouEntity lzout:liuZhouEntityList){
                            if(lz.getVin().equals(lzout.getVin())){
                                LIUZHOUInventory = LIUZHOUInventory+1;
                            }
                        }
                        ctv.setLIUZHOUInventory(lzInventoryLists.size()-LIUZHOUInventory);
                    }
                }

                //柳州在制 柳州底盘出库-成品入库
                int LIUZHOUOut = 0;
                int LIUZHOUProduction = 0;
                for(ChassisOutboundLiuZhouEntity lzout:liuZhouEntityList){
                    if(ctv.getVehicleModel().equals(lzout.getVehicleModel()) && ctv.getVariety().equals(lzout.getVariety()) && ctv.getBatchNumber().equals(lzout.getBatchNumber()) && ctv.getYear()==Integer.parseInt(year.format(lzout.getStandingTime())) && ctv.getMonth()==Integer.parseInt(month.format(lzout.getStandingTime()))){
                        LIUZHOUOut=LIUZHOUOut+1;
                    }
                }

                for(ProductionWarehouseEntity pwlz:lzWarehouseList){
                    if(ctv.getVehicleModel().equals(pwlz.getVehicleModel()) && ctv.getVariety().equals(pwlz.getVariety()) && ctv.getBatchNumber().equals(pwlz.getBatchNumber()) && ctv.getYear()==Integer.parseInt(year.format(pwlz.getInboundDate())) && ctv.getMonth()==Integer.parseInt(month.format(pwlz.getInboundDate()))){
                        LIUZHOUProduction = LIUZHOUProduction+1;
                    }
                }
                ctv.setLIUZHOUMaking(LIUZHOUOut-LIUZHOUProduction);
                //实车库存小计 已分待车+重庆底盘库存+昆明底盘库存+青岛底盘库存+发柳州在途+青岛缓冲+柳州底盘库存+柳州在制
                ctv.setVehicleSum(ctv.getWaitingExtract()+ctv.getCHONGQINGInventory()+ctv.getKUNMINGInventory()+ctv.getQINGDAOInventory()+ctv.getLIUZHOUOnTheWay()+ctv.getQINGDAOBufferInventory()+ctv.getLIUZHOUInventory()+ctv.getLIUZHOUMaking());
                //资源合计 实车库存小计+待股份分车
                ctv.setResourcesSum(ctv.getVehicleSum()+ctv.getSharesWaiting());

            }

//            PageResult<ChassisTrackReportView> chassisTrackReportViewPageResult = new PageResult<ChassisTrackReportView>();

            for(Object[] cp:chassisProcurementEntities){
                ChassisTrackReportView chassisview = new ChassisTrackReportView();
                int procurementPlan = 0; //采购计划
                int sharesAlready = 0; //股份已分车
                int sharesWaiting = 0; //采购计划
                int alreadyWarehouse = 0; //已入底盘库
                int waitingExtract = 0; //已分车待提
                int CHONGQINGInventory = 0; //重庆底盘库库存
                int KUNMINGInventory = 0; //昆明前置库库存
                int LIUZHOUOnTheWay = 0; //发柳州在途
                int QINGDAOInventory = 0; //青岛底盘库库存
                int QINGDAOBufferInventory = 0; //青岛缓冲区库存
                int LIUZHOUInventory = 0; //柳州底盘库
                int LIUZHOUMaking = 0; //柳州在制
                int vehicleSum = 0; //实车库存小计
                int resourcesSum = 0; //资源合计
                String vehicleModel = cp[0].toString();
                String variety = cp[1].toString();
                String config = cp[2].toString();
                for (ChassisTrackReportView ct : pages.getContent()) {
                    if (cp[0].equals(ct.getVehicleModel()) && cp[2].equals(ct.getConfig()) && cp[1].equals(ct.getVariety())) {
                        procurementPlan += ct.getProcurementPlan();
                        if(ct.getVehicleModel().equals("LZW5021XXYJY") && ct.getVariety().equals("CMNH")){
                            System.out.println(procurementPlan+"============="+ct.getProcurementPlan());
                        }
                        sharesAlready += ct.getSharesAlready();
                        sharesWaiting += ct.getSharesWaiting();
                        alreadyWarehouse += ct.getAlreadyWarehouse();
                        waitingExtract += ct.getWaitingExtract();
                        CHONGQINGInventory += ct.getCHONGQINGInventory();
                        KUNMINGInventory += ct.getKUNMINGInventory();
                        LIUZHOUOnTheWay += ct.getLIUZHOUOnTheWay();
                        QINGDAOInventory += ct.getQINGDAOInventory();
                        QINGDAOBufferInventory += ct.getQINGDAOBufferInventory();
                        LIUZHOUInventory += ct.getLIUZHOUInventory();
                        LIUZHOUMaking += ct.getLIUZHOUMaking();
                        vehicleSum += ct.getVehicleSum();
                        resourcesSum += ct.getResourcesSum();
//                        System.out.println(cp[0]+"=="+cp[1]+"=="+cp[2]);
                        vehicleModel = cp[0].toString();
                        variety = cp[1].toString();
                        config = cp[2].toString();
                    }
                }

                chassisview.setVehicleModel(vehicleModel);
                chassisview.setVariety(variety);
                chassisview.setConfig(config);
                chassisview.setProcurementPlan(procurementPlan);
                chassisview.setSharesAlready(sharesAlready);
                chassisview.setSharesWaiting(sharesWaiting);
                chassisview.setAlreadyWarehouse(alreadyWarehouse);
                chassisview.setWaitingExtract(waitingExtract);
                chassisview.setCHONGQINGInventory(CHONGQINGInventory);
                chassisview.setKUNMINGInventory(KUNMINGInventory);
                chassisview.setLIUZHOUOnTheWay(LIUZHOUOnTheWay);
                chassisview.setQINGDAOInventory(QINGDAOInventory);
                chassisview.setQINGDAOBufferInventory(QINGDAOBufferInventory);
                chassisview.setLIUZHOUInventory(LIUZHOUInventory);
                chassisview.setLIUZHOUMaking(LIUZHOUMaking);
                chassisview.setVehicleSum(vehicleSum);
                chassisview.setResourcesSum(resourcesSum);
//                chassisTrackReportViews.add(chassisview);
                if(chassisview.getProcurementPlan()>0){
                    chassisTrackReportViews.add(chassisview);
                }

            }
//            chassisTrackReportViewPageResult.setRows(chassisTrackReportViews);  pages.getContent().add(chassisview);
//            pages.getContent().clear();
//            Page<ChassisTrackReportView> chassisPage;
//            for(ChassisTrackReportView ctv:chassisTrackReportViews){
//                pages.getContent().add(ctv);
//            }
        }

        //5.返回
        return PageUtil.getPageResult(chassisTrackReportViews, pages, pageParam);
    }
}
