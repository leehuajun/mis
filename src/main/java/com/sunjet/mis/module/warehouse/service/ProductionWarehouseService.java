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
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
import com.sunjet.mis.module.warehouse.entity.ProductionWarehouseEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.AvailableInventoryRepository;
import com.sunjet.mis.module.warehouse.repository.ProductionWarehouseRepository;
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
@Service("productionWarehouseService")
public class ProductionWarehouseService {

    @Autowired
    private ProductionWarehouseRepository productionWarehouseRepository;
//    @Autowired
//    private PlanRepository planRepository;
//    @Autowired
//    private BalanceRepository balanceRepository;
//    @Autowired
//    private AllotVehicleRepository allotVehicleRepository;
//    @Autowired
//    private VehicleInvRepository vehicleInvRepository;

    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;
    public List<ProductionWarehouseEntity> findAll() {
        try {
            return this.productionWarehouseRepository.findAll();
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
    public PageResult<ProductionWarehouseEntity> getPageList(PageParam<ProductionWarehouseEntity> pageParam) {
        //1.查询条件
        ProductionWarehouseEntity productionWarehouseEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ProductionWarehouseEntity> specification1 = null;
        Specification<ProductionWarehouseEntity> specification2 = null;
        Specification<ProductionWarehouseEntity> specification3 = null;

        //页面条件
        if (productionWarehouseEntity != null && (StringUtils.isNotBlank(productionWarehouseEntity.getVin())  || StringUtils.isNotBlank(productionWarehouseEntity.getVsn())  || StringUtils.isNotBlank(productionWarehouseEntity.getVehicleModel()))) {
            specification2 = Specifications.<ProductionWarehouseEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(productionWarehouseEntity.getVin()), "vin", "%"+productionWarehouseEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(productionWarehouseEntity.getVsn()), "vsn", "%"+productionWarehouseEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(productionWarehouseEntity.getVehicleModel()), "vehicleModel", "%"+productionWarehouseEntity.getVehicleModel()+"%")
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
        specification1 = Specifications.<ProductionWarehouseEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ProductionWarehouseEntity> pages = productionWarehouseRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ProductionWarehouseEntity findById(String id) {
        Optional<ProductionWarehouseEntity> entityOptional = productionWarehouseRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ProductionWarehouseEntity save(ProductionWarehouseEntity entity) {
        return productionWarehouseRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            productionWarehouseRepository.deleteById(id);
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
    public List<ProductionWarehouseEntity> saveAll(List<ProductionWarehouseEntity> importEntities) {
//        List<ProductionWarehouseEntity> resultEntities = new ArrayList<>();
        List<ProductionWarehouseEntity> addEntities = new ArrayList<>();
        HashSet<ProductionWarehouseEntity> resultEntities =new HashSet<>();

        List<AcceptanceDetailEntity> acceptanceDetailEntityList = acceptanceDetailRepository.findAll();

        List<ProductionWarehouseEntity> availableInventoryEntityList = productionWarehouseRepository.findAll();
        for(ProductionWarehouseEntity im:importEntities){
            for(AcceptanceDetailEntity ad:acceptanceDetailEntityList){
                if(StringUtils.isNotBlank(im.getVin()) && StringUtils.isNotBlank(ad.getVin())){
                    if(im.getVin().equals(ad.getVin())){
                        im.setVariety(ad.getVariety());
                        im.setBatchNumber(ad.getBatchNumber());
                    }
                }else {
                    resultEntities.add(im);
                }
            }
        }
        for(ProductionWarehouseEntity entity:availableInventoryEntityList){
            for(ProductionWarehouseEntity importEntity:importEntities){
                if(entity.getVin().equals(importEntity.getVin())){
                    addEntities.add(entity);
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("SGMW验收明细缺少VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            productionWarehouseRepository.deleteAllByVinIn(list);
            productionWarehouseRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;




    }

}
