package com.sunjet.mis.vm.basic.TruckSvarietyProvenanc;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.basic.entity.PassengerCompartmenProvenancCarSeriesEntity;
import com.sunjet.mis.module.basic.entity.TruckSvarietyProvenancEntity;
import com.sunjet.mis.module.basic.service.PassengerCompartmenProvenancCarSeriesService;
import com.sunjet.mis.module.basic.service.TruckSvarietyProvenancService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
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
 * @Title: PassengerCompartmenProvenancCarSeriesMV
 * @ProjectName mis
 * @Description: 客厢车 车系 产地 vsn 的匹配表
 * @date 2019/4/317:15
 */
public class PassengerCompartmenProvenancCarSeriesMV extends ListVM<PassengerCompartmenProvenancCarSeriesEntity> {

    @WireVariable
    private PassengerCompartmenProvenancCarSeriesService passengerCompartmenProvenancCarSeriesService;
    @Getter
    @Setter
    private PassengerCompartmenProvenancCarSeriesEntity passengerCompartmenProvenancCarSeriesEntity = PassengerCompartmenProvenancCarSeriesEntity.builder().build();
    //    @Getter
//    @Setter
//    private PassengerCompartmenProvenancCarSeriesEntity passengerCompartmenProvenancCarSeriesEntity = PassengerCompartmenProvenancCarSeriesEntity.builder().build();
    @Getter
    @Setter
    List<PassengerCompartmenProvenancCarSeriesEntity> passengerCompartmenProvenancCarSeriesEntities = new ArrayList<>();
    @Getter
    @Setter
    private List<PassengerCompartmenProvenancCarSeriesEntity> importEntities = new ArrayList<>();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    @Setter
    private PassengerCompartmenProvenancCarSeriesEntity selectedPassengerCompartmenProvenancCarSeriesEntity;


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("ABS-VSN").build(),
            TableHeaderInfo.builder().label("产品类别").build(),
            TableHeaderInfo.builder().label("产品名称").build(),
            TableHeaderInfo.builder().label("基地").build()
    );


    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(PassengerCompartmenProvenancCarSeriesEntity.class.getSimpleName());
//  if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        this.setMode(OpenMode.DIALOG);
        refreshFirstPage(passengerCompartmenProvenancCarSeriesEntity, Order.DESC, "production");
        //passengerCompartmenProvenancCarSeriesEntities = passengerCompartmenProvenancCarSeriesService.findAll();
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

        this.setPageResult(passengerCompartmenProvenancCarSeriesService.getPageList(this.getPageParam()));
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


     /*下一页*/
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(passengerCompartmenProvenancCarSeriesEntity);
        getPageList();
    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        if (passengerCompartmenProvenancCarSeriesEntity != null) {
            this.passengerCompartmenProvenancCarSeriesEntity.setAbsvsn(null);
            this.passengerCompartmenProvenancCarSeriesEntity.setProduction(null);
        }
        this.getPageList();
    }

    /*快速搜索框*/
    @Command
    @NotifyChange("pageResult")
   // @Override
//    public void simpleSearch() {
//        passengerCompartmenProvenancCarSeriesEntity.setAbsvsn(this.getKeyword());
//      //  passengerCompartmenProvenancCarSeriesEntity.setProduction(this.getKeyword());
////        truckSvarietyProvenancEntity.setProductName(this.getKeyword());
////        truckSvarietyProvenancEntity.setProduction(this.getKeyword());
////        truckSvarietyProvenancEntity.setProductCategory(this.getKeyword());
//        getPageList();
//    }


    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedPassengerCompartmenProvenancCarSeriesEntity = PassengerCompartmenProvenancCarSeriesEntity.builder().build();
            } else {
                this.selectedPassengerCompartmenProvenancCarSeriesEntity = passengerCompartmenProvenancCarSeriesService.findById(id);
            }
            this.showForm = true;
            //   this.setTitle("操作列表管理");
            //  this.setFormUrl("/views/system/operation_form.zul");
            BindUtils.postNotifyChange(null, null, PassengerCompartmenProvenancCarSeriesMV.this, "showForm");
            BindUtils.postNotifyChange(null, null, PassengerCompartmenProvenancCarSeriesMV.this, "readonly");
            BindUtils.postNotifyChange(null, null, PassengerCompartmenProvenancCarSeriesMV.this, "selectedPassengerCompartmenProvenancCarSeriesEntity");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("showForm")
    public void showForm(String id) {
        if (StringUtils.isBlank(id)) {
            this.selectedPassengerCompartmenProvenancCarSeriesEntity = PassengerCompartmenProvenancCarSeriesEntity.builder().build();
        } else {
            this.selectedPassengerCompartmenProvenancCarSeriesEntity = passengerCompartmenProvenancCarSeriesService.findById(id);
        }
        this.showForm = true;
    }

    /*保存新增*/
    @Command
    @NotifyChange("*")
    public void submit() {
        passengerCompartmenProvenancCarSeriesService.save(this.selectedPassengerCompartmenProvenancCarSeriesEntity);
        this.showForm = null;
        this.showForm = false;
        this.getPageList();
    }

    /**
     * 删除对象
     *
     * @param id
     */
    @Command
    public void confirmDeleteObject(@BindingParam("id") String id) {
        boolean bl = passengerCompartmenProvenancCarSeriesService.deleteById(id);
        if (bl == true) {
            ZkUtils.showInformation("删除成功", "系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null, null, this, "pageResult");
        } else {
            ZkUtils.showError("删除失败", "系统提示");
        }
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

            importEntities = ExcelUtil.readExcel(path + fileName, PassengerCompartmenProvenancCarSeriesEntity.class);

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
        this.importEntities = passengerCompartmenProvenancCarSeriesService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(passengerCompartmenProvenancCarSeriesService.getPageList(this.getPageParam()));
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange("*")
    public void abort(@BindingParam("event") Event event) {
        this.importEntities.clear();
        this.uploadFilename = "";
        this.showForm = false;
        this.showImportWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
        this.setPageResult(passengerCompartmenProvenancCarSeriesService.getPageList(this.getPageParam()));
    }


    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(PassengerCompartmenProvenancCarSeriesEntity entity) {
        if (StringUtils.isBlank(entity.getAbsvsn())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/客厢车车系产地vsn的匹配表.xlsx";
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
            Filedownload.save(buffer, null, "客厢车车系产地vsn的匹配表.xlsx");
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
        List<PassengerCompartmenProvenancCarSeriesEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (PassengerCompartmenProvenancCarSeriesEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("absvsn");
        keyList.add("productCategory");
        keyList.add("productName");
        keyList.add("production");


        ExcelUtil.listMapToExcel(title, keyList, maps);
    }


}
