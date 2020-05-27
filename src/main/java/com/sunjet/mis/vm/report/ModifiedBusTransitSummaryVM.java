package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.ModifiedBusTransitSummaryEntity;
import com.sunjet.mis.module.report.service.ModifiedBusTransitSummaryService;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.*;


/**
 * @author SUNJET-YFS
 * @Title: ModifiedBusTransitSummaryVM
 * @ProjectName mis
 * @Description: 改装车在途汇总表
 * @date 2019/1/2914:03
 */
public class ModifiedBusTransitSummaryVM extends ListVM<ModifiedBusTransitSummaryEntity> {

    @WireVariable
    private ModifiedBusTransitSummaryService modifiedBusTransitSummaryService;
    @Getter
    @Setter
    private   ModifiedBusTransitSummaryEntity modifiedBusTransitSummaryEntity = new ModifiedBusTransitSummaryEntity();
    @Getter
    @Setter
    private List<ModifiedBusTransitSummaryEntity> importEntities =new ArrayList<>();
    @Getter
    @Setter
    private List<ModifiedBusTransitSummaryEntity> modifiedBusTransitSummaryEntities = new ArrayList<>();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private boolean showSearchWindow = false;


    @Init
    public void init() {
       // this.setSearchFormHeight(49);
        this.initPermissionStatus(ModifiedBusTransitSummaryEntity.class.getSimpleName());
        modifiedBusTransitSummaryEntities = modifiedBusTransitSummaryService.findAll();
        getPageList();

    }
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

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
    @Command
    public void getPageList() {
        // this.getPageParam().setOrderName("id");
        this.setPageResult(modifiedBusTransitSummaryService.getPageList(this.getPageParam()));

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

           //
//            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
//            for (List list : lists) {
//                System.out.println(list.toString());
//            }
            // 返回CustomerBalanceEntity對象列表

            importEntities = ExcelUtil.readExcel(path + fileName, ModifiedBusTransitSummaryEntity.class);

            //用SGMW的客户编号替换五菱工业的客户编号
         /*   List<DistributorEntity> listDistributor = distributorService.findAll();
            for (SolidPinEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
                if (list.size() == 1) {
                    entity.setDistributorCode(list.get(0).getSgmwCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                }
            }*/

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
        this.importEntities = modifiedBusTransitSummaryService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(modifiedBusTransitSummaryService.getPageList(this.getPageParam()));
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
        this.setPageResult(modifiedBusTransitSummaryService.getPageList(this.getPageParam()));
    }



    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }


    public String genStyle(ModifiedBusTransitSummaryEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorName()) || StringUtils.isBlank(entity.getVin())) {
            return "background:#ff9600";
        }
        return "";
    }





}
