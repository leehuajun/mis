package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.service.ChassisTrackingReportService;
import com.sunjet.mis.module.report.view.ChassisTrackReportView;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingSummaryView;
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

public class ChassisTrackingReportVM extends ListVM<ChassisTrackReportView> {
    @WireVariable
    private ChassisTrackingReportService chassisTrackingReportService;

    @Getter
    @Setter
    private ChassisTrackReportView chassisTrackReportView = ChassisTrackReportView.builder().build();

    @Getter
    @Setter
    private ChassisTrackingView chassisTrackingView = ChassisTrackingView.builder().build();

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("车系").width("60px").build(),
            TableHeaderInfo.builder().label("配置").width("60px").build(),
            TableHeaderInfo.builder().label("品种").width("60px").build(),
            TableHeaderInfo.builder().label("采购计划").width("100px").build(),
            TableHeaderInfo.builder().label("股份已分车").width("120px").build(),
            TableHeaderInfo.builder().label("股份待份车").width("70px").build(),
            TableHeaderInfo.builder().label("已入底盘库").width("70px").build(),
            TableHeaderInfo.builder().label("已分车待提").width("70px").build(),
            TableHeaderInfo.builder().label("重庆底盘库库存").width("70px").build(),

            TableHeaderInfo.builder().label("昆明前置库库存").width("70px").build(),
            TableHeaderInfo.builder().label("发柳州在途").width("70px").build(),
            TableHeaderInfo.builder().label("青岛底盘库库存").width("70px").build(),
            TableHeaderInfo.builder().label("青岛缓冲区库存").width("70px").build(),

            TableHeaderInfo.builder().label("柳州底盘库").width("70px").build(),
            TableHeaderInfo.builder().label("柳州在制").width("70px").build(),
            TableHeaderInfo.builder().label("实车库存小计").width("70px").build(),
            TableHeaderInfo.builder().label("资源合计").width("70px").build()

    );
    /* 底盘跟踪汇总*/
    @Init
    public void init() {
        LocalDate.now().getYear();

        for(int i=2015;i<=LocalDate.now().getYear();i++){
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(ChassisTrackReportView.class.getSimpleName());

//        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
//            this.planExecSummaryView.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        chassisTrackReportView.setYear(LocalDate.now().getYear());
        chassisTrackReportView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(chassisTrackReportView, Order.DESC, "id");
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
        refreshPage(chassisTrackReportView);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
            chassisTrackReportView.setVariety(this.getKeyword());
        }
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
        this.setPageResult(chassisTrackingReportService.getPageList(this.getPageParam()));
        if(this.getPageResult()!=null) {
            long i = this.getPageResult().getTotal();
            if(i>0){
                this.getPageParam().setPageSize((int) i);
            }
            this.setPageResult(chassisTrackingReportService.getPageList(this.getPageParam()));
        }
    }

    @Command
    public void openPlanList(@BindingParam("model") ChassisTrackReportView model) throws TabDuplicateException {
        Map<String, Object> map = new HashMap<>();
        map.put("model", model);
        ZkTabboxUtil.newTab(model.getId(), "详情", true, ZkTabboxUtil.OverFlowType.HIDDEN, "/views/report/plan_exec_summary/plan_detail.zul", map);
    }

    public String genStyle(ChassisTrackReportView entity) {
        if (StringUtils.isBlank(entity.getVehicleModel()) || StringUtils.isBlank(entity.getVariety())) {
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
        List<ChassisTrackReportView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (ChassisTrackReportView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("vehicleModel");
        keyList.add("config");
        keyList.add("variety");
        keyList.add("procurementPlan");
        keyList.add("sharesAlready");
        keyList.add("sharesWaiting");
        keyList.add("alreadyWarehouse");
        keyList.add("waitingExtract");
        keyList.add("CHONGQINGInventory");
        keyList.add("KUNMINGInventory");
        keyList.add("LIUZHOUOnTheWay");
        keyList.add("QINGDAOInventory");
        keyList.add("QINGDAOBufferInventory");
        keyList.add("LIUZHOUInventory");
        keyList.add("LIUZHOUMaking");
        keyList.add("vehicleSum");
        keyList.add("resourcesSum");
        ExcelUtil.listMapToExcel(title, keyList, maps);


    }
    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.chassisTrackReportView.setVehicleModel(null);
        this.chassisTrackReportView.setVariety(null);
        this.chassisTrackReportView.setYear(LocalDate.now().getYear());
        this.chassisTrackReportView.setMonth(LocalDate.now().getMonthValue());
        this.getPageList();
    }
}


