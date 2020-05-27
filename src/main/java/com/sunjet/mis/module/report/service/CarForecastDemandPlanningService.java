package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.CarForecastDemandPlanningRepository;
import com.sunjet.mis.module.report.view.CarForecastDemandPlanningView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SUNJET-YFS
 * @Title: CarForecastDemandPlanningService
 * @ProjectName mis
 * @Description: 客厢车月预测客厢车销售需求计划 - 客厢车
 * @date 2019/4/2810:29
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("carForecastDemandPlanningService")
public class CarForecastDemandPlanningService {
    @Autowired
    private CarForecastDemandPlanningRepository carForecastDemandPlanningRepository;

    public PageResult<CarForecastDemandPlanningView> getPageList(PageParam<CarForecastDemandPlanningView> pageParam) {
        //1.查询条件
        CarForecastDemandPlanningView carForecastDemandPlanningView = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<CarForecastDemandPlanningView> specification = null;
        if (carForecastDemandPlanningView != null) {
            specification = Specifications.<CarForecastDemandPlanningView>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                   // .eq(carForecastDemandPlanningView.getZtime() !=null,"ztime", carForecastDemandPlanningView.getZtime())
                    .between((carForecastDemandPlanningView.getStartDate() != null && carForecastDemandPlanningView.getEndDate() != null), "ztime", carForecastDemandPlanningView.getStartDate(), carForecastDemandPlanningView.getEndDate())
                    .eq(carForecastDemandPlanningView.getAbsvsn() != null, "absvsn", carForecastDemandPlanningView.getAbsvsn())
                    .build();
        }
        //3.执行查询
        Page<CarForecastDemandPlanningView> pages = carForecastDemandPlanningRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

}
