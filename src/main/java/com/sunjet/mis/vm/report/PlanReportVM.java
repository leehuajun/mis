package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanReportService;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PlanReportView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
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
/**
 * 计划执行汇总报表
 * */
public class PlanReportVM extends ListVM<PlanReportView> {
    @WireVariable
    private PlanReportService planReportService;

    @Getter
    @Setter
    private PlanReportView planReportView = PlanReportView.builder().build();

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    @Getter
    private List<String> types = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("100px").build(),
            TableHeaderInfo.builder().label("类型").width("60px").build(),
            TableHeaderInfo.builder().label("区域").width("120px").build(),
            TableHeaderInfo.builder().label("省份").width("100px").build(),
            TableHeaderInfo.builder().label("月度需求").width("100px").build(),
            TableHeaderInfo.builder().label("月度满足数").width("60px").build(),
            TableHeaderInfo.builder().label("累计分车").width("120px").build(),
            TableHeaderInfo.builder().label("完成率").width("80px").build()
    );

    @Init
    public void init() {
//        planReportView.setType("改装车");
        LocalDate.now().getYear();
        types = findType();
        for(int i=2015;i<=LocalDate.now().getYear();i++){
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(PlanReportView.class.getSimpleName());

//        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
//            this.planReportView.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        planReportView.setYear(LocalDate.now().getYear());
        planReportView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(planReportView, Order.DESC, "type");
        getPageList();
    }

    public List<String> findType(){
        return planReportService.findType();
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
        refreshPage(planReportView);
        getPageList();
    }



    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {

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

        this.setPageResult(planReportService.getPageList(this.getPageParam()));
    }

    @Command
    public void openPlanList(@BindingParam("model") PlanExecSummaryView model) throws TabDuplicateException {
        Map<String, Object> map = new HashMap<>();
        map.put("model", model);
        ZkTabboxUtil.newTab(model.getId(), "详情", true, ZkTabboxUtil.OverFlowType.HIDDEN, "/views/report/plan_exec_summary/plan_detail.zul", map);
    }

    public String genStyle(PlanEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
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
    /**
     * 新增
     */

    @Command
    @NotifyChange("showForm")
    public void openForm() {
        //refreshFirstPage(targetOrderEntity, Order.DESC, "name");
        getPageList();
    }
    /**
     * 导出
     */
    @Command
    @NotifyChange("*")
    @Override
    public void export(@BindingParam("type") String type) {
        //List<List<String>> titleList = new ArrayList<>();
        //List<String> title1 = new ArrayList<>();
        List<String> title = new ArrayList<>();
        tableHeaderList.forEach(e -> {
            if (!"行号".equals(e.getLabel())) {
                title.add(e.getLabel());
            }
        });
        List<Map<String, Object>> maps = new ArrayList<>();
        //当前页
        List<PlanReportView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (PlanReportView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("type");
        keyList.add("region");
        keyList.add("province");
        keyList.add("requiredAmount");
        keyList.add("agreedAmount");
        keyList.add("allot");
        keyList.add("completion");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }



    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.getPageList();
    }

}
