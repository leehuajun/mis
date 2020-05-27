package com.sunjet.mis.vm.report;

import com.sunjet.mis.base.vm.ListVM;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.service.PlanService;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class PlanDetailVM extends ListVM<PlanEntity> {
    @WireVariable
    private PlanService planService;

    @Getter
    @Setter
    private PlanEntity planEntity = PlanEntity.builder().build();

    @Init
    public void init(){

        if(Executions.getCurrent().getArg().get("model")==null){
            ZkUtils.showError("参数不对，无法打开页面！", "系统提示");
            return ;
        }

        PlanExecSummaryView planExecSummaryView = (PlanExecSummaryView)Executions.getCurrent().getArg().get("model");


        this.planEntity.setSgmwCode(planExecSummaryView.getSgmwCode());
        this.planEntity.setYear(planExecSummaryView.getYear());
        this.planEntity.setMonth(planExecSummaryView.getMonth());
        this.planEntity.setVsn(planExecSummaryView.getVsn());
//        this.getPageParam().setUserType(UserType.DISTRIBUTOR.getIndex());

        refreshFirstPage(planEntity, Order.DESC, "id");
        getPageList();
    }
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);

    }
    /**
     * 下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(planEntity);
        getPageList();
    }
    /**
     * 刷新列表
     */
    @Command
    @NotifyChange("pageResult")
    public void refreshList(){
        this.getPageList();
    }
    /**
     * 分页
     */
    public void getPageList() {
        this.setPageResult(planService.getDetailPageList(this.getPageParam()));
    }
}
