package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.service.OrgService;
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

import java.util.List;

/**
 * 操作列表
 *
 * @author zyf
 * @create 2017-7-13 上午10:50
 */
public class OrgListVM extends ListVM<OrgEntity> {

    @WireVariable
    private OrgService orgService;

    @Getter
    private List<OrgEntity> parentOrgs;

    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private OrgEntity selectedOrg;

    @Getter
    @Setter
    private OrgEntity orgEntity = OrgEntity.builder().build();

    /**
     * 获取列表集合
     */
    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(OrgEntity.class.getSimpleName());
//        this.setTitle("操作列表管理");
//        this.setFormUrl("/views/system/operation_form.zul");
        refreshFirstPage(orgEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshFirstPage(orgEntity);
        this.getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        orgEntity.setCode(this.getKeyword());
        orgEntity.setName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(orgService.getPageList(this.getPageParam()));
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        refreshFirstPage(orgEntity);
        this.getPageList();
    }


    /**
     * 关闭窗口
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(orgEntity);
        this.getPageList();
    }


    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteObject(@BindingParam("id") String id) {
//        boolean bl = orgService.deleteByObjId(objId);
        boolean bl = true;
        if (bl) {
            ZkUtils.showInformation("删除成功", "系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null, null, this, "pageResult");
        } else {
            ZkUtils.showError("删除失败", "系统提示");
        }
    }

//    @Command
//    @NotifyChange("*")
//    public void showForm(@BindingParam("model")OrgEntity org){
//        this.parentOrgs = orgService.findParentOrgs();
//        if(org==null){
//            this.selectedOrg = OrgEntity.builder().build();
//        }else{
//            this.selectedOrg = org;
//        }
//        this.showForm = true;
//    }

    @Override
    protected void openDialog(String id) {
        try {
            this.parentOrgs = orgService.findParentOrgs();
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedOrg = OrgEntity.builder().build();
            } else {
                this.selectedOrg = orgService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null,null,OrgListVM.this,"showForm");
            BindUtils.postNotifyChange(null,null,OrgListVM.this,"readonly");
            BindUtils.postNotifyChange(null,null,OrgListVM.this,"selectedOrg");
            BindUtils.postNotifyChange(null,null,OrgListVM.this,"parentOrgs");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange({"showForm","selectedOrg","parentOrgs"})
    public void abort(@BindingParam("event") Event event){
        this.parentOrgs.clear();
        this.selectedOrg = null;
        this.showForm = false;
        if(event!=null){
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        orgService.save(this.selectedOrg);
        this.selectedOrg = null;
        this.showForm = false;
        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl  =  orgService.deleteById(id);
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
