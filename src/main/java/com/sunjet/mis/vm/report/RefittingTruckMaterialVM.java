package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.warehouse.entity.RefittingTruckMaterialEntity;
import com.sunjet.mis.module.warehouse.entity.SpecialVehicleMonthlyPlanBalanceEntity;
import com.sunjet.mis.module.warehouse.service.RefittingTruckMaterialService;
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

/**
 * @author SUNJET-YFS
 * @Title: RefittingTruckMaterialVM
 * @ProjectName mis
 * @Description: 改装车月物料申报表
 * @date 2019/4/1110:17
 */
public class RefittingTruckMaterialVM extends ListVM<RefittingTruckMaterialEntity> {

    @WireVariable
    private RefittingTruckMaterialService refittingTruckMaterialService;
    @Getter
    @Setter
    private RefittingTruckMaterialEntity refittingTruckMaterialEntity = RefittingTruckMaterialEntity.builder().build();
    @Getter
    @Setter
    private List<RefittingTruckMaterialEntity> refittingTruckMaterialEntities = new ArrayList<>();

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
    private List<RefittingTruckMaterialEntity> importEntities = new ArrayList<>();

    @Init
    public void init() {
        //this.setSearchFormHeight(49);
        this.initPermissionStatus(RefittingTruckMaterialEntity.class.getSimpleName());

//        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(refittingTruckMaterialEntity, Order.DESC, "vsn");
        refittingTruckMaterialEntities = refittingTruckMaterialService.findAll();
        getPageList();
    }


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号" ).build(),
            TableHeaderInfo.builder().label("时间" ).build(),
            TableHeaderInfo.builder().label("物料号" ).build(),
            TableHeaderInfo.builder().label("品种代码" ).build(),
            TableHeaderInfo.builder().label("平台" ).build(),
            TableHeaderInfo.builder().label("车系(APP)" ).build(),
            TableHeaderInfo.builder().label("车型(APP)" ).build(),
            TableHeaderInfo.builder().label("车型" ).build(),
            TableHeaderInfo.builder().label("车型名称-1" ).build(),
            TableHeaderInfo.builder().label("发动机号" ).build(),
            TableHeaderInfo.builder().label("颜色" ).build(),
            TableHeaderInfo.builder().label("空调" ).build(),
            TableHeaderInfo.builder().label("车型平台" ).build(),
            TableHeaderInfo.builder().label("排放标准" ).build(),
            TableHeaderInfo.builder().label("备注" ).build(),
            TableHeaderInfo.builder().label("配置" ).build(),
            TableHeaderInfo.builder().label("A价（ABS)" ).build(),
            TableHeaderInfo.builder().label("物料号（对应非ABS代码）" ).build(),
            TableHeaderInfo.builder().label("产地" ).build()

    );

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.refittingTruckMaterialEntities.clear();
        this.refittingTruckMaterialEntity.setVsn(null);
        this.refittingTruckMaterialEntity.setBrandCode(null);
        this.refittingTruckMaterialEntity.setStartDate(null);
        this.refittingTruckMaterialEntity.setEndDate(null);
        this.getPageList();
    }

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(refittingTruckMaterialService.getPageList(this.getPageParam()));
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

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(refittingTruckMaterialEntity);
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

        this.uploadFilename = event.getMedia().getName();    // 原始文件名

        if (!Objects.equals("xls", extension) && !Objects.equals("xlsx", extension)) {
            ZkUtils.showError("XLS 文件类型！！\n当前类型为：" + extension.toUpperCase(), "文件格式不对!");
            return;
        }

        if (extension.equals("xls") || extension.equals("xlsx")) {
            // 转义后的文件名
            String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_TEMP;
            String fileName = ZkUtils.onUploadFile(event.getMedia(), path);
            this.uploadFilename = event.getMedia().getName();    // 原始文件名

            // 返回Object對象
            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
            for (List list : lists) {
                System.out.println(list.toString());
            }

            // 返回CustomerBalanceEntity對象列表

            importEntities = ExcelUtil.readExcel(path + fileName, RefittingTruckMaterialEntity.class);


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
    public void importData() {

//        //时间截取
//        for (VehicleDisembarksWarehousingEntity entity : importEntities) {
//
//            try {
//                String orderDateStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(entity.getInboundDate());
//                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(orderDateStart.substring(0, 10));
//                entity.setInboundDate(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        int count = this.importEntities.size();
        this.importEntities = refittingTruckMaterialService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(refittingTruckMaterialService.getPageList(this.getPageParam()));
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
        this.setPageResult(refittingTruckMaterialService.getPageList(this.getPageParam()));
    }


    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(RefittingTruckMaterialEntity entity) {
        if (StringUtils.isBlank(entity.getVsn())||StringUtils.isBlank(entity.getNoabsvsn())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/改装车月物料申报表.xls";
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
            Filedownload.save(buffer, null, "改装车月物料申报表.xls");
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
        List<RefittingTruckMaterialEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (RefittingTruckMaterialEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("dateYearMonth");
        keyList.add("vsn");
        keyList.add("brandCode");
        keyList.add("terrace");
        keyList.add("audiApp");
        keyList.add("vehicleTypeApp");
        keyList.add("vehicleType");
        keyList.add("vehicleTypeName");
        keyList.add("engineCode");
        keyList.add("color");
        keyList.add("airConditioner");
        keyList.add("vehiclePlatform");
        keyList.add("effluent");
        keyList.add("note");
        keyList.add("allocation");
        keyList.add("aprice");
        keyList.add("noabsvsn");
        keyList.add("production");
        ExcelUtil.listMapToExcel(title, keyList, maps);
    }
}
