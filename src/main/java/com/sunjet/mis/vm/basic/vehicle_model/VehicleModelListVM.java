package com.sunjet.mis.vm.basic.vehicle_model;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.VehicleModelEntity;
import com.sunjet.mis.module.basic.service.VehicleModelService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
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
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;

/*车型管理*/
public class VehicleModelListVM extends ListVM<VehicleModelEntity> {

    @WireVariable
    private VehicleModelService vehicleModelService;


    @Getter
    @Setter
    private VehicleModelEntity vehicleModelEntity = VehicleModelEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private VehicleModelEntity selectedVehicleModel;

    @Getter
    @Setter
    private boolean showImportWindow = false;

    @Getter
    private List<VehicleModelEntity> importEntities = new ArrayList<>();

    @Getter
    private String uploadFilename;

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("车型编号").build(),
            TableHeaderInfo.builder().label("车型名称").build(),
            TableHeaderInfo.builder().label("车系").build(),
            TableHeaderInfo.builder().label("排放").build(),
            TableHeaderInfo.builder().label("排量").build(),
            TableHeaderInfo.builder().label("描述").build()

    );

    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(VehicleModelEntity.class.getSimpleName());
        refreshFirstPage(vehicleModelEntity);
        getPageList();
    }

    @Override
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        vehicleModelEntity.setName(this.getKeyword());
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

            importEntities = ExcelUtil.readExcel(path + fileName, VehicleModelEntity.class);

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
    public void importData(){
        int count = this.importEntities.size();
        this.importEntities = vehicleModelService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count,count-errs), "系统提示");

        if(errs<=0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(vehicleModelService.getPageList(this.getPageParam()));
        }
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.getPageParam().setOrderName("id");
        this.setPageResult(vehicleModelService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(vehicleModelEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(vehicleModelEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }

    /*
    重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.vehicleModelEntity.setVehicleSeries(null);
        this.vehicleModelEntity.setCode(null);
        this.getPageList();
    }



    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedVehicleModel = VehicleModelEntity.builder().build();
            } else {
                this.selectedVehicleModel = vehicleModelService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, VehicleModelListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, VehicleModelListVM.this, "selectedVehicleModel");
//            BindUtils.postNotifyChange(null,null,ResourceListVM.this,"parentOrgs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange("*")
    public void abort(@BindingParam("event") Event event) {
//        this.parentOrgs.clear();
        this.selectedVehicleModel = null;
        this.showForm = false;
        showImportWindow = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        vehicleModelService.save(this.selectedVehicleModel);
        this.selectedVehicleModel = null;
        this.showForm = false;
        this.getPageList();
    }

    /**
     * 删除对象
     */
    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id) {
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = vehicleModelService.deleteById(id);
                if (bl == true) {
                    ZkUtils.showInformation("删除成功", "系统提示");
                    //重新获取分页数据
                    this.getPageList();
                } else {
                    ZkUtils.showError("删除失败", "系统提示");
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });


    }

    @Command
    @NotifyChange("*")
    public void showForm() {
        this.showForm = true;
    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }

    public String genStyle(VehicleModelEntity entity) {
        if (StringUtils.isBlank(entity.getCode()) || StringUtils.isBlank(entity.getVehicleSeries())) {
            return "background:#ff9600";
        }
        return "";
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
        List<VehicleModelEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (VehicleModelEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("code");
        keyList.add("name");
        keyList.add("vehicleSeries");
        keyList.add("effluent");
        keyList.add("displacement");
        keyList.add("comment");


        ExcelUtil.listMapToExcel(title, keyList, maps);
    }


}
