package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.service.WeeklyBalancedStatementService;
import com.sunjet.mis.module.report.view.WeeklyBalancedStatementView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author SUNJET-YFS
 * @Title: WeeklyBalancedStatementVM
 * @ProjectName mis
 * @Description: 周产平衡报表
 * @date 2019/3/716:08
 */
public class WeeklyBalancedStatementVM extends ListVM<WeeklyBalancedStatementView> {
    @WireVariable
    private WeeklyBalancedStatementService weeklyBalancedStatementService;

    @Getter
    @Setter
    private WeeklyBalancedStatementView weeklyBalancedStatementView = new WeeklyBalancedStatementView();

    @Getter
    @Setter
    private List<WeeklyBalancedStatementView> weeklyBalancedStatementViewList = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("生产计划月份").width("100px").build(),
            TableHeaderInfo.builder().label("生产入库时间").width("100px").build(),
            TableHeaderInfo.builder().label("分车月份时间").width("100px").build(),

            TableHeaderInfo.builder().label("产地").width("60px").build(),
            TableHeaderInfo.builder().label("品种大类").width("100px").build(),
            TableHeaderInfo.builder().label("常规品种").width("120px").build(),
            TableHeaderInfo.builder().label("完整ABSVSN").width("100px").build(),
            TableHeaderInfo.builder().label("车型").width("100px").build(),
            TableHeaderInfo.builder().label("平衡后订单").width("70px").build(),
            TableHeaderInfo.builder().label("上月结余量").width("70px").build(),

            TableHeaderInfo.builder().label("月度生产计划量").width("70px").build(),
            TableHeaderInfo.builder().label("生产入库量").width("70px").build(),
            TableHeaderInfo.builder().label("厂内成品库存量").width("70px").build(),
            TableHeaderInfo.builder().label("本月已分车").width("70px").build()


    );


    @Init
    public void init() {
       // this.setSearchFormHeight(49);
        this.initPermissionStatus(WeeklyBalancedStatementView.class.getSimpleName());

//        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(weeklyBalancedStatementView, Order.DESC, "production");
       // weeklyBalancedStatementViewList = weeklyBalancedStatementService.findAll();
        getPageList();
    }


    /**
     * 分页
     */

    public void getPageList() {

        this.setPageResult(weeklyBalancedStatementService.getPageList(this.getPageParam()));
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

        List<WeeklyBalancedStatementView> currentPage = new ArrayList<>();
        if("all".equals(type)){
            long i = this.getPageResult().getTotal();
            this.getPageParam().setPageSize((int)i);
            this.setPageResult(weeklyBalancedStatementService.getPageList(this.getPageParam()));
            currentPage = this.getPageResult().getRows();
        }else{
            currentPage = this.getPageResult().getRows();
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (WeeklyBalancedStatementView detailItem : currentPage) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }

        List<String> keyList = new ArrayList<>();
        keyList.add("scsj");
        keyList.add("inboundDate");
        keyList.add("fcsj");
        keyList.add("production");
        keyList.add("productCategory");
        keyList.add("productName");
        keyList.add("absVsn");
        keyList.add("vehicleType1");
        keyList.add("zycphs");
        keyList.add("syjys");
        keyList.add("scjh");
        keyList.add("scrus");
        keyList.add("cncpkc");
        keyList.add("fcs");
        ExcelUtil.listMapToExcel(title, keyList, maps);


    }
    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.weeklyBalancedStatementViewList.clear();
        this.weeklyBalancedStatementView.setVehicleType1(null);
        this.weeklyBalancedStatementView.setAbsVsn(null);
        this.weeklyBalancedStatementView.setStartDate(null);
        this.weeklyBalancedStatementView.setEndDate(null);
        this.weeklyBalancedStatementView.setStartDate1(null);
        this.weeklyBalancedStatementView.setEndDate1(null);
        this.weeklyBalancedStatementView.setStartDate2(null);
        this.weeklyBalancedStatementView.setEndDate2(null);
        this.getPageList();
    }


    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        this.getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }
    /*下一页*/
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(weeklyBalancedStatementView);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
//        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
////            solidPinEntity.setSgmwCode(this.getKeyword());
////            solidPinEntity.setDistributorCode(this.getKeyword());
//            solidPinEntity.setCustomerName(this.getKeyword());
//        }
        getPageList();
    }




}
