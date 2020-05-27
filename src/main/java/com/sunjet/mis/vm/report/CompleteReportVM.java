package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.service.DistributorCompleteReportService;
import com.sunjet.mis.module.report.view.DistributorCompleteReportView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.vm.helper.AuxHeaderInfo;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: wushi
 * @description: 指标完成报表
 * @Date: Created in 11:53 2019/2/21
 * @Modify by: wushi
 * @ModifyDate by: 11:53 2019/2/21
 */
public class CompleteReportVM extends ListVM<DistributorCompleteReportView> {


    @Getter
    @Setter
    private DistributorCompleteReportView distributorCompleteReportView = DistributorCompleteReportView.builder().build();

    @WireVariable
    private DistributorCompleteReportService distributorCompleteReportService;

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("地区").width("60px").build(),
            TableHeaderInfo.builder().label("省份").width("60px").build(),
            TableHeaderInfo.builder().label("级别").width("60px").build(),
            TableHeaderInfo.builder().label("经销商代码(SGMW)").width("100px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("120px").build(),
            //年度目标(合并)
            TableHeaderInfo.builder().label("年度目标").width("70px").build(),
            TableHeaderInfo.builder().label("年累开票").width("70px").build(),
            TableHeaderInfo.builder().label("目标差距").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            //年度目标(货改车)
            TableHeaderInfo.builder().label("年度目标").width("70px").build(),
            TableHeaderInfo.builder().label("年累开票").width("70px").build(),
            TableHeaderInfo.builder().label("目标差距").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            //年度目标(客改车)
            TableHeaderInfo.builder().label("年度目标").width("70px").build(),
            TableHeaderInfo.builder().label("年累开票").width("70px").build(),
            TableHeaderInfo.builder().label("目标差距").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            //月开票指标(合并)
            TableHeaderInfo.builder().label("开票目标").width("70px").build(),
            TableHeaderInfo.builder().label("累计开票").width("70px").build(),
            TableHeaderInfo.builder().label("目标差距").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            TableHeaderInfo.builder().label("需求缺口").width("70px").build(),
            //月开票指标(货改车)
            TableHeaderInfo.builder().label("开票指标").width("70px").build(),
            TableHeaderInfo.builder().label("累计开票指标").width("70px").build(),
            TableHeaderInfo.builder().label("目标差距").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            TableHeaderInfo.builder().label("需求缺口").width("70px").build(),
            //月开票指标(客改车)
            TableHeaderInfo.builder().label("开票目标").width("70px").build(),
            TableHeaderInfo.builder().label("累计开票").width("70px").build(),
            TableHeaderInfo.builder().label("累计开票指标").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            TableHeaderInfo.builder().label("需求缺口").width("70px").build(),
            //其他
            TableHeaderInfo.builder().label("发车余额").width("70px").build(),
            TableHeaderInfo.builder().label("分车条件").width("70px").build()
    );

    @Getter
    List<AuxHeaderInfo> auxHeaderInfos = Arrays.asList(
            AuxHeaderInfo.builder().title("经销商信息").firstCol(0).lastCol(4).isMerge(true).build(),
            AuxHeaderInfo.builder().title("年度目标(合并)").firstCol(5).lastCol(8).isMerge(true).build(),
            AuxHeaderInfo.builder().title("年度目标(货改车)").firstCol(9).lastCol(12).isMerge(true).build(),
            AuxHeaderInfo.builder().title("年度目标(客改车)").firstCol(13).lastCol(16).isMerge(true).build(),
            AuxHeaderInfo.builder().title("月开票指标(合并)").firstCol(17).lastCol(21).isMerge(true).build(),
            AuxHeaderInfo.builder().title("月开票指标(货改车)").firstCol(22).lastCol(26).isMerge(true).build(),
            AuxHeaderInfo.builder().title("月开票指标(客改车)").firstCol(27).lastCol(31).isMerge(true).build(),
            AuxHeaderInfo.builder().title("月开票指标(其他)").firstCol(32).lastCol(33).isMerge(true).build()

    );


    @Init
    public void init() {
        this.initPermissionStatus(DistributorCompleteReportView.class.getSimpleName());

        LocalDate.now().getYear();

        for (int i = 2015; i <= LocalDate.now().getYear(); i++) {
            years.add(i);
        }
        distributorCompleteReportView.setYear(LocalDate.now().getYear());
        distributorCompleteReportView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(distributorCompleteReportView, Order.ASC, "province.region");
        getPageList();
    }


    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(distributorCompleteReportService.getPageList(this.getPageParam()));
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(distributorCompleteReportView);
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
    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(distributorCompleteReportView, Order.ASC, "province.region");
        getPageList();
    }

    /**
     * 查询数据
     */
    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }


    @Command
    @NotifyChange("*")
    public void reset() {
        this.distributorCompleteReportView.setDistributorCode("");
        this.distributorCompleteReportView.setDistributorName("");
        this.distributorCompleteReportView.setYear(LocalDate.now().getYear());
        this.distributorCompleteReportView.setMonth(LocalDate.now().getMonthValue());
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
        List<DistributorCompleteReportView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            dataSet = distributorCompleteReportService.findAll(this.getPageParam().getInfoWhere());
        }

        for (DistributorCompleteReportView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("regionName");
        keyList.add("provinceName");
        keyList.add("level");
        keyList.add("distributorCode");
        keyList.add("distributorName");
        keyList.add("yearTotalTarget");
        keyList.add("yearTotalInvoice");
        keyList.add("yearTotalDifference");
        keyList.add("yearTotalCompletionRate");
        keyList.add("yearTotalBusTarget");
        keyList.add("yearTotalBusInvoice");
        keyList.add("yearTotalBusDifference");
        keyList.add("yearTotalBusCompletionRate");
        keyList.add("yearTotalTruckTarget");
        keyList.add("yearTotalTruckInvoice");
        keyList.add("yearTotalTruckDifference");
        keyList.add("yearTotalTruckCompletionRate");
        keyList.add("monthTotalTarget");
        keyList.add("monthTotalInvoice");
        keyList.add("monthTotalDifference");
        keyList.add("monthTotalCompletionRate");
        keyList.add("monthSurplus");
        keyList.add("monthTotalTruckTarget");
        keyList.add("monthTotalTruckInvoice");
        keyList.add("monthTotalTruckDifference");
        keyList.add("monthTotalTruckCompletionRate");
        keyList.add("monthTruckSurplus");
        keyList.add("monthTotalBusTarget");
        keyList.add("monthTotalBusInvoice");
        keyList.add("monthTotalBusDifference");
        keyList.add("monthTotalBusCompletionRate");
        keyList.add("monthBusSurplus");
        keyList.add("creditBalance");
        keyList.add("distribution");
        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(auxHeaderInfos, title, keyList, maps);


    }


}
