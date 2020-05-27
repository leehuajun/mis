package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.module.system.entity.DictionaryEntity;
import com.sunjet.mis.module.system.entity.DictionaryEntryEntity;
import com.sunjet.mis.module.system.service.DictionaryEntryService;
import com.sunjet.mis.module.system.service.DictionaryService;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典列表
 *
 * @author zyf
 * @create 2017-7-13 上午10:50
 */
public class DictionaryListVM extends ListVM<DictionaryEntity> {

    @WireVariable
    private DictionaryService dictionaryService;
    @WireVariable
    private DictionaryEntryService dictionaryEntryService;
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private Boolean showAddictionary = false;
    @Getter
    @Setter
    private Boolean openModifierModifierDictionaryEntryEntity = false;

    @Getter
    @Setter
    private DictionaryEntity selectedDictionary;
    @Getter
    @Setter
    private DictionaryEntity dictionaryEntity;
    @Setter
    @Getter
    private DictionaryEntryEntity dictionaryEntryEntity;
    @Getter
    @Setter
    private DictionaryEntryEntity selecteDictionaryEntryEntity;

    @Setter
    @Getter
    private List<DictionaryEntryEntity> dictionaryEntryEntitys = new ArrayList<>();

    /**
     * 获取列表集合
     */
    @Init
    public void init() {
//
//        this.getPermissionStatus().setCanCreate(hasPermission("ResourceEntity:create"));
//        this.getPermissionStatus().setCanUpdate(hasPermission("ResourceEntity:modify"));
//        this.getPermissionStatus().setCanDelete(hasPermission("ResourceEntity:delete"));

        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(DictionaryEntity.class.getSimpleName());
//        this.setTitle("操作列表管理");
//        this.setFormUrl("/views/system/operation_form.zul");

        dictionaryEntryEntitys=dictionaryEntryService.findAll();


        refreshFirstPage(dictionaryEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshFirstPage(dictionaryEntity);
        this.getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        dictionaryEntity.setName(this.getKeyword());
        dictionaryEntity.setComment(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(dictionaryService.getPageList(this.getPageParam()));
    }


    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(dictionaryEntity);
        this.getPageList();
    }


    /**
     * 增加字典键值
     */
    @Command
    @NotifyChange("dictionaryEntity")
    public void addDictionaryEntryEntity() {
        dictionaryEntryEntity = new DictionaryEntryEntity();
        this.selectedDictionary.getDictionaryEntries().add(dictionaryEntryEntity);
    }


    @Override
    protected void openDialog(String id) {
        try {
//            this.parentOrgs = orgService.findParentOrgs();
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                //this.dictionaryEntity = DictionaryEntity.builder().build();
                  this.selectedDictionary=DictionaryEntity.builder().build();
            } else {
                this.selectedDictionary = dictionaryService.findById(id);
            }
            this.showForm = true;
            //   this.setTitle("操作列表管理");
            //  this.setFormUrl("/views/system/operation_form.zul");
            BindUtils.postNotifyChange(null, null, DictionaryListVM.this, "showForm");
            BindUtils.postNotifyChange(null, null, DictionaryListVM.this, "readonly");
            BindUtils.postNotifyChange(null, null, DictionaryListVM.this, "selectedDictionary");
//            BindUtils.postNotifyChange(null,null,ResourceListVM.this,"parentOrgs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("showAddictionary")
    public void openAddictionary(String id) {
      if (StringUtils.isBlank(id)){
          this.selecteDictionaryEntryEntity=selecteDictionaryEntryEntity.builder().build();
      }else {
          this.selecteDictionaryEntryEntity = dictionaryEntryService.findById(id);
      }
        this.showAddictionary = true;
      //  BindUtils.postNotifyChange(null,null,DictionaryListVM.this,"selecteDictionaryEntryEntity");

    }

      /*@NotifyChange({"cityInfos", "countyInfos", "agencyAdmitRequest"})*/
    @Command
    @NotifyChange({"*"})
    public void openModifierModifierDictionaryEntryEntity(String id) {


        this.openModifierModifierDictionaryEntryEntity = true;

    }

    @Command
    @NotifyChange("dictionary")
    public void dictionary(String id) {
        if (StringUtils.isBlank(id)){
            this.selecteDictionaryEntryEntity=selecteDictionaryEntryEntity.builder().build();
        }else {
            this.selecteDictionaryEntryEntity = dictionaryEntryService.findById(id);
        }
        this.showAddictionary = true;
    }



    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange("*")
    public void abort(@BindingParam("event") Event event) {
        this.selectedDictionary = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void abort02(@BindingParam("event") Event event) {
        this.selectedDictionary = null;
        this.showAddictionary = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }
    @Command
    @NotifyChange("*")
    public void abort03(@BindingParam("event") Event event) {
        this.selectedDictionary = null;
        this.openModifierModifierDictionaryEntryEntity = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }
    @Command
    @NotifyChange("*")
    public void submit() {
        dictionaryService.save(this.selectedDictionary);
        this.showForm = null;
        this.showForm = false;
        this.getPageList();
    }

    @Command
    @NotifyChange("*")
    public void submit02(){
        this.selecteDictionaryEntryEntity.setDictionary(selectedDictionary);
        this.selecteDictionaryEntryEntity.setKey(selecteDictionaryEntryEntity.getKey());
        this.selecteDictionaryEntryEntity.setValue(selecteDictionaryEntryEntity.getValue());
        dictionaryEntryService.save(this.selecteDictionaryEntryEntity);
        this.selecteDictionaryEntryEntity = null;
        this.showAddictionary = false;
        this.getPageList();
    }
    @Command
    @NotifyChange("*")
    public void submit03(){
        this.selecteDictionaryEntryEntity.setDictionary(selectedDictionary);
        this.selecteDictionaryEntryEntity.setKey(selecteDictionaryEntryEntity.getKey());
        this.selecteDictionaryEntryEntity.setValue(selecteDictionaryEntryEntity.getValue());
        dictionaryEntryService.save(this.selecteDictionaryEntryEntity);
        this.selecteDictionaryEntryEntity = null;
        this.showAddictionary = false;
        this.getPageList();
    }
    /**
     * 删除对象
     *
     * @param id
     */

    @Command
    public void confirmDeleteObject01(@BindingParam("id") String id) {
        boolean bl = dictionaryService.deleteById(id);
        if (bl == true) {
            ZkUtils.showInformation("删除成功", "系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null, null, this, "pageResult");
        } else {
            ZkUtils.showError("删除失败", "系统提示");
        }
    }
    @Command
    public void confirmDeleteObject02(@BindingParam("id") String id) {
        boolean bl = dictionaryEntryService.deleteById(id);

        if (bl == true) {
            ZkUtils.showInformation("删除成功", "系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null, null, this, "pageResult");
        } else {
            ZkUtils.showError("删除失败", "系统提示");
        }
    }
    /**
     * 删除对象
     *
     * @param id
     */

    @Command
    public void confirmDeleteObject(@BindingParam("id") String id) {
        boolean bl = dictionaryEntryService.deleteById(id);
        if (bl == true) {
            ZkUtils.showInformation("删除成功", "系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null, null, this, "pageResult");
        } else {
            ZkUtils.showError("删除失败", "系统提示");
        }
    }




}
