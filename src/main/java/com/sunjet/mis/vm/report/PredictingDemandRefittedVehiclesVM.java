package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.service.PredictingDemandRefittedVehiclesService;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PredictingDemandRefittedVehiclesView;
import com.sunjet.mis.module.report.view.WeeklyBalancedStatementView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
 * @author SUNJET-YFS
 * @Title: PredictingDemandRefittedVehiclesVM
 * @ProjectName mis
 * @Description: 货改车月预测改装车销售需求计划
 * @date 2019/4/2423:48
 */
public class PredictingDemandRefittedVehiclesVM extends ListVM<PredictingDemandRefittedVehiclesView> {
    @WireVariable
    private PredictingDemandRefittedVehiclesService predictingDemandRefittedVehiclesService;

    @Getter
    @Setter
    private PredictingDemandRefittedVehiclesView predictingDemandRefittedVehiclesView = new PredictingDemandRefittedVehiclesView();
    @Getter
    @Setter
    private List<PredictingDemandRefittedVehiclesView> predictingDemandRefittedVehiclesViews = new ArrayList<>();
//    @Getter
//    @Setter
//    private List<PredictingDemandRefittedVehiclesView> predictingDemandRefittedVehiclesViews = new ArrayList<>();
    @Getter
    @Setter
    private boolean showImportWindow = false;

    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("生产计划时间").width("200px").build(),
            TableHeaderInfo.builder().label("产地").width("100px").build(),
            TableHeaderInfo.builder().label("产品类别").width("60px").build(),
            TableHeaderInfo.builder().label("产品名称").width("200px").build(),
            TableHeaderInfo.builder().label("车型").width("120px").build(),
            TableHeaderInfo.builder().label("非ABS-VSN").width("200px").build(),
            TableHeaderInfo.builder().label("ABS-VSN").width("200px").build(),
            TableHeaderInfo.builder().label("上月底成品预计结余").width("70px").build(),
            TableHeaderInfo.builder().label("专用车月平衡后订单").width("70px").build(),
            TableHeaderInfo.builder().label("月生产计划\n").width("70px").build()

    );



    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(PredictingDemandRefittedVehiclesView.class.getSimpleName());
//  if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(predictingDemandRefittedVehiclesView, Order.DESC, "production");
        // solidPinEntities = solidPinService.findAll();
        getPageList();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(predictingDemandRefittedVehiclesService.getPageList(this.getPageParam()));
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

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
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

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.predictingDemandRefittedVehiclesViews.clear();
        this.predictingDemandRefittedVehiclesView.setZtime(null);
        this.predictingDemandRefittedVehiclesView.setAbsvsn(null);
        this.predictingDemandRefittedVehiclesView.setStartDate(null);
        this.predictingDemandRefittedVehiclesView.setEndDate(null);
        this.getPageList();
    }


    /**
     * 点击"查询"时的响应方法，显示搜索窗口
     */
    @Command
    @NotifyChange("showSearchWindow")
    public void showSearchWindow() {
        this.showSearchWindow = true;
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
        List<PredictingDemandRefittedVehiclesView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (PredictingDemandRefittedVehiclesView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }

        List<String> keyList = new ArrayList<>();
        keyList.add("ztime");
        keyList.add("production");
        keyList.add("productCategory");
        keyList.add("productName");
        keyList.add("vehicleType1");
        keyList.add("noabsvsn");
        keyList.add("absvsn");
        keyList.add("finishedBalance");
        keyList.add("zycphs");
        keyList.add("scjh");
//        keyList.add("scrus");
//        keyList.add("cncpkc");
        ExcelUtil.listMapToExcel(title, keyList, maps);


    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"showImportWindow", "importEntities", "uploadFilename"})
    public void abort(@BindingParam("event") Event event) {
     //   this.importEntities.clear();
        this.uploadFilename = "";
        this.showImportWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
       // this.setPageResult(solidPinReportService.getPageList(this.getPageParam()));
    }
    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(predictingDemandRefittedVehiclesView);
        getPageList();
    }

    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(PredictingDemandRefittedVehiclesView entity) {
        if (StringUtils.isBlank(entity.getAbsvsn()) || StringUtils.isBlank(entity.getNoabsvsn())) {
            return "background:#ff9600";
        }
        return "";
    }
}
