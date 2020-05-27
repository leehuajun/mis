package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.service.SolidPinReportService;
import com.sunjet.mis.module.report.service.SolidPinService;
import com.sunjet.mis.module.report.view.CarForecastDemandPlanningView;
import com.sunjet.mis.module.report.view.SolidPinReportView;
import com.sunjet.mis.module.warehouse.entity.PassengeCarBalanceEntity;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinReportVm
 * @ProjectName mis
 * @Description: 35实销整合报表
 * @date 2019/2/1817:08
 */
public class SolidPinReportVm extends ListVM<SolidPinReportView> {
    @WireVariable
    private SolidPinReportService solidPinReportService;

    @WireVariable
    private SolidPinService solidPinService;
    @Getter
    @Setter
    private SolidPinReportView solidPinReportView = new SolidPinReportView();
    @Getter
    @Setter
    private List<SolidPinReportView> solidPinReportViews = new ArrayList<>();
    @Getter
    @Setter
    private List<SolidPinEntity> solidPinEntities = new ArrayList<>();
    @Getter
    @Setter
    private SolidPinEntity solidPinEntity = new SolidPinEntity();
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
    private List<SolidPinEntity> importEntities = new ArrayList<>();
    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("车架号").build(),
            TableHeaderInfo.builder().label("销售日期").build(),
            TableHeaderInfo.builder().label("经销商名称").build(),
            TableHeaderInfo.builder().label("单据日期").build(),
            TableHeaderInfo.builder().label("客户名称").build(),
            TableHeaderInfo.builder().label("VSN").build(),
            TableHeaderInfo.builder().label("客户省份").build(),
            TableHeaderInfo.builder().label("车辆型号").build(),
            TableHeaderInfo.builder().label("经销商号").build(),
            TableHeaderInfo.builder().label("一级代码").build(),
            TableHeaderInfo.builder().label("车型").build(),
            TableHeaderInfo.builder().label("开票代码").build(),
            TableHeaderInfo.builder().label("所属区域").build()

    );


    @Init
    public void init() {
       this.setSearchFormHeight(49);
        this.initPermissionStatus(SolidPinReportView.class.getSimpleName());
//  if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(solidPinReportView, Order.DESC, "invoiceDate");

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

        this.setPageResult(solidPinReportService.getPageList(this.getPageParam()));
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
        List<SolidPinReportView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (SolidPinReportView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }

        List<String> keyList = new ArrayList<>();
        keyList.add("vin");
        keyList.add("invoiceDate");
        keyList.add("distributorName");
        keyList.add("billDate");
        keyList.add("customerName");
        keyList.add("vsn");
        keyList.add("customerProvinces");
        keyList.add("model");
        keyList.add("distributorCode");
        keyList.add("onecode");
        keyList.add("name");
        keyList.add("distributorCode");
        keyList.add("regionName");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.solidPinEntities.clear();
        this.solidPinReportViews.clear();
        this.solidPinReportView.setCustomerName("");
        this.solidPinReportView.setDistributorName("");
        this.solidPinReportView.setDistributorCode("");
        this.solidPinReportView.setVsn("");
        this.solidPinReportView.setVin("");
        this.solidPinReportView.setStartDate(null);
        this.solidPinReportView.setEndDate(null);
       // this.getPageList();
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
     *
     * @param event
     *//*
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

            importEntities = ExcelUtil.readExcel(path + fileName, SolidPinEntity.class);

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
    }*/

    /**
     * 执行导入数据操作
     */
  /*  @Command
    @NotifyChange("*")
    public void importData() throws ParseException {

        //字符串时间格式转化
        for (SolidPinEntity entity : importEntities) {
            Date orderDateStart = new SimpleDateFormat("yyyyMMdd").parse(entity.getSaleDate());
            String DateStart = new SimpleDateFormat("yyyy-MM-dd").format(orderDateStart);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(DateStart);
            entity.setInvoiceDate(date);
        }
        int count = this.importEntities.size();
        this.importEntities = solidPinService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(solidPinReportService.getPageList(this.getPageParam()));
        }
    }*/

    /**
     * 放弃导入操作
     */
//    @Command
//    @NotifyChange({"showImportWindow", "importEntities", "uploadFilename"})
//    public void abort(@BindingParam("event") Event event) {
//        this.importEntities.clear();
//        this.uploadFilename = "";
//        this.showImportWindow = false;
//        if (event != null) {
//            event.stopPropagation();
//        }
//        this.setPageResult(solidPinReportService.getPageList(this.getPageParam()));
//    }
    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(solidPinReportView);
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

    public String genStyle(SolidPinEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorName()) || StringUtils.isBlank(entity.getDistributorCode())) {
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
        /*try {
            byte[] buffer = null;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/35天实销客户信息卡.xls";
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
            Filedownload.save(buffer, null, "35天实销客户信息卡.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}