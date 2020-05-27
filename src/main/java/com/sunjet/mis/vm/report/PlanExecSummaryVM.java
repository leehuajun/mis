package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanExecSummaryService;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PlanExecSummaryViewBak;
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

public class PlanExecSummaryVM extends ListVM<PlanExecSummaryView> {
    @WireVariable
    private PlanExecSummaryService planExecSummaryService;

    @Getter
    @Setter
    private PlanExecSummaryView planExecSummaryView = PlanExecSummaryView.builder().build();

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
//            TableHeaderInfo.builder().label("明细").build(),
            TableHeaderInfo.builder().label("年度" ).build(),
            TableHeaderInfo.builder().label("月份" ).build(),
            TableHeaderInfo.builder().label("经销商代码(工业)").build(),
            TableHeaderInfo.builder().label("经销商代码(SGMW)").build(),
            TableHeaderInfo.builder().label("经销商名称").build(),
            TableHeaderInfo.builder().label("VSN").build(),
            TableHeaderInfo.builder().label("申报数量").build(),
            TableHeaderInfo.builder().label("满足数量").build(),
            TableHeaderInfo.builder().label("月满足计划").build(),
            TableHeaderInfo.builder().label("余额").build(),
            TableHeaderInfo.builder().label("已分配数量").build(),
            TableHeaderInfo.builder().label("已发数量").build()
    );

    @Init
    public void init() {
//        LocalDate.now().getYear();

        for(int i=2015;i<=LocalDate.now().getYear();i++){
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(PlanExecSummaryView.class.getSimpleName());

        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            this.planExecSummaryView.setSgmwCode(this.getActiveUser().getLogId());
            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
        }
        planExecSummaryView.setYear(LocalDate.now().getYear());
        planExecSummaryView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(planExecSummaryView, Order.DESC, "sgmwCode");
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
        refreshPage(planExecSummaryView);
        getPageList();
    }


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
            planExecSummaryView.setSgmwCode(this.getKeyword());
            planExecSummaryView.setDistributorCode(this.getKeyword());
            planExecSummaryView.setDistributorName(this.getKeyword());
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

        this.setPageResult(planExecSummaryService.getPageList(this.getPageParam()));
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
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.getPageList();
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
        List<PlanExecSummaryView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (PlanExecSummaryView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("year");
        keyList.add("month");
        keyList.add("distributorCode");
        keyList.add("sgmwCode");
        keyList.add("distributorName");
        keyList.add("vsn");
        keyList.add("requiredAmount");
        keyList.add("agreedAmount");
        keyList.add("monthPlanAmount");
        keyList.add("balance");
        keyList.add("allotAmount");
        keyList.add("delivedAmount");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }
}
