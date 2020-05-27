package com.sunjet.mis.vm.basic.region;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.entity.RegionEntity;
import com.sunjet.mis.module.basic.service.ProvinceService;
import com.sunjet.mis.module.basic.service.RegionService;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wushi
 * @description: 区域管理
 * @Date: Created in 14:42 2019/1/8
 * @Modify by: wushi
 * @ModifyDate by: 14:42 2019/1/8
 */
public class RegionListVM extends ListVM<RegionEntity> {

    @WireVariable
    private RegionService regionService;
    @WireVariable
    private ProvinceService provinceService;

    @Getter
    @Setter
    private RegionEntity regionEntity = RegionEntity.builder().build();
    //地区列表
    @Getter
    @Setter
    private List<RegionEntity> regionEntityList = new ArrayList<>();
    //省份列表
    @Getter
    @Setter
    private List<ProvinceEntity> provinceEntityList = new ArrayList<>();
    //筛选后的省份列表
    @Getter
    @Setter
    private List<ProvinceEntity> infoProvince = new ArrayList<>();
    //选择的地区
    @Getter
    @Setter
    private RegionEntity selectedRegion;

    @Getter
    @Setter
    private ProvinceEntity selectProvince;

    @Getter
    @Setter
    private String keyword;

    @Getter
    @Setter
    private List<RegionEntity> allRegion = new ArrayList<>();

    @Getter
    @Setter
    private RegionEntity selectedBindRegion;


    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ConfigEntity.class.getSimpleName());
        this.provinceEntityList = provinceService.findAll();
        this.infoProvince.addAll(this.provinceEntityList);
        this.regionEntityList = regionService.findAll();
        RegionEntity all = RegionEntity.builder().build();
        all.setName("--全部--");
        all.setId("0");
        allRegion.add(all);
        this.allRegion.addAll(this.regionEntityList);
        this.selectedRegion = all;


    }


    /**
     * @Author: wushi
     * @description: 搜索省份
     * @Date: Created in 15:48 2019/1/10
     * @Modify by: wushi
     * @ModifyDate by: 15:48 2019/1/10
     */
    @Command
    @NotifyChange("infoProvince")
    public void searchProvince() {
        if (StringUtils.isBlank(this.keyword) && "--全部--".equals(this.selectedRegion.getName())) {
            this.infoProvince = this.provinceEntityList;
        } else if (StringUtils.isBlank(this.keyword)) {
            this.infoProvince = provinceEntityList.stream()
                    .filter(info -> info.getRegion().getName().toUpperCase().equals(this.selectedRegion.getName().toUpperCase()))
                    .collect(Collectors.toList());
        } else if ("--全部--".equals(this.selectedRegion.getName())) {
            this.infoProvince = provinceEntityList.stream()
                    .filter(info -> info.getName().toUpperCase().contains(this.keyword.toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            this.infoProvince = provinceEntityList.stream()
                    .filter(info -> info.getName().toUpperCase().contains(this.keyword.toUpperCase()))
                    .collect(Collectors.toList());
        }

    }


    /**
     * @Author: wushi
     * @description: 搜索省份
     * @Date: Created in 16:11 2019/1/10
     * @Modify by: wushi
     * @ModifyDate by: 16:11 2019/1/10
     */
    @Command
    @NotifyChange("*")
    public void textChangingHandler(@BindingParam("key") String keyword) {
        this.keyword = keyword;
        searchProvince();
    }

    /**
     * @Author: wushi
     * @description:
     * @Date: Created in 16:04 2019/1/10
     * @Modify by: wushi
     * @ModifyDate by: 16:04 2019/1/10
     */
    @Command
    @NotifyChange("*")
    public void selectProvince(@BindingParam("model") ProvinceEntity provinceEntity) {
        this.selectProvince = provinceEntity;
        this.selectedBindRegion = provinceEntity.getRegion();
    }


    /**
     * @Author: wushi
     * @description: 选择地区
     * @Date: Created in 16:29 2019/1/10
     * @Modify by: wushi
     * @ModifyDate by: 16:29 2019/1/10
     */
    @Command
    @NotifyChange("*")
    public void selectRegion(@BindingParam("model") RegionEntity regionEntity) {
        if (this.selectProvince == null) {
            ZkUtils.showInformation("请选择省份", "提示");
            return;
        }
        this.selectedBindRegion = regionEntity;
    }


    /**
     *@Author: wushi
     *@description: 添加省份
     *@Date: Created in 14:13 2019/1/11
     *@Modify by: wushi
     *@ModifyDate by: 14:13 2019/1/11
     *
     */
    @Command
    @NotifyChange("selectProvince")
    public void addProvince(){
        this.selectProvince = ProvinceEntity.builder().build();
    }


    /**
     * @Author: wushi
     * @description: 保存省份信息
     * @Date: Created in 9:22 2019/1/11
     * @Modify by: wushi
     * @ModifyDate by: 9:22 2019/1/11
     */
    @Command
    @NotifyChange({"selectProvince","selectedBindRegion","infoProvince"})
    public void submit() {
        if (this.selectedBindRegion != null) {
            this.selectProvince.setRegion(this.selectedBindRegion);
        }
        this.selectProvince = this.provinceService.save(this.selectProvince);
        this.provinceEntityList = provinceService.findAll();
        searchProvince();
        ZkUtils.showInformation("操作成功！", "系统提示");
    }
    
    /**
     *@Author: wushi
     *@description: 删除省份
     *@Date: Created in 14:24 2019/1/11
     *@Modify by: wushi
     *@ModifyDate by: 14:24 2019/1/11
     *
     */
    @Command
    @NotifyChange("*")
    public void deleteEntity(){
        this.provinceService.deleteById(this.selectProvince.getId());
        this.selectProvince = null;
        this.provinceEntityList = provinceService.findAll();
        searchProvince();
        ZkUtils.showInformation("操作成功！", "系统提示");
    }


}
