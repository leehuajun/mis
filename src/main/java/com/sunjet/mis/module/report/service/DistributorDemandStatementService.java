package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.distributor.entity.DistributionDemandSuperadditionEntity;
import com.sunjet.mis.module.report.entity.DistributorDemandStatementEntity;
import com.sunjet.mis.module.report.repository.DistributorDemandStatementRepositoty;
import com.sunjet.mis.module.report.repository.DistributorDemandStatementViewRepositoty;
import com.sunjet.mis.module.report.view.DistributorDemandStatementView;
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


/**
 * @author SUNJET-YFS
 * @Title: DistributorDemandStatementService
 * @ProjectName mis
 * @Description: 经销商需求表
 * @date 2019/3/1213:50
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("distributorDemandStatementService")
public class DistributorDemandStatementService {

    @Autowired
    private DistributorDemandStatementRepositoty distributorDemandStatementRepositoty;
    @Autowired
    private DistributorDemandStatementViewRepositoty distributorDemandStatementViewRepositoty;


    public PageResult<DistributorDemandStatementView> getPageList(PageParam<DistributorDemandStatementView> pageParam) {
        //1.查询条件
        DistributorDemandStatementView distributorDemandStatementView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DistributorDemandStatementView> specification = null;

        if (distributorDemandStatementView != null) {
            specification = Specifications.<DistributorDemandStatementView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(distributorDemandStatementView.getVsn()), "vsn", distributorDemandStatementView.getVsn())
                    .like(StringUtils.isNotBlank(distributorDemandStatementView.getCustomerCode()), "customerCode", distributorDemandStatementView.getCustomerCode())
                    .like(StringUtils.isNotBlank(distributorDemandStatementView.getCustomerName()), "customerName", distributorDemandStatementView.getCustomerName())
                    .between((distributorDemandStatementView.getStartDate() != null && distributorDemandStatementView.getEndDate() != null), "producerDate", distributorDemandStatementView.getStartDate(), distributorDemandStatementView.getEndDate())
                    .build();

        }

        Page<DistributorDemandStatementView> pages = distributorDemandStatementViewRepositoty.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * @param importEntities
     * @return
     */

    public List<DistributorDemandStatementEntity> saveAll(List<DistributorDemandStatementEntity> importEntities) {

        return distributorDemandStatementRepositoty.saveAll(importEntities);
    }

    /**
     * 查询所有
     *
     * @return
     */

    public List<DistributorDemandStatementView> findAll() {
        return distributorDemandStatementViewRepositoty.findAll();
    }

    /**
     * @Description: 保存配车单明细
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */


}

