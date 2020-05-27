package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.ProductionPlanTrackingReportRepository;
import com.sunjet.mis.module.report.repository.ProductionPlanTrackingSummaryRepository;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingSummaryView;
import com.sunjet.mis.module.warehouse.repository.AvailableInventoryRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("productionPlanTrackingSummaryService")
public class ProductionPlanTrackingSummaryService {

    @Autowired
    private ProductionPlanTrackingSummaryRepository productionPlanTrackingSummaryRepository;
    @Autowired
    private AvailableInventoryRepository availableInventoryRepository;
    @Autowired
    private PlanRepository planRepository;


    public PageResult<ProductionPlanTrackingSummaryView> getPageList(PageParam<ProductionPlanTrackingSummaryView> pageParam) {
        //1.查询条件
        ProductionPlanTrackingSummaryView productionPlanTrackingSummaryView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ProductionPlanTrackingSummaryView> specification = null;
        Specification<ProductionPlanTrackingSummaryView> specification2 = null;
        Specification<ProductionPlanTrackingSummaryView> specification3 = null;
        System.out.println(productionPlanTrackingSummaryView.getYear() + "-" + productionPlanTrackingSummaryView.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (productionPlanTrackingSummaryView != null && StringUtils.isNotBlank(productionPlanTrackingSummaryView.getVehicleSeries())) {
                specification2 = Specifications.<ProductionPlanTrackingSummaryView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(productionPlanTrackingSummaryView.getYear()!=null, "year", productionPlanTrackingSummaryView.getYear())
                        .eq(productionPlanTrackingSummaryView.getMonth()!=null,"month", productionPlanTrackingSummaryView.getMonth())
                        .like(StringUtils.isNotEmpty(productionPlanTrackingSummaryView.getOrigin()), "origin", "%" + productionPlanTrackingSummaryView.getOrigin() + "%")
                        .build();
            }
        } else {
            if (productionPlanTrackingSummaryView != null ) {
                specification2 = Specifications.<ProductionPlanTrackingSummaryView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(productionPlanTrackingSummaryView.getYear()!=null, "year", productionPlanTrackingSummaryView.getYear())
                        .eq(productionPlanTrackingSummaryView.getMonth()!=null,"month", productionPlanTrackingSummaryView.getMonth())
                        .like(StringUtils.isNotEmpty(productionPlanTrackingSummaryView.getOrigin()), "origin", "%"+productionPlanTrackingSummaryView.getOrigin()+ "%")
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<ProductionPlanTrackingSummaryView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ProductionPlanTrackingSummaryView> pages = productionPlanTrackingSummaryRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

//        List<Object[]> ailist = availableInventoryRepository.findVehicleModelAndVsn();
        if(pages.getContent().size() > 0) {
            for (ProductionPlanTrackingSummaryView ppts:pages.getContent()){
                ppts.setUnfinishedNumber(ppts.getMonthPlan()-ppts.getCompleteNumber());
                if(ppts.getCompleteNumber()>0 && ppts.getMonthPlan()>0){
                    float num= (float)ppts.getCompleteNumber()/ppts.getMonthPlan();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String s = df.format(num*100);
                    String intNumber = s.substring(0,s.indexOf("."));
                    ppts.setCompletion(intNumber+"%");
                }
            }
//            for (ProductionPlanTrackingSummaryView ppts:pages.getContent()){
//                if(StringUtils.isNotBlank(ppts.getVehicleSeries())&&StringUtils.isNotBlank(ppts.getType())){
////                    if(ppts.getVehicleSeries()){
////
////                    }
//                }
//            }

        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}
