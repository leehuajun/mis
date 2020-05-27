package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.IconEntity;
import com.sunjet.mis.module.system.service.IconService;
import lombok.Getter;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统图标
 *
 * @author zyf
 * @create 2017-7-13 上午11:01
 */
public class IconListVM extends ListVM<IconEntity> {

    @WireVariable
    private IconService iconService;

    @Getter
    private List<IconEntity> icons = new ArrayList<>();

//    @Getter
//    private List<String> categories = new ArrayList<>();
//
//    @Getter
//    private Map<String,List<IconEntity>> iconMap = new HashMap<>();
//
//    @Getter
//    @Setter
//    private Map<String,Boolean> categoryStatusMap = new HashMap<>();

    @Init
    public void init() {
        icons = iconService.findAll();

//        categories = icons.stream().map(icon -> icon.getCategory()).distinct().collect(Collectors.toList());
//        categories.forEach(c->categoryStatusMap.put(c,false));
//
//        icons.stream().forEach(icon->{
//            if(iconMap.get(icon.getCategory())==null){
//                iconMap.put(icon.getCategory(),new ArrayList<>());
//                iconMap.get(icon.getCategory()).add(icon);
//            }else{
//                iconMap.get(icon.getCategory()).add(icon);
//            }
//        });
    }

//    @Command
//    @NotifyChange("categoryStatusMap")
//    public void updateCategoryStatus(@BindingParam("category") String category){
//        boolean origin = this.categoryStatusMap.get(category);
//        for(String key : this.categoryStatusMap.keySet()){
//            this.categoryStatusMap.put(key,false);
//        }
//        this.categoryStatusMap.put(category,!origin);
//    }
}
