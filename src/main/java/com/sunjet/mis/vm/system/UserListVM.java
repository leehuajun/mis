package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.module.system.service.OrgService;
import com.sunjet.mis.module.system.service.RoleService;
import com.sunjet.mis.module.system.service.UserService;
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
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class UserListVM extends ListVM<UserEntity> {
    enum Action {
        View,
        Create,
        Update
    }
    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;
    @WireVariable
    private OrgService orgService;

    @Getter
    @Setter
    private UserEntity userEntity = UserEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private UserEntity selectedUser;
    @Getter
    private List<OrgEntity> orgs;
    @Getter
    private List<RoleEntity> roles;
    @Getter
    @Setter
    private String passwordConfirm = "";

    private Action action = Action.View;

    @Init
    public void init() {
        this.orgs = orgService.findAll();
        this.roles = roleService.findAll();

        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(UserEntity.class.getSimpleName());

        refreshFirstPage(userEntity);
        getPageList();
    }


    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(userEntity);
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
        userEntity.setLogId(this.getKeyword());
        userEntity.setName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.setPageResult(userService.getPageList(this.getPageParam()));
    }

    @Override
    protected void openDialog(String id) {
        try {
//            this.parentOrgs = orgService.findParentOrgs();
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.action = Action.Create;
                this.selectedUser = UserEntity.builder().build();
            } else {
                this.action = this.getReadonly() ? Action.View : Action.Update;
                this.selectedUser = userService.findById(id);
                this.passwordConfirm = this.selectedUser.getPassword();
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, UserListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, UserListVM.this, "readonly");
            BindUtils.postNotifyChange(null, null, UserListVM.this, "selectedUser");
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
        this.selectedUser = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        switch (this.action) {
            case Update:
                userService.save(this.selectedUser);
                break;
            case Create:
                //新增用户的时候初始化密码盐及加密密码
                this.selectedUser = CommonHelper.genPassword(this.selectedUser);
                userService.save(this.selectedUser);
                break;
        }
//        if (this.action == Action.Update) {
//
//        }
//        userService.save(this.selectedUser);


        this.selectedUser = null;
        this.showForm = false;


        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = userService.deleteById(id);
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

//    @Command
//    @NotifyChange("pageResult")
//    public Boolean checkOperationIsSelected(@BindingParam("model") OperationEntity operation) {
//        Boolean result = false;
//        try {
//            if (this.selectedResource == null) {
//                return false;
//            }
//            Set<OperationEntity> operations = this.selectedResource.getOperations();
//            for (OperationEntity entity : operations) {
//                if (entity.getId().equalsIgnoreCase(operation.getId())) {
//                    result = true;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            return false;
//        }finally {
//            return result;
//        }
//
//    }

}
