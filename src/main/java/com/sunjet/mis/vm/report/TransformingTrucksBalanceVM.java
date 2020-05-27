package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.plan.view.TargetOrderView;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.DistributorDemandStatementEntity;
import com.sunjet.mis.module.warehouse.entity.TransformingTrucksBalanceEntity;
import com.sunjet.mis.module.warehouse.service.TransformingTrucksBalanceService;
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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author SUNJET-YFS
 * @Title: TransformingTrucksBalanceVM
 * @ProjectName mis
 * @Description: 货改车月成品结余
 * @date 2019/4/1816:16
 */
public class TransformingTrucksBalanceVM extends ListVM<TransformingTrucksBalanceEntity> {
    @WireVariable
    private TransformingTrucksBalanceService transformingTrucksBalanceService;

    @Getter
    @Setter
    private TransformingTrucksBalanceEntity transformingTrucksBalanceEntity = new TransformingTrucksBalanceEntity();

    @Getter
    @Setter
    private List<TransformingTrucksBalanceEntity> transformingTrucksBalanceEntities = new ArrayList<>();

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private String uploadFilename;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private List<TransformingTrucksBalanceEntity> importEntities = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("时间").width("60px").build(),
            TableHeaderInfo.builder().label("非ABS-VSN").width("60px").build(),
            TableHeaderInfo.builder().label("ABS-VSN").width("60px").build(),
            TableHeaderInfo.builder().label("月底成品结余").width("100px").build()


    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(TransformingTrucksBalanceEntity.class.getSimpleName());
        refreshFirstPage(transformingTrucksBalanceEntity, Order.ASC, "dateYearMonth");
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {

        this.setPageResult(transformingTrucksBalanceService.getPageList(this.getPageParam()));

    }

    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(transformingTrucksBalanceEntity);
        getPageList();
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

    /*刷新*/
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        //设置分页参数// refreshFirstPage(distributorDemandStatementView);  //有这个会有createdTime
        //刷新分页
        getPageList();
    }

    /*重置*/
    @Command
    @NotifyChange("*")
    public void reset() {
        this.transformingTrucksBalanceEntities.clear();
        this.transformingTrucksBalanceEntity.setAbsvsn(null);
        this.transformingTrucksBalanceEntity.setStartDate(null);
        this.transformingTrucksBalanceEntity.setEndDate(null);
        this.getPageList();
    }





    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
//            solidPinEntity.setSgmwCode(this.getKeyword());
//            solidPinEntity.setDistributorCode(this.getKeyword());
            //    solidPinEntity.setCustomerName(this.getKeyword());
        }
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

    /*查询*/
    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }

    /**
     * 点击"，显示导入窗口
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

            // 返回Object對象 这里有时间日期
            List<List<Object>> lists = ExcelUtil.readExcel(path + fileName);
            for (List list : lists) {
                System.out.println(list.toString());
            }
            //下面时间日期消失
            // 返回CustomerBalanceEntity對象列表

            importEntities = ExcelUtil.readExcel(path + fileName, TransformingTrucksBalanceEntity.class);

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
        int count = this.importEntities.size();
        this.importEntities = transformingTrucksBalanceService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(transformingTrucksBalanceService.getPageList(this.getPageParam()));
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
        this.setPageResult(transformingTrucksBalanceService.getPageList(this.getPageParam()));
    }


    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }


    public String genStyle(TransformingTrucksBalanceEntity entity) {
        if (StringUtils.isBlank(entity.getAbsvsn())||StringUtils.isBlank(entity.getNoabsvsn()) ) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/货改车月成品结余.xls";
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
            Filedownload.save(buffer, null, "货改车月成品结余.xls");
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
        List<TransformingTrucksBalanceEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (TransformingTrucksBalanceEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("dateYearMonth");
        keyList.add("noabsvsn");
        keyList.add("absvsn");
        keyList.add("finishedBalance");



        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }
}
