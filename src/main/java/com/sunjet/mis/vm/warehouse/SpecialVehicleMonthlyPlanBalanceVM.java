package com.sunjet.mis.vm.warehouse;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.warehouse.entity.SpecialVehicleMonthlyPlanBalanceEntity;
import com.sunjet.mis.module.warehouse.service.SpecialVehicleMonthlyPlanBalanceService;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;

/**
 * @author SUNJET-YFS
 * @Title: SpecialVehicleMonthlyPlanBalanceVM
 * @ProjectName mis
 * @Description: 专用车月计划平衡详细表
 * @date 2019/4/1014:18
 */
public class SpecialVehicleMonthlyPlanBalanceVM extends ListVM<SpecialVehicleMonthlyPlanBalanceEntity> {

    @WireVariable
    private SpecialVehicleMonthlyPlanBalanceService specialVehicleMonthlyPlanBalanceService;

    @Getter
    @Setter
    private List<SpecialVehicleMonthlyPlanBalanceEntity> specialVehicleMonthlyPlanBalanceEntities = new ArrayList<>();
    @Getter
    @Setter
    private SpecialVehicleMonthlyPlanBalanceEntity specialVehicleMonthlyPlanBalanceEntity = new SpecialVehicleMonthlyPlanBalanceEntity();
    @Getter
    @Setter
    private boolean showImportWindow = false;

    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    @Setter
    private List<SpecialVehicleMonthlyPlanBalanceEntity> importEntities = new ArrayList<>();

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(SpecialVehicleMonthlyPlanBalanceEntity.class.getSimpleName());
//  if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(specialVehicleMonthlyPlanBalanceEntity, Order.DESC, "vsn");
        // solidPinEntities = solidPinService.findAll();
        getPageList();
    }

//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
//        Selectors.wireComponents(view, this, false);
//    }



    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("ID ").build(),
            TableHeaderInfo.builder().label("时间").build(),
            TableHeaderInfo.builder().label("经销商代码").build(),
            TableHeaderInfo.builder().label("申报类型").build(),
            TableHeaderInfo.builder().label("类型").build(),
            TableHeaderInfo.builder().label("物料号").build(),
            TableHeaderInfo.builder().label("车型A").build(),
            TableHeaderInfo.builder().label("品种代码").build(),
            TableHeaderInfo.builder().label("颜色").build(),
            TableHeaderInfo.builder().label("排放标准").build(),
            TableHeaderInfo.builder().label("物料品种").build(),
            TableHeaderInfo.builder().label("上报数").build(),
            TableHeaderInfo.builder().label("SGMW大区满足数	").build(),
            TableHeaderInfo.builder().label("SGMW总部满足数").build(),
            TableHeaderInfo.builder().label("是否集团车").build(),
            TableHeaderInfo.builder().label("是否异地配送").build(),
            TableHeaderInfo.builder().label("是否套色").build(),
            TableHeaderInfo.builder().label("考核第一周").build(),
            TableHeaderInfo.builder().label("考核第二周").build(),
            TableHeaderInfo.builder().label("考核第三周").build(),
            TableHeaderInfo.builder().label("考核第四周").build(),
            TableHeaderInfo.builder().label("考核第五周").build(),
            TableHeaderInfo.builder().label("考核第六周").build(),
            TableHeaderInfo.builder().label("备注").build(),
            TableHeaderInfo.builder().label("备注2").build(),
            TableHeaderInfo.builder().label("经销商").build(),
            TableHeaderInfo.builder().label("所属省份").build(),
            TableHeaderInfo.builder().label("所属区域").build(),
            TableHeaderInfo.builder().label("可申报区域").build(),
            TableHeaderInfo.builder().label("车型").build(),
            TableHeaderInfo.builder().label("车型1").build(),
            TableHeaderInfo.builder().label("车系").build(),
            TableHeaderInfo.builder().label("排量").build()

    );

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(specialVehicleMonthlyPlanBalanceService.getPageList(this.getPageParam()));
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
        this.specialVehicleMonthlyPlanBalanceEntities.clear();
        this.specialVehicleMonthlyPlanBalanceEntity.setDistributorCode(null);
        this.specialVehicleMonthlyPlanBalanceEntity.setVsn(null);
        this.specialVehicleMonthlyPlanBalanceEntity.setVehicleType1(null);
        this.specialVehicleMonthlyPlanBalanceEntity.setStartDate(null);
        this.specialVehicleMonthlyPlanBalanceEntity.setEndDate(null);
        this.getPageList();
    }

    /**
     * 点击"导入"时的响应方法，显示导入窗口
     */
    @Command
    @NotifyChange("showImportWindow")
    public void showImportWindow() {
        this.showImportWindow = true;
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
     * 选择文件时，后台响应方法
     * @param event
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) throws Exception {

        String extension = event.getMedia().getFormat().toLowerCase();
//        String fileEncode= EncodingDetect.getJavaEncodeByte(event.getMedia().getStringData().getBytes());

        this.uploadFilename = event.getMedia().getName();    // 原始文件名

//        String extension = this.uploadFilename.substring(this.uploadFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Objects.equals("xls", extension) && !Objects.equals("xlsx", extension)) {
//            throw new Exception("只支持 XLS、XLSX 文件类型，当前类型为：" + extension.toUpperCase());
            ZkUtils.showError("XLS 文件类型！！\n当前类型为：" + extension.toUpperCase(), "文件格式不对!");
            return;
        }

        if (extension.equals("xls") || extension.equals("xlsx")) {
            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
            String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名

            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
            for (List list : lists) {
                System.out.println(list.toString());
            }
            importEntities = ExcelUtil.readExcel(path + fileName, SpecialVehicleMonthlyPlanBalanceEntity.class);

            // 删除上传来的临时文件
            FileUtils.deleteQuietly(new File(path + fileName));

        } else if (extension.equals("csv")) {
            String enc = CSVUtil.getEncoding(event.getMedia().getFormat());

            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
            String fileName = ZkUtils.saveCsv(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名
            ZkUtils.showInformation(path + fileName, "转义后的文件名");

            Set<String> headerSet = new HashSet<>();
            Class<AllotVehicleEntity> clazz = AllotVehicleEntity.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                ExcelColumn header = field.getAnnotation(ExcelColumn.class);
                if (header != null) {
                    headerSet.add(header.value());
                }
            }

            String[] headerArr = headerSet.toArray(new String[headerSet.size()]);
            List<CSVRecord> csvRecords = CSVUtil.readCSV(path + fileName, headerArr);
            for (CSVRecord record : csvRecords) {
                System.out.println(record);
            }

            File file = new File(path + fileName);
            FileReader reader = new FileReader(file);
            System.out.println(EncodingDetect.getJavaEncodeFile(path + fileName));
        }
    }

    /**
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData() throws ParseException {

        int count = this.importEntities.size();

        this.importEntities = specialVehicleMonthlyPlanBalanceService.saveAll(this.importEntities);
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count ), "系统提示");
       // int errs = this.importEntities.size();
       // ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        //if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(specialVehicleMonthlyPlanBalanceService.getPageList(this.getPageParam()));
       // }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"showImportWindow", "importEntities", "uploadFilename"})
    public void abort(@BindingParam("event") Event event) {
        this.importEntities.clear();
        this.uploadFilename = "";
        this.showImportWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
        this.setPageResult(specialVehicleMonthlyPlanBalanceService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(specialVehicleMonthlyPlanBalanceEntity);
        getPageList();
    }
    public String genStyle(SpecialVehicleMonthlyPlanBalanceEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorName())) {
            return "background:#ff9600";
        }
        return "";
    }


    /**
     * 下载模板
     */
    @Command
    @NotifyChange("*")
    public void downloadTemplate() {
        try {
            byte[] buffer = null;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/专用车月计划平衡详细.xlsx";
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
            Filedownload.save(buffer, null, "专用车月计划平衡详细.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        List<SpecialVehicleMonthlyPlanBalanceEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (SpecialVehicleMonthlyPlanBalanceEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("ssid");
        keyList.add("dateYearMonth");
        keyList.add("distributorCode");
        keyList.add("planType");
        keyList.add("type");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("brandCode");
        keyList.add("color");
        keyList.add("effluent");
        keyList.add("vehicleBreed");
        keyList.add("requiredAmount");
        keyList.add("regionNumber");
        keyList.add("headquartersNumber");
        keyList.add("isNoGroupCar");
        keyList.add("allopatryDelivery");
        keyList.add("colorRegister");
        keyList.add("firstWeekAssessment");
        keyList.add("secondWeekAssessment");
        keyList.add("thirdWeekAssessment");
        keyList.add("fourthWeekAssessment");
        keyList.add("fifthWeekAssessment");
        keyList.add("sixthWeekAssessment");
        keyList.add("remarks");
        keyList.add("remarks2");
        keyList.add("distributorName");
        keyList.add("province");
        keyList.add("region");
        keyList.add("declarableRegion");
        keyList.add("vehicleType");
        keyList.add("vehicleType1");
        keyList.add("vehicleSeries");
        keyList.add("displacement");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }
}
