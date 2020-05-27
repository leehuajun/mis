package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.entity.DistributionVehicleEntity;
import com.sunjet.mis.module.warehouse.repository.ChassisProcurementRepository;
import com.sunjet.mis.module.warehouse.repository.DistributionVehicleRepository;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("distributionVehicleService")
public class DistributionVehicleService {

    @Autowired
    private DistributionVehicleRepository distributionVehicleRepository;

    public List<DistributionVehicleEntity> findAll() {
        try {
            return this.distributionVehicleRepository.findAll();
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
    public PageResult<DistributionVehicleEntity> getPageList(PageParam<DistributionVehicleEntity> pageParam) {
        //1.查询条件
        DistributionVehicleEntity distributionVehicleEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DistributionVehicleEntity> specification1 = null;
        Specification<DistributionVehicleEntity> specification2 = null;
        Specification<DistributionVehicleEntity> specification3 = null;

        //页面条件
        if (distributionVehicleEntity != null && (StringUtils.isNotBlank(distributionVehicleEntity.getVehicleModel()) || StringUtils.isNotBlank(distributionVehicleEntity.getVariety()) || StringUtils.isNotBlank(distributionVehicleEntity.getBatchNumber()))) {
            specification2 = Specifications.<DistributionVehicleEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(distributionVehicleEntity.getBatchNumber()), "batchNumber", "%"+distributionVehicleEntity.getBatchNumber()+"%")
                    .like(StringUtils.isNotEmpty(distributionVehicleEntity.getVariety()), "variety", "%"+distributionVehicleEntity.getVariety()+"%")
                    .like(StringUtils.isNotEmpty(distributionVehicleEntity.getVehicleModel()), "vehicleModel", "%"+distributionVehicleEntity.getVehicleModel()+"%")
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
        specification1 = Specifications.<DistributionVehicleEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<DistributionVehicleEntity> pages = distributionVehicleRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public DistributionVehicleEntity findById(String id) {
        Optional<DistributionVehicleEntity> entityOptional = distributionVehicleRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public DistributionVehicleEntity save(DistributionVehicleEntity entity) {
        return distributionVehicleRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            distributionVehicleRepository.deleteById(id);
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
    public List<DistributionVehicleEntity> saveAll(List<DistributionVehicleEntity> importEntities) {
        List<DistributionVehicleEntity> resultEntities = new ArrayList<>();
        List<DistributionVehicleEntity> addEntities = new ArrayList<>();
        for(DistributionVehicleEntity db:importEntities){
            if(StringUtils.isNotBlank(db.getVsn()) && StringUtils.isNotBlank(db.getCargoData())) {
                if(db.getPointsMonth()==null){
                    db.setPointsMonth(Integer.parseInt(db.getCargoData().substring(4,6)));
                }
                db.setVariety(db.getVsn().substring(0, 4));
                db.setBatchNumber(db.getVsn().substring(db.getVsn().length() - 5));
                db.setPointsYear(Integer.parseInt(db.getCargoData().substring(0,4)));
                addEntities.add(db);
            }else {
                resultEntities.add(db);
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("缺少vsn或缺少分货日期,请检查原因重新导入", "系统提示");
        }else{
            distributionVehicleRepository.saveAll(addEntities);
        }
        return resultEntities;

//        try {
            //1. 用SGMW的客户编号替换五菱工业的客户编号
//            List<OrderPlanEntity> listDistributor = orderPlanRepository.findAll();
//            for (OrderPlanEntity entity : importEntities) {
//                List<OrderPlanEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
//                if (list.size() == 1) {
////                    entity.setCustomerCode(list.get(0).getSgmwCode());
//                    entity.setSgmwCode(list.get(0).getSgmwCode());
//                    addEntities.add(entity);
//                } else {
//                    resultEntities.add(entity);
//                }
//            }
            //2. 删除对应vin的数据
//            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
//            allotVehicleRepository.deleteAllByVinIn(list);

            //3. 保存数据
//            orderPlanRepository.saveAll(addEntities);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MisException(e.getMessage());
//        } finally {
//            return resultEntities;
//        }

    }

    List<DistributionVehicleEntity> findByVehicleModel(String vehicleModel, String variety, String batchNumber, Integer year, Integer month){
        return distributionVehicleRepository.findByVehicleModel(vehicleModel,variety,batchNumber,year,month);
    }

}
