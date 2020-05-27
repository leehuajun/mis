package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.repository.ChassisTrackingRepository;
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
@Service("chassisTrackingRewriteService")
public class ChassisTrackingRewriteService {

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
        ChassisTrackingView chassisTrackingView = pageParam.getInfoWhere();

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
                        .eq(chassisTrackingView.getYear() != null, "year", chassisTrackingView.getYear())
                        .eq(chassisTrackingView.getMonth() != null, "month", chassisTrackingView.getMonth())
                        .eq(chassisTrackingView.getVehicleModel() != null, "vehicleModel", chassisTrackingView.getVehicleModel())
                        .eq(chassisTrackingView.getVariety() != null, "variety", chassisTrackingView.getVariety())
                        .build();
            }
        } else {
            if (chassisTrackingView != null) {
                specification2 = Specifications.<ChassisTrackingView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(chassisTrackingView.getYear() != null, "year", chassisTrackingView.getYear())
                        .eq(chassisTrackingView.getMonth() != null, "month", chassisTrackingView.getMonth())
                        .eq(chassisTrackingView.getVehicleModel() != null, "vehicleModel", chassisTrackingView.getVehicleModel())
                        .eq(chassisTrackingView.getVariety() != null, "variety", chassisTrackingView.getVariety())
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<ChassisTrackingView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ChassisTrackingView> pages = chassisTrackingRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        if (pages.getContent().size() > 0) {
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
            for (ChassisTrackingView ctv : pages.getContent()) {
                //股份已分车
                int sharesAlready = 0;
                for (DistributionVehicleEntity dv : distributionVehicleEntityList) {
                    if (ctv.getVehicleModel().equals(dv.getVehicleModel()) && ctv.getVariety().equals(dv.getVariety()) && ctv.getBatchNumber().equals(dv.getBatchNumber()) && ctv.getMonth().equals(dv.getPointsMonth()) && ctv.getYear().equals(Integer.parseInt(dv.getCargoData().substring(0,4)))) {
                        sharesAlready += dv.getAmount();
                    }
                }
                ctv.setSharesAlready(sharesAlready);
                //股份待分车 数据=底盘采购数-股份已分车
                ctv.setSharesWaiting(ctv.getProcurementPlan() - ctv.getSharesAlready());
                //已入底盘库
                int alreadyWarehouse = 0;
                
                for (ChassisInventoryEntity ci : chassisInventoryEntityList) {
                    if (ctv.getVehicleModel().equals(ci.getVehicleModel()) && ctv.getVariety().equals(ci.getVariety()) && ctv.getBatchNumber().equals(ci.getBatchNumber()) && ctv.getMonth().equals(Integer.parseInt(ci.getShipmentData().substring(4,6))) && ctv.getYear().equals(Integer.parseInt(ci.getShipmentData().substring(0,4)))) {
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
                    if (ctv.getVehicleModel().equals(rskm.getVehicleModel()) && ctv.getVariety().equals(rskm.getVariety()) && ctv.getBatchNumber().equals(rskm.getBatchNumber()) && ctv.getMonth() == Integer.parseInt(rskm.getShipmentData().substring(4, 6)) && ctv.getYear() == Integer.parseInt(rskm.getShipmentData().substring(0, 4))) {
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

        }
        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}