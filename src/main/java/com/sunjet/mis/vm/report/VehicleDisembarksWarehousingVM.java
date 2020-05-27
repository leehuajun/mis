//package com.sunjet.mis.vm.report;
//
//import com.sunjet.mis.annotation.ExcelColumn;
//import com.sunjet.mis.base.vm.ListVM;
//import com.sunjet.mis.helper.CommonHelper;
//import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
//import com.sunjet.mis.module.warehouse.entity.VehicleDisembarksWarehousingEntity;
//import com.sunjet.mis.module.warehouse.service.VehicleDisembarksWarehousingService;
//import com.sunjet.mis.utils.common.CSVUtil;
//import com.sunjet.mis.utils.common.EncodingDetect;
//import com.sunjet.mis.utils.common.ExcelUtil;
//import com.sunjet.mis.utils.dto.Order;
//import com.sunjet.mis.utils.zk.ZkUtils;
//import lombok.Getter;
//import lombok.Setter;
//import org.apache.commons.csv.CSVRecord;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.ClassUtils;
//import org.zkoss.bind.annotation.*;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.UploadEvent;
//import org.zkoss.zk.ui.select.Selectors;
//import org.zkoss.zk.ui.select.annotation.WireVariable;
//import org.zkoss.zul.Filedownload;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @author SUNJET-YFS
// * @Title: VehicleDisembarksWarehousingVM
// * @ProjectName mis
// * @Description: 车辆下线入库 （生产入库）
// * @date 2019/3/816:50
// */
//public class VehicleDisembarksWarehousingVM extends ListVM<VehicleDisembarksWarehousingEntity> {
//
//    @WireVariable
//    private VehicleDisembarksWarehousingService vehicleDisembarksWarehousingService;
//    @Getter
//    @Setter
//    private VehicleDisembarksWarehousingEntity vehicleDisembarksWarehousingEntity = VehicleDisembarksWarehousingEntity.builder().build();
//    @Getter
//    @Setter
//    private List<VehicleDisembarksWarehousingEntity> vehicleDisembarksWarehousingEntities = new ArrayList<>();
//
//    @Getter
//    @Setter
//    private boolean showImportWindow = false;
//    @Getter
//    private String uploadFilename;
//    @Getter
//    @Setter
//    private boolean showSearchWindow = false;
//    @Getter
//    @Setter
//    private List<VehicleDisembarksWarehousingEntity> importEntities = new ArrayList<>();
//
//    @Init
//    public void init() {
//        //this.setSearchFormHeight(49);
//        this.initPermissionStatus(VehicleDisembarksWarehousingEntity.class.getSimpleName());
//
////        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
////            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
////            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
////        }
//        refreshFirstPage(vehicleDisembarksWarehousingEntity, Order.DESC, "vin_");
//        vehicleDisembarksWarehousingEntities = vehicleDisembarksWarehousingService.findAll();
//        getPageList();
//    }
//
//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
//        Selectors.wireComponents(view, this, false);
//
//    }
//
//    /**
//     * 重置
//     */
//    @Command
//    @NotifyChange({"vehicleDisembarksWarehousingEntity", "vin", "vehicleModel", "vsn", "startDate", "endDate"})
//    public void reset() {
//        this.vehicleDisembarksWarehousingEntity.setVin(null);
//        this.vehicleDisembarksWarehousingEntity.setVsn(null);
//        this.vehicleDisembarksWarehousingEntity.setVehicleModel(null);
//        this.vehicleDisembarksWarehousingEntity.setEndDate(null);
//        this.vehicleDisembarksWarehousingEntity.setStartDate(null);
//        //this.getPageList();
//    }
//
//    /**
//     * 分页
//     */
//    public void getPageList() {
//
//        this.setPageResult(vehicleDisembarksWarehousingService.getPageList(this.getPageParam()));
//    }
//
//    /**
//     * 刷新列表
//     */
//    @Command
//    @NotifyChange("pageResult")
//    public void refreshList() {
//        this.getPageList();
//    }
//
//    @Command
//    @NotifyChange("pageResult")
//    public void searchData() {
//        this.getPageParam().setPage(0);
//        getPageList();
//    }
//
//    @Command
//    @NotifyChange("pageResult")
//    public void simpleSearch() {
////        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
//////            solidPinEntity.setSgmwCode(this.getKeyword());
//////            solidPinEntity.setDistributorCode(this.getKeyword());
////            solidPinEntity.setCustomerName(this.getKeyword());
////        }
//        getPageList();
//    }
//
//    @Command
//    @NotifyChange("pageResult")
//    public void gotoPageNo(@BindingParam("e") Event event) {
//        refreshPage(vehicleDisembarksWarehousingEntity);
//        getPageList();
//    }
//
//    /**
//     * 点击"导入"时的响应方法，显示导入窗口
//     */
//    @Command
//    @NotifyChange("showImportWindow")
//    public void showImportWindow() {
//        this.showImportWindow = true;
//    }
//
//    /**
//     * 点击"查询"时的响应方法，显示搜索窗口
//     */
//    @Command
//    @NotifyChange("showSearchWindow")
//    public void showSearchWindow() {
//        this.showSearchWindow = true;
//    }
//
//    /**
//     * 选择文件时，后台响应方法
//     *
//     * @param event
//     */
//    @Command
//    @NotifyChange("*")
//    public void doUploadFile(@BindingParam("event") UploadEvent event) throws Exception {
//
//        String extension = event.getMedia().getFormat().toLowerCase();
//
//        this.uploadFilename = event.getMedia().getName();    // 原始文件名
//
//        if (!Objects.equals("xls", extension) && !Objects.equals("xlsx", extension)) {
//            ZkUtils.showError("XLS 文件类型！！\n当前类型为：" + extension.toUpperCase(), "文件格式不对!");
//            return;
//        }
//
//        if (extension.equals("xls") || extension.equals("xlsx")) {
//            // 转义后的文件名
//            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
//            String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
//            this.uploadFilename = event.getMedia().getName();    // 原始文件名
//
//            // 返回Object對象
//            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
//            for (List list : lists) {
//
//                System.out.println(list.toString());
//            }
//
//
//            // 返回CustomerBalanceEntity對象列表
//
//            importEntities = ExcelUtil.readExcel(path + fileName, VehicleDisembarksWarehousingEntity.class);
//
//
//            // 删除上传来的临时文件
//            FileUtils.deleteQuietly(new File(path + fileName));
//
//
//        } else if (extension.equals("csv")) {
//            String enc = CSVUtil.getEncoding(event.getMedia().getFormat());
//            // 转义后的文件名
//            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
//            String fileName = ZkUtils.saveCsv(event.getMedia(), path);
//            this.uploadFilename = event.getMedia().getName();    // 原始文件名
//            ZkUtils.showInformation(path + fileName, "转义后的文件名");
//
//            Set<String> headerSet = new HashSet<>();
//            Class<AllotVehicleEntity> clazz = AllotVehicleEntity.class;
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                ExcelColumn header = field.getAnnotation(ExcelColumn.class);
//                if (header != null) {
//                    headerSet.add(header.value());
//                }
//            }
//            String[] headerArr = headerSet.toArray(new String[headerSet.size()]);
//            List<CSVRecord> csvRecords = CSVUtil.readCSV(path + fileName, headerArr);
//            for (CSVRecord record : csvRecords) {
//                System.out.println(record);
//            }
//            File file = new File(path + fileName);
//            FileReader reader = new FileReader(file);
//            System.out.println(EncodingDetect.getJavaEncodeFile(path + fileName));
//
//        }
//    }
//
//
//    /**
//     * 执行导入数据操作
//     */
//    @Command
//    @NotifyChange("*")
//    public void importData() {
//
//        //时间截取
//        for (VehicleDisembarksWarehousingEntity entity : importEntities) {
//
//            try {
//
//                String orderDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(entity.getInboundDate());
//
//                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(orderDateStart.substring(0, 10));
//
//                entity.setInboundDate(date);
//
//            } catch (ParseException e) {
//
//                e.printStackTrace();
//            }
//
//        }
//        int count = this.importEntities.size();
//        this.importEntities = vehicleDisembarksWarehousingService.saveAll(this.importEntities);
//        int errs = this.importEntities.size();
//        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
//        if (errs <= 0) {
//            this.importEntities.clear();
//            this.uploadFilename = "";
//            this.showImportWindow = false;
//            this.setPageResult(vehicleDisembarksWarehousingService.getPageList(this.getPageParam()));
//        }
//    }
//
//
//    /**
//     * 放弃导入操作
//     */
//    @Command
//    @NotifyChange({"showImportWindow", "importEntities", "uploadFilename"})
//    public void abort(@BindingParam("event") Event event) {
//        this.importEntities.clear();
//        this.uploadFilename = "";
//        this.showImportWindow = false;
//        if (event != null) {
//            event.stopPropagation();
//        }
//        this.setPageResult(vehicleDisembarksWarehousingService.getPageList(this.getPageParam()));
//    }
//
//
//    @Command
//    @NotifyChange("showSearchWindow")
//    public void abortSearch(@BindingParam("event") Event event) {
//        this.showSearchWindow = false;
//        if (event != null) {
//            event.stopPropagation();
//        }
//    }
//
//    public String genStyle(VehicleDisembarksWarehousingEntity entity) {
//        if (StringUtils.isBlank(entity.getVin()) || StringUtils.isBlank(entity.getVsn())) {
//            return "background:#ff9600";
//        }
//        return "";
//    }
//    /**
//     * 下载模板
//     */
//    @Command
//    @NotifyChange("*")
//    public void downloadTemplate() {
//        try {
//            byte[] buffer = null;
//            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/车辆下线入库.xlsx";
//            File file = new File(path);
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//            byte[] b = new byte[1000];
//            int n;
//            while ((n = fis.read(b)) != -1) {
//                bos.write(b, 0, n);
//            }
//            fis.close();
//            bos.close();
//            buffer = bos.toByteArray();
//            Filedownload.save(buffer, null, "车辆下线入库.xlsx");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//}
