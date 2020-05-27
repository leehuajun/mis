package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.repository.PlanRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("planService")
public class PlanService {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private DistributorRepository distributorRepository;

    public PageResult<PlanEntity> getPageList(PageParam<PlanEntity> pageParam) {
        //1.查询条件
        PlanEntity planEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PlanEntity> specification = null;
        Specification<PlanEntity> specification2 = null;
        Specification<PlanEntity> specification3 = null;

        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (planEntity != null && StringUtils.isNotBlank(planEntity.getSgmwCode())) {
                specification2 = Specifications.<PlanEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(StringUtils.isNotEmpty(planEntity.getSgmwCode()), "sgmwCode", planEntity.getSgmwCode())
                        .like(StringUtils.isNotEmpty(planEntity.getVsn()), "vsn", "%" + planEntity.getVsn() + "%")
                        .build();
            }
        } else {
            if (planEntity != null && (StringUtils.isNotBlank(planEntity.getSgmwCode()) || StringUtils.isNotBlank(planEntity.getDistributorCode()) || StringUtils.isNotBlank(planEntity.getDistributorName()) || StringUtils.isNotBlank(planEntity.getVsn()))) {
                specification3 = Specifications.<PlanEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotEmpty(planEntity.getSgmwCode()), "sgmwCode", "%" + planEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(planEntity.getDistributorCode()), "distributorCode", "%" + planEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(planEntity.getDistributorName()), "distributorName", "%" + planEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(planEntity.getVsn()), "vsn", "%" + planEntity.getVsn() + "%")
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<PlanEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<PlanEntity> pages = planRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public PageResult<PlanEntity> getDetailPageList(PageParam<PlanEntity> pageParam) {
        //1.查询条件
        PlanEntity planEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PlanEntity> specification = null;
        Specification<PlanEntity> specification2 = null;
        Specification<PlanEntity> specification3 = null;

//        //页面查询条件
//        if(balanceEntity!=null&& StringUtils.isNotBlank(balanceEntity.getName())) {
//            specification2 = Specifications.<UserEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .like(StringUtils.isNotEmpty(userEntity.getName()), "name", "%" + userEntity.getName() + "%")
//                    .like(StringUtils.isNotEmpty(userEntity.getName()), "logId", "%" + userEntity.getName() + "%")
//                    .build();
//        }
        //页面查询条件
        if (planEntity != null && StringUtils.isNotBlank(planEntity.getSgmwCode())) {
            specification2 = Specifications.<PlanEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(planEntity.getSgmwCode()), "sgmwCode", planEntity.getSgmwCode())
                    .eq(planEntity.getYear() > 0, "year", planEntity.getYear())
                    .eq(planEntity.getMonth() > 0, "month", planEntity.getMonth())
                    .eq(StringUtils.isNotEmpty(planEntity.getVsn()), "vsn", planEntity.getVsn())
                    .build();
        }

//        if(planEntity!=null&& StringUtils.isNotBlank(planEntity.getDistributorName())) {
//            specification3 = Specifications.<PlanEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .like(StringUtils.isNotEmpty(planEntity.getSgmwCode()), "sgmwCode", "%" + planEntity.getSgmwCode()+ "%")
//                    .like(StringUtils.isNotEmpty(planEntity.getDistributorCode()), "distributorCode", "%" + planEntity.getDistributorCode() + "%")
//                    .like(StringUtils.isNotEmpty(planEntity.getDistributorName()), "distributorName", "%" + planEntity.getDistributorName()+ "%")
//
//                    .build();
//        }

        //组合查询条件
        specification = Specifications.<PlanEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<PlanEntity> pages = planRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public List<PlanEntity> saveAll(List<PlanEntity> importEntities) {
//        return planRepository.saveAll(importEntities);


        List<PlanEntity> resultEntities = new ArrayList<>();
        List<PlanEntity> addEntities = new ArrayList<>();
        try {
            List<DistributorEntity> listDistributor = distributorRepository.findAll();
            for (PlanEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getSgmwCode().equalsIgnoreCase(entity.getSgmwCode())).collect(Collectors.toList());
                if (list.size() == 1) {
                    entity.setDistributorCode(list.get(0).getCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                    if (entity.getYear() != null && StringUtils.isNotBlank(entity.getYear().toString()) && entity.getMonth() != null && StringUtils.isNotBlank(entity.getMonth().toString())) {
                        addEntities.add(entity);
                    } else {
                        System.out.println(entity.getDistributorCode()+"==========");
                        resultEntities.add(entity);
                    }
                } else {
                    resultEntities.add(entity);
                }
            }
            if(resultEntities.size()>0){
                ZkUtils.showError("缺少经销商！", "系统提示");
            }else{
                //3. 保存数据
                planRepository.saveAll(addEntities);
            }
//            //2. 删除对应vin的数据
//            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
//            vehicleInvRepository.deleteAllByVinIn(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return resultEntities;
        }
    }

    /**
     * 查询所有
     * @return
     */
    public List<PlanEntity> findAll() {
        return planRepository.findAll();
    }
    /**
     * @Description: 保存客户余额
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:56
     */
    public PlanEntity save(PlanEntity planEntity) {
        try {
            return planRepository.save(planEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:59
     */

    public boolean delete(PlanEntity balanceEntity) {
        try {
            planRepository.delete(balanceEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: 通过id删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:02
     */
    public boolean deleteById(String id) {
        try {
            planRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @Description: 通过id查询对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:04
     */

    public PlanEntity findById(String id) {
        try {
            return planRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
