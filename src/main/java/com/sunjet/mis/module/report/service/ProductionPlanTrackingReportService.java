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
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
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

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("productionPlanTrackingReportService")
public class ProductionPlanTrackingReportService {

    @Autowired
    private ProductionPlanTrackingReportRepository productionPlanTrackingReportRepository;
    @Autowired
    private AvailableInventoryRepository availableInventoryRepository;
    @Autowired
    private PlanRepository planRepository;


    public PageResult<ProductionPlanTrackingReportView> getPageList(PageParam<ProductionPlanTrackingReportView> pageParam) {
        //1.查询条件
        ProductionPlanTrackingReportView productionPlanTrackingReportView = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ProductionPlanTrackingReportView> specification = null;
        Specification<ProductionPlanTrackingReportView> specification2 = null;
        Specification<ProductionPlanTrackingReportView> specification3 = null;
        System.out.println(productionPlanTrackingReportView.getYear() + "-" + productionPlanTrackingReportView.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (productionPlanTrackingReportView != null && StringUtils.isNotBlank(productionPlanTrackingReportView.getVsn())) {
                specification2 = Specifications.<ProductionPlanTrackingReportView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(productionPlanTrackingReportView.getYear()!=null, "year", productionPlanTrackingReportView.getYear())
                        .eq(productionPlanTrackingReportView.getMonth()!=null,"month", productionPlanTrackingReportView.getMonth())
                        .eq(StringUtils.isNotEmpty(productionPlanTrackingReportView.getVsn()), "vsn", productionPlanTrackingReportView.getVsn())
                        .eq(StringUtils.isNotEmpty(productionPlanTrackingReportView.getVehicleModel()), "vehicleModel", productionPlanTrackingReportView.getVehicleModel())
                        .build();
            }
        } else {
            if (productionPlanTrackingReportView != null ) {
                specification2 = Specifications.<ProductionPlanTrackingReportView>and()
                //第一个参数为真假值，第二各为实体变量名，第三个为条件
                .eq(productionPlanTrackingReportView.getYear()!=null, "year", productionPlanTrackingReportView.getYear())
                .eq(productionPlanTrackingReportView.getMonth()!=null,"month", productionPlanTrackingReportView.getMonth())
                .eq(StringUtils.isNotEmpty(productionPlanTrackingReportView.getVsn()), "vsn", productionPlanTrackingReportView.getVsn())
                .eq(StringUtils.isNotEmpty(productionPlanTrackingReportView.getVehicleModel()), "vehicleModel", productionPlanTrackingReportView.getVehicleModel())
               .build();
            }
        }

        //组合查询条件
        specification = Specifications.<ProductionPlanTrackingReportView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ProductionPlanTrackingReportView> pages = productionPlanTrackingReportRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

//        List<Object[]> ailist = availableInventoryRepository.findVehicleModelAndVsn();
        if(pages.getContent().size() > 0) {
            for (ProductionPlanTrackingReportView pptv:pages.getContent()){
                LinkedList<Integer> days = new LinkedList<Integer>();
                for(int i =0;i<=30;i++){
                    days.add(i,0);
                }
                List<Object[]> daylist = availableInventoryRepository.findDayAndDayAmount(pptv.getVehicleModel(),pptv.getVsn(),pptv.getYear().toString(),pptv.getMonth().toString());
                for(Object[] obj:daylist){
                    //System.out.println(pptv.getVehicleModel() +"=="+pptv.getVsn()+"=="+obj[0]+"==="+obj[3]);
                    days.set(Integer.parseInt(obj[0].toString())-1,Integer.parseInt(obj[3].toString()));
                    pptv.getCompleteNumber();
                }
                pptv.setDays(days);

                Integer firstWeekAmount =  planRepository.findFirstWeekAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                Integer secondWeekAmount =  planRepository.findSecondWeekAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                Integer thirdWeekAmount =  planRepository.findThirdWeekAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                Integer fourthWeekAmount =  planRepository.findFourthWeekAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                Integer fifthWeekAmount =  planRepository.findFifthWeekAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                pptv.setFifthWeekPlan(firstWeekAmount);
                pptv.setSecondWeekPlan(secondWeekAmount);
                pptv.setThirdWeekPlan(thirdWeekAmount);
                pptv.setFourthWeekPlan(fourthWeekAmount);
                pptv.setFifthWeekPlan(fifthWeekAmount);

                String requiredAmount =  planRepository.findRequiredAmount(pptv.getVehicleName(),pptv.getVsn(),pptv.getYear(),pptv.getMonth());
                if(requiredAmount != null && StringUtils.isNotBlank(requiredAmount)) {
                    pptv.setMonthPlan(Integer.parseInt(requiredAmount));
                }
                //第1周完成
                int firstWeekComplete = 0;
                if(productionPlanTrackingReportView.getFirstWeek()!=null&& productionPlanTrackingReportView.getFirstWeek().size()>0) {
                    for (Integer day : productionPlanTrackingReportView.getFirstWeek()) {
                        firstWeekComplete += pptv.getDays().get(day-1);
                    }
                }else{
                    for(int i =0;i<=6;i++){
                        firstWeekComplete+=pptv.getDays().get(i);
                    }
                }
                pptv.setFirstWeekComplete(firstWeekComplete);
                //第2周完成
                int secondWeekComplete = 0;
                if(productionPlanTrackingReportView.getSecondWeek()!=null&&productionPlanTrackingReportView.getSecondWeek().size()>0) {
                    for (Integer day : productionPlanTrackingReportView.getSecondWeek()) {
                        secondWeekComplete += pptv.getDays().get(day-1);
                    }
                }else{
                    for(int i =7;i<=13;i++){
                        secondWeekComplete+=pptv.getDays().get(i);
                    }
                }
                pptv.setSecondWeekComplete(secondWeekComplete);
                //第3周完成
                int thirdWeekComplete = 0;
                if(productionPlanTrackingReportView.getThirdWeek()!=null&&productionPlanTrackingReportView.getThirdWeek().size()>0) {
                    for (Integer day : productionPlanTrackingReportView.getThirdWeek()) {
                        thirdWeekComplete += pptv.getDays().get(day-1);
                    }
                }else{
                    for(int i =14;i<=20;i++){
                        thirdWeekComplete+=pptv.getDays().get(i);
                    }
                }
                pptv.setThirdWeekComplete(thirdWeekComplete);
                //第4周完成
                int fourthWeekComplete = 0;
                if(productionPlanTrackingReportView.getFourthWeek()!=null&&productionPlanTrackingReportView.getFourthWeek().size()>0) {
                    for (Integer day : productionPlanTrackingReportView.getFourthWeek()) {
                        fourthWeekComplete += pptv.getDays().get(day-1);
                    }
                }else{
                    for(int i =21;i<=27;i++){
                        fourthWeekComplete+=pptv.getDays().get(i);
                    }
                }
                pptv.setFourthWeekComplete(fourthWeekComplete);
                //第5周完成
                int fifthWeekComplete = 0;
                if(productionPlanTrackingReportView.getFifthWeek()!=null&&productionPlanTrackingReportView.getFifthWeek().size()>0) {
                    for (Integer day : productionPlanTrackingReportView.getFifthWeek()) {
                        fifthWeekComplete += pptv.getDays().get(day-1);
                    }
                }else{
                    for(int i =28;i<=30;i++){
                        fifthWeekComplete+=pptv.getDays().get(i);
                    }
                }
                pptv.setFifthWeekComplete(fifthWeekComplete);

                int complete = 0;
                for(Integer i:pptv.getDays()){
                    complete+=i;
                }
                pptv.setCompleteNumber(complete);

                if(pptv.getMonthPlan()>0){
                    float num= (float)complete/pptv.getMonthPlan();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String s = df.format(num*100);
                    String intNumber = s.substring(0,s.indexOf("."));
                    pptv.setCompletion(intNumber+"%");
                }
            }
        }

//
//        if (pages.getContent().size() > 0) {
//            for(ProductionPlanTrackingReportView pptv:pages.getContent()){
//                for(AvailableInventoryEntity ai:ailist){
//                    if(pptv.getVehicleModel().equals(ai.getVehicleModel()) && pptv.getVsn().equals(ai.getVsn())){
//                        System.out.println(pptv.getInboundDate());
//                    }
//                }
//            }
//        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public List<PlanEntity> findAllByModelAndVsn(ProductionPlanTrackingReportView pptv){
        List<PlanEntity> list= planRepository.findAllByModelAndVsn(pptv.getVehicleModel()+"%",pptv.getVsn(),pptv.getYear(),pptv.getMonth());
        return list;
    }
}
