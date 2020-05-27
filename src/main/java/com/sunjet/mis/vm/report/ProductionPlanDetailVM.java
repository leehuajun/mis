package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanService;
import com.sunjet.mis.module.report.service.ProductionPlanTrackingReportService;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlanDetailVM extends ListVM<PlanEntity> {
    @WireVariable
    private PlanService planService;
    @WireVariable
    private ProductionPlanTrackingReportService productionPlanTrackingReportService;


    @Getter
    @Setter
    private List<PlanEntity> planEntityList = new ArrayList<>();

    @Init
    public void init(){

        if(Executions.getCurrent().getArg().get("model")==null){
            ZkUtils.showError("参数不对，无法打开页面！", "系统提示");
            return ;
        }

        ProductionPlanTrackingReportView productionPlanTrackingReportView = (ProductionPlanTrackingReportView)Executions.getCurrent().getArg().get("model");
        planEntityList = productionPlanTrackingReportService.findAllByModelAndVsn(productionPlanTrackingReportView);

    }

}
