package com.sunjet.mis.vm.warehouse;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
import com.sunjet.mis.module.warehouse.entity.DistributionVehicleEntity;
import com.sunjet.mis.module.warehouse.service.DistributionVehicleService;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class DistributionVehicleVM extends ListVM<DistributionVehicleEntity> {

    @WireVariable
    private DistributionVehicleService distributionVehicleService;

    @Getter
    @Setter
    private DistributionVehicleEntity distributionVehicleEntity = DistributionVehicleEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private DistributionVehicleEntity selectedDistributionVehicle;

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<DistributionVehicleEntity> importEntities = new ArrayList<>();


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("订单号").build(),
            TableHeaderInfo.builder().label("单位代码").build(),
            TableHeaderInfo.builder().label("单位名称").build(),
            TableHeaderInfo.builder().label("办事处").build(),
            TableHeaderInfo.builder().label("收车单位").build(),
            TableHeaderInfo.builder().label("收车地址").build(),
            TableHeaderInfo.builder().label("VSN码").build(),
            TableHeaderInfo.builder().label("车型").build(),
            TableHeaderInfo.builder().label("数量").build(),
            TableHeaderInfo.builder().label("发货凭证号").build(),
            TableHeaderInfo.builder().label("分货日期").build(),
            TableHeaderInfo.builder().label("发货日期").build(),
            TableHeaderInfo.builder().label("出库仓库").build(),
            TableHeaderInfo.builder().label("承运商").build(),
            TableHeaderInfo.builder().label("发票号").build(),
            TableHeaderInfo.builder().label("开票日期").build(),
            TableHeaderInfo.builder().label("发票金额").build(),
            TableHeaderInfo.builder().label("CUR").build(),
            TableHeaderInfo.builder().label("分车单价").build(),
            TableHeaderInfo.builder().label("分车金额").build(),
            TableHeaderInfo.builder().label("分车月份").build(),
            TableHeaderInfo.builder().label("品种").build(),
            TableHeaderInfo.builder().label("批次号").build()

    );


    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(DistributionVehicleEntity.class.getSimpleName());
        refreshFirstPage(distributionVehicleEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        distributionVehicleEntity.setBatchNumber(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(distributionVehicleService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(distributionVehicleEntity);
        getPageList();
    }


    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(distributionVehicleEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }




    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedDistributionVehicle = DistributionVehicleEntity.builder().build();
            } else {
                this.selectedDistributionVehicle = distributionVehicleService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, DistributionVehicleVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, DistributionVehicleVM.this, "selectedDistributionVehicle");
//            BindUtils.postNotifyChange(null,null,ResourceListVM.this,"parentOrgs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"*","showImportWindow"})
    public void abort(@BindingParam("event") Event event) {
//        this.parentOrgs.clear();
        this.selectedDistributionVehicle = null;
        this.showForm = false;
        this.importEntities.clear();
        this.uploadFilename = "";
        this.showImportWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
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
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        distributionVehicleService.save(this.selectedDistributionVehicle);
        this.selectedDistributionVehicle = null;
        this.showForm = false;


        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = distributionVehicleService.deleteById(id);
                if(bl==true){
                    ZkUtils.showInformation("删除成功","系统提示");
                    //重新获取分页数据
                    getPageList();
                    //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
                    BindUtils.postNotifyChange(null,null,this,"pageResult");
                }else{
                    ZkUtils.showError("删除失败","系统提示");
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
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
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData() {
        int count = this.importEntities.size();
        this.importEntities = distributionVehicleService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        if(errs>0){
            ZkUtils.showInformation(String.format("导入失败！总记录数：%d, 没有问题数据：%d条", count, count - errs), "系统提示");
        }else{
            ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        }
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.getPageList();
        }
    }

    /**
     * 选择文件时，后台响应方法
     *
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
//            ZkUtils.showInformation(path + fileName, "转义后的文件名");

            // 返回Object對象
            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
            for (List list : lists) {
                System.out.println(list.toString());
            }
            // 返回CustomerBalanceEntity對象列表
            importEntities = ExcelUtil.readExcel(path + fileName, DistributionVehicleEntity.class);



            // 删除上传来的临时文件
            FileUtils.deleteQuietly(new File(path + fileName));

//            System.out.println(entities.size());
//            for(BalanceEntity entity:entities){
//                System.out.println(entity.toString());
//            }
        } else if (extension.equals("csv")) {
            String enc = CSVUtil.getEncoding(event.getMedia().getFormat());
//            System.out.println(CSVUtil.getEncoding(event.getMedia().getFormat()));
//            System.out.println(EncodingDetect.getJavaEncodeByte(event.getMedia().getStringData().getBytes()));
//            System.out.println(event.getMedia().getStringData());

            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
//          String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
            String fileName = ZkUtils.saveCsv(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名
            ZkUtils.showInformation(path + fileName, "转义后的文件名");
//
//            System.out.println(event.getMedia().getStringData());
////
            Set<String> headerSet = new HashSet<>();
            Class<DistributionVehicleEntity> clazz = DistributionVehicleEntity.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                ExcelColumn header = field.getAnnotation(ExcelColumn.class);
                if (header != null) {
                    headerSet.add(header.value());
                }
            }
//
            String[] headerArr = headerSet.toArray(new String[headerSet.size()]);
            List<CSVRecord> csvRecords = CSVUtil.readCSV(path + fileName, headerArr);
            for (CSVRecord record : csvRecords) {
                System.out.println(record);
            }
////            System.out.println("原文件流的编码格式: " + fileEncode);
            File file = new File(path + fileName);
            FileReader reader = new FileReader(file);
            System.out.println(EncodingDetect.getJavaEncodeFile(path + fileName));
//            System.out.println("新文件流的编码格式: " + reader.getEncoding());
//            System.out.println("新文件的编码格式: " + EncodingDetect.getJavaEncodeFile(path + fileName));
        }
    }

    public String genStyle(DistributionVehicleEntity entity) {
        if (StringUtils.isBlank(entity.getBatchNumber())) {
            return "background:#ff9600";
        }
        return "";
    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.distributionVehicleEntity.setVehicleModel("");
        this.distributionVehicleEntity.setVariety("");
        this.distributionVehicleEntity.setBatchNumber("");
        this.getPageList();
    }


    @Command
    @NotifyChange("*")
    public void downloadTemplate() {
        try {
            byte[] buffer = null;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/底盘分车统计.xlsx";
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
            Filedownload.save(buffer, null, "底盘分车统计.xlsx");
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
        List<DistributionVehicleEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (DistributionVehicleEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("orderNumber");
        keyList.add("unitCode");
        keyList.add("unitName");
        keyList.add("office");
        keyList.add("collectUnit");
        keyList.add("collectAddress");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("amount");
        keyList.add("deliveryProve");
        keyList.add("cargoData");
        keyList.add("deliveryData");
        keyList.add("outboundWarehouse");
        keyList.add("carriers");
        keyList.add("invoiceNo");
        keyList.add("invoiceData");
        keyList.add("invoiceAmount");
        keyList.add("cur");
        keyList.add("pointsPrice");
        keyList.add("pointsAmount");
        keyList.add("pointsMonth");
        keyList.add("variety");
        keyList.add("batchNumber");

        ExcelUtil.listMapToExcel(title, keyList, maps);
    }

}
