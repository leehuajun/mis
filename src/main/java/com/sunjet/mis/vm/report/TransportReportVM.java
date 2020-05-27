package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.report.entity.TransportEntity;
import com.sunjet.mis.module.report.service.TransportService;
import com.sunjet.mis.utils.common.ExcelUtil;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: wushi
 * @description: 在途报表
 * @Date: Created in 13:40 2019/3/14
 * @Modify by: wushi
 * @ModifyDate by: 13:40 2019/3/14
 */
public class TransportReportVM extends ListVM<TransportEntity> {


    @WireVariable
    private TransportService transportService;

    @WireVariable
    private DistributorService distributorService;

    //销售订单实体
    @Getter
    @Setter
    private TransportEntity transportEntity =new  TransportEntity();
    @Getter
    @Setter
    private boolean showImportWindow = false;
    @Getter
    @Setter
    private boolean showSearchWindow = false;
    @Getter
    private String uploadFilename;

    @Getter
    private List<TransportEntity> importEntities = new ArrayList<>();

    @Init
    public void init() {
        this.setSearchFormHeight(49);
        this.initPermissionStatus(TransportEntity.class.getSimpleName());

        refreshFirstPage(transportEntity, Order.DESC, "vin");
        getPageList();
    }


    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(transportService.getPageList(this.getPageParam()));
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(transportEntity);
        getPageList();
    }
    /**
     * 重置
     */
    @Command
    @NotifyChange("*")
    public void reset() {
        this.transportEntity.setDistributorName(null);
        this.transportEntity.setVin(null);
        this.getPageList();
    }
    /*查询*/
    @Command
    @NotifyChange("pageResult")
    public void searchData() {
       // this.getPageParam().setPage(0);
        getPageList();
    }
    /**
     * 刷新列表
     */
    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        //refreshFirstPage(transportEntity, Order.DESC, "name");
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
        this.importEntities = transportService.importData(this.importEntities);
        int errs = this.importEntities.size();
        ZkUtils.showInformation(String.format("总记录数：%d, 成功导入记录数：%d", count, count - errs), "系统提示");

        if (errs <= 0) {
            this.importEntities.clear();
            this.uploadFilename = "";
            this.showImportWindow = false;
            this.setPageResult(transportService.getPageList(this.getPageParam()));
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

        this.setPageResult(transportService.getPageList(this.getPageParam()));
    }

    public String genStyle(TransportEntity entity) {
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
            importEntities = ExcelUtil.readExcel(path + fileName, TransportEntity.class);

            List<DistributorEntity> listDistributor = distributorService.findAll();
            for (TransportEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getName().equalsIgnoreCase(entity.getDistributorName())).collect(Collectors.toList());
                if (list.size() == 1) {
//                    entity.setCustomerCode(list.get(0).getSgmwCode());
                    entity.setDistributorCode(list.get(0).getSgmwCode());
                    //entity.setSgmwCode(list.get(0).getSgmwCode());
                }
            }


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


}
