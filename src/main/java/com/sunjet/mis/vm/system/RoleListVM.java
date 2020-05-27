package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.*;
import com.sunjet.mis.module.system.service.*;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class RoleListVM extends ListVM<RoleEntity> {

    @WireVariable
    private RoleService roleService;
    @WireVariable
    private OrgService orgService;
    @WireVariable
    private UserService userService;
//    @WireVariable
//    private ResourceService resourceService;
//    @WireVariable
//    private OperationService operationService;
    @WireVariable
    private PermissionService permissionService;

    @Getter
    @Setter
    private RoleEntity roleEntity = RoleEntity.builder().build();
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private RoleEntity selectedRole;
//    @Getter
//    private List<PermissionEntity> permissions;
    @Getter
    private List<OrgEntity> orgs;
    @Getter
    private List<UserEntity> users;

    @Getter
    private List<UserEntity> filterUsers;

    @Getter
    private Map<String,String> userPermissions = new HashMap<>();
    @Getter
    private Map<ResourceEntity,List<PermissionEntity>> allPermissions = new HashMap<>();
//    @Getter
//    private List<ResourceEntity> resources;
//    @Getter
//    private List<OperationEntity> operations;
    @Getter
    private ResourceEntity selectedResource;
    @Getter
    @Setter
    private String user_key;


    @Init
    public void init() {
//        this.setTitle("用户管理");
//        this.setFormUrl("/views/system/user_form.zul");
        this.orgs = orgService.findAll();

        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(RoleEntity.class.getSimpleName());
        refreshFirstPage(roleEntity);
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
        refreshPage(roleEntity);
        getPageList();
    }
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        roleEntity.setCode(this.getKeyword());
        roleEntity.setName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.setPageResult(roleService.getPageList(this.getPageParam()));
    }

    /**
     * 刷新
     */
    @Command
    @NotifyChange({"pageResult","permissionStatus"})
    public void refreshList() {
        refreshPage(roleEntity);
        getPageList();
    }


    @Override
    protected void openDialog(String id) {
        try {
            this.user_key = "";
            this.userPermissions.clear();
            this.users = userService.findAll();
            this.filterUsers = this.users;

            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedRole = RoleEntity.builder().build();
            } else {
                this.selectedRole = roleService.findById(id);

                List<ResourceEntity> resourceEntities = this.selectedRole.getPermissions().stream().distinct().map(p -> p.getResource()).collect(Collectors.toList());

                for (ResourceEntity resourceEntity : resourceEntities) {
                    List<String> list = this.selectedRole.getPermissions().stream().filter(p -> p.getResource().equals(resourceEntity)).map(p->p.getOperation().getName()).collect(Collectors.toList());
                    this.userPermissions.put(resourceEntity.getName(), list.toString());
                }
            }

            List<PermissionEntity> permissions = permissionService.findAll();
            List<ResourceEntity> resourceEntities1 = permissions.stream().map(p -> p.getResource()).distinct().collect(Collectors.toList());
            for (ResourceEntity resourceEntity : resourceEntities1) {
                List<PermissionEntity> permissionEntities1 = permissions.stream().filter(p -> p.getResource().equals(resourceEntity)).collect(Collectors.toList());
                this.allPermissions.put(resourceEntity, permissionEntities1);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "users");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "filterUsers");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "user_key");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "readonly");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "selectedRole");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "userPermissions");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "allPermissions");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "resources");
            BindUtils.postNotifyChange(null, null, RoleListVM.this, "operations");
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
        this.selectedRole = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        roleService.save(this.selectedRole);
        this.selectedRole = null;
        this.showForm = false;

        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void selectResource(@BindingParam("model")ResourceEntity resource){
        this.selectedResource = resource;
    }


    @Command
    @NotifyChange("*")
    public void checkPermissions(){
        this.userPermissions.clear();
        List<ResourceEntity> resourceEntities = this.selectedRole.getPermissions().stream().distinct().map(p -> p.getResource()).collect(Collectors.toList());

        for (ResourceEntity resourceEntity : resourceEntities) {
            List<String> list = this.selectedRole.getPermissions().stream().filter(p -> p.getResource().equals(resourceEntity)).map(p->p.getOperation().getName()).collect(Collectors.toList());
            this.userPermissions.put(resourceEntity.getName(), list.toString());
        }
        System.out.println(this.userPermissions.toString());
    }

    @Command
    @NotifyChange({"filterUsers","user_key"})
    public void searchUsers(@BindingParam("v") String key) {
        this.user_key = key;
        System.out.println(key);
        this.filterUsers = this.users.stream().filter(p->(p.getName().contains(key)||p.getLogId().contains(key))).collect(Collectors.toList());
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = roleService.deleteById(id);
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
