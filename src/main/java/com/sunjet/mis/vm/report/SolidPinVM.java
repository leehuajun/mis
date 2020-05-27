package com.sunjet.mis.vm.report;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.service.SolidPinService;
import com.sunjet.mis.module.report.view.SolidPinReportView;
import com.sunjet.mis.module.warehouse.entity.PassengeCarBalanceEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.w3c.dom.html.HTMLSelectElement;
import org.zkoss.bind.annotation.*;
import org.zkoss.idom.util.IDOMs;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zsoup.select.Evaluator;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.South;
import org.zkoss.zul.Window;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinVM
 * @ProjectName mis
 * @Description: 客户信息卡
 * @date 2019/5/1714:19
 */
public class SolidPinVM  extends ListVM<SolidPinEntity> {
    @WireVariable
    private SolidPinService solidPinService;

    @Getter
    @Setter
    private List<SolidPinEntity> solidPinEntities = new ArrayList<>();
    @Getter
    @Setter
    private SolidPinEntity solidPinEntity = new SolidPinEntity();
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
    private List<SolidPinEntity> importEntities = new ArrayList<>();


    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("车架号").width("60px").build(),
            TableHeaderInfo.builder().label("经销商编号").width("60px").build(),
            TableHeaderInfo.builder().label("经销商简称").width("60px").build(),
            TableHeaderInfo.builder().label("客户编号").width("100px").build(),
            TableHeaderInfo.builder().label("销售员").width("120px").build(),
            TableHeaderInfo.builder().label("客户名称").width("120px").build(),
            TableHeaderInfo.builder().label("使用性质").width("120px").build(),
            TableHeaderInfo.builder().label("集团车").width("70px").build(),
            TableHeaderInfo.builder().label("车牌号").width("70px").build(),
            TableHeaderInfo.builder().label("出生年月日").width("70px").build(),
            TableHeaderInfo.builder().label("证件类型").width("70px").build(),
            TableHeaderInfo.builder().label("证件号").width("70px").build(),
            TableHeaderInfo.builder().label("所在省份").width("70px").build(),
            TableHeaderInfo.builder().label("所属城市").width("70px").build(),
            TableHeaderInfo.builder().label("所属地区").width("70px").build(),
            TableHeaderInfo.builder().label("客户性别").width("70px").build(),
            TableHeaderInfo.builder().label("行业").width("70px").build(),
            TableHeaderInfo.builder().label("职业").width("70px").build(),
            TableHeaderInfo.builder().label("文化程度").width("70px").build(),
            TableHeaderInfo.builder().label("个人收入水平").width("70px").build(),
            TableHeaderInfo.builder().label("家庭月收入水平").width("70px").build(),
            TableHeaderInfo.builder().label("是否愿意参加活动").width("70px").build(),
            TableHeaderInfo.builder().label("车辆使用场所").width("70px").build(),
            TableHeaderInfo.builder().label("车辆用途").width("70px").build(),
            TableHeaderInfo.builder().label("联系人").width("70px").build(),
            TableHeaderInfo.builder().label("座机").width("70px").build(),
            TableHeaderInfo.builder().label("手机").width("70px").build(),
            TableHeaderInfo.builder().label("实际地址").width("70px").build(),
            TableHeaderInfo.builder().label("邮编").width("70px").build(),
            TableHeaderInfo.builder().label("电子邮箱").width("70px").build(),
            TableHeaderInfo.builder().label("QQ").width("70px").build(),
            TableHeaderInfo.builder().label("微信").width("70px").build(),
            TableHeaderInfo.builder().label("身份证地址所在省份").width("70px").build(),
            TableHeaderInfo.builder().label("身份证地址所在城市").width("70px").build(),
            TableHeaderInfo.builder().label("身份证地址所在县区").width("70px").build(),
            TableHeaderInfo.builder().label("身份证地址").width("70px").build(),
            TableHeaderInfo.builder().label("物料号").width("70px").build(),
            TableHeaderInfo.builder().label("车系").width("70px").build(),
            TableHeaderInfo.builder().label("型号").width("70px").build(),
            TableHeaderInfo.builder().label("颜色").width("70px").build(),
            TableHeaderInfo.builder().label("发动机号").width("70px").build(),
            TableHeaderInfo.builder().label("合格证号").width("70px").build(),
            TableHeaderInfo.builder().label("制造日期").width("70px").build(),
            TableHeaderInfo.builder().label("基地代码").width("70px").build(),
            TableHeaderInfo.builder().label("车型代码").width("70px").build(),
            TableHeaderInfo.builder().label("建档时间").width("70px").build(),
            TableHeaderInfo.builder().label("交车日期").width("70px").build(),
            TableHeaderInfo.builder().label("开票日期").width("70px").build(),
            TableHeaderInfo.builder().label("统计日期").width("70px").build(),
            TableHeaderInfo.builder().label("开票价格").width("70px").build(),
            TableHeaderInfo.builder().label("付款方式一次性方式").width("70px").build(),
            TableHeaderInfo.builder().label("付款方式分期首付金额").width("70px").build(),
            TableHeaderInfo.builder().label("付款方式分期贷款金额").width("70px").build(),
            TableHeaderInfo.builder().label("付款方式分期贷款期限").width("70px").build(),
            TableHeaderInfo.builder().label("付款方式分期贷款利率").width("70px").build(),
            TableHeaderInfo.builder().label("购买类型").width("70px").build(),
            TableHeaderInfo.builder().label("增/换购前品牌车型").width("70px").build(),
            TableHeaderInfo.builder().label("渠道").width("70px").build(),
            TableHeaderInfo.builder().label("信息来源-介绍-姓名").width("70px").build(),
            TableHeaderInfo.builder().label("信息来源-介绍-手机").width("70px").build(),
            TableHeaderInfo.builder().label("适合拜访的时间地点").width("70px").build(),
            TableHeaderInfo.builder().label("用户兴趣").width("70px").build(),
            TableHeaderInfo.builder().label("家庭状况").width("70px").build(),
            TableHeaderInfo.builder().label("新车保险-交强险").width("70px").build(),
            TableHeaderInfo.builder().label("新车保险-商业险").width("70px").build(),
            TableHeaderInfo.builder().label("新车保险-承保公司").width("70px").build(),
            TableHeaderInfo.builder().label("新车保险-金额").width("70px").build(),
            TableHeaderInfo.builder().label("新车保险-其他").width("70px").build(),
            TableHeaderInfo.builder().label("顾客自费加装配件-品名1").width("70px").build(),
            TableHeaderInfo.builder().label("顾客自费加装配件-品名2").width("70px").build(),
            TableHeaderInfo.builder().label("介绍记录-顾客姓名3" ).width("70px").build(),
            TableHeaderInfo.builder().label("重要事项纪要" ).width("70px").build(),
            TableHeaderInfo.builder().label("最后修改时间" ).width("70px").build(),
            TableHeaderInfo.builder().label("创建方式" ).width("70px").build(),
            TableHeaderInfo.builder().label("提交档案方式" ).width("70px").build(),
            TableHeaderInfo.builder().label("经销商申请修改状态" ).width("70px").build(),
            TableHeaderInfo.builder().label("经销商申请修改人" ).width("70px").build(),
            TableHeaderInfo.builder().label("经销商申请修改时间" ).width("70px").build(),
            TableHeaderInfo.builder().label("申请修改内容" ).width("70px").build(),
            TableHeaderInfo.builder().label("状态" ).width("70px").build(),
            TableHeaderInfo.builder().label("经销商审核人" ).width("70px").build(),
            TableHeaderInfo.builder().label("经销商审核时间" ).width("70px").build(),
            TableHeaderInfo.builder().label("SGMW审核结果(0表示不通过，1表示通过)" ).width("70px").build(),
            TableHeaderInfo.builder().label("审核说明" ).width("70px").build(),
            TableHeaderInfo.builder().label("销售公司审核人" ).width("70px").build(),
            TableHeaderInfo.builder().label("销售公司审核时间" ).width("70px").build(),
            TableHeaderInfo.builder().label("客户类型" ).width("70px").build(),
            TableHeaderInfo.builder().label("厂发线索渠道" ).width("70px").build(),
            TableHeaderInfo.builder().label("厂发线索编号" ).width("70px").build(),
            TableHeaderInfo.builder().label("原厂发线索编号" ).width("70px").build(),
            TableHeaderInfo.builder().label("自建线索流水号" ).width("70px").build(),
            TableHeaderInfo.builder().label("线索数据来源" ).width("70px").build(),
            TableHeaderInfo.builder().label("用于用户数据中心接口的上传状态(1表示已上传，0表示未上传)" ).width("70px").build(),
            TableHeaderInfo.builder().label("用于用户数据中心接口的上传时间" ).width("70px").build(),
            TableHeaderInfo.builder().label("用于用户数据中心接口的上传提示信息" ).width("70px").build()

    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(SolidPinEntity.class.getSimpleName());
//  if(this.getActiveUser().getUserType()== UserType.DISTRIBUTOR.getIndex()){
//            this.vehicleInvEntity.setSgmwCode(this.getActiveUser().getLogId());
//            this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());
//        }
        refreshFirstPage(solidPinEntity, Order.DESC, "vin");
        // solidPinEntities = solidPinService.findAll();
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

        this.setPageResult(solidPinService.getPageList(this.getPageParam()));
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
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.solidPinEntities.clear();
        this.solidPinEntity.setDistributorCode(null);
        this.solidPinEntity.setVin(null);
        this.solidPinEntity.setDistributorName(null);
        this.solidPinEntity.setDistributorCode(null);
        this.solidPinEntity.setVsn("");
        this.solidPinEntity.setStartDate(null);
        this.solidPinEntity.setEndDate(null);
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

            importEntities = ExcelUtil.readExcel(path + fileName, SolidPinEntity.class);

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

        for (SolidPinEntity entity : importEntities) {
            if (entity.getDistributorCode()!=null){
                String  sb =entity.getDistributorCode();
                entity.setOnecode(sb.substring(0,7));
            }

            Date orderDateStart = null;
            String DateStart =null;
            try {
                orderDateStart = new SimpleDateFormat("yyyyMMdd").parse(entity.getSaleDate());
                 DateStart = new SimpleDateFormat("yyyy-MM-dd").format(orderDateStart);
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(DateStart);
                entity.setInvoiceDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        int count = this.importEntities.size();
        this.importEntities = solidPinService.saveAll(this.importEntities);
       // int errs = this.importEntities.size();
       // ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count ), "系统提示");
       // if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(solidPinService.getPageList(this.getPageParam()));
       // }
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
        this.setPageResult(solidPinService.getPageList(this.getPageParam()));
    }
    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(solidPinEntity);
        getPageList();
    }

    @Command
    @NotifyChange("showSearchWindow")
    public void abortSearch(@BindingParam("event") Event event) {
        this.showSearchWindow = false;
        if (event != null) {
            event.stopPropagation();
        }
    }

    public String genStyle(SolidPinEntity entity) {
        if (StringUtils.isBlank(entity.getDistributorName()) || StringUtils.isBlank(entity.getDistributorCode())) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/35天实销客户信息卡.xls";
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
            Filedownload.save(buffer, null, "35天实销客户信息卡.xls");
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
        List<SolidPinEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (SolidPinEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("vin");
        keyList.add("distributorCode");
        keyList.add("distributorName");
        keyList.add("shopperCode");
        keyList.add("salesman");
        keyList.add("shopperName");
        keyList.add("usageMode");
        keyList.add("groupCar");
        keyList.add("licenseNumber");
        keyList.add("birthdayDate");
        keyList.add("certificateType");
        keyList.add("certificateCode");
        keyList.add("addressProvince");
        keyList.add("addressCity");
        keyList.add("addressRegion");
        keyList.add("yourgender");
        keyList.add("industry");
        keyList.add("vocation");
        keyList.add("culture");
        keyList.add("personalMonthlyIncome");
        keyList.add("familyMonthlyIncome");
        keyList.add("activity");
        keyList.add("placeOfUse");
        keyList.add("vehicularApplications");
        keyList.add("contacts");
        keyList.add("telephone");
        keyList.add("phone");
        keyList.add("actualAddress");
        keyList.add("postcode");
        keyList.add("email");
        keyList.add("qq");
        keyList.add("wechat");
        keyList.add("addressIdentityProvince");
        keyList.add("addressIdentityCity");
        keyList.add("addressIdentityCounty");
        keyList.add("addressIdentity");
        keyList.add("vsn");
        keyList.add("vehicleSeries");
        keyList.add("model");
        keyList.add("color");
        keyList.add("engineCode");
        keyList.add("qualifiedCode");
        keyList.add("manufacturingDate");
        keyList.add("baseCode");
        keyList.add("vehicleModel");
        keyList.add("inputtingTime");
        keyList.add("transactionVehicleDate");
        keyList.add("saleDate");
        keyList.add("statisticalDate");
        keyList.add("price");
        keyList.add("fullPayment");
        keyList.add("downPaymentAmount");
        keyList.add("loanAmount");
        keyList.add("loanPeriod");
        keyList.add("loanRate");
        keyList.add("purchasingTypes");
        keyList.add("addAnTrade");
        keyList.add("operations");
        keyList.add("sponsorsName");
        keyList.add("sponsorsNamePhone");
        keyList.add("visitTimeSite");
        keyList.add("interest");
        keyList.add("familyStatus");
        keyList.add("compulsoryInsurance");
        keyList.add("commercialInsurance");
        keyList.add("insuranceCarriers");
        keyList.add("insuranceAmount");
        keyList.add("insuranceOther");
        keyList.add("oneAdditionalAssembly");
        keyList.add("twoAdditionalAssembly");
        keyList.add("oneCustomer");
        keyList.add("twoCustomer");
        keyList.add("threeCustomer");
        keyList.add("importantNotes");
        keyList.add("finalModificationTime");
        keyList.add("creatingMode");
        keyList.add("fileSubmissionMethod");
        keyList.add("distributorStatus");
        keyList.add("distributorReviser");
        keyList.add("distributorModificationTime");
        keyList.add("modifyContent");
        keyList.add("status");
        keyList.add("distributorAuditor");
        keyList.add("distributorAuditTime");
        keyList.add("auditResult");
        keyList.add("auditSpecification");
        keyList.add("salesCompanyAuditor");
        keyList.add("auditTimeSalesCompany");
        keyList.add("customerType");
        keyList.add("factoryClueChannel");
        keyList.add("factoryClueCode");
        keyList.add("originalFactorySendsClueCode");
        keyList.add("clueRunningCode");
        keyList.add("clueDataSources");
        keyList.add("interfaceStatus");
        keyList.add("uploadTimeOfInterface");
        keyList.add("interfacePromptInformation");



        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }



}
