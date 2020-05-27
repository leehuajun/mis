package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.ProductionPlanTrackingReportService;
import com.sunjet.mis.module.report.service.ProductionPlanTrackingSummaryService;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingSummaryView;
import com.sunjet.mis.module.warehouse.entity.SpecialVehicleMonthlyPlanBalanceEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*生产计划跟踪汇总*/
public class ProductionPlanTrackingSummaryVM extends ListVM<ProductionPlanTrackingSummaryView> {
    @WireVariable
    private ProductionPlanTrackingSummaryService productionPlanTrackingSummaryService;

    @Getter
    @Setter
    private ProductionPlanTrackingSummaryView productionPlanTrackingSummaryView = ProductionPlanTrackingSummaryView.builder().build();

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("产地").build(),
            TableHeaderInfo.builder().label("车系").build(),
            TableHeaderInfo.builder().label("类型").build(),
            TableHeaderInfo.builder().label("月度计划").build(),
            TableHeaderInfo.builder().label("第1周平衡数量").build(),
            TableHeaderInfo.builder().label("第2周平衡数量").build(),
            TableHeaderInfo.builder().label("第3周平衡数量").build(),
            TableHeaderInfo.builder().label("第4周平衡数量").build(),
            TableHeaderInfo.builder().label("第5周平衡数量").build(),
            TableHeaderInfo.builder().label("月度已入库完成").build(),
            TableHeaderInfo.builder().label("未完成数量").build(),
            TableHeaderInfo.builder().label("完成率").build()

    );


    @Init
    public void init() {
        LocalDate.now().getYear();
        for(int i=2015;i<=LocalDate.now().getYear();i++){
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(ProductionPlanTrackingSummaryView.class.getSimpleName());

//        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
//            this.planReportView.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        productionPlanTrackingSummaryView.setYear(LocalDate.now().getYear());
        productionPlanTrackingSummaryView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(productionPlanTrackingSummaryView, Order.DESC, "origin");
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
        refreshPage(productionPlanTrackingSummaryView);
        getPageList();
    }

    /**
     * 新增
     */

    @Command
    @NotifyChange("showForm")
    public void showForm() {
        //refreshFirstPage(targetOrderEntity, Order.DESC, "name");
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

        this.setPageResult(productionPlanTrackingSummaryService.getPageList(this.getPageParam()));
    }

//    @Command
//    public void openPlanList(@BindingParam("model") PlanExecSummaryView model) throws TabDuplicateException {
//        Map<String, Object> map = new HashMap<>();
//        map.put("model", model);
//        ZkTabboxUtil.newTab(model.getId(), "详情", true, ZkTabboxUtil.OverFlowType.HIDDEN, "/views/report/plan_exec_summary/plan_detail.zul", map);
//    }
//
//    public String genStyle(PlanEntity entity) {
//        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
//            return "background:#ff9600";
//        }
//        return "";
//    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }

    @Command
    public void calculate(@BindingParam("vehicleModel") String vehicleModel,@BindingParam("vsn") String vsn,@BindingParam("month") String month) {
        System.out.println(vehicleModel+"=="+vsn+"=="+month+"==");
    }

    /**
     * 重置origin
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.productionPlanTrackingSummaryView.setOrigin(null);
        this.productionPlanTrackingSummaryView.setYear(LocalDate.now().getYear());
        this.productionPlanTrackingSummaryView.setMonth(LocalDate.now().getMonthValue());
        this.getPageList();
    }


    /**
     * 导出
     */
    @Command
    @NotifyChange("*")
    @Override
    public void export(@BindingParam("type") String type) {
        List<String> title = new ArrayList<>();
        tableHeaderList.forEach(e -> {
            if (!"行号".equals(e.getLabel())) {
                title.add(e.getLabel());
            }
        });
        List<Map<String, Object>> maps = new ArrayList<>();
        //当前页
        List<ProductionPlanTrackingSummaryView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (ProductionPlanTrackingSummaryView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("origin");
        keyList.add("vehicleSeries");
        keyList.add("type");
        keyList.add("monthPlan");
        keyList.add("firstWeekBalance ");
        keyList.add("secondWeekBalance ");
        keyList.add("thirdWeekBalance ");
        keyList.add("fourthWeekBalance ");
        keyList.add("fifthWeekBalance ");
        keyList.add("completeNumber");
        keyList.add("unfinishedNumber");
        keyList.add("completion");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }
}
