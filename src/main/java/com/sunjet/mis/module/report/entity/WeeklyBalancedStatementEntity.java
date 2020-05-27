package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author SUNJET-YFS
 * @Title: WeeklyBalancedStatementEntity
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/3/1110:02
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_weekly_balanceds_tatement")
public class WeeklyBalancedStatementEntity extends DocEntity {
    //产地 Production Place
    @Column(name = "production_place_", length = 50)
    private String productionPlace;

    //品种大类 broad heading
    @Column(name = "broadHeading_", length = 50)
    private String broadHeading;

    //常规品种 conventional variety
    @Column(name = "conventional_variety_", length = 50)
    private String  conventionalVariety;

    //完整的VSN VSN
    @Column(name = "vsn_", length = 50)
    private String vsn;

    //车型  model
    @Column(name = "model_", length = 50)
    private String model;

    //月度生产计划 Monthly Production Schedule
    @Column(name = "monthlyProduction_schedule_", length = 50)
    private String monthlyProductionSchedule;

    //本月已分车 Months car
    @Column(name = "months_car_", length = 50)
    private String monthsCar;

    //销售需求缺口 Sales demand gap
    @Column(name = "sales_demand_gap", length = 50)
    private String salesDemandGap;

    //经销商第4周需求预报（含追加）distributor Week Four demand forecast
    @Column(name = "distributor_week_four_demand_forecast_", length = 50)
    private String distributorWeekFourDemandForecast;

    //厂内成品库存  Finished Goods Inventory
    @Column(name = "finished_goods_inventory_", length = 50)
    private String finishedGoodsInventory;

    //底盘采购未完成数 Chassis purchasing undone
    @Column(name = "chassis_purchasing_undone_", length = 50)
    private String chassisPurchasingUndone;

    //底盘实车库存（含在制流转）Chassis vehicle stock
    @Column(name = "chassis_vehicle_stock_", length = 50)
    private String chassisVehicleStock;

    //柳州底盘在途  Liuzhou chassis on the way
    @Column(name = "liuzhou_chassis_way_", length = 50)
    private String liuzhouChassisWay;

    //上周生产入库 Last week's production warehousing
    @Column(name = "lastWeek_production_warehousing_", length = 50)
    private String lastWeekProductionWarehousing;

    //7月累计入库（截止18号） Accumulative storage in July
    @Column(name = "accumulative_storage_july_", length = 50)
    private String accumulativeStorageJuly;

    //下周销售需求建议  Next Week's Sale Demand suggestion
    @Column(name = "nextWeek_sale_demand_suggestion_", length = 50)
    private String nextWeekSaleDemandSuggestion;

    //下周生产建议  Suggestions for next week's production
    @Column(name = "suggestions_next_week_production_", length = 50)
    private String suggestionsNextWeekProduction;

    //下周平衡数量  Next week's Balanced Quantity
    @Column(name = "next_week_balance_quantity_", length = 50)
    private String nextWeekBalancedQuantity;



}
