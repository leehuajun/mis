package com.sunjet.mis.vm.plan;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.plan.entity.OrderPlanEntity;
import com.sunjet.mis.module.plan.service.OrderPlanService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.service.ConfigService;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import com.sunjet.mis.vm.system.ConfigListVM;
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
import java.util.stream.Collectors;

public class PlanListVM extends ListVM<OrderPlanEntity> {

    @WireVariable
    private OrderPlanService orderPlanService;

    @Getter
    @Setter
    private OrderPlanEntity orderPlanEntity = OrderPlanEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private OrderPlanEntity selectedOrderPlan;

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<OrderPlanEntity> importEntities = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("年度").width("60px").build(),
            TableHeaderInfo.builder().label("月份").width("60px").build(),
            TableHeaderInfo.builder().label("经销商代码").width("60px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("100px").build(),
            TableHeaderInfo.builder().label("省份").width("120px").build(),
            TableHeaderInfo.builder().label("区域").width("70px").build(),
            TableHeaderInfo.builder().label("申报类型").width("70px").build(),
            TableHeaderInfo.builder().label("类型").width("70px").build(),
            TableHeaderInfo.builder().label("物料号").width("70px").build(),
            TableHeaderInfo.builder().label("型号").width("70px").build(),
            TableHeaderInfo.builder().label("车型").width("70px").build(),
            TableHeaderInfo.builder().label("品种代码").width("70px").build(),
            TableHeaderInfo.builder().label("颜色").width("70px").build(),
            TableHeaderInfo.builder().label("排放标准").width("70px").build(),
            TableHeaderInfo.builder().label("经销商申报数").width("70px").build(),
            TableHeaderInfo.builder().label("全月满足数").width("70px").build(),
            TableHeaderInfo.builder().label("不满足原因").width("70px").build(),
            TableHeaderInfo.builder().label("第1周满足计划").width("70px").build(),
            TableHeaderInfo.builder().label("第2周满足计划").width("70px").build(),
            TableHeaderInfo.builder().label("第3周满足计划").width("70px").build(),
            TableHeaderInfo.builder().label("第4周满足计划").width("70px").build(),
            TableHeaderInfo.builder().label("第5周满足计划").width("70px").build(),
            TableHeaderInfo.builder().label("第6周满足计划").width("70px").build()

    );


    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(OrderPlanEntity.class.getSimpleName());
        refreshFirstPage(orderPlanEntity);
        getPageList();
    }

    @Command
    @Override
    @NotifyChange("pageResult")
    public void simpleSearch(){
        orderPlanEntity.setDistributorCode(this.getKeyword());
        orderPlanEntity.setDistributorName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(orderPlanService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(orderPlanEntity);
        getPageList();
    }


    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(orderPlanEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }

    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedOrderPlan = OrderPlanEntity.builder().build();
            } else {
                this.selectedOrderPlan = orderPlanService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, PlanListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, PlanListVM.this, "selectedOrderPlan");
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
        this.selectedOrderPlan = null;
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
    @NotifyChange("*")
    public void submit() {
        orderPlanService.save(this.selectedOrderPlan);
        this.selectedOrderPlan = null;
        this.showForm = false;


        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = orderPlanService.deleteById(id);
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
        this.importEntities = orderPlanService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");

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
            importEntities = ExcelUtil.readExcel(path + fileName, OrderPlanEntity.class);

//            //用SGMW的客户编号替换五菱工业的客户编号
//            List<OrderPlanEntity> listDistributor = orderPlanService.findAll();
//            for (AllotVehicleEntity entity : importEntities) {
//                List<OrderPlanEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
//                if (list.size() == 1) {
////                    entity.setCustomerCode(list.get(0).getSgmwCode());
//                    entity.setSgmwCode(list.get(0).getSgmwCode());
//                }
//            }

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
            Class<OrderPlanEntity> clazz = OrderPlanEntity.class;
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

    public String genStyle(OrderPlanEntity entity) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/计划执行表.xlsx";
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
            Filedownload.save(buffer, null, "计划执行表.xlsx");
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
        List<OrderPlanEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (OrderPlanEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("year");
        keyList.add("month");
        keyList.add("distributorCode");
        keyList.add("distributorName");
        keyList.add("province");
        keyList.add("region");
        keyList.add("planType");
        keyList.add("type");
        keyList.add("vsn");
        keyList.add("model");
        keyList.add("vehicleModel");
        keyList.add("brandCode");
        keyList.add("color");
        keyList.add("effluent");
        keyList.add("requiredAmount");
        keyList.add("agreedAmount");
        keyList.add("reason");
        keyList.add("firstWeekAmount");
        keyList.add("secondWeekAmount");
        keyList.add("thirdWeekAmount");
        keyList.add("fourthWeekAmount");
        keyList.add("fifthWeekAmount");
        keyList.add("sixthWeekAmount");

        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }

}
