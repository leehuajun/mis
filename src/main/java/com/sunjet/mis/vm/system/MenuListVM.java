package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.entity.MenuEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.service.MenuService;
import com.sunjet.mis.module.system.service.PermissionService;
import com.sunjet.mis.utils.zk.MenuTreeUtil;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyf
 * @create 2017-7-13 上午10:53
 */
public class MenuListVM extends ListVM<MenuEntity> {

    @Getter
    @Setter
    private TreeModel treeModel;    // 菜单树形模型
    //@Getter
    //@Setter
    //private Window formDialog;        // 用于显示编辑对话框
    @Getter
    @Setter
    private MenuEntity selectedMenu; // 当前选中的实体对象
    //@Getter
    //@Setter
    //private String formUrl = "";        // 设置编辑对话框的Url

    @WireVariable
    MenuService menuService;
    @WireVariable
    PermissionService permissionService;
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    private List<MenuEntity> parentMenus = new ArrayList<>();

    @Getter
    private List<PermissionEntity> permissions = new ArrayList<>();



    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(MenuEntity.class.getSimpleName());
//        this.setTitle("菜单管理");
//        this.setFormUrl("/views/system/menu_form.zul");
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));

    }


    /**
     * 刷新按钮功能
     */
    @Command
    @NotifyChange("treeModel")
    public void refreshData() {
        //CommonTreeUtil<DictionaryInfo> commonTreeUtil = new CommonTreeUtil<>();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));
    }

    /**
     * 删除实体
     *
     * @param id
     */
    @Command
    @NotifyChange("treeModel")
    public void deleteEntity(@BindingParam("id") String id) {
        menuService.deleteById(id);
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));

    }


    /**
     * 刷新列表
     */
//    @GlobalCommand(GlobalCommandValues.REFRESH_MENU_LIST)
    @Command
    @NotifyChange("treeModel")
    public void refreshList() {
//        this.closeDialog();
        this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menuService.findAll()));
    }


//    /**
//     * 重写打开页签的方法,处理左边菜单id 和右边重复
//     *
//     * @param objId
//     * @param url
//     * @param title
//     */
//    @Command
//    @Override
//    public void openForm(@BindingParam("objId") String objId, @BindingParam("url") String url, @BindingParam("title") String title) {
//        Map<String, Object> paramMap = new HashMap<>();
//        if (objId != null) {
//            paramMap.put("objId", objId);
//            paramMap.put("businessId", objId);
//        }
//        try {
//            objId = "right_tab" + objId;
//            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (TabDuplicateException ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
    protected void openDialog(String id) {
        try {
            this.permissions = permissionService.findAll();
            this.parentMenus = menuService.findParents();
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedMenu = MenuEntity.builder().build();
            } else {
                this.selectedMenu = menuService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null,null,MenuListVM.this,"showForm");
            BindUtils.postNotifyChange(null,null,MenuListVM.this,"readonly");
            BindUtils.postNotifyChange(null,null,MenuListVM.this,"permissions");
            BindUtils.postNotifyChange(null,null,MenuListVM.this,"selectedMenu");
            BindUtils.postNotifyChange(null,null,MenuListVM.this,"parentMenus");
        }catch(Exception e){
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
        this.selectedMenu = null;
        this.showForm = false;
        this.refreshList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        menuService.save(this.selectedMenu);
        this.selectedMenu = null;
        this.showForm = false;

        this.refreshList();
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = menuService.deleteById(id);
                if(bl==true){
                    ZkUtils.showInformation("删除成功","系统提示");
                    //重新获取分页数据
                    refreshList();
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
