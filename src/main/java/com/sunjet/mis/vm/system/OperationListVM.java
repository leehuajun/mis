package com.sunjet.mis.vm.system;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.system.entity.OperationEntity;
import com.sunjet.mis.module.system.service.OperationService;
import com.sunjet.mis.utils.dto.Order;
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

/**
 * 操作列表
 *
 * @author zyf
 * @create 2017-7-13 上午10:50
 */
public class OperationListVM extends ListVM<OperationEntity> {

    @WireVariable
    private OperationService operationService;

//    @Getter
//    @Setter
//    private OperationEntity operationEntity = new OperationEntity();

    @Getter
    @Setter
    private Boolean showForm = false;

    @Getter
    @Setter
    private OperationEntity selectedOperation;

    @Getter
    @Setter
    private OperationEntity operationEntity = OperationEntity.builder().build();

    /**
     * 获取列表集合
     */
    @Init
    public void init() {
        this.setMode(OpenMode.DIALOG);
        this.initPermissionStatus(OperationEntity.class.getSimpleName());

        refreshFirstPage(operationEntity, Order.ASC, "seq");
        getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshFirstPage(operationEntity, Order.ASC, "seq");
        this.getPageList();
    }

    @Command
    @NotifyChange("pageResult")
    public void simpleSearch(){
        operationEntity.setCode(this.getKeyword());
        operationEntity.setName(this.getKeyword());
        getPageList();
    }

    /**
     * 分页
     */
//    @Command
    public void getPageList() {
        this.setPageResult(operationService.getPageList(this.getPageParam()));
    }


//    /**
//     * 刷新
//     */
//    @Command
//    @NotifyChange("pageResult")
//    public void refreshData() {
//        refreshFirstPage(operationEntity, Order.ASC, "seq");
//        this.getPageList();
//    }


    @Override
    protected void openDialog(String id) {
        try {
            // id:null 标识新增
            if (StringUtils.isBlank(id)) {
                this.selectedOperation = OperationEntity.builder().build();
            } else {
                this.selectedOperation = operationService.findOneById(id);
            }
            this.showForm = true;
            BindUtils.postNotifyChange(null,null,OperationListVM.this,"showForm");
            BindUtils.postNotifyChange(null,null,OperationListVM.this,"readonly");
            BindUtils.postNotifyChange(null,null,OperationListVM.this,"selectedOperation");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("*")
    public void submit() {
        operationService.save(this.selectedOperation);
        this.selectedOperation = null;
        this.showForm = false;
        this.getPageList();
    }

    /**
     * 关闭窗口
     */
//    @GlobalCommand(GlobalCommandValues.REFRESH_OPERATION_LIST)
    @Command
    @NotifyChange("pageResult")
    public void refreshList() {
        refreshFirstPage(operationEntity);
        this.getPageList();
    }


    /**
     * 删除对象
     *
     * @param objId
     */
    public void deleteObject(@BindingParam("id") String objId) {
//        boolean bl = operationService.deleteByObjId(objId);
        boolean bl = true;
        if(bl){
            ZkUtils.showInformation("删除成功","系统提示");
            //重新获取分页数据
            getPageList();
            //刷新列表 第三个参数为当前vm，第三个参数为当前vm下的属性
            BindUtils.postNotifyChange(null,null,this,"pageResult");
        }else{
            ZkUtils.showError("删除失败","系统提示");
        }
    }

    /**
     * 放弃导入操作
     */
    @Command
    @NotifyChange("*")
    public void abort(@BindingParam("event") Event event) {
        this.selectedOperation = null;
        this.showForm = false;
        this.getPageList();
        if (event != null) {
            event.stopPropagation();
        }
    }

    @Command
    @NotifyChange("*")
    public void confirmDeleteObject(@BindingParam("id") String id){
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                boolean bl = operationService.deleteById(id);
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
