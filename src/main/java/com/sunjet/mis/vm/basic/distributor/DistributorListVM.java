package com.sunjet.mis.vm.basic.distributor;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.basic.service.ProvinceService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.service.OrgService;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DistributorListVM extends ListVM<DistributorEntity> {

    @WireVariable
    private DistributorService distributorService;
    @WireVariable
    private ProvinceService provinceService;
    @WireVariable
    private OrgService orgService;

    @Getter
    @Setter
    private DistributorEntity distributorEntity = DistributorEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private DistributorEntity selectedDistributor = DistributorEntity.builder().build();

    @Getter
    @Setter
    private ProvinceEntity selectedProvince;
    @Getter
    @Setter
    private List<ProvinceEntity> provinceEntities = new ArrayList<>();
    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private  OrgEntity selectOrganization;
    @Getter
    @Setter
    private List<OrgEntity> organizationEntities = new ArrayList<>();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private List<DistributorEntity> importEntities = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("经销商编码").width("100px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("100px").build(),
            TableHeaderInfo.builder().label("省份").width("100px").build(),
            TableHeaderInfo.builder().label("区域").width("100px").build(),
            TableHeaderInfo.builder().label("详细地址").build(),
            TableHeaderInfo.builder().label("等级").width("100px").build(),
            TableHeaderInfo.builder().label("电话").width("100px").build(),
            TableHeaderInfo.builder().label("法人").width("100px").build(),
            TableHeaderInfo.builder().label("是否重点商家").width("100px").build(),
            TableHeaderInfo.builder().label("操作").width("66px").build()
    );


    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(DistributorEntity.class.getSimpleName());
        this.provinceEntities = provinceService.findAll();
        this.organizationEntities = orgService.findAll();

        refreshFirstPage(distributorEntity, Order.DESC, "code");
        getPageList();
    }


    @Override
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        distributorEntity.setCode(distributorEntity.getCode());
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.getPageParam().setOrderName("code");
        this.setPageResult(distributorService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("selectedProvince")
    public void selectProvince(@BindingParam("event") Event event){
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectedProvince != null) {
            this.distributorEntity.setProvince(selectedProvince);
        }
    }
    @Command
    @NotifyChange("selectOrganization")
    public void selectOrganiza(@BindingParam("event") Event event){
        this.selectOrganization = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectOrganization != null) {

        }
    }


    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(distributorEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(distributorEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }
    /*
       重置
        */
    @Command
    @NotifyChange("*")
    public void reset() {
        //provinceEntities
        this.distributorEntity.setProvince(null);
        this.distributorEntity.setName(null);
        this.distributorEntity.setCode(null);
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

    @Command
    @NotifyChange("*")
    public void showForm() {

        this.showForm = true;
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

            importEntities = ExcelUtil.readExcel(path + fileName, DistributorEntity.class);

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
        this.importEntities = distributorService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count,count-errs), "系统提示");

        if(errs<=0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(distributorService.getPageList(this.getPageParam()));
        }
    }

    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedDistributor = DistributorEntity.builder().build();
            } else {
                this.selectedDistributor = distributorService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, DistributorListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, DistributorListVM.this, "selectedDistributor");
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
        this.selectedDistributor = null;
        this.showImportWindow = false;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        distributorService.save(this.selectedDistributor);
        this.selectedDistributor = null;
        this.showForm = false;
        this.getPageList();
    }

    /**
     * 删除对象
     */
    @Command
    @NotifyChange("pageResult")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl  =  distributorService.deleteById(id);
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

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }



    public String genStyle(DistributorEntity entity) {
        if (StringUtils.isBlank(entity.getCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/经销商.xlsx";
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
            Filedownload.save(buffer, null, "经销商.xlsx");
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
        List<DistributorEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (DistributorEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("code");
        keyList.add("name");
        keyList.add("province");
        keyList.add("provinceName");
        keyList.add("address");
        keyList.add("level");
        keyList.add("mobile");
        keyList.add("linkman");
        keyList.add("isFocusMerchants");

        ExcelUtil.listMapToExcel(title, keyList, maps);
    }

}
