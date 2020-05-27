package com.sunjet.mis.module.distributor.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.DateHelper;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.distributor.entity.DistributionDemandSuperadditionEntity;

import com.sunjet.mis.module.distributor.repository.DistributionDemandSuperadditionRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: DistributionDemandSuperadditionService
 * @ProjectName mis
 * @Description: 经销商需求追加表
 * @date 2019/3/1515:34
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("distributionDemandSuperadditionService")
public class DistributionDemandSuperadditionService {
    @Autowired
    private DistributionDemandSuperadditionRepository distributionDemandSuperadditionRepository;

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */

    public PageResult<DistributionDemandSuperadditionEntity> getPageList(PageParam<DistributionDemandSuperadditionEntity> pageParam) {

        //1.查询条件
        DistributionDemandSuperadditionEntity distributionDemandSuperadditionEntity = pageParam.getInfoWhere();
        //  System.out.println("我的组织ID是:" + authObject.getOrg().getOrgId());
        //2.设置查询参数
        Specification<DistributionDemandSuperadditionEntity> specification = null;
        if (distributionDemandSuperadditionEntity != null) {
            specification = Specifications.<DistributionDemandSuperadditionEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(distributionDemandSuperadditionEntity.getVsn()), "vsn", distributionDemandSuperadditionEntity.getVsn())
                    .eq(StringUtils.isNotBlank(distributionDemandSuperadditionEntity.getDistributorCode()), "distributorCode", distributionDemandSuperadditionEntity.getDistributorCode())
                    .eq(distributionDemandSuperadditionEntity.getCycle() != null, "cycle", distributionDemandSuperadditionEntity.getCycle())
                    .between((distributionDemandSuperadditionEntity.getStartDate() != null && distributionDemandSuperadditionEntity.getEndDate() != null), "applicationTime", distributionDemandSuperadditionEntity.getStartDate(), distributionDemandSuperadditionEntity.getEndDate())
                    .build();

        }

        //3.执行查询
        Page<DistributionDemandSuperadditionEntity> pages = distributionDemandSuperadditionRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * @param importEntities
     * @return
     */
    public List<DistributionDemandSuperadditionEntity> saveAll(List<DistributionDemandSuperadditionEntity> importEntities) {

        return distributionDemandSuperadditionRepository.saveAll(importEntities);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<DistributionDemandSuperadditionEntity> findAll() {
        return distributionDemandSuperadditionRepository.findAll();

    }

}
