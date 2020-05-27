package com.sunjet.mis.vm.basic.vehicle_type;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.basic.entity.VehicleTypeEntity;
import com.sunjet.mis.module.basic.service.VehicleTypeService;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

public class VehicleTypeListVM extends ListVM<VehicleTypeEntity> {

    @WireVariable
    private VehicleTypeService vehicleTypeService;


    @Getter
    @Setter
    private VehicleTypeEntity vehicleTypeEntity = VehicleTypeEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private VehicleTypeEntity selectedVehicleModel;

    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ConfigEntity.class.getSimpleName());
        refreshFirstPage(vehicleTypeEntity);
        getPageList();
    }

    @Override
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        vehicleTypeEntity.setKey(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.getPageParam().setOrderName("id");
        this.setPageResult(vehicleTypeService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(vehicleTypeEntity);
        getPageList();
    }

    @Override
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(vehicleTypeEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }

    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedVehicleModel = VehicleTypeEntity.builder().build();
            } else {
                this.selectedVehicleModel = vehicleTypeService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, VehicleTypeListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, VehicleTypeListVM.this, "selectedVehicleModel");
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
        this.selectedVehicleModel = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        vehicleTypeService.save(this.selectedVehicleModel);
        this.selectedVehicleModel = null;
        this.showForm = false;
        this.getPageList();
    }

    /**
     * 删除对象
     */
    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id) {
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = vehicleTypeService.deleteById(id);
                if (bl == true) {
                    ZkUtils.showInformation("删除成功", "系统提示");
                    //重新获取分页数据
                    this.getPageList();
                    BindUtils.postNotifyChange(null, null, this, "pageResult");
                } else {
                    ZkUtils.showError("删除失败", "系统提示");
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });


    }
}
