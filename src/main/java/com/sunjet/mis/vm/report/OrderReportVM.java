package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
import com.sunjet.mis.module.sales.service.SalesOrderService;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.TableHeaderInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author: wushi
 * @description: 销售订单
 * @Date: Created in 9:21 2019/2/28
 * @Modify by: wushi
 * @ModifyDate by: 9:21 2019/2/28
 */
public class OrderReportVM extends ListVM<SalesOrderEntity> {


    @WireVariable
    private SalesOrderService salesOrderService;

    //销售订单实体
    @Getter
    @Setter
    private SalesOrderEntity salesOrderEntity = SalesOrderEntity.builder().build();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<SalesOrderEntity> importEntities = new ArrayList<>();
    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("省份").width("60px").build(),
            TableHeaderInfo.builder().label("制单人").width("60px").build(),
            TableHeaderInfo.builder().label("单据类型").width("60px").build(),
            TableHeaderInfo.builder().label("系统销售订单号").width("100px").build(),
            TableHeaderInfo.builder().label("特殊需求单号").width("120px").build(),
            TableHeaderInfo.builder().label("客户名称").width("70px").build(),
            TableHeaderInfo.builder().label("车辆型号").width("70px").build(),
            TableHeaderInfo.builder().label("VSN").width("70px").build(),
            TableHeaderInfo.builder().label("车辆颜色").width("70px").build(),
            TableHeaderInfo.builder().label("配置描述").width("70px").build(),
            TableHeaderInfo.builder().label("需求量").width("70px").build(),
            TableHeaderInfo.builder().label("已配车量").width("70px").build(),
            TableHeaderInfo.builder().label("缺口").width("70px").build(),
            TableHeaderInfo.builder().label("制单日期").width("70px").build(),
            TableHeaderInfo.builder().label("合同交货日期").width("70px").build(),
            TableHeaderInfo.builder().label("客户送货地址").width("70px").build(),
            TableHeaderInfo.builder().label("收货人").width("70px").build(),
            TableHeaderInfo.builder().label("联系电话").width("70px").build(),
            TableHeaderInfo.builder().label("销售A价").width("70px").build(),
            TableHeaderInfo.builder().label("增减后价").width("70px").build(),
            TableHeaderInfo.builder().label("合同价格").width("70px").build(),
            TableHeaderInfo.builder().label("销售合同号").width("70px").build(),
            TableHeaderInfo.builder().label("备注").width("70px").build(),
            TableHeaderInfo.builder().label("客户编码").width("70px").build(),
            TableHeaderInfo.builder().label("客户发车余额").width("70px").build(),
            TableHeaderInfo.builder().label("车辆名称").width("70px").build(),
            TableHeaderInfo.builder().label("品种").width("70px").build(),
            TableHeaderInfo.builder().label("缺口需款").width("70px").build(),
            TableHeaderInfo.builder().label("款项情况").width("70px").build()

    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(SalesOrderEntity.class.getSimpleName());
        //this.setSort(Order.DESC, "name");
        refreshFirstPage(salesOrderEntity, Order.ASC, "distributorName");

        getPageList();
    }


    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(salesOrderService.getPageList(this.getPageParam()));
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(salesOrderEntity);
        getPageList();
    }


    /**
     * 刷新列表
     */
    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(salesOrderEntity, Order.DESC, "distributorName");
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
     * 点击"导入"时的响应方法，显示导入窗口
     */
    @Command
    @NotifyChange("showImportWindow")
    public void showImportWindow() {
        this.showImportWindow = true;

    }


    /**
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData() {
        int count = this.importEntities.size();
        this.importEntities = salesOrderService.importData(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");

        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(salesOrderService.getPageList(this.getPageParam()));
        }

    }


    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"showImportWindow", "importEntities", "uploadFilename"})
    public void abort(@BindingParam("event") Event event) {
        this.uploadFilename = "";
        this.showImportWindow = false;
        if (event != null) {
            event.stopPropagation();
        }

        this.setPageResult(salesOrderService.getPageList(this.getPageParam()));
    }

    public String genStyle(SalesOrderEntity entity) {
        //if (StringUtils.isBlank(entity.getDistributorCode())
        //        || StringUtils.isBlank(entity.getSgmwCode())
        //        || StringUtils.isBlank(entity.getYear().toString())
        //        || StringUtils.isBlank(entity.getMonth().toString())) {
        //    return "background:#ff9600";
        //}
        return "";
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
            // 返回CustomerBalanceEntity對象列表
            importEntities = ExcelUtil.readExcel(path + fileName, SalesOrderEntity.class);

//            List<DistributorEntity> listDistributor = distributorService.findAll();
//            for (SalesOrderEntity entity : importEntities) {
//                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getName().equalsIgnoreCase(entity.getDistributorName())).collect(Collectors.toList());
//                if (list.size() == 1) {
////                    entity.setCustomerCode(list.get(0).getSgmwCode());
//                    entity.setDistributorCode(list.get(0).getCode());
//                    entity.setSgmwCode(list.get(0).getSgmwCode());
//                }
//            }
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

        }
    }


    /**
     * 下载模板
     */
    @Command
    @NotifyChange("*")
    public void downloadTemplate() {
        try {
            byte[] buffer = null;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/销售订单.xlsx";
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
            Filedownload.save(buffer, null, "销售订单.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        this.getPageParam().setPage(0);
        getPageList();
    }


    @Command
    @NotifyChange("*")
    public void reset() {
        this.salesOrderEntity.setVsn("");
        this.salesOrderEntity.setDistributorName("");
        this.getPageList();
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
        List<SalesOrderEntity> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (SalesOrderEntity detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("province");
        keyList.add("producer");
        keyList.add("docType");
        keyList.add("orderNo");
        keyList.add("specialRequestNo");
        keyList.add("distributorName");
        keyList.add("vehicleModel");
        keyList.add("vsn");
        keyList.add("vehicleColor");
        keyList.add("configDescription");
        keyList.add("requestNum");
        keyList.add("allotCount");
        keyList.add("surplusNum");
        keyList.add("producerDate");
        keyList.add("contractDeliveryDate");
        keyList.add("customerShippingAddress");
        keyList.add("receiver");
        keyList.add("contactPhone");
        keyList.add("price");
        keyList.add("adjustedPrice");
        keyList.add("contractPrice");
        keyList.add("salesContractNumber");
        keyList.add("remarks");
        keyList.add("customerCode");
        keyList.add("customerDepartureBalance");
        keyList.add("vehicleName");
        keyList.add("variety");
        keyList.add("surplusQuotedPrice");
        keyList.add("priceStatus");


        //titleList.add(title1);
        //titleList.add(title);
        ExcelUtil.listMapToExcel(null, title, keyList, maps);


    }



}
