package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.service.ConfigService;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

/**
 * @author zyf
 * @create 2017-7-13 上午10:59
 */
public class ConfigListVM extends ListVM<ConfigEntity> {

    @WireVariable
    private ConfigService configService;

    @Getter
    @Setter
    private ConfigEntity configEntity = ConfigEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private ConfigEntity selectedConfig;


    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ConfigEntity.class.getSimpleName());
        refreshFirstPage(configEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        configEntity.setKey(this.getKeyword());
        configEntity.setValue(this.getKeyword());
        configEntity.setComment(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(configService.getPageList(this.getPageParam()));
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(configEntity);
        getPageList();
    }


    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(configEntity);
//        refreshFirstPage(configEntity);
        getPageList();
    }

    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedConfig = ConfigEntity.builder().build();
            } else {
                this.selectedConfig = configService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, ConfigListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, ConfigListVM.this, "selectedConfig");
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
        this.selectedConfig = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        configService.save(this.selectedConfig);
        this.selectedConfig = null;
        this.showForm = false;


        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = configService.deleteById(id);
                if(bl==true){
                    ZkUtils.showInformation("删除成功","系统提示");
                    //重新获取分页数据
                    getPageList();
                    //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
                    BindUtils.postNotifyChange(null,null,this,"pageResult");
                }else{
                    ZkUtils.showError("删除失败","系统提示");
                }
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });
    }

}
