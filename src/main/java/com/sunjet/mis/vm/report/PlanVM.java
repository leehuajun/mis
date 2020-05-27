package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanService;
import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.AuxHeaderInfo;
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
/*订单计划管理--销售计划*/
public class PlanVM extends ListVM<PlanEntity> {
    @WireVariable
    private PlanService planService;
    @WireVariable
    private DistributorService distributorService;

    @Getter
    @Setter
    private PlanEntity planEntity = PlanEntity.builder().build();

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<PlanEntity> importEntities = new ArrayList<>();

    @Getter
    List<AuxHeaderInfo> auxHeaderInfos = Arrays.asList(
            AuxHeaderInfo.builder().title("申报时间").firstCol(0).lastCol(1).isMerge(true).build(),
            AuxHeaderInfo.builder().title("经销商信息").firstCol(2).lastCol(6).isMerge(true).build(),
            AuxHeaderInfo.builder().title("详细申报信息").firstCol(7).lastCol(17).isMerge(true).build(),
            AuxHeaderInfo.builder().title("周满足计划").firstCol(18).lastCol(24).isMerge(true).build()

    );
    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("年度").width("60px").build(),
            TableHeaderInfo.builder().label("月份").width("60px").build(),
            TableHeaderInfo.builder().label("经销商代码(工业)").width("60px").build(),
            TableHeaderInfo.builder().label("经销商代码(SGMW)").width("100px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("120px").build(),
            TableHeaderInfo.builder().label("省份").width("70px").build(),
            TableHeaderInfo.builder().label("区域").width("70px").build(),
            TableHeaderInfo.builder().label("申报类型").width("70px").build(),
            TableHeaderInfo.builder().label("类型").width("70px").build(),
            TableHeaderInfo.builder().label("物料号").width("70px").build(),
            TableHeaderInfo.builder().label("型号").width("70px").build(),
            TableHeaderInfo.builder().label("车型").width("70px").build(),
            TableHeaderInfo.builder().label("品种代码").width("70px").build(),
            TableHeaderInfo.builder().label("颜色").width("70px").build(),
            TableHeaderInfo.builder().label("排放标准").width("70px").build(),
            TableHeaderInfo.builder().label("经销商申报数量").width("70px").build(),
            TableHeaderInfo.builder().label("全月满足数").width("70px").build(),
            TableHeaderInfo.builder().label("不满足原因").width("70px").build(),
            TableHeaderInfo.builder().label("第一周").width("70px").build(),
            TableHeaderInfo.builder().label("第二周").width("70px").build(),
            TableHeaderInfo.builder().label("第三周").width("70px").build(),
            TableHeaderInfo.builder().label("第四周").width("70px").build(),
            TableHeaderInfo.builder().label("第五周").width("70px").build(),
            TableHeaderInfo.builder().label("第六周").width("70px").build()

    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(PlanEntity.class.getSimpleName());

        for (String permission : this.getActiveUser().getPermissions()) {
            System.out.println(permission);
        }
        if (this.getActiveUser().getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            this.planEntity.setSgmwCode(this.getActiveUser().getLogId());
            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
        }
        refreshFirstPage(planEntity, Order.DESC, "id");
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
        refreshPage(planEntity);
        getPageList();
    }

    @Override
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        if (!(this.getPageParam().getUserType() == UserType.DISTRIBUTOR.getIndex())) {
            planEntity.setSgmwCode(this.getKeyword());
            planEntity.setDistributorCode(this.getKeyword());
            planEntity.setDistributorName(this.getKeyword());
        }
        getPageList();
    }

    /**
     * 刷新列表
     */
    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        this.getPageList();
    }

    /**
     * 分页
     */
    public void getPageList() {

        this.setPageResult(planService.getPageList(this.getPageParam()));
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
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.planEntity.setSgmwCode(null);
        this.planEntity.setDistributorCode(null);
        this.planEntity.setDistributorName(null);
        this.planEntity.setVsn(null);
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
        if (!Objects.equals("csv", extension) && !Objects.equals("xls", extension) && !Objects.equals("xlsx", extension)) {
//            throw new Exception("只支持 XLS、XLSX 文件类型，当前类型为：" + extension.toUpperCase());
            ZkUtils.showError("只支持 CSV、XLS 文件类型！！\n当前类型为：" + extension.toUpperCase(), "文件格式不对!");
            return;
        }

        if (extension.equals("xls") || extension.equals("xlsx")) {
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
            importEntities = ExcelUtil.readExcel(path + fileName, PlanEntity.class);

            List<DistributorEntity> listDistributor = distributorService.findAll();
            for (PlanEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getName().equalsIgnoreCase(entity.getDistributorName())).collect(Collectors.toList());
                if (list.size() == 1) {
//                    entity.setCustomerCode(list.get(0).getSgmwCode());
                    entity.setDistributorCode(list.get(0).getCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                }
            }
//            importEntities.stream().forEach(entity->{
//                entity.setEnabled(true);
//                entity.setSubjectCode(entity.getSubjectCode().replaceAll(".00", ""));
//            });

            // 删除上传来的临时文件
            FileUtils.deleteQuietly(new File(path + fileName));

//            System.out.println(entities.size());
//            for(BalanceEntity entity:entities){
//                System.out.println(entity.toString());
//            }
        } else if (extension.equals("csv")) {
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
    public void importData() {
        int count = this.importEntities.size();
        this.importEntities = planService.saveAll(this.importEntities);
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
            this.setPageResult(planService.getPageList(this.getPageParam()));
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

        this.setPageResult(planService.getPageList(this.getPageParam()));
    }

    public String genStyle(PlanEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode())
                || StringUtils.isBlank(entity.getSgmwCode())
                || StringUtils.isBlank(entity.getYear().toString())
                || StringUtils.isBlank(entity.getMonth().toString())) {
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
        List<PlanEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (PlanEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("year");
        keyList.add("month");
        keyList.add("distributorCode");
        keyList.add("sgmwCode");
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
        ExcelUtil.listMapToExcel(auxHeaderInfos, title, keyList, maps);


    }

}
