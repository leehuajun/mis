package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/***
 *  中铁发柳州
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_railway_send_liuzhou")
public class RailwaySendLIUZHOUEntity extends DocEntity {
    // 底盘道位
    @ExcelColumn("经销商代码")
    @Column(name = "dealers_code_",length = 100)
    private String dealersCode;
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    // vsn
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;
    // 订单号
    @ExcelColumn("订单号")
    @Column(name = "order_number_", length = 100)
    private String orderNumber;
    // 订单日期
    @ExcelColumn("订单日期")
    @Column(name = "order_date_", length = 100)
    private String orderDate;
    // 出库日期
    @ExcelColumn("出库日期")
    @Column(name = "outbound_date_", length = 100)
    private String outboundDate;
    //品种
    @ExcelColumn("车型")
    @Column(name = "variety_", length = 20)
    private String variety;
    // 股份生产批号
    @ExcelColumn("生产批次")
    @Column(name = "shares_produce_number_", length = 50)
    private String sharesProduceNumber;
    // 车型
//    @ExcelColumn("车型")
    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    // 状态
    @ExcelColumn("状态")
    @Column(name = "state_",length = 100)
    private String state;

}
