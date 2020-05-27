package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.service.SocialInventoryService;
import com.sunjet.mis.module.report.service.SolidPinService;
import com.sunjet.mis.module.report.view.ChassisTrackingView;
import com.sunjet.mis.module.report.view.SocialInventoryView;
import com.sunjet.mis.module.report.view.SolidPinReportView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * 社会库存
 * */
public class SocialInventoryVM extends ListVM<SocialInventoryView> {

    @WireVariable
    private SolidPinService solidPinService;
    @WireVariable
    private SocialInventoryService socialInventoryService;

    @Getter
    @Setter
    private SocialInventoryView socialInventoryView = SocialInventoryView.builder().build();

    @Getter
    @Setter
    private List<SolidPinEntity> solidPinEntities = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("100px").build(),
            TableHeaderInfo.builder().label("经销商号").width("60px").build(),
            TableHeaderInfo.builder().label("VIN").width("120px").build(),
            TableHeaderInfo.builder().label("客户名称").width("100px").build(),
            TableHeaderInfo.builder().label("车辆型号").width("100px").build(),
            TableHeaderInfo.builder().label("发动机号").width("60px").build(),
            TableHeaderInfo.builder().label("VSN").width("120px").build(),
            TableHeaderInfo.builder().label("颜色").width("80px").build(),
            TableHeaderInfo.builder().label("车系").width("120px").build(),
            TableHeaderInfo.builder().label("单据日期").width("80px").build(),
            TableHeaderInfo.builder().label("型号").width("90px").build(),
            TableHeaderInfo.builder().label("车型").width("120px").build(),
            TableHeaderInfo.builder().label("所属区域").width("90px").build()
    );

    @Init
    public void init(){
        this.setSearchFormHeight(49);
        this.initPermissionStatus(SocialInventoryView.class.getSimpleName());
//        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(socialInventoryView, Order.DESC, "vin");
        solidPinEntities = solidPinService.findAll();
        getPageList();
    }
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

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
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(socialInventoryView);
        getPageList();
    }
//    @Command
//    @NotifyChange("pageResult")
//    public void simpleSearch(){
//        if(!(this.getPageParam().getUserType()==UserType.DISTRIBUTOR.getIndex())){
//            vehicleInvEntity.setSgmwCode(this.getKeyword());
//            vehicleInvEntity.setDistributorCode(this.getKeyword());
//            vehicleInvEntity.setDistributorName(this.getKeyword());
//        }
//        getPageList();
//    }
    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshList(){
        this.getPageList();
    }

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(socialInventoryService.getPageList(this.getPageParam()));
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
        List<SocialInventoryView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (SocialInventoryView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("distributorCode");
        keyList.add("vin");
        keyList.add("customerName");
        keyList.add("customerProvinces");
        keyList.add("vehicleType");
        keyList.add("engineCode");
        keyList.add("vsn");
        keyList.add("color");
        keyList.add("vehicleSeries");
        keyList.add("invoiceTime");
        keyList.add("model");
        keyList.add("vehicleModel");
        keyList.add("regionName");

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
