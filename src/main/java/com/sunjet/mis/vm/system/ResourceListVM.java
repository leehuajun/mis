package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.OperationEntity;
import com.sunjet.mis.module.system.entity.ResourceEntity;
import com.sunjet.mis.module.system.service.OperationService;
import com.sunjet.mis.module.system.service.ResourceService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class ResourceListVM extends ListVM<ResourceEntity> {
    enum Action {
        View,
        Create,
        Update
    }
    @WireVariable
    private ResourceService resourceService;
    @WireVariable
    private OperationService operationService;


    @Getter
    @Setter
    private ResourceEntity resourceEntity = ResourceEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private ResourceEntity selectedResource;

    @Getter
    @Setter
    private List<OperationEntity> operations = new ArrayList<>();

    //private Action action = Action.View;

    @Init
    public void init() {

        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ResourceEntity.class.getSimpleName());

        refreshFirstPage(resourceEntity);
        getPageList();
        this.operations = operationService.findAll();

    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(resourceEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    @Override
    public void refreshList() {
        this.getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    @Override
    public void simpleSearch(){
        resourceEntity.setCode(this.getKeyword());
        resourceEntity.setName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.setPageResult(resourceService.getPageList(this.getPageParam()));
    }

    @Override
    protected void openDialog(String id) {
        try {
            if (StringUtils.isBlank(id)) {
                this.selectedResource = ResourceEntity.builder().build();
            } else {
                this.selectedResource = resourceService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, ResourceListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, ResourceListVM.this, "readonly");
            BindUtils.postNotifyChange(null, null, ResourceListVM.this, "selectedResource");
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
        this.selectedResource = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        resourceService.save(this.selectedResource);
        this.selectedResource = null;
        this.showForm = false;
        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = resourceService.deleteById(id);
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
