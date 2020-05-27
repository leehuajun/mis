package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.AllotVehicleRepository;
import com.sunjet.mis.module.report.repository.BalanceRepository;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.VehicleInvRepository;
import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import com.sunjet.mis.module.warehouse.repository.ChassisProcurementRepository;
import com.sunjet.mis.module.warehouse.repository.InventoryRepository;
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

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisProcurementService")
public class ChassisProcurementService {

    @Autowired
    private ChassisProcurementRepository chassisProcurementRepository;

    public List<ChassisProcurementEntity> findAll() {
        try {
            return this.chassisProcurementRepository.findAll();
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
    public PageResult<ChassisProcurementEntity> getPageList(PageParam<ChassisProcurementEntity> pageParam) {
        //1.查询条件
        ChassisProcurementEntity chassisProcurementEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisProcurementEntity> specification1 = null;
        Specification<ChassisProcurementEntity> specification2 = null;
        Specification<ChassisProcurementEntity> specification3 = null;

        //页面条件
        if (chassisProcurementEntity != null && (StringUtils.isNotBlank(chassisProcurementEntity.getVehicleModel()) || StringUtils.isNotBlank(chassisProcurementEntity.getVariety()) || StringUtils.isNotBlank(chassisProcurementEntity.getBatchNumber()))) {
            specification2 = Specifications.<ChassisProcurementEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chassisProcurementEntity.getBatchNumber()), "batchNumber", "%"+chassisProcurementEntity.getBatchNumber()+"%")
                    .like(StringUtils.isNotEmpty(chassisProcurementEntity.getVariety()), "variety", "%"+chassisProcurementEntity.getVariety()+"%")
                    .like(StringUtils.isNotEmpty(chassisProcurementEntity.getVehicleModel()), "vehicleModel", "%"+chassisProcurementEntity.getVehicleModel()+"%")
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
        specification1 = Specifications.<ChassisProcurementEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChassisProcurementEntity> pages = chassisProcurementRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ChassisProcurementEntity findById(String id) {
        Optional<ChassisProcurementEntity> entityOptional = chassisProcurementRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChassisProcurementEntity save(ChassisProcurementEntity entity) {
        return chassisProcurementRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chassisProcurementRepository.deleteById(id);
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
    public List<ChassisProcurementEntity> saveAll(List<ChassisProcurementEntity> importEntities) {
//        List<ChassisProcurementEntity> resultEntities = new ArrayList<>();
        List<ChassisProcurementEntity> addEntities = new ArrayList<>();
        HashSet<ChassisProcurementEntity> resultEntities =new HashSet<>();
        Date t = new Date();
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        for(ChassisProcurementEntity cp:importEntities){
//            System.out.println(cp.getVsn().substring(0,4)+"=========");
            if(StringUtils.isNotBlank(cp.getVsn()) && StringUtils.isNotBlank(cp.getSalesDemand())){
                cp.setVariety(cp.getVsn().substring(0,4));
                cp.setYear(Integer.parseInt(yy.format(t)));
                cp.setMonth(Integer.parseInt(cp.getSalesDemand().substring(0,1)));
            }else{
                resultEntities.add(cp);
            }

        }
        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VSN或缺少销售需求,请检查原因重新导入", "系统提示");
        }else{
            chassisProcurementRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;


    }

}
