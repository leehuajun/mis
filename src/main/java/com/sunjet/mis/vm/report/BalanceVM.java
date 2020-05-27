package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.service.BalanceService;
import com.sunjet.mis.module.report.view.DistributorCompleteReportView;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.P;
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

@Slf4j
public class BalanceVM extends ListVM<BalanceEntity> {

    @WireVariable
    private BalanceService balanceService;
    @WireVariable
    private DistributorService distributorService;

    @Getter
    @Setter
    private BalanceEntity balanceEntity = BalanceEntity.builder().build();

    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<BalanceEntity> importEntities = new ArrayList<>();

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("科目编码").width("60px").build(),
            TableHeaderInfo.builder().label("客户编码(工业)").width("60px").build(),
            TableHeaderInfo.builder().label("客户编码(SGMW)").width("60px").build(),
            TableHeaderInfo.builder().label("客户名称").width("100px").build(),
            TableHeaderInfo.builder().label("方向").width("120px").build(),
            TableHeaderInfo.builder().label("期初本币余额").width("70px").build(),
            TableHeaderInfo.builder().label("开票金额").width("70px").build(),
            TableHeaderInfo.builder().label("回款金额").width("70px").build(),
            TableHeaderInfo.builder().label("方向20").width("70px").build(),
            TableHeaderInfo.builder().label("期末余额本币").width("70px").build(),
            TableHeaderInfo.builder().label("在途资金").width("70px").build(),
            TableHeaderInfo.builder().label("信用金额").width("70px").build(),
            TableHeaderInfo.builder().label("客户发车余额(含授信额度)").width("70px").build(),
            TableHeaderInfo.builder().label("客户发车余额(不含授信额度)").width("70px").build(),
            TableHeaderInfo.builder().label("最后操作员").width("70px").build(),
            TableHeaderInfo.builder().label("最后操作时间").width("70px").build()

    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(BalanceEntity.class.getSimpleName());
        if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
            this.balanceEntity.setSgmwCode(this.getActiveUser().getLogId());
            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
        }

        refreshFirstPage(balanceEntity, Order.DESC, "id");
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
        refreshPage(balanceEntity);
        getPageList();
    }
//    @Command
//    @NotifyChange("pageResult")
//    public void simpleSearch(){
//        if(!(this.getPageParam().getUserType()==UserType.DISTRIBUTOR.getIndex())){
//            balanceEntity.setSgmwCode(this.getKeyword());
//            balanceEntity.setDistributorCode(this.getKeyword());
//            balanceEntity.setDistributorName(this.getKeyword());
//        }
//
//        getPageList();
//    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
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

    /**
     * 分页
     */
    public void getPageList() {
        this.setPageResult(balanceService.getPageList(this.getPageParam()));
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
            importEntities = ExcelUtil.readExcel(path + fileName, BalanceEntity.class);

            //用SGMW的客户编号替换五菱工业的客户编号
            List<DistributorEntity> listDistributor = distributorService.findAll();
            for (BalanceEntity entity : importEntities) {
                List<DistributorEntity> lst = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
                if (lst.size() == 1) {
//                    entity.setCustomerCode(lst.get(0).getSgmwCode());
                    entity.setSgmwCode(lst.get(0).getSgmwCode());
                }
            }

//            importEntities.stream().forEach(entity->{
////                entity.setEnabled(true);
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
        this.importEntities = balanceService.saveAll(this.importEntities);
        int errs = this.importEntities.size();

        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count,count-errs), "系统提示");

        if(errs<=0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(balanceService.getPageList(this.getPageParam()));
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

        this.setPageResult(balanceService.getPageList(this.getPageParam()));
    }

    public String genStyle(BalanceEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorCode()) || StringUtils.isBlank(entity.getSgmwCode())) {
            return "background:#ff9600";
        }
        return "";
    }


    @Command
    @NotifyChange("*")
    public void reset() {
        this.balanceEntity.setSgmwCode("");
        this.balanceEntity.setDistributorCode("");
        this.balanceEntity.setDistributorName("");
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/客户发车余额.xls";
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
            Filedownload.save(buffer, null, "客户发车余额.xls");
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
        List<BalanceEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
            this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (BalanceEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("subjectCode");
        keyList.add("distributorCode");
        keyList.add("sgmwCode");
        keyList.add("distributorName");
        keyList.add("direction");
        keyList.add("initialBalance");
        keyList.add("invoiceAmount");
        keyList.add("cashedAmount");
        keyList.add("direction20");
        keyList.add("closingBalance");
        keyList.add("transitFund");
        keyList.add("creditAmount");
        keyList.add("creditBalance");
        keyList.add("balance");
        keyList.add("lastOperator");
        keyList.add("lastOperateTime");

        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }
}
