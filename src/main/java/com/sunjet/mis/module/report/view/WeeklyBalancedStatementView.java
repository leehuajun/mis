package com.sunjet.mis.module.report.view;

import com.sunjet.mis.helper.DateHelper;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: WeeklyBalancedStatementEntity
 * @ProjectName mis
 * @Description: 周产销平衡报表
 * @date 2019/3/68:55
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT\n" +
        "	t.id_,\n" +
        "	t.scsj,#月生产时间\n" +
        "	t.inbound_date_,\n" +
        "	t.production_,\n" +
        "	t.product_category_,\n" +
        "	t.product_name_,\n" +
        "	t.vehicle_Type1_,\n" +
        "	t.abs_vsn_,\n" +
        "	t.no_abs_vsn_,\n" +
        "	t.syjys,\n" +
        "	t.zycphs,\n" +
        "	t.scjh,#生产计划\n" +
        "	t.scrkvsn,#生产入库VSN\n" +
        "	IFNULL(t.scrks,0) AS scrus,#生产入库\n" +
        "	IFNULL(t.wpcs,0) AS cncpkc,#产内库存\n" +
        "	t.fcsj,#分车时间\n" +
        "	t.pcvsn,# 配车明细单VSN\n" +
        "IFNULL(t.fcs,0)	AS fcs \n" +
        "	\n" +
        "FROM\n" +
        "	(\n" +
        "	SELECT\n" +
        "		xhg.id_,\n" +
        "		xhg.scsj,#月生产时间\n" +
        "		xhg.inbound_date_,\n" +
        "		xhg.production_,\n" +
        "		xhg.product_category_,\n" +
        "		xhg.product_name_,\n" +
        "		xhg.vehicle_Type1_,\n" +
        "		xhg.abs_vsn_,\n" +
        "		xhg.no_abs_vsn_,\n" +
        "		xhg.syjys,\n" +
        "		xhg.zycphs,\n" +
        "		xhg.scjh,\n" +
        "		xhg.scrkvsn,\n" +
        "		xhg.scrks,\n" +
        "		xhg.wpcs,\n" +
        "		sc.invoice_date_ AS fcsj,#分车时间\n" +
        "		sc.vsn_ AS pcvsn,# 配车明细单VSN\n" +
        "		sc.fcs #\"本月分车数\"\n" +
        "		\n" +
        "	FROM\n" +
        "		(\n" +
        "		SELECT\n" +
        "			hh.id_,\n" +
        "			hh.scsj,#月生产时间\n" +
        "			hh.production_,#生产基地\n" +
        "			hh.product_category_,#品种大类\n" +
        "			hh.product_name_,#品种名称\n" +
        "			hh.vehicle_Type1_,#车型\n" +
        "			hh.abs_vsn_,\n" +
        "			hh.no_abs_vsn_,\n" +
        "			hh.syjys,#上月结余量\n" +
        "			hh.zycphs,#专用车平衡量\n" +
        "			hh.scjh,#生产计划\n" +
        "			scrk.inbound_date_,##生产入库时间\n" +
        "			scrk.scrkvsn,#生产入库svn\n" +
        "			scrk.scrks,#生产入库量\n" +
        "			scrk.wpcs #厂内成品库存\n" +
        "			\n" +
        "		FROM\n" +
        "			(\n" +
        "				SELECT#货改车 ABS-VSN 产品名称，产品类别，产地的匹配表\n" +
        "				btsp.id_,\n" +
        "				HGC.ac AS scsj,#专用车平衡表时间时间\n" +
        "				btsp.production_,#生产基地\n" +
        "				btsp.product_category_,#品种大类\n" +
        "				btsp.product_name_,#品种名称\n" +
        "				HGC.vehicle_Type1_,#车型\n" +
        "				btsp.abs_vsn_,\n" +
        "				rrtm.no_abs_vsn_,\n" +
        "				HGC.ZS AS zycphs,#专用车平衡数\n" +
        "				ifnull( rttb.finished_balance_, 0 ) AS syjys,#上月结余数\n" +
        "				HGC.ZS - ifnull( rttb.finished_balance_, 0 ) AS scjh #月生产数\n" +
        "				\n" +
        "			FROM\n" +
        "				(\n" +
        "				SELECT\n" +
        "					rsvmpb.vsn_,\n" +
        "					rsvmpb.vehicle_Type1_,\n" +
        "					rsvmpb.date_year_month_ AS ac,\n" +
        "					SUM( headquarters_number_ ) AS ZS \n" +
        "				FROM\n" +
        "					rpt_special_vehicle_monthly_plan_balance rsvmpb # 专用车平衡表\n" +
        "					\n" +
        "				WHERE\n" +
        "					rsvmpb.date_year_month_ \n" +
        "				GROUP BY\n" +
        "					rsvmpb.vsn_ \n" +
        "				) HGC\n" +
        "				LEFT JOIN basic_truck_svariety_provenanc btsp ON btsp.abs_vsn_ = HGC.vsn_ #`货改车匹配表\n" +
        "				LEFT JOIN rpt_transforming_trucks_balance rttb ON btsp.abs_vsn_ = rttb.abs_vsn_ #货改车月成品结余\n" +
        "				\n" +
        "				AND DATE_FORMAT( rttb.date_year_month_, '%Y-%m' ) IN ( SELECT DATE_FORMAT( DATE_SUB( rsvmpb.date_year_month_, INTERVAL 1 MONTH ), '%Y-%m' ) FROM rpt_special_vehicle_monthly_plan_balance rsvmpb )\n" +
        "				LEFT JOIN rpt_refitting_truck_material rrtm ON rrtm.vsn_ = btsp.abs_vsn_ #改装车月物料申报表 主要是获取非vsn\n" +
        "				\n" +
        "			WHERE\n" +
        "				HGC.vsn_ = btsp.abs_vsn_ #货改车的vsn等于平衡表货改的vsn\n" +
        "				\n" +
        "			GROUP BY\n" +
        "				btsp.id_,\n" +
        "				btsp.production_,\n" +
        "				btsp.product_category_,\n" +
        "				btsp.product_Name_,\n" +
        "				btsp.abs_vsn_,\n" +
        "				HGC.ac \n" +
        "			) hh,\n" +
        "			(\n" +
        "			SELECT\n" +
        "				rp.inbound_date_,#生产入库时间\n" +
        "				rp.vsn_ AS scrkvsn,#AS \"生产入库vsn\"\n" +
        "				COUNT( rp.vsn_ ) AS scrks,# AS \"入库vsn数\"\n" +
        "				IFNULL( wpc.wpcs, 0 ) AS wpcs #AS \"未配车数\"\n" +
        "				\n" +
        "			FROM\n" +
        "				(\n" +
        "				SELECT\n" +
        "					r.vsn_ AS AA,\n" +
        "					COUNT( r.vsn_ ) AS wpcs,\n" +
        "					r.inventory_status_ \n" +
        "				FROM\n" +
        "					rpt_production_warehouse r \n" +
        "				WHERE\n" +
        "					r.inventory_status_ = \"WPC\" \n" +
        "				GROUP BY\n" +
        "					r.vsn_ \n" +
        "				) wpc\n" +
        "				RIGHT JOIN rpt_production_warehouse rp ON rp.vsn_ = wpc.AA\n" +
        "				RIGHT JOIN basic_truck_svariety_provenanc bts ON bts.abs_vsn_ = rp.vsn_ \n" +
        "			WHERE\n" +
        "				rp.inbound_date_ \n" +
        "			GROUP BY\n" +
        "				rp.vsn_ \n" +
        "			) scrk\n" +
        "			RIGHT JOIN basic_truck_svariety_provenanc btsp ON scrk.scrkvsn = btsp.abs_vsn_ #货改车匹配表\n" +
        "			\n" +
        "		WHERE\n" +
        "			btsp.abs_vsn_ = hh.abs_vsn_ \n" +
        "		GROUP BY\n" +
        "			btsp.id_,\n" +
        "			btsp.production_,\n" +
        "			btsp.product_category_,\n" +
        "			btsp.product_Name_,\n" +
        "			btsp.abs_vsn_ \n" +
        "		) xhg,\n" +
        "		(\n" +
        "		SELECT\n" +
        "			r.vsn_,\n" +
        "			r.invoice_date_,\n" +
        "			SUM( r.allot_count_ ) AS fcs #周产表字段本月分车数\n" +
        "			\n" +
        "		FROM\n" +
        "			rpt_allot_vehicle r #配车单明细表 周产字段本月分车（本月开票）\n" +
        "			\n" +
        "		WHERE\n" +
        "			r.invoice_date_ \n" +
        "		GROUP BY\n" +
        "			r.vsn_ \n" +
        "		) sc\n" +
        "		RIGHT JOIN basic_truck_svariety_provenanc bts ON bts.abs_vsn_ = sc.vsn_ #货改车匹配表\n" +
        "		\n" +
        "	WHERE\n" +
        "		bts.abs_vsn_ = xhg.abs_vsn_ \n" +
        "	GROUP BY\n" +
        "		bts.production_,\n" +
        "		bts.abs_vsn_ \n" +
        "	) t UNION#合并符\n" +
        "SELECT\n" +
        "	t.id_,\n" +
        "	t.scsj,#生产计划时间\n" +
        "	t.inbound_date_,#生产入库时间\n" +
        "	t.production_,\n" +
        "	t.product_category_,\n" +
        "	t.product_name_,\n" +
        "	t.vehicle_Type1_,\n" +
        "	t.abs_vsn_,\n" +
        "	t.no_abs_vsn_,\n" +
        "	t.syjys,#上月结余数\n" +
        "	t.zycphs,#平衡数\n" +
        "	t.cgs,#采购数\n" +
        "	t.scrkvsn,#生产入库VSN\n" +
        "	t.scrus,#生产入库数\n" +
        "	t.wpcs AS cncpkc,#厂内成品库存\n" +
        "	t.fcsj,#分车时间\n" +
        "	t.pcvsn,# 配车明细单VSN\n" +
        "	t.fcs #\"本月分车数\"\n" +
        "	\n" +
        "FROM\n" +
        "	(\n" +
        "	SELECT\n" +
        "		kxc.id_,\n" +
        "		kxc.scsj,#生产计划时间\n" +
        "		kxc.inbound_date_,#生产入库时间\n" +
        "		kxc.production_,\n" +
        "		kxc.product_category_,\n" +
        "		kxc.product_name_,\n" +
        "		kxc.vehicle_Type1_,\n" +
        "		kxc.abs_vsn_,\n" +
        "		kxc.no_abs_vsn_,\n" +
        "		kxc.syjys,#上月结余数\n" +
        "		kxc.zycphs,#平衡数\n" +
        "		kxc.cgs,#采购数\n" +
        "		kxc.scrkvsn,#生产入库VSN\n" +
        "		kxc.scrus,#生产入库数\n" +
        "		kxc.wpcs,#厂内成品库存\n" +
        "		sc.invoice_date_ AS fcsj,#分车时间\n" +
        "		sc.vsn_ AS pcvsn,# 配车明细单VSN\n" +
        "		IFNULL( sc.fcs, 0 ) AS fcs \n" +
        "		\n" +
        "	FROM\n" +
        "		(\n" +
        "		SELECT\n" +
        "			kx.id_,\n" +
        "			kx.scsj,#生产计划时间\n" +
        "			scrk.inbound_date_,#生产入库时间\n" +
        "			kx.production_,\n" +
        "			kx.product_category_,\n" +
        "			kx.product_name_,\n" +
        "			kx.vehicle_Type1_,\n" +
        "			kx.abs_vsn_,\n" +
        "			kx.no_abs_vsn_,\n" +
        "			kx.syjys,#上月结余数\n" +
        "			kx.zycphs,#平衡数\n" +
        "			kx.cgs,#采购数\n" +
        "			scrk.scrkvsn,#生产入库vsn\n" +
        "			IFNULL( scrk.scrks, 0 ) AS scrus,#生产入库数\n" +
        "			IFNULL( scrk.wpcs, 0 ) AS wpcs #厂内成品库存\n" +
        "			\n" +
        "		FROM\n" +
        "			(\n" +
        "			SELECT\n" +
        "				bpcpcs.id_,\n" +
        "				KXC.ac AS scsj,#生产时间\n" +
        "				bpcpcs.production_,\n" +
        "				bpcpcs.product_category_,\n" +
        "				bpcpcs.product_name_,\n" +
        "				KXC.vehicle_Type1_,\n" +
        "				bpcpcs.abs_vsn_,\n" +
        "				rrtm.no_abs_vsn_,\n" +
        "				KXC.ZS AS zycphs,#专用车平衡数就是生产计划数\n" +
        "				ifnull( rcb.finished_balance_, 0 ) AS syjys,#上月结余数\n" +
        "				KXC.ZS - ifnull( rcb.finished_balance_, 0 ) AS cgs #采购数\n" +
        "				\n" +
        "			FROM\n" +
        "				(\n" +
        "				SELECT\n" +
        "					rsvmpb.vsn_,\n" +
        "					rsvmpb.vehicle_Type1_,\n" +
        "					rsvmpb.date_year_month_ AS ac,\n" +
        "					SUM( headquarters_number_ ) AS ZS \n" +
        "				FROM\n" +
        "					rpt_special_vehicle_monthly_plan_balance rsvmpb # 专用车平衡表\n" +
        "					\n" +
        "				WHERE\n" +
        "					rsvmpb.date_year_month_ \n" +
        "				GROUP BY\n" +
        "					rsvmpb.vsn_ \n" +
        "				) KXC\n" +
        "				LEFT JOIN basic_passenger_compartmen_provenanc_car_series bpcpcs ON bpcpcs.abs_vsn_ = KXC.vsn_ #`客厢车匹配表\n" +
        "				LEFT JOIN rpt_passenge_car_balance rcb ON bpcpcs.abs_vsn_ = rcb.abs_vsn_ #客厢车月成品结余\n" +
        "				\n" +
        "				AND DATE_FORMAT( rcb.date_year_momth_, '%Y-%m' ) IN ( SELECT DATE_FORMAT( DATE_SUB( rsvmpb.date_year_month_, INTERVAL 1 MONTH ), '%Y-%m' ) FROM rpt_special_vehicle_monthly_plan_balance rsvmpb )\n" +
        "				LEFT JOIN rpt_refitting_truck_material rrtm ON rrtm.vsn_ = bpcpcs.abs_vsn_ #客厢车车月物料申报表 主要是获取非vsn\n" +
        "				\n" +
        "			WHERE\n" +
        "				KXC.vsn_ = bpcpcs.abs_vsn_ \n" +
        "			GROUP BY\n" +
        "				bpcpcs.production_,\n" +
        "				bpcpcs.product_category_,\n" +
        "				bpcpcs.product_Name_,\n" +
        "				KXC.vsn_ \n" +
        "			) kx,\n" +
        "			(\n" +
        "			SELECT\n" +
        "				bpcpcs.abs_vsn_,\n" +
        "				wpc.AA,\n" +
        "				rp.inbound_date_,#生产入库时间\n" +
        "				rp.vsn_ AS scrkvsn,#AS \"生产入库vsn\"\n" +
        "				COUNT( rp.vsn_ ) AS scrks,# AS \"入库vsn数\"\n" +
        "				IFNULL( wpc.wpcs, 0 ) AS wpcs #AS \"未配车数\"\n" +
        "				\n" +
        "			FROM\n" +
        "				(\n" +
        "				SELECT\n" +
        "					r.vsn_ AS AA,\n" +
        "					COUNT( r.vsn_ ) AS wpcs,\n" +
        "					r.inventory_status_ \n" +
        "				FROM\n" +
        "					rpt_production_warehouse r \n" +
        "				WHERE\n" +
        "					r.inventory_status_ = \"WPC\" \n" +
        "				GROUP BY\n" +
        "					r.vsn_ \n" +
        "				) wpc\n" +
        "				RIGHT JOIN rpt_production_warehouse rp ON rp.vsn_ = wpc.AA\n" +
        "				RIGHT JOIN basic_passenger_compartmen_provenanc_car_series bpcpcs ON bpcpcs.abs_vsn_ = rp.vsn_ \n" +
        "			WHERE\n" +
        "				rp.vsn_ IS NOT NULL \n" +
        "				AND rp.inbound_date_ \n" +
        "			GROUP BY\n" +
        "				bpcpcs.abs_vsn_ \n" +
        "			) scrk\n" +
        "			RIGHT JOIN basic_passenger_compartmen_provenanc_car_series bpcpcs ON scrk.scrkvsn = bpcpcs.abs_vsn_ #客厢车匹配表\n" +
        "			\n" +
        "		WHERE\n" +
        "			bpcpcs.abs_vsn_ = kx.abs_vsn_ \n" +
        "		GROUP BY\n" +
        "			bpcpcs.production_,\n" +
        "			bpcpcs.product_category_,\n" +
        "			bpcpcs.product_Name_,\n" +
        "			bpcpcs.abs_vsn_ \n" +
        "		) kxc,\n" +
        "		(\n" +
        "		SELECT\n" +
        "			r.vsn_,\n" +
        "			r.invoice_date_,\n" +
        "			SUM( r.allot_count_ ) AS fcs #周产表字段本月分车数\n" +
        "			\n" +
        "		FROM\n" +
        "			rpt_allot_vehicle r #配车单明细表 周产字段本月分车（本月开票）\n" +
        "			\n" +
        "		WHERE\n" +
        "			r.invoice_date_ \n" +
        "		GROUP BY\n" +
        "			r.vsn_ \n" +
        "		) sc\n" +
        "		RIGHT JOIN basic_passenger_compartmen_provenanc_car_series bpcpcs ON bpcpcs.abs_vsn_ = sc.vsn_ #货改车匹配表\n" +
        "		\n" +
        "	WHERE\n" +
        "		bpcpcs.abs_vsn_ = kxc.abs_vsn_ \n" +
        "	GROUP BY\n" +
        "		bpcpcs.production_,\n" +
        "	bpcpcs.abs_vsn_ \n" +
        "	) t")
public class WeeklyBalancedStatementView {
    @Id
    @Column(name = "id_")
    private String id;
    //时间
    @Column(name = "scsj")
    private Date scsj;
    //生产入库时间
    @Column(name = "inbound_date_")
    private Date inboundDate;

    //产地 Production Place
    @Column(name = "production_", length = 50)
    private String production;

    //品种大类 broad heading
    @Column(name = "product_category_", length = 50)
    private String productCategory;

    //常规品种 conventional variety
    @Column(name = "product_name_", length = 50)
    private String productName;

    //完整的VSN VSN
    @Column(name = "abs_vsn_", length = 50)
    private String absVsn;

    //非完整的VSN VSN
//    @Column(name = "no_abs_vsn_", length = 50)
//    private String noAbsVsn;

    //车型  model
    @Column(name = "vehicle_Type1_", length = 50)
    private String vehicleType1;

    //专用车平衡数
    @Column(name = "zycphs", length = 50)
    private Integer zycphs;

    //上月结余数
    @Column(name = "syjys", length = 50)
    private Integer syjys;

    //月生产数
    @Column(name = "scjh", length = 50)
    private Integer scjh;

    //生产入库数
    @Column(name = "scrus", length = 50)
    private Integer scrus;

    //产内成品库存
    @Column(name = "cncpkc", length = 50)
    private Integer cncpkc;

    //分车时间
    @Column(name = "fcsj", length = 50)
    private Date fcsj;

    // 配车明细单VSN
    @Column(name = "pcvsn", length = 50)
    private String  pcvsn;

    // 本月分车数
    @Column(name = "fcs", length = 50)
    private Integer  fcs;





    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate = DateHelper.getEndDateTime();
    @Transient
    private Date startDate1 = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate1 = DateHelper.getEndDateTime();
    @Transient
    private Date startDate2;// = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate2 ;//= DateHelper.getEndDateTime();



}


























