package com.sunjet.mis.vm.distributor;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.DateHelper;
import com.sunjet.mis.module.distributor.entity.DistributionDemandSuperadditionEntity;
import com.sunjet.mis.module.distributor.service.DistributionDemandSuperadditionService;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.view.DistributorDemandStatementView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SUNJET-YFS
 * @Title: DistributionDemandSuperadditionVM
 * @ProjectName mis
 * @Description: 经销商需求追加表
 * @date 2019/3/1515:43
 */
public class DistributionDemandSuperadditionVM extends ListVM<DistributionDemandSuperadditionEntity> {


    @WireVariable
    private DistributionDemandSuperadditionService distributionDemandSuperadditionService;
    @Getter
    @Setter
    private DistributionDemandSuperadditionEntity distributionDemandSuperadditionEntity = DistributionDemandSuperadditionEntity.builder().build();
    @Getter
    @Setter
    private List<DistributionDemandSuperadditionEntity> distributionDemandSuperadditionEntities = new ArrayList<>();

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
    private List<DistributionDemandSuperadditionEntity> importEntities = new ArrayList<>();


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").build(),
            TableHeaderInfo.builder().label("ID").build(),
            TableHeaderInfo.builder().label("年度").build(),
            TableHeaderInfo.builder().label("月份").build(),
            TableHeaderInfo.builder().label("周次").build(),
            TableHeaderInfo.builder().label("经销商代码").build(),
            TableHeaderInfo.builder().label("物料号").build(),
            TableHeaderInfo.builder().label("车型").build(),
            TableHeaderInfo.builder().label("颜色").build(),
            TableHeaderInfo.builder().label("类型").build(),
            TableHeaderInfo.builder().label("物料品种").build(),
            TableHeaderInfo.builder().label("排放标准").build(),
            TableHeaderInfo.builder().label("申报数").build(),
            TableHeaderInfo.builder().label("满足数").build(),
            TableHeaderInfo.builder().label("售达方").build(),
            TableHeaderInfo.builder().label("送达方").build(),
            TableHeaderInfo.builder().label("调整发起方").build(),
            TableHeaderInfo.builder().label("申报原因").build(),
            TableHeaderInfo.builder().label("申报备注").build(),
            TableHeaderInfo.builder().label("平衡原因").build(),
            TableHeaderInfo.builder().label("平衡备注").build(),
            TableHeaderInfo.builder().label("是否瞒足").build(),
            TableHeaderInfo.builder().label("状态").build(),
            TableHeaderInfo.builder().label("平衡人").build(),
            TableHeaderInfo.builder().label("平衡时间").build(),
            TableHeaderInfo.builder().label("拆分后调整").build(),
            TableHeaderInfo.builder().label("申请时间").build()
    );

    @Init
    public void init() {
        //this.setSearchFormHeight(49);
        this.initPermissionStatus(DistributionDemandSuperadditionEntity.class.getSimpleName());

//        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(distributionDemandSuperadditionEntity, Order.DESC, "vsn_");
        distributionDemandSuperadditionEntities = distributionDemandSuperadditionService.findAll();
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

        this.setPageResult(distributionDemandSuperadditionService.getPageList(this.getPageParam()));
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
        refreshPage(distributionDemandSuperadditionEntity);
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
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {

        if (distributionDemandSuperadditionEntity != null) {
            this.distributionDemandSuperadditionEntities.clear();
            this.distributionDemandSuperadditionEntity.setCycle(null);
            this.distributionDemandSuperadditionEntity.setEndDate(null);
            this.distributionDemandSuperadditionEntity.setStartDate(null);
            this.distributionDemandSuperadditionEntity.setVsn(null);
            this.distributionDemandSuperadditionEntity.setDistributorCode(null);
        }

        this.getPageList();
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
     * 查询
     */
  /*  @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        dealerLevelChangeRequestView.setStartDate(dealerView.getStartDate());
        dealerLevelChangeRequestView.setEndDate(dealerView.getEndDate());
        //设置分页参数
        refreshFirstPage(dealerLevelChangeRequestView);
        //刷新分页
        getPageList();
    }*/


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

            importEntities = ExcelUtil.readExcel(path + fileName, DistributionDemandSuperadditionEntity.class);


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
            Class<DistributionDemandSuperadditionEntity> clazz = DistributionDemandSuperadditionEntity.class;
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

        //时间截取
        for (DistributionDemandSuperadditionEntity entity : importEntities) {

            try {
                String orderDateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getApplicationTime());

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(orderDateStart.substring(0, 10));

                entity.setApplicationTime(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int count = this.importEntities.size();
        this.importEntities = distributionDemandSuperadditionService.saveAll(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(distributionDemandSuperadditionService.getPageList(this.getPageParam()));
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
        this.setPageResult(distributionDemandSuperadditionService.getPageList(this.getPageParam()));
    }


    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(DistributionDemandSuperadditionEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getVsn())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/经销商追加需求.xlsx";
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
            Filedownload.save(buffer, null, "经销商追加需求.xlsx");
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
        List<DistributionDemandSuperadditionEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (DistributionDemandSuperadditionEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("tid");
        keyList.add("year");
        keyList.add("month");
        keyList.add("cycle");
        keyList.add("distributorCode");
        keyList.add("vsn");
        keyList.add("vehicleModel");
        keyList.add("color");
        keyList.add("type");
        keyList.add("materialVarieties");
        keyList.add("emissionStandard");
        keyList.add("declareNumber");
        keyList.add("satisfyNumber");
        keyList.add("soldToParty");
        keyList.add("shipToParty");
        keyList.add("adjustment");
        keyList.add("declareReasons");
        keyList.add("declareNote");
        keyList.add("balanceReason");
        keyList.add("balanceNote");
        keyList.add("whetherSatisfy");
        keyList.add("status");
        keyList.add("balancePeople");
        keyList.add("equilibriumTime");
        keyList.add("splitAdjustment");
        keyList.add("applicationTime");

        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }
}
