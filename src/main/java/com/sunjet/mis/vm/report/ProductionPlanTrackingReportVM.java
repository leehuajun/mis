package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanExecSummaryService;
import com.sunjet.mis.module.report.service.ProductionPlanTrackingReportService;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.report.view.SocialInventoryView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.validation.constraints.Null;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/*订单计划管理-生产计划跟踪*/
public class ProductionPlanTrackingReportVM extends ListVM<ProductionPlanTrackingReportView> {
    @WireVariable
    private ProductionPlanTrackingReportService productionPlanTrackingReportService;

    @Getter
    @Setter
    private ProductionPlanTrackingReportView productionPlanTrackingReportView = ProductionPlanTrackingReportView.builder().build();

    @Getter
    private List<Integer> years = new ArrayList<>();
    @Getter
    private List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    @Getter
    @Setter
    private Date beginDate = new Date();
    @Getter
    @Setter
    private Date endDate = new Date();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("产地").width("60px").build(),
            TableHeaderInfo.builder().label("车系").width("60px").build(),
            TableHeaderInfo.builder().label("产品名称").width("60px").build(),
            TableHeaderInfo.builder().label("型号").width("100px").build(),
            TableHeaderInfo.builder().label("vsn").width("120px").build(),
            TableHeaderInfo.builder().label("月度计划").width("70px").build(),
            TableHeaderInfo.builder().label("完成量").width("70px").build(),
            TableHeaderInfo.builder().label("完成率").width("70px").build(),
            TableHeaderInfo.builder().label("第一周计划").width("70px").build(),
            TableHeaderInfo.builder().label("第二周计划").width("70px").build(),
            TableHeaderInfo.builder().label("第三周计划").width("70px").build(),
            TableHeaderInfo.builder().label("第四周计划").width("70px").build(),
            TableHeaderInfo.builder().label("第五周计划").width("70px").build(),
            TableHeaderInfo.builder().label("第一周完成").width("70px").build(),
            TableHeaderInfo.builder().label("第二周完成").width("70px").build(),
            TableHeaderInfo.builder().label("第三周完成").width("70px").build(),
            TableHeaderInfo.builder().label("第四周完成").width("70px").build(),
            TableHeaderInfo.builder().label("第五周完成").width("70px").build(),
            TableHeaderInfo.builder().label("1日").width("70px").build(),
            TableHeaderInfo.builder().label("2日").width("70px").build(),
            TableHeaderInfo.builder().label("3日").width("70px").build(),
            TableHeaderInfo.builder().label("4日").width("70px").build(),
            TableHeaderInfo.builder().label("5日").width("70px").build(),
            TableHeaderInfo.builder().label("6日").width("70px").build(),
            TableHeaderInfo.builder().label("7日").width("70px").build(),
            TableHeaderInfo.builder().label("8日").width("70px").build(),
            TableHeaderInfo.builder().label("9日").width("70px").build(),
            TableHeaderInfo.builder().label("10日").width("70px").build(),
            TableHeaderInfo.builder().label("11日").width("70px").build(),
            TableHeaderInfo.builder().label("12日").width("70px").build(),
            TableHeaderInfo.builder().label("13日").width("70px").build(),
            TableHeaderInfo.builder().label("14日").width("70px").build(),
            TableHeaderInfo.builder().label("15日").width("70px").build(),
            TableHeaderInfo.builder().label("16日").width("70px").build(),
            TableHeaderInfo.builder().label("17日").width("70px").build(),
            TableHeaderInfo.builder().label("18日").width("70px").build(),
            TableHeaderInfo.builder().label("19日").width("70px").build(),
            TableHeaderInfo.builder().label("20日").width("70px").build(),
            TableHeaderInfo.builder().label("21日").width("70px").build(),
            TableHeaderInfo.builder().label("22日").width("70px").build(),
            TableHeaderInfo.builder().label("23日").width("70px").build(),
            TableHeaderInfo.builder().label("24日").width("70px").build(),
            TableHeaderInfo.builder().label("25日").width("70px").build(),
            TableHeaderInfo.builder().label("26日").width("70px").build(),
            TableHeaderInfo.builder().label("27日").width("70px").build(),
            TableHeaderInfo.builder().label("28日").width("70px").build(),
            TableHeaderInfo.builder().label("29日").width("70px").build(),
            TableHeaderInfo.builder().label("30日").width("70px").build(),
            TableHeaderInfo.builder().label("31日").width("70px").build()
    );


    @Init
    public void init() {
        LocalDate.now().getYear();
        for (int i = 2015; i <= LocalDate.now().getYear(); i++) {
            years.add(i);
        }

        this.setSearchFormHeight(49);
        this.initPermissionStatus(ProductionPlanTrackingReportView.class.getSimpleName());

//        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
//            this.planReportView.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        productionPlanTrackingReportView.setYear(LocalDate.now().getYear());
        productionPlanTrackingReportView.setMonth(LocalDate.now().getMonthValue());
        refreshFirstPage(productionPlanTrackingReportView, Order.DESC, "vsn");
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
        refreshPage(productionPlanTrackingReportView);
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
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.productionPlanTrackingReportView.setVehicleModel(null);
        this.productionPlanTrackingReportView.setVsn(null);

        this.productionPlanTrackingReportView.setYear(LocalDate.now().getYear());
        this.productionPlanTrackingReportView.setMonth(LocalDate.now().getMonthValue());
        this.getPageList();
    }


    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(productionPlanTrackingReportService.getPageList(this.getPageParam()));
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
    public void calculate(@BindingParam("vehicleModel") String vehicleModel, @BindingParam("vsn") String vsn, @BindingParam("month") String month) {
        System.out.println(vehicleModel + "==" + vsn + "==" + month + "==");
    }

    @Command
    public List<PlanEntity> findAllByModelAndVsn(@BindingParam("model") ProductionPlanTrackingReportView pptv) {
        return productionPlanTrackingReportService.findAllByModelAndVsn(pptv);
    }

    @Command
    public void openPlanList(@BindingParam("model") ProductionPlanTrackingReportView model) throws TabDuplicateException {
        Map<String, Object> map = new HashMap<>();
        map.put("model", model);
        ZkTabboxUtil.newTab(model.getId(), "详情", true, ZkTabboxUtil.OverFlowType.HIDDEN, "/views/report/production_plan_tracking_report/production_plan_tracking_datail.zul", map);
    }

    @Command
    @NotifyChange({"years", "months", "productionPlanTrackingReportView"})
    public void setFirstWeek() throws TabDuplicateException {
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if (!(yy.format(beginDate).equals(yy.format(endDate))) || !(mm.format(beginDate).equals(mm.format(endDate)))) {
            System.out.println("请选择同一年和同一个月");
            ZkUtils.showError("请选择同一年和同一个月！", "系统提示");
//            ZkTabboxUtil.
        } else {
            List<Date> dateList = findDates(beginDate, endDate);

            List<Integer> days = new ArrayList<>();
            for (Date dt : dateList) {
                Integer day = Integer.parseInt(dd.format(dt));
                days.add(day);
            }
            productionPlanTrackingReportView.setYear(Integer.parseInt(yy.format(beginDate)));
            productionPlanTrackingReportView.setMonth(Integer.parseInt(mm.format(beginDate)));
            productionPlanTrackingReportView.setFirstWeek(days);
        }
    }

    @Command
    @NotifyChange({"productionPlanTrackingReportView"})
    public void setSecondWeek() throws TabDuplicateException {
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if (!(yy.format(beginDate).equals(yy.format(endDate))) || !(mm.format(beginDate).equals(mm.format(endDate)))) {
            System.out.println("请选择同一年和同一个月");
            ZkUtils.showError("请选择同一年和同一个月！", "系统提示");
//            ZkTabboxUtil.
        } else {
            List<Date> dateList = findDates(beginDate, endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            List<Integer> days = new ArrayList<>();
            for (Date dt : dateList) {
                Integer day = Integer.parseInt(sdf.format(dt));
                days.add(day);
            }
            productionPlanTrackingReportView.setSecondWeek(days);
        }
    }

    @Command
    @NotifyChange({"productionPlanTrackingReportView"})
    public void setThirdWeek() throws TabDuplicateException {
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if (!(yy.format(beginDate).equals(yy.format(endDate))) || !(mm.format(beginDate).equals(mm.format(endDate)))) {
            System.out.println("请选择同一年和同一个月");
            ZkUtils.showError("请选择同一年和同一个月！", "系统提示");
//            ZkTabboxUtil.
        } else {
            List<Date> dateList = findDates(beginDate, endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            List<Integer> days = new ArrayList<>();
            for (Date dt : dateList) {
                Integer day = Integer.parseInt(sdf.format(dt));
                days.add(day);
            }
            productionPlanTrackingReportView.setThirdWeek(days);
        }
    }

    @Command
    @NotifyChange({"productionPlanTrackingReportView"})
    public void setFourthWeek() throws TabDuplicateException {
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if (!(yy.format(beginDate).equals(yy.format(endDate))) || !(mm.format(beginDate).equals(mm.format(endDate)))) {
            System.out.println("请选择同一年和同一个月");
            ZkUtils.showError("请选择同一年和同一个月！", "系统提示");
//            ZkTabboxUtil.
        } else {
            List<Date> dateList = findDates(beginDate, endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            List<Integer> days = new ArrayList<>();
            for (Date dt : dateList) {
                Integer day = Integer.parseInt(sdf.format(dt));
                days.add(day);
            }
            productionPlanTrackingReportView.setFourthWeek(days);
        }
    }

    @Command
    @NotifyChange({"productionPlanTrackingReportView"})
    public void setFifthWeek() throws TabDuplicateException {
        SimpleDateFormat yy = new SimpleDateFormat("yyyy");
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        SimpleDateFormat dd = new SimpleDateFormat("dd");
        if (!(yy.format(beginDate).equals(yy.format(endDate))) || !(mm.format(beginDate).equals(mm.format(endDate)))) {
            System.out.println("请选择同一年和同一个月");
            ZkUtils.showError("请选择同一年和同一个月！", "系统提示");
//            ZkTabboxUtil.
        } else {
            List<Date> dateList = findDates(beginDate, endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            List<Integer> days = new ArrayList<>();
            for (Date dt : dateList) {
                Integer day = Integer.parseInt(sdf.format(dt));
                days.add(day);
            }
            productionPlanTrackingReportView.setFifthWeek(days);
        }
    }

    @Command
    @NotifyChange({"productionPlanTrackingReportView"})
    public void updateTime() throws TabDuplicateException {
        String date = productionPlanTrackingReportView.getYear() + "-" + productionPlanTrackingReportView.getMonth();
        System.out.println(date);
//        Date day = new Date(productionPlanTrackingReportView.getYear(),productionPlanTrackingReportView.getMonth(),productionPlanTrackingReportView.getMonth());
//        long dateTime = day.getTime();
//        System.out.println(dateTime+"-------");
//        beginDate.setTime(dateTime);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println( sdf.format(beginDate)+"=======");
    }

    public static List<Date> findDates(Date beginDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        dateList.add(beginDate);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        while (endDate.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calBegin.getTime());
        }
        return dateList;
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

        List<ProductionPlanTrackingReportView> currentPage = new ArrayList<>();
        if ("all".equals(type)) {
            int total = (int) this.getPageResult().getTotal();
            if (total > 0)
                this.getPageParam().setPageSize(total);
            this.setPageResult(productionPlanTrackingReportService.getPageList(this.getPageParam()));
            currentPage = this.getPageResult().getRows();
        } else {
            currentPage = this.getPageResult().getRows();
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (ProductionPlanTrackingReportView detailItem : currentPage) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            map.put("oneDay", detailItem.getDays().get(0));
            map.put("twoDay", detailItem.getDays().get(1));
            map.put("threeDay", detailItem.getDays().get(2));
            map.put("fourDay", detailItem.getDays().get(3));
            map.put("fiveDay", detailItem.getDays().get(4));
            map.put("sixDay", detailItem.getDays().get(5));
            map.put("sevenDay", detailItem.getDays().get(6));
            map.put("eightDay", detailItem.getDays().get(7));
            map.put("nineDay", detailItem.getDays().get(8));
            map.put("tenDay", detailItem.getDays().get(9));
            map.put("elevenDay", detailItem.getDays().get(10));
            map.put("twelveDay", detailItem.getDays().get(11));
            map.put("thirteenDay", detailItem.getDays().get(12));
            map.put("fourteenDay", detailItem.getDays().get(13));
            map.put("fifteenDay", detailItem.getDays().get(14));
            map.put("sixteenDay", detailItem.getDays().get(15));
            map.put("seventeenDay", detailItem.getDays().get(16));
            map.put("eighteenDay", detailItem.getDays().get(17));
            map.put("nineteenDay", detailItem.getDays().get(18));
            map.put("twentyDay", detailItem.getDays().get(19));
            map.put("twentyOneDay", detailItem.getDays().get(20));
            map.put("twentyTwoDay", detailItem.getDays().get(21));
            map.put("twentyThreeDay", detailItem.getDays().get(22));
            map.put("twentyFourDay", detailItem.getDays().get(23));
            map.put("twentyFiveDay", detailItem.getDays().get(24));
            map.put("twentySixDay", detailItem.getDays().get(25));
            map.put("twentySevenDay", detailItem.getDays().get(26));
            map.put("twentyEightDay", detailItem.getDays().get(27));
            map.put("twentyNineDay", detailItem.getDays().get(28));
            map.put("thirtyDay", detailItem.getDays().get(29));
            map.put("thirtyOneDay", detailItem.getDays().get(30));

            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("origin");
        keyList.add("vehicleSeries");
        keyList.add("vehicleName");
        keyList.add("vehicleModel");
        keyList.add("vsn");
        keyList.add("monthPlan");
        keyList.add("completeNumber");
        keyList.add("completion");
        keyList.add("firstWeekPlan");
        keyList.add("secondWeekPlan");
        keyList.add("thirdWeekPlan");
        keyList.add("fourthWeekPlan");
        keyList.add("fifthWeekPlan");
        keyList.add("firstWeekComplete");
        keyList.add("secondWeekComplete");
        keyList.add("thirdWeekComplete");
        keyList.add("fourthWeekComplete");
        keyList.add("fifthWeekComplete");
        keyList.add("oneDay");
        keyList.add("twoDay");
        keyList.add("threeDay");
        keyList.add("fourDay");
        keyList.add("fiveDay");
        keyList.add("sixDay");
        keyList.add("sevenDay");
        keyList.add("eightDay");
        keyList.add("nineDay");
        keyList.add("tenDay");
        keyList.add("elevenDay");
        keyList.add("twelveDay");
        keyList.add("thirteenDay");
        keyList.add("fourteenDay");
        keyList.add("fifteenDay");
        keyList.add("sixteenDay");
        keyList.add("seventeenDay");
        keyList.add("eighteenDay");
        keyList.add("nineteenDay");
        keyList.add("twentyDay");
        keyList.add("twentyOneDay");
        keyList.add("twentyTwoDay");
        keyList.add("twentyThreeDay");
        keyList.add("twentyFourDay");
        keyList.add("twentyFiveDay");
        keyList.add("twentySixDay");
        keyList.add("twentySevenDay");
        keyList.add("twentyEightDay");
        keyList.add("twentyNineDay");
        keyList.add("thirtyDay");
        keyList.add("thirtyOneDay");


        ExcelUtil.listMapToExcel(null, title, keyList, maps);
    }


}
