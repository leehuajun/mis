package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import com.sunjet.mis.module.report.service.VehicleInvService;
import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleInvVM extends ListVM<VehicleInvEntity> {
    @WireVariable
    private VehicleInvService vehicleInvService;
    @WireVariable
    private DistributorService distributorService;

    @Getter
    @Setter
    private VehicleInvEntity vehicleInvEntity = VehicleInvEntity.builder().build();

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<VehicleInvEntity> importEntities = new ArrayList<>();

    @Init
    public void init(){
        this.setSearchFormHeight(49);
        this.initPermissionStatus(VehicleInvEntity.class.getSimpleName());
        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
        }
        refreshFirstPage(vehicleInvEntity, Order.DESC, "id");
        getPageList();
    }
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

    }



    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("库存状态").build(),
            TableHeaderInfo.builder().label("VIN").build(),
            TableHeaderInfo.builder().label("VSN").build(),
            TableHeaderInfo.builder().label("车辆型号").build(),
            TableHeaderInfo.builder().label("车辆名称").build(),
            TableHeaderInfo.builder().label("仓库").build(),
            TableHeaderInfo.builder().label("入库时间").build(),
            TableHeaderInfo.builder().label("运单生成时间").build(),
            TableHeaderInfo.builder().label("出库时间").build(),
            TableHeaderInfo.builder().label("销售订单号").build(),
            TableHeaderInfo.builder().label("客户订单号").build(),
            TableHeaderInfo.builder().label("客户省份").build(),
            TableHeaderInfo.builder().label("客户编码(工业)").build(),
            TableHeaderInfo.builder().label("客户编码(SGMW)").build(),
            TableHeaderInfo.builder().label("客户名称").build(),
            TableHeaderInfo.builder().label("客户送车地址").build(),
            TableHeaderInfo.builder().label("承运商").build(),
            TableHeaderInfo.builder().label("配车单号").build(),
            TableHeaderInfo.builder().label("配车人").build(),
            TableHeaderInfo.builder().label("开票通知单号").build(),
            TableHeaderInfo.builder().label("开票日期").build(),
            TableHeaderInfo.builder().label("制单人").build(),
            TableHeaderInfo.builder().label("审核人").build(),
            TableHeaderInfo.builder().label("发运单号").build()

    );

    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(vehicleInvEntity);
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

        this.setPageResult(vehicleInvService.getPageList(this.getPageParam()));
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
    /**
     * 点击"导入"时的响应方法，显示导入窗口
     */
    @Command
    @NotifyChange("showImportWindow")
    public void showImportWindow() {
        this.showImportWindow = true;

    }

    /**
     * 选择文件时，后台响应方法
     *
     * @param event
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) throws Exception {

//        String fileEncode= EncodingDetect.getJavaEncodeByte(event.getMedia().getStringData().getBytes());

        this.uploadFilename = event.getMedia().getName();    // 原始文件名

        String extension = this.uploadFilename.substring(this.uploadFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!Objects.equals("csv", extension) && !Objects.equals("xls", extension)&& !Objects.equals("xlsx", extension)) {
//            throw new Exception("只支持 XLS、XLSX 文件类型，当前类型为：" + extension.toUpperCase());
            ZkUtils.showError("只支持 CSV、XLS 文件类型！！\n当前类型为：" + extension.toUpperCase(), "文件格式不对!");
            return;
        }

        if(extension.equals("xls") || extension.equals("xlsx")){
            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
            String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名
//            ZkUtils.showInformation(path + fileName, "转义后的文件名");

            // 返回Object對象
//            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
//            for (List list : lists){
//                System.out.println(list.toString());
//            }
            // 返回CustomerBalanceEntity對象列表
            importEntities = ExcelUtil.readExcel(path + fileName, VehicleInvEntity.class);

            List<DistributorEntity> listDistributor = distributorService.findAll();
            for (VehicleInvEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getName().equalsIgnoreCase(entity.getDistributorName())).collect(Collectors.toList());
                if(list.size()==1){
                    entity.setDistributorCode(list.get(0).getCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                }
            }

//            importEntities.stream().forEach(entity->{
//                entity.setEnabled(true);
//                entity.setSubjectCode(entity.getSubjectCode().replaceAll(".00", ""));
//            });

            // 删除上传来的临时文件
            FileUtils.deleteQuietly( new File(path + fileName));

//            System.out.println(entities.size());
//            for(BalanceEntity entity:entities){
//                System.out.println(entity.toString());
//            }
        }else if(extension.equals("csv")){
            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
//          String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
            String fileName = ZkUtils.saveCsv(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名
            ZkUtils.showInformation(path + fileName, "转义后的文件名");

            System.out.println(event.getMedia().getStringData());
//
//            Set<String> headerSet = new HashSet<>();
//            Class<BalanceEntity> clazz = BalanceEntity.class;
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                ExcelColumn header = field.getAnnotation(ExcelColumn.class);
//                if (header != null) {
//                    headerSet.add(header.value());
//                }
//            }
//
//            String[] headerArr=headerSet.toArray(new String[headerSet.size()]);
//            List<CSVRecord> csvRecords = CSVUtil.readCSV(path + fileName, headerArr);
//            for(CSVRecord record : csvRecords){
//                System.out.println(record);
//            }
////            System.out.println("原文件流的编码格式: " + fileEncode);
//            File file = new File(path + fileName);
//            FileReader reader = new FileReader(file);
//            System.out.println("新文件流的编码格式: " + reader.getEncoding());
//            System.out.println("新文件的编码格式: " + EncodingDetect.getJavaEncodeFile(path + fileName));
        }
    }

    /**
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData(){
        int count = this.importEntities.size();
        this.importEntities = vehicleInvService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count,count-errs), "系统提示");

        if(errs<=0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(vehicleInvService.getPageList(this.getPageParam()));
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"showImportWindow","importEntities","uploadFilename"})
    public void abort(@BindingParam("event") Event event){
        this.importEntities.clear();
        this.uploadFilename = "";
        this.showImportWindow = false;
        if(event!=null){
            event.stopPropagation();
        }

        this.setPageResult(vehicleInvService.getPageList(this.getPageParam()));
    }

    public String genStyle(VehicleInvEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
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
        this.vehicleInvEntity.setVehicleModel(null);
        this.vehicleInvEntity.setVin(null);
        this.vehicleInvEntity.setVsn(null);
        this.vehicleInvEntity.setSgmwCode(null);
        this.vehicleInvEntity.setDistributorCode(null);
        this.getPageList();
    }

    /**
     * 下载模板
     */
    @Command
    @NotifyChange("*")
    public void downloadTemplate() {
        try {
            byte[] buffer = null;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/库存车辆状态跟踪.xlsx";
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
            Filedownload.save(buffer, null, "库存车辆状态跟踪.xlsx");
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
        List<VehicleInvEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if (total > 0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (VehicleInvEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("status");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("vehicleName");
        keyList.add("storage");
        keyList.add("entryTime");
        keyList.add("waybillTime");
        keyList.add("outTime");
        keyList.add("orderNo");
        keyList.add("distributorOrderNo");
        keyList.add("province");
        keyList.add("distributorCode");
        keyList.add("sgmwCode");
        keyList.add("distributorName");
        keyList.add("distributorAddress");
        keyList.add("carrier");
        keyList.add("allotNo");
        keyList.add("allotOperator");
        keyList.add("invoiceAdviceNo");
        keyList.add("invoiceTime");
        keyList.add("originator");
        keyList.add("auditor");
        keyList.add("dispatchNo");

        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }

}
