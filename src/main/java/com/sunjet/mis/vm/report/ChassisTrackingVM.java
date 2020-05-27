package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.ChassisTrackingService;
import com.sunjet.mis.module.report.service.PlanExecSummaryService;
import com.sunjet.mis.module.report.service.ProductionPlanTrackingReportService;
import com.sunjet.mis.module.report.view.ChassisTrackReportView;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.time.LocalDate;
import java.util.*;

public class ChassisTrackingVM extends ListVM<ChassisTrackingView> {
    @WireVariable
    private ChassisTrackingService chassisTrackingService;

    @Getter
    @Setter
    private ChassisTrackingView chassisTrackingView = ChassisTrackingView.builder().build();

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    @Init
    public void init() {
        LocalDate.now().getYear();

        for(int i=2015;i<=LocalDate.now().getYear();i++){
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(PlanEntity.class.getSimpleName());

//        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
//            this.planExecSummaryView.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        chassisTrackingView.setYear(LocalDate.now().getYear());
        chassisTrackingView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(chassisTrackingView, Order.DESC, "vehicleModel");
        getPageList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

    }

    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(chassisTrackingView);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
            chassisTrackingView.setVehicleModel(this.getKeyword());
            chassisTrackingView.setVariety(this.getKeyword());
        }
        getPageList();
    }

    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        this.getPageList();
    }

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(chassisTrackingService.getPageList(this.getPageParam()));
    }

    @Command
    public void openPlanList(@BindingParam("model") ChassisTrackingView model) throws TabDuplicateException {
        Map<String, Object> map = new HashMap<>();
        map.put("model", model);
        ZkTabboxUtil.newTab(model.getId(), "详情", true, ZkTabboxUtil.OverFlowType.HIDDEN, "/views/report/plan_exec_summary/plan_detail.zul", map);
    }

    public String genStyle(ChassisTrackingView entity) {
        if (StringUtils.isBlank(entity.getBatchNumber()) || StringUtils.isBlank(entity.getVariety())) {
            return "background:#ff9600";
        }
        return "";
    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }

}
