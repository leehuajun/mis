package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.AllotVehicleRepository;
import com.sunjet.mis.module.report.repository.BalanceRepository;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.VehicleInvRepository;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisInventoryEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisOutboundLiuZhouEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.AvailableInventoryRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisInventoryRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisInventoryService")
public class ChassisInventoryService {

    @Autowired
    private ChassisInventoryRepository chassisInventoryRepository;

    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<ChassisInventoryEntity> findAll() {
        try {
            return this.chassisInventoryRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数
     * @return result 包含 List<Entity> 和分页数据
     */
    public PageResult<ChassisInventoryEntity> getPageList(PageParam<ChassisInventoryEntity> pageParam) {
        //1.查询条件
        ChassisInventoryEntity chassisInventoryEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisInventoryEntity> specification1 = null;
        Specification<ChassisInventoryEntity> specification2 = null;
        Specification<ChassisInventoryEntity> specification3 = null;

        //页面条件
        if (chassisInventoryEntity != null && (StringUtils.isNotBlank(chassisInventoryEntity.getVin()) || StringUtils.isNotBlank(chassisInventoryEntity.getVsn()))|| StringUtils.isNotBlank(chassisInventoryEntity.getBatchNumber())|| StringUtils.isNotBlank(chassisInventoryEntity.getVehicleModel())) {
            specification2 = Specifications.<ChassisInventoryEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chassisInventoryEntity.getVin()), "vin", "%"+chassisInventoryEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(chassisInventoryEntity.getVsn()), "vsn", "%"+chassisInventoryEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(chassisInventoryEntity.getBatchNumber()), "batchNumber", "%"+chassisInventoryEntity.getBatchNumber()+"%")
                    .like(StringUtils.isNotEmpty(chassisInventoryEntity.getVehicleModel()), "vehicleModel", "%"+chassisInventoryEntity.getVehicleModel()+"%")
                    .build();
        }

        //组织
//        if(authObject.getOrg().getOrgHierarchy().intValue()!=1) {
//            specification3 = Specifications.<ConfigEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .eq("orgId", authObject.getOrg().getObjId())
//                    .eq("orgType", "1")
//                    .build();
//        }

        //组合查询条件
        specification1 = Specifications.<ChassisInventoryEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChassisInventoryEntity> pages = chassisInventoryRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        //4.数据转换
//        List<ConfigEntity> rows = new ArrayList<>();
//        for (ConfigEntity entity : pages.getContent()) {
//            rows.add(entity);
//        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        System.out.println(pages.getContent().size());
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public ChassisInventoryEntity findById(String id) {
        Optional<ChassisInventoryEntity> entityOptional = chassisInventoryRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChassisInventoryEntity save(ChassisInventoryEntity entity) {
        return chassisInventoryRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chassisInventoryRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<ChassisInventoryEntity> saveAll(List<ChassisInventoryEntity> importEntities) {
      //  List<ChassisInventoryEntity> resultEntities = new ArrayList<>();
        List<ChassisInventoryEntity> addEntities = new ArrayList<>();
        List<ChassisInventoryEntity> saveEntities = new ArrayList<>();
        HashSet<ChassisInventoryEntity> resultEntities =new HashSet<>();
        List<ChassisInventoryEntity> resultChassisInventoryEntitys = importEntities;

        //查询匹配验收明细获取数据
        List<AcceptanceDetailEntity> availableInventoryEntityList = acceptanceDetailRepository.findAll();
        for(ChassisInventoryEntity cientity:importEntities){
            for(AcceptanceDetailEntity entity:availableInventoryEntityList){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(cientity.getVin())) {
                    if (entity.getVin().equals(cientity.getVin())) {
                        cientity.setOrderId(entity.getOrderId());
                        cientity.setVsn(entity.getVsn());
                        cientity.setEngineCode(entity.getEngineCode());
                        cientity.setShipmentData(entity.getShipmentData());
                        cientity.setBatchNumber(entity.getBatchNumber());
                        cientity.setColor(entity.getColor());
                        cientity.setQualifiedCertificate(entity.getQualifiedCertificate());
                        cientity.setBaseCode(entity.getBaseCode());
                        cientity.setMakeData(entity.getMakeData());
                        cientity.setVehicleModel(entity.getVehicleModel());
                        cientity.setVariety(entity.getVsn().substring(0, 4));
                        saveEntities.add(cientity);
                    }
                }else{
                    resultEntities.add(cientity);
                }
            }
        }
        //查询出验收明细中没有的vin
        if(saveEntities.size()!=importEntities.size()){
            resultChassisInventoryEntitys.removeAll(saveEntities);
            for(ChassisInventoryEntity ci:resultChassisInventoryEntitys){
                resultEntities.add(ci);
            }
        }
        //查询底盘库存除去重复数据
        List<ChassisInventoryEntity> chassisInventoryEntityList = chassisInventoryRepository.findAll();
        for(ChassisInventoryEntity cientity:chassisInventoryEntityList){
            for(ChassisInventoryEntity entity:saveEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(cientity.getVin())) {
                    if (entity.getVin().equals(cientity.getVin())) {
                        addEntities.add(cientity);
                    }
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("SGWM验收明细缺少VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            chassisInventoryRepository.deleteAllByVinIn(list);
            chassisInventoryRepository.saveAll(saveEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;

    }

}
