package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.service.AllotVehicleService;
import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 配车单
 */
public class AllotVehicleVM extends ListVM<AllotVehicleEntity> {

    @WireVariable
    private AllotVehicleService allotVehicleService;
    @WireVariable
    private DistributorService distributorService;


    @Getter
    @Setter
    private AllotVehicleEntity allotVehicleEntity = AllotVehicleEntity.builder().build();
    @Getter
    @Setter
    private List<AllotVehicleEntity>  allotVehicleEntities = new  ArrayList<>();

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<AllotVehicleEntity> importEntities = new ArrayList<>();
    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("开票审核状态").width("60px").build(),
            TableHeaderInfo.builder().label("U9返回信息").width("60px").build(),
            TableHeaderInfo.builder().label("配车单号").width("60px").build(),
            TableHeaderInfo.builder().label("配车员").width("100px").build(),
            TableHeaderInfo.builder().label("特殊需求单号").width("120px").build(),
            TableHeaderInfo.builder().label("客户编码(工业)").width("70px").build(),
            TableHeaderInfo.builder().label("客户编码(SGMW)").width("70px").build(),
            TableHeaderInfo.builder().label("客户名称").width("70px").build(),
            TableHeaderInfo.builder().label("配车量").width("70px").build(),
            TableHeaderInfo.builder().label("销售A价").width("70px").build(),
            TableHeaderInfo.builder().label("增减后价").width("70px").build(),
            TableHeaderInfo.builder().label("合同单价").width("70px").build(),
            TableHeaderInfo.builder().label("VIN").width("70px").build(),
            TableHeaderInfo.builder().label("VSN").width("70px").build(),
            TableHeaderInfo.builder().label("车辆型号").width("70px").build(),
            TableHeaderInfo.builder().label("车辆名称").width("70px").build(),
            TableHeaderInfo.builder().label("颜色").width("70px").build(),
            TableHeaderInfo.builder().label("仓库名称").width("70px").build(),
            TableHeaderInfo.builder().label("库位").width("70px").build(),
            TableHeaderInfo.builder().label("入库时间").width("70px").build(),
            TableHeaderInfo.builder().label("单据日期").width("70px").build(),
            TableHeaderInfo.builder().label("销售订单号").width("70px").build(),
            TableHeaderInfo.builder().label("配车用途").width("70px").build(),
            TableHeaderInfo.builder().label("省份").width("70px").build(),
            TableHeaderInfo.builder().label("客户地址").width("70px").build(),
            TableHeaderInfo.builder().label("客户联系人").width("70px").build(),
            TableHeaderInfo.builder().label("联系方式").width("70px").build()

    );

    //配车单列表
    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(AllotVehicleEntity.class.getSimpleName());
        if (this.getActiveUser().getUserType().equals(UserType.DISTRIBUTOR.getIndex())) {
            allotVehicleEntity.setSgmwCode(this.getActiveUser().getLogId());
            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
        }
        refreshFirstPage(allotVehicleEntity, Order.ASC, "entryTime");
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
        refreshPage(allotVehicleEntity);
        getPageList();
    }

//    @Command
//    @NotifyChange("pageResult")
//    public void simpleSearch() {
//
//        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
//            allotVehicleEntity.setSgmwCode(this.getKeyword());
//            allotVehicleEntity.setDistributorCode(this.getKeyword());
//            allotVehicleEntity.setDistributorName(this.getKeyword());
//        }
//
//        getPageList();
//    }

    @Command
    @NotifyChange("*")
    public void reset() {
        this.allotVehicleEntities.clear();
        this.allotVehicleEntity.setDistributorCode(null);
        this.allotVehicleEntity.setVin(null);
        this.allotVehicleEntity.setDistributorName(null);
        this.allotVehicleEntity.setDistributorCode(null);
        this.allotVehicleEntity.setVsn(null);
        this.allotVehicleEntity.setSgmwCode(null);
//        this.allotVehicleEntity.setStartDate(null);sgmwCode
//        this.allotVehicleEntity.setEndDate(null);
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

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(allotVehicleService.getPageList(this.getPageParam()));
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
     * 新增
     */

    @Command
    @NotifyChange("showForm")
    public void showForm() {
        //refreshFirstPage(targetOrderEntity, Order.DESC, "name");
        getPageList();
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
            importEntities = ExcelUtil.readExcel(path + fileName, AllotVehicleEntity.class);

            //用SGMW的客户编号替换五菱工业的客户编号
            List<DistributorEntity> listDistributor = distributorService.findAll();
            for (AllotVehicleEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
                if (list.size() == 1) {
//                    entity.setCustomerCode(list.get(0).getSgmwCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                }
            }

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
            Class<AllotVehicleEntity> clazz = AllotVehicleEntity.class;
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

    /**
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData() {
        int count = this.importEntities.size();
        this.importEntities = allotVehicleService.saveAll(this.importEntities);
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
            this.setPageResult(allotVehicleService.getPageList(this.getPageParam()));
        }
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
        this.setPageResult(allotVehicleService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(AllotVehicleEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/配车单明细表.xls";
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
            Filedownload.save(buffer, null, "配车单明细表.xls");
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
        List<AllotVehicleEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (AllotVehicleEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("auditStatus");
        keyList.add("u9Result");
        keyList.add("allotNo");
        keyList.add("allotOperator");
        keyList.add("specialRequestNo");
        keyList.add("distributorCode");
        keyList.add("sgmwCode");
        keyList.add("distributorName");
        keyList.add("allotCount");
        keyList.add("price");
        keyList.add("adjustedPrice");
        keyList.add("contractPrice");
        keyList.add("vin");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("vehicleName");
        keyList.add("color");
        keyList.add("storage");
        keyList.add("storageLocation");
        keyList.add("entryTime");
        keyList.add("invoiceDate");
        keyList.add("orderNo");
        keyList.add("allotUsage");
        keyList.add("province");
        keyList.add("distributorAddress");
        keyList.add("distributorContact");
        keyList.add("contactPhone");



        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }
}
