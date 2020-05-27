package com.sunjet.mis.module.plan.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.DateHelper;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.plan.entity.OrderPlanEntity;
import com.sunjet.mis.module.plan.repository.OrderPlanRepository;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import com.sunjet.mis.module.report.repository.*;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
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
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("orderPlanService")
public class OrderPlanService {

    @Autowired
    private OrderPlanRepository orderPlanRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private AllotVehicleRepository allotVehicleRepository;
    @Autowired
    private VehicleInvRepository vehicleInvRepository;

    public List<OrderPlanEntity> findAll() {
        try {
            return this.orderPlanRepository.findAll();
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
    public PageResult<OrderPlanEntity> getPageList(PageParam<OrderPlanEntity> pageParam) {
        //1.查询条件
        OrderPlanEntity orderPlanEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<OrderPlanEntity> specification1 = null;
        Specification<OrderPlanEntity> specification2 = null;
        Specification<OrderPlanEntity> specification3 = null;

        //页面条件
        if (orderPlanEntity != null && StringUtils.isNotBlank(orderPlanEntity.getDistributorCode())) {
            specification2 = Specifications.<OrderPlanEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(orderPlanEntity.getDistributorCode()), "distributor_code_", "%"+orderPlanEntity.getDistributorCode()+"%")
                    .like(StringUtils.isNotEmpty(orderPlanEntity.getDistributorName()), "distributor_name_", "%"+orderPlanEntity.getDistributorName()+"%")
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
        specification1 = Specifications.<OrderPlanEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<OrderPlanEntity> pages = orderPlanRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public OrderPlanEntity findById(String id) {
        Optional<OrderPlanEntity> entityOptional = orderPlanRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public OrderPlanEntity save(OrderPlanEntity entity) {
        return orderPlanRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            orderPlanRepository.deleteById(id);
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
    public List<OrderPlanEntity> saveAll(List<OrderPlanEntity> importEntities) {
        List<OrderPlanEntity> resultEntities = new ArrayList<>();
        List<OrderPlanEntity> addEntities = new ArrayList<>();
        return orderPlanRepository.saveAll(importEntities);

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

}
