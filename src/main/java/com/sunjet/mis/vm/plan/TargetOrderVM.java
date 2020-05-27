package com.sunjet.mis.vm.plan;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.plan.entity.TargetOrderEntity;
import com.sunjet.mis.module.plan.service.TargetOrderService;
import com.sunjet.mis.module.plan.view.TargetOrderView;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.utils.common.BeanUtils;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.AuxHeaderInfo;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @Author: wushi
 * @description: 目标管理
 * @Date: Created in 11:59 2019/3/5
 * @Modify by: wushi
 * @ModifyDate by: 11:59 2019/3/5
 */
public class TargetOrderVM extends ListVM<TargetOrderView> {

    @WireVariable
    private TargetOrderService targetOrderService;

    //销售订单实体
    @Getter
    @Setter
    private TargetOrderView targetOrderEntity = TargetOrderView.builder().build();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;
    @Getter
    private List<Integer> nians = new ArrayList<>();
    @Getter
    private List<TargetOrderEntity> importEntities = new ArrayList<>();

    @Getter
    List<AuxHeaderInfo> auxHeaderInfos = Arrays.asList(
            AuxHeaderInfo.builder().title("经销商信息").firstCol(0).lastCol(4).isMerge(true).build(),
            AuxHeaderInfo.builder().title("指标").firstCol(5).lastCol(17).isMerge(true).build()

    );

    /**
     * 设置表头
     */
    @Getter
    private List<TableHeaderInfo> tableHeaderList = Arrays.asList(
            //经销商信息
            TableHeaderInfo.builder().label("行号").width("40px").build(),
            TableHeaderInfo.builder().label("区域").width("60px").build(),
            TableHeaderInfo.builder().label("省份").width("60px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("60px").build(),
            TableHeaderInfo.builder().label("经销商代码").width("100px").build(),
            TableHeaderInfo.builder().label("经销商名称").width("120px").build(),
            TableHeaderInfo.builder().label("1月").width("70px").build(),
            TableHeaderInfo.builder().label("2月").width("70px").build(),
            TableHeaderInfo.builder().label("3月").width("70px").build(),
            TableHeaderInfo.builder().label("4月").width("70px").build(),
            TableHeaderInfo.builder().label("5月").width("70px").build(),
            TableHeaderInfo.builder().label("6月").width("70px").build(),
            TableHeaderInfo.builder().label("7月").width("70px").build(),
            TableHeaderInfo.builder().label("8月").width("70px").build(),
            TableHeaderInfo.builder().label("9月").width("70px").build(),
            TableHeaderInfo.builder().label("10月").width("70px").build(),
            TableHeaderInfo.builder().label("11月").width("70px").build(),
            TableHeaderInfo.builder().label("12月").width("70px").build(),
            TableHeaderInfo.builder().label("指标合计").width("70px").build()

    );

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        LocalDate.now().getYear();
        for (int i = 2015; i <= LocalDate.now().getYear(); i++) {
            nians.add(i);
        }
        targetOrderEntity.setNian(LocalDate.now().getYear());
        this.initPermissionStatus(TargetOrderEntity.class.getSimpleName());
        //this.setSort(Order.DESC, "name");
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {

        this.setPageResult(targetOrderService.getPageList(this.getPageParam()));
    }

    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(targetOrderEntity);
        getPageList();
    }

    /**
     * 刷新列表
     */
    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
       // refreshFirstPage(targetOrderEntity, Order.DESC, "name");
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



    /*查询*/
    @Command
    @NotifyChange("pageResult")
    public void searchData() {

        if (targetOrderEntity != null & targetOrderEntity.getDistributorName() != null && targetOrderEntity.getVehicleType() != "") {
            refreshPage(targetOrderEntity);
        }
        if (targetOrderEntity != null & targetOrderEntity.getVehicleType() != null && targetOrderEntity.getVehicleType() != "") {
            refreshPage(targetOrderEntity);
        }

        if (targetOrderEntity != null && targetOrderEntity.getNian() != 0) {

            int a = targetOrderEntity.getNian();
            //数字转化成字符串
            String b = Integer.toString(a);
            //字符串转化成时间格式
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            Date date = null;
            try {
                date = format.parse(b);

                targetOrderEntity.setYear(date);

                if(targetOrderEntity != null &&targetOrderEntity.getYear()!=null){

                    refreshPage(targetOrderEntity);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            getPageList();
        }

    }

    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.targetOrderEntity.setDistributorName(null);
       // this.targetOrderEntity.setDistributorName("");
        //this.targetOrderEntity.setNian(0);
        this.targetOrderEntity.setVehicleType(null);
       // this.targetOrderEntity.setVehicleType("");
        this.getPageList();
    }

    /**
     * 执行导入数据操作
     */
    @Command
    @NotifyChange("*")
    public void importData() {
        int count = this.importEntities.size();
        this.importEntities = targetOrderService.importData(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");
        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(targetOrderService.getPageList(this.getPageParam()));
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

        this.setPageResult(targetOrderService.getPageList(this.getPageParam()));
    }

    public String genStyle(TargetOrderEntity entity) {
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
            importEntities = ExcelUtil.readExcel(path + fileName, TargetOrderEntity.class);

//            List<DistributorEntity> listDistributor = distributorService.findAll();
//            for (targetOrderEntity entity : importEntities) {
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
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "template/目标管理.xlsx";
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
            Filedownload.save(buffer, null, "目标管理.xlsx");
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
        List<TargetOrderView> dataSet = null;
        if ("current".equals(type)) {
            dataSet = this.getPageResult().getRows();
        } else {
            int total = (int) this.getPageResult().getTotal();
            if(total>0)
                this.getPageParam().setPageSize(total);
            getPageList();
            dataSet = this.getPageResult().getRows();
        }

        for (TargetOrderView detailItem : dataSet) {
            Map<String, Object> map = BeanUtils.transBean2Map(detailItem);
            maps.add(map);
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("region");
        keyList.add("provinceName");
        keyList.add("distributorName");
        keyList.add("sgmwCode");
        keyList.add("january");
        keyList.add("february");
        keyList.add("march");
        keyList.add("april");
        keyList.add("may");
        keyList.add("june");
        keyList.add("july");
        keyList.add("august");
        keyList.add("september");
        keyList.add("october");
        keyList.add("november");
        keyList.add("december");
        keyList.add("yearTarget");


        ExcelUtil.listMapToExcel(auxHeaderInfos, title, keyList, maps);


    }
}
