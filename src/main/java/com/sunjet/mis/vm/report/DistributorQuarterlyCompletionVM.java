package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.service.DistributorQuarterlyCompletionService;
import com.sunjet.mis.module.report.view.DistributorQuarterlyCompletionView;
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
 * @description: 经销商目标季度完成汇总表
 * @Date: Created in 14:51 2019/3/29
 * @Modify by: wushi
 * @ModifyDate by: 14:51 2019/3/29
 */
public class DistributorQuarterlyCompletionVM extends ListVM<DistributorQuarterlyCompletionView> {


    @WireVariable
    private DistributorQuarterlyCompletionService distributorQuarterlyCompletionService;

    @Getter
    private List<Integer> years = new ArrayList<>();

    /**
     * 货改车列头
     */
    @Getter
    @Setter
    private String tuckQuarterTitle;
    /**
     * 货改车列头
     */
    @Getter
    @Setter
    private String busQuarterTitle;
    @Getter
    @Setter
    private String firstMonthTitle;
    @Getter
    @Setter
    private String secondMonthTitle;
    @Getter
    @Setter
    private String thirdMonthTitle;
    /**
     * 季度
     */
    @Getter
    private List<String> quarters = Arrays.asList("第一季度", "第二季度", "第三季度", "第四季度");


    @Getter
    @Setter
    private DistributorQuarterlyCompletionView distributorQuarterlyCompletionView = DistributorQuarterlyCompletionView.builder().build();


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
            TableHeaderInfo.builder().label("经销商代码(SGMW)").width("120px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("120px").build(),
            //合计
            TableHeaderInfo.builder().label("奖励合计(元)").width("80px").build(),
            //季度奖励(货改车)
            TableHeaderInfo.builder().label("指标合计(辆)").width("80px").build(),
            TableHeaderInfo.builder().label("完成合计(辆)").width("80px").build(),
            TableHeaderInfo.builder().label("指标差距").width("80px").build(),
            TableHeaderInfo.builder().label("完成率").width("80px").build(),
            TableHeaderInfo.builder().label("奖励标准(元/辆)").width("80px").build(),
            TableHeaderInfo.builder().label("小计(元))").width("80px").build(),
            //季度奖励(客改车)
            TableHeaderInfo.builder().label("指标合计(辆)").width("80px").build(),
            TableHeaderInfo.builder().label("完成合计(辆)").width("80px").build(),
            TableHeaderInfo.builder().label("指标差距").width("80px").build(),
            TableHeaderInfo.builder().label("完成率").width("80px").build(),
            TableHeaderInfo.builder().label("奖励标准(元/辆)").width("80px").build(),
            TableHeaderInfo.builder().label("小计(元))").width("80px").build(),
            //第一个月
            TableHeaderInfo.builder().label("指标(货)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(货)").width("80px").build(),
            TableHeaderInfo.builder().label("指标(客)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(客)").width("80px").build(),
            //第二个月
            TableHeaderInfo.builder().label("指标(货)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(货)").width("80px").build(),
            TableHeaderInfo.builder().label("指标(客)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(客)").width("80px").build(),
            //第三个月
            TableHeaderInfo.builder().label("指标(货)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(货)").width("80px").build(),
            TableHeaderInfo.builder().label("指标(客)").width("80px").build(),
            TableHeaderInfo.builder().label("实际完成(客)").width("80px").build()

    );

    @Getter
    @Setter
    List<AuxHeaderInfo> auxHeaderList = new ArrayList<>();


    @Init
    public void init() {
        this.initPermissionStatus(DistributorQuarterlyCompletionView.class.getSimpleName());

        LocalDate.now().getYear();

        for (int i = 2015; i <= LocalDate.now().getYear(); i++) {
            years.add(i);
        }
        distributorQuarterlyCompletionView.setYear(LocalDate.now().getYear());

        //季度开始月份
        int quarterStartMonth = getQuarterInMonth(LocalDate.now().getMonthValue(), true);
        //季度结束月份
        int quarterEndMonth = getQuarterInMonth(LocalDate.now().getMonthValue(), false);
        //设置当月季度选项
        distributorQuarterlyCompletionView.setQuarterStartMonth(quarterStartMonth);
        distributorQuarterlyCompletionView.setQuarterEndMonth(quarterEndMonth);
        //设置标题
        setTitle(quarterStartMonth, quarterEndMonth);
        refreshFirstPage(distributorQuarterlyCompletionView, Order.ASC, "province.region");
        getPageList();


    }


    /**
     * 分页
     */
    public void getPageList() {
        this.setPageResult(distributorQuarterlyCompletionService.getPageList(this.getPageParam()));
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(distributorQuarterlyCompletionView);
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
        refreshFirstPage(distributorQuarterlyCompletionView, Order.ASC, "province.region");
        getPageList();
    }

/*查询*/
    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }


    // 返回第几个月份，不是几月
    // 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
    private int getQuarterInMonth(int month, boolean isQuarterStart) {
        int months[] = {1, 4, 7, 10};
        if (!isQuarterStart) {
            months = new int[]{3, 6, 9, 12};
        }
        if (month >= 1 && month <= 3) {
            distributorQuarterlyCompletionView.setQuarter("第一季度");
            return months[0];
        } else if (month >= 4 && month <= 6) {
            distributorQuarterlyCompletionView.setQuarter("第二季度");
            return months[1];
        } else if (month >= 7 && month <= 9) {
            distributorQuarterlyCompletionView.setQuarter("第三季度");
            return months[2];
        } else {
            distributorQuarterlyCompletionView.setQuarter("第四季度");
            return months[3];
        }
    }

    /**
     * 设置列头
     */
    @Command
    @NotifyChange({"tuckQuarterTitle", "busQuarterTitle", "firstMonthTitle", "secondMonthTitle", "thirdMonthTitle", "pageResult"})
    public void selectQuarterly() {
        String quarter = distributorQuarterlyCompletionView.getQuarter();
        if ("第一季度".equals(quarter)) {
            distributorQuarterlyCompletionView.setQuarterStartMonth(1);
            distributorQuarterlyCompletionView.setQuarterEndMonth(3);
            setTitle(1, 3);
        } else if ("第二季度".equals(quarter)) {
            distributorQuarterlyCompletionView.setQuarterStartMonth(4);
            distributorQuarterlyCompletionView.setQuarterEndMonth(6);
            setTitle(4, 6);
        } else if ("第三季度".equals(quarter)) {
            distributorQuarterlyCompletionView.setQuarterStartMonth(7);
            distributorQuarterlyCompletionView.setQuarterEndMonth(9);
            setTitle(7, 9);
        } else {
            distributorQuarterlyCompletionView.setQuarterStartMonth(10);
            distributorQuarterlyCompletionView.setQuarterEndMonth(12);
            setTitle(10, 12);
        }

        getPageList();
    }

    /**
     * 动态设置标题月份
     *
     * @param quarterStartMonth
     * @param quarterEndMonth
     */
    private void setTitle(int quarterStartMonth, int quarterEndMonth) {
        this.tuckQuarterTitle = "(货改车)奖励";
        this.busQuarterTitle = "(客改车)奖励";
        this.firstMonthTitle = "月完成";
        this.secondMonthTitle = "月完成";
        this.thirdMonthTitle = "月完成";
        this.tuckQuarterTitle = quarterStartMonth + "~" + quarterEndMonth + "月" + tuckQuarterTitle;
        this.busQuarterTitle = quarterStartMonth + "~" + quarterEndMonth + "月" + busQuarterTitle;
        int startMonth = this.distributorQuarterlyCompletionView.getQuarterStartMonth();
        this.firstMonthTitle = startMonth + this.firstMonthTitle;
        this.secondMonthTitle = startMonth + 1 + this.secondMonthTitle;
        this.thirdMonthTitle = startMonth + 2 + this.thirdMonthTitle;

        auxHeaderList = Arrays.asList(
                AuxHeaderInfo.builder().title("经销商信息").firstCol(0).lastCol(4).isMerge(true).build(),
                AuxHeaderInfo.builder().title("年度目标(合并)").firstCol(5).lastCol(5).build(),
                AuxHeaderInfo.builder().title(this.getTuckQuarterTitle()).firstCol(6).lastCol(11).isMerge(true).build(),
                AuxHeaderInfo.builder().title(this.getBusQuarterTitle()).firstCol(12).lastCol(17).isMerge(true).build(),
                AuxHeaderInfo.builder().title(this.getFirstMonthTitle()).firstCol(18).lastCol(21).isMerge(true).build(),
                AuxHeaderInfo.builder().title(this.getSecondMonthTitle()).firstCol(22).lastCol(25).isMerge(true).build(),
                AuxHeaderInfo.builder().title(this.getThirdMonthTitle()).firstCol(26).lastCol(29).isMerge(true).build()

        );
    }


    @Command
    @NotifyChange("*")
    public void reset() {
        this.distributorQuarterlyCompletionView.setDistributorName(null);
       this.distributorQuarterlyCompletionView.setDistributorCode(null);
        distributorQuarterlyCompletionView.setYear(LocalDate.now().getYear());

        //季度开始月份
        int quarterStartMonth = getQuarterInMonth(LocalDate.now().getMonthValue(), true);
        //季度结束月份
        int quarterEndMonth = getQuarterInMonth(LocalDate.now().getMonthValue(), false);
        //设置当月季度选项
        distributorQuarterlyCompletionView.setQuarterStartMonth(quarterStartMonth);
        distributorQuarterlyCompletionView.setQuarterEndMonth(quarterEndMonth);
        //设置标题
        setTitle(quarterStartMonth, quarterEndMonth);
        this.getPageList();
    }


//    @Command
//    @NotifyChange("*")
//    public void reset() {
//        this.distributorQuarterlyCompletionView.setDistributorName(null);
//        this.distributorQuarterlyCompletionView.setDistributorCode(null);
//        this.getPageList();
//    }

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

        List<DistributorQuarterlyCompletionView> currentPage = this.getPageResult().getRows();
        List<Map<String, Object>> maps = new ArrayList<>();
        for (DistributorQuarterlyCompletionView detailItem : currentPage) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("regionName");
        keyList.add("provinceName");
        keyList.add("level");
        keyList.add("distributorCode");
        keyList.add("distributorName");
        keyList.add("rewardTotal");
        //(货改车)季度奖励
        keyList.add("truckTargetTotal");
        keyList.add("truckCompleteTotal");
        keyList.add("truckTargetDifference");
        keyList.add("truckCompletionRate");
        keyList.add("truckRewardCriterion");
        keyList.add("truckSubtotal");
        //(客改车)季度奖励
        keyList.add("busTargetTotal");
        keyList.add("busCompleteTotal");
        keyList.add("busTargetDifference");
        keyList.add("busCompletionRate");
        keyList.add("busRewardCriterion");
        keyList.add("busSubtotal");
        //第一个月完成
        keyList.add("firstMonthTruckTarget");
        keyList.add("firstMonthTruckComplete");
        keyList.add("firstMonthBusTarget");
        keyList.add("firstMonthBusComplete");
        //第二个月完成
        keyList.add("secondMonthTruckTarget");
        keyList.add("secondMonthTruckComplete");
        keyList.add("secondMonthBusTarget");
        keyList.add("secondMonthBusComplete");
        //第三个月完成
        keyList.add("thirdMonthTruckTarget");
        keyList.add("thirdMonthTruckComplete");
        keyList.add("thirdMonthBusTarget");
        keyList.add("thirdMonthBusComplete");
        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(auxHeaderList, title, keyList, maps);


    }

}
