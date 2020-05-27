package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.PredictingDemandRefittedVehiclesRepository;
import com.sunjet.mis.module.report.view.PredictingDemandRefittedVehiclesView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SUNJET-YFS
 * @Title: PredictingDemandRefittedVehiclesService
 * @ProjectName mis
 * @Description: 货改车月预测改装车销售需求计划
 * @date 2019/4/2423:43
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("predictingDemandRefittedVehiclesService")
public class PredictingDemandRefittedVehiclesService {

    @Autowired
    private PredictingDemandRefittedVehiclesRepository predictingDemandRefittedVehiclesRepository;

    public PageResult<PredictingDemandRefittedVehiclesView> getPageList(PageParam<PredictingDemandRefittedVehiclesView> pageParam) {
        //1.查询条件
        PredictingDemandRefittedVehiclesView predictingDemandRefittedVehiclesView = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<PredictingDemandRefittedVehiclesView> specification = null;
        if (predictingDemandRefittedVehiclesView != null) {
            specification = Specifications.<PredictingDemandRefittedVehiclesView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(predictingDemandRefittedVehiclesView.getAbsvsn() != null, "absvsn", predictingDemandRefittedVehiclesView.getAbsvsn())
                    .between((predictingDemandRefittedVehiclesView.getStartDate() != null && predictingDemandRefittedVehiclesView.getEndDate() != null), "ztime", predictingDemandRefittedVehiclesView.getStartDate(), predictingDemandRefittedVehiclesView.getEndDate())
                    .build();
        }
        //3.执行查询
        Page<PredictingDemandRefittedVehiclesView> pages = predictingDemandRefittedVehiclesRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}
