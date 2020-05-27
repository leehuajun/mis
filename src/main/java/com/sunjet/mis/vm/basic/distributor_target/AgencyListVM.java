package com.sunjet.mis.vm.basic.distributor_target;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.vm.helper.OpenMode;
import org.apache.poi.ss.formula.functions.T;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * @author SUNJET-YFS
 * @Title: AgencyListVM
 * @ProjectName mis
 * @Description:配件合作商指标管理
 * @date 2019/1/119:35
 *
 */
public class AgencyListVM extends ListVM<T> {

    @Init
    public  void init(){
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(ConfigEntity.class.getSimpleName());

    }

    //selectedCity 城市
    //selectOrganiza 组织

    public  void selectedCity(){

    }
/**
* @Description:    查询省份
* @Author:         YFS
* @CreateDate:     2019/1/11 12:00
* @UpdateDate:     2019/1/11 12:00
* @UpdateRemark:   修改内容
* @Modify by:    YFS
* @Version:        1.0
*/
@Command
@NotifyChange({"selectProvince","selectedBindRegion","infoProvince"})
public void submit() {
//    if (this.selectedBindRegion != null) {
//        this.selectProvince.setRegion(this.selectedBindRegion);
//    }
//    this.selectProvince = this.provinceService.save(this.selectProvince);
//    this.provinceEntityList = provinceService.findAll();
//    searchProvince();
}
}
