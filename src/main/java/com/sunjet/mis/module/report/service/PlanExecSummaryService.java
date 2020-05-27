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
import com.sunjet.mis.module.report.view.PlanExecSummaryViewBak;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("planExecSummaryService")
public class PlanExecSummaryService {

    @Autowired
    private PlanExecSummaryRepository planExecSummaryRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private AllotVehicleRepository allotVehicleRepository;
    @Autowired
    private VehicleInvRepository vehicleInvRepository;

    public PageResult<PlanExecSummaryView> getPageList(PageParam<PlanExecSummaryView> pageParam) {
        //1.查询条件
        PlanExecSummaryView planExecSummaryEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PlanExecSummaryView> specification = null;
        Specification<PlanExecSummaryView> specification2 = null;
        Specification<PlanExecSummaryView> specification3 = null;
        System.out.println(planExecSummaryEntity.getYear() + "-" + planExecSummaryEntity.getMonth());

//        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (planExecSummaryEntity != null && StringUtils.isNotBlank(planExecSummaryEntity.getSgmwCode())) {
                specification2 = Specifications.<PlanExecSummaryView>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(planExecSummaryEntity.getYear()!=null, "year", planExecSummaryEntity.getYear())
                        .eq(planExecSummaryEntity.getMonth()!=null,"month", planExecSummaryEntity.getMonth())
                        .eq(StringUtils.isNotEmpty(planExecSummaryEntity.getSgmwCode()), "sgmwCode", planExecSummaryEntity.getSgmwCode())
                        .like(StringUtils.isNotEmpty(planExecSummaryEntity.getVsn()), "vsn", "%" + planExecSummaryEntity.getVsn() + "%")
                        .build();
            }
        } else {
            if (planExecSummaryEntity != null ) {
                specification2 = Specifications.<PlanExecSummaryView>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(planExecSummaryEntity.getYear()!=null, "year", planExecSummaryEntity.getYear())
                        .eq(planExecSummaryEntity.getMonth()!=null, "month", planExecSummaryEntity.getMonth())
                        .like(StringUtils.isNotEmpty(planExecSummaryEntity.getSgmwCode()), "sgmwCode", "%" + planExecSummaryEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(planExecSummaryEntity.getDistributorCode()), "distributorCode", "%" + planExecSummaryEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(planExecSummaryEntity.getDistributorName()), "distributorName", "%" + planExecSummaryEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(planExecSummaryEntity.getVsn()), "vsn", "%" + planExecSummaryEntity.getVsn() + "%")
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<PlanExecSummaryView>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<PlanExecSummaryView> pages = planExecSummaryRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        if (pages.getContent().size() > 0) {
            List<String> list = pages.getContent().stream().map(p -> p.getSgmwCode()).collect(Collectors.toList());
            List<PlanEntity> planEntities = planRepository.findAllByYearAndMonthAndSgmwCodeIn(planExecSummaryEntity.getYear(), planExecSummaryEntity.getMonth(), list);
            List<BalanceEntity> balanceEntities = balanceRepository.findAllBySgmwCodeIn(list);
            List<AllotVehicleEntity> allotVehicleEntities = allotVehicleRepository.findAllByInvoiceDateBetweenAndSgmwCodeIn(
                    DateHelper.getFirstDayByYeanAndMonth(planExecSummaryEntity.getYear(), planExecSummaryEntity.getMonth()),
                    DateHelper.getFirstDayOfNextMonthByYeanAndMonth(planExecSummaryEntity.getYear(), planExecSummaryEntity.getMonth()),
                    list);
            List<VehicleInvEntity> vehicleInvEntities = vehicleInvRepository.findAllByInvoiceTimeBetweenAndStatusEqualsAndSgmwCodeIn(DateHelper.getFirstDayByYeanAndMonth(planExecSummaryEntity.getYear(), planExecSummaryEntity.getMonth()),
                    DateHelper.getFirstDayOfNextMonthByYeanAndMonth(planExecSummaryEntity.getYear(), planExecSummaryEntity.getMonth()),
                    "已出库",
                    list);

            for (PlanExecSummaryView view : pages.getContent()) {
                List<PlanEntity> entities = planEntities.stream()
                        .filter(p -> p.getSgmwCode().equals(view.getSgmwCode()) && p.getVsn().equals(view.getVsn())).collect(Collectors.toList());
                for (PlanEntity entity : entities) {
                    view.setRequiredAmount(view.getRequiredAmount() == null ? 0 : view.getRequiredAmount() + entity.getRequiredAmount());
                    view.setAgreedAmount(view.getAgreedAmount() == null ? 0 : view.getAgreedAmount() + entity.getAgreedAmount());
                    view.setMonthPlanAmount((view.getMonthPlanAmount() == null ? 0 : view.getMonthPlanAmount())
                            + (entity.getFirstWeekAmount() == null ? 0 : entity.getFirstWeekAmount())
                            + (entity.getSecondWeekAmount() == null ? 0 : entity.getSecondWeekAmount())
                            + (entity.getThirdWeekAmount() == null ? 0 : entity.getThirdWeekAmount())
                            + (entity.getFourthWeekAmount() == null ? 0 : entity.getFourthWeekAmount())
                            + (entity.getFifthWeekAmount() == null ? 0 : entity.getFifthWeekAmount())
                            + (entity.getSixthWeekAmount() == null ? 0 : entity.getSixthWeekAmount()));
                }

                List<BalanceEntity> balanceEntities1 = balanceEntities.stream().filter(b -> b.getSgmwCode().equals(view.getSgmwCode())).collect(Collectors.toList());
                if (balanceEntities1.size() > 0) {
                    view.setBalance(balanceEntities1.get(0).getCreditBalance());
                }

                int count01 = allotVehicleEntities.stream().filter(av -> av.getSgmwCode().equals(view.getSgmwCode()) && av.getVsn().equals(view.getVsn())).collect(Collectors.toList()).size();
                view.setAllotAmount(count01);
                int count02 = vehicleInvEntities.stream().filter(vi -> vi.getSgmwCode().equals(view.getSgmwCode()) && vi.getVsn().equals(view.getVsn())).collect(Collectors.toList()).size();
                view.setDelivedAmount(count02);
            }
        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }





}
