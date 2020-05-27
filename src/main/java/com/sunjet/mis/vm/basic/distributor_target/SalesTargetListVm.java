package com.sunjet.mis.vm.basic.distributor_target;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.entity.SalesTargetEntity;
import com.sunjet.mis.module.basic.service.ProvinceService;
import com.sunjet.mis.module.basic.service.SalesTargetService;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.service.OrgService;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SalesTargetListVm
 * @ProjectName mis
 * @Description: 经销商销售目标
 * @date 2019/1/1414:28
 */
public class SalesTargetListVm extends ListVM<SalesTargetEntity> {
    @WireVariable
    private SalesTargetService salesTargetService;
    @WireVariable
    private ProvinceService provinceService;
    @WireVariable
    private OrgService orgService;
    @Getter
    @Setter
    private ProvinceEntity selectProvince;
    @Getter
    @Setter
    private List<ProvinceEntity> provinceEntities = new ArrayList<>();
    @Getter
    @Setter
    private SalesTargetEntity salesTargetEntity = SalesTargetEntity.builder().build();
    @Getter
    @Setter
    private OrgEntity selectOrganization;
    @Getter
    @Setter
    private Boolean showForm = false;
    @Getter
    @Setter
    private List<OrgEntity> organizationEntities = new ArrayList<>();

    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ConfigEntity.class.getSimpleName());
        this.provinceEntities = provinceService.findAll();
        this.organizationEntities = orgService.findAll();
        refreshFirstPage(salesTargetEntity);
        getPageList();

    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        this.getPageParam().setOrderName("id");
        this.setPageResult(salesTargetService.getPageList(this.getPageParam()));

    }

    @Override
    @Command
    @NotifyChange("pageResult")
    public void simpleSearch() {
        salesTargetEntity.setName(this.getKeyword());
        getPageList();
    }

    @Command
    @NotifyChange("selectProvince")
    public void selectProvince(@BindingParam("event") Event event) {

        this.selectProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectProvince != null) {
            this.salesTargetEntity.setProvince(selectProvince);
        }
    }

    @Command
    @NotifyChange("selectOrganization")
    public void selectOrganiza(@BindingParam("event") Event event) {
        this.selectOrganization = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        if (selectOrganization != null) {
            this.selectOrganization.setName(selectOrganization.getName());
        }

    }


    /**
     * @Description: 头部查询
     * @Author: YFS
     * @CreateDate: 2019/1/15 14:04
     */
    @Command
    @NotifyChange("pageResult")
    public void searchData() {
        salesTargetEntity.setCode(salesTargetEntity.getCode());
        salesTargetEntity.setName(salesTargetEntity.getName());
        refreshList();
        getPageList();
    }


    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(salesTargetEntity);
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(salesTargetEntity);
//      refreshFirstPage(configEntity);
        getPageList();
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        salesTargetService.save(this.salesTargetEntity);
        this.salesTargetEntity = null;
        this.showForm = false;
        this.getPageList();
    }

    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.salesTargetEntity = SalesTargetEntity.builder().build();
            } else {
                this.salesTargetEntity = salesTargetService.findById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null, null, SalesTargetListVm.this, "showForm");
            BindUtils.postNotifyChange(null, null, SalesTargetListVm.this, "salesTargetEntity");
//            BindUtils.postNotifyChange(null,null,ResourceListVM.this,"parentOrgs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
