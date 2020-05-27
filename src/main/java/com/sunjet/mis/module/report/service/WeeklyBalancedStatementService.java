package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.WeeklyBalancedStatementRepository;
import com.sunjet.mis.module.report.view.CarForecastDemandPlanningView;
import com.sunjet.mis.module.report.view.WeeklyBalancedStatementView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: WeeklyBalancedStatementService
 * @ProjectName mis
 * @Description: 周产平衡报表
 * @date 2019/3/711:10
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("weeklyBalancedStatementService")
public class WeeklyBalancedStatementService {
    @Autowired
    private WeeklyBalancedStatementRepository weeklyBalancedStatementRepository;

    public PageResult<WeeklyBalancedStatementView> getPageList(PageParam<WeeklyBalancedStatementView> pageParam) {

            //1.查询条件
            WeeklyBalancedStatementView weeklyBalancedStatementView = pageParam.getInfoWhere();
            //2.设置查询参数
            Specification<WeeklyBalancedStatementView> specification = null;
            if (weeklyBalancedStatementView != null) {
                specification = Specifications.<WeeklyBalancedStatementView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(weeklyBalancedStatementView.getAbsVsn() !=null,"absVsn", weeklyBalancedStatementView.getAbsVsn())
                        .eq(weeklyBalancedStatementView.getVehicleType1() != null, "vehicleType1", weeklyBalancedStatementView.getVehicleType1())
                        .between((weeklyBalancedStatementView.getStartDate() != null && weeklyBalancedStatementView.getEndDate() != null), "inboundDate", weeklyBalancedStatementView.getStartDate(), weeklyBalancedStatementView.getEndDate())
                        .between((weeklyBalancedStatementView.getStartDate1() != null && weeklyBalancedStatementView.getEndDate1() != null), "scsj", weeklyBalancedStatementView.getStartDate1(), weeklyBalancedStatementView.getEndDate1())
                        .between((weeklyBalancedStatementView.getStartDate2() != null && weeklyBalancedStatementView.getEndDate2() != null), "fcsj", weeklyBalancedStatementView.getStartDate2(), weeklyBalancedStatementView.getEndDate2())
                        .build();
            }
            //3.执行查询
            Page<WeeklyBalancedStatementView> pages = weeklyBalancedStatementRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

            //5.返回
            return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 查询所有
     * @return
     */
    public List<WeeklyBalancedStatementView> findAll() {
        return weeklyBalancedStatementRepository.findAll();
    }


}
