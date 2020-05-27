package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.DateHelper;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import com.sunjet.mis.module.report.repository.*;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PlanReportView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("planReportService")
public class PlanReportService {

    @Autowired
    private PlanReportRepository planReportRepository;
    @Autowired
    private  PlanRepository planRepository;

    public PageResult<PlanReportView> getPageList(PageParam<PlanReportView> pageParam) {
        //1.查询条件
        PlanReportView planReportView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PlanReportView> specification = null;
        Specification<PlanReportView> specification2 = null;
        Specification<PlanReportView> specification3 = null;
        System.out.println(planReportView.getYear() + "-" + planReportView.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (planReportView != null && StringUtils.isNotBlank(planReportView.getType())) {
                specification2 = Specifications.<PlanReportView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(planReportView.getYear()!=null, "year", planReportView.getYear())
                        .eq(planReportView.getMonth()!=null,"month", planReportView.getMonth())
                        .eq(StringUtils.isNotEmpty(planReportView.getType()), "type", planReportView.getType())
                        .like(StringUtils.isNotEmpty(planReportView.getProvince()), "province", "%" + planReportView.getProvince() + "%")
                        .build();
            }
        } else {
            if (planReportView != null ) {
                specification2 = Specifications.<PlanReportView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(planReportView.getYear()!=null, "year", planReportView.getYear())
                        .eq(planReportView.getMonth()!=null,"month", planReportView.getMonth())
                        .eq(StringUtils.isNotEmpty(planReportView.getType()), "type", planReportView.getType())
                        .like(StringUtils.isNotEmpty(planReportView.getProvince()), "province", "%" + planReportView.getProvince() + "%")
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<PlanReportView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<PlanReportView> pages = planReportRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public List<String> findType(){
        return planRepository.findType();
    }

}
