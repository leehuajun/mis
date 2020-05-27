package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.SolidPinReportRepository;
import com.sunjet.mis.module.report.view.SocialInventoryView;
import com.sunjet.mis.module.report.view.SolidPinReportView;
import com.sunjet.mis.module.report.view.WeeklyBalancedStatementView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinReportService
 * @ProjectName mis
 * @Description: 35实销整合报表service
 * @date 2019/2/1816:52
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("solidPinReportService")
public class SolidPinReportService {

    @Autowired
    private SolidPinReportRepository solidPinReportRepository;

    public PageResult<SolidPinReportView> getPageList(PageParam<SolidPinReportView> pageParam) {
        //1.查询条件
        SolidPinReportView solidPinReportView = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<SolidPinReportView> specification = null;
        //2.设置查询参数

        if (solidPinReportView != null) {
            specification = Specifications.<SolidPinReportView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(solidPinReportView.getVsn()), "vsn","%"+ solidPinReportView.getVsn()+"%")
                    .like(StringUtils.isNotBlank(solidPinReportView.getVin()), "vin", "%"+solidPinReportView.getVin()+"%")
                    .like(StringUtils.isNotBlank(solidPinReportView.getDistributorCode()), "distributorCode","%"+ solidPinReportView.getDistributorCode()+"%")
                    .like(StringUtils.isNotBlank(solidPinReportView.getDistributorName()), "distributorName", "%"+solidPinReportView.getDistributorName()+"%")
                    .like(StringUtils.isNotBlank(solidPinReportView.getCustomerName()), "customerName", "%"+solidPinReportView.getCustomerName()+"%")
                    .between((solidPinReportView.getStartDate() != null && solidPinReportView.getEndDate() != null), "invoiceDate", solidPinReportView.getStartDate(), solidPinReportView.getEndDate())
                    .build();
        }
        //3.执行查询
        Page<SolidPinReportView> pages = solidPinReportRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}
