package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.repository.BalanceRepository;
import com.sunjet.mis.module.report.view.DistributorCompleteReportView;
import com.sunjet.mis.module.report.view.InvoiceInfo;
import com.sunjet.mis.module.report.view.SalesOrderSurplusInfo;
import com.sunjet.mis.module.report.view.TargetInfo;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wushi
 * @description: 经销商完成指标
 * @Date: Created in 9:59 2019/2/22
 * @Modify by: wushi
 * @ModifyDate by: 9:59 2019/2/22
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("distributorCompleteReportService")
public class DistributorCompleteReportService {


    //@Autowired
    //private DistributorCompleteReportRepository distributorCompleteReportRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 经销商
     */
    @Autowired
    private DistributorRepository distributorRepository;
    @Autowired
    private BalanceRepository balanceRepository;


    /**
     * 分页方法
     *
     * @param pageParam
     * @return
     */
    public PageResult<DistributorCompleteReportView> getPageList(PageParam<DistributorCompleteReportView> pageParam) {

        //1.查询条件
        DistributorCompleteReportView infoWhere = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DistributorEntity> specification = null;
        if (infoWhere != null) {
            specification = Specifications.<DistributorEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(infoWhere.getDistributorCode()), "sgmwCode", infoWhere.getDistributorCode())
                    .eq(StringUtils.isNotBlank(infoWhere.getDistributorName()), "name", infoWhere.getDistributorName())
                    .build();
        }

        //3.获取所有经销商
        //List<DistributorEntity> allDistributors = distributorRepository.findAll();
        Page<DistributorEntity> pages = distributorRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        List<DistributorEntity> distributorEntities = pages.getContent();
        //List<DistributorCompleteReportView> distributorCompleteReportViews = new ArrayList<>();
        List<DistributorCompleteReportView> distributorCompleteReportViews = getData(distributorEntities, infoWhere);

        //5.返回
        return PageUtil.getPageResult(distributorCompleteReportViews, pages, pageParam);
    }

    private List<DistributorCompleteReportView> getData(List<DistributorEntity> distributorEntities, DistributorCompleteReportView infoWhere) {
        List<DistributorCompleteReportView> distributorCompleteReportViews = new ArrayList<>();
        //4.设置省份信息
        for (DistributorEntity allDistributor : distributorEntities) {
            DistributorCompleteReportView info = DistributorCompleteReportView.builder().build();
            if(allDistributor.getProvince()!=null){
                info.setRegionName(allDistributor.getProvince().getRegion().getName());
                info.setProvinceName(allDistributor.getProvince().getName());
                info.setCode(allDistributor.getCode());
                info.setDistributorCode(allDistributor.getSgmwCode());
                info.setDistributorName(allDistributor.getName());
                info.setLevel(allDistributor.getLevel());
                distributorCompleteReportViews.add(info);
            }

        }

        //5.获取年度目标统计
        distributorCompleteReportViews = getTarget(distributorCompleteReportViews, infoWhere);
        //6.获取年开票
        distributorCompleteReportViews = getInvoice(distributorCompleteReportViews, infoWhere);
        //7.获取缺口数
        distributorCompleteReportViews = getSurplusNum(distributorCompleteReportViews, infoWhere);

        //9.获取客户发车余额
        List<BalanceEntity> balanceEntities = balanceRepository.findAll();

        //8.计算统计
        distributorCompleteReportViews.forEach(e -> {
                    //计算年目标差距
                    e.setYearTotalDifference(e.getYearTotalTarget() - e.getYearTotalInvoice());
                    //计算年完成率
                    double yearTotalCompletionRate = e.getYearTotalInvoice() / e.getYearTotalTarget();
                    if (!new Double(yearTotalCompletionRate).isNaN() && !new Double(yearTotalCompletionRate).isInfinite()) {
                        e.setYearTotalCompletionRate(yearTotalCompletionRate);
                    }
                    //计算年度客车差距
                    e.setYearTotalBusDifference(e.getYearTotalBusTarget() - e.getYearTotalBusInvoice());
                    //计算年完成率
                    double yearTotalBusCompletionRate = e.getYearTotalBusInvoice() / e.getYearTotalBusTarget();
                    if (!new Double(yearTotalBusCompletionRate).isNaN() && !new Double(yearTotalBusCompletionRate).isInfinite()) {
                        e.setYearTotalBusCompletionRate(yearTotalBusCompletionRate);
                    }

                    //计算年度货车差距
                    e.setYearTotalTruckDifference(e.getYearTotalTruckTarget() - e.getYearTotalTruckInvoice());
                    //计算年完成率
                    double yearTotalTruckCompletionRate = e.getYearTotalTruckInvoice() / e.getYearTotalBusTarget();
                    if (!new Double(yearTotalTruckCompletionRate).isNaN() && !new Double(yearTotalTruckCompletionRate).isInfinite()) {
                        e.setYearTotalTruckCompletionRate(yearTotalTruckCompletionRate);
                    }
                    //计算月开票目标合并
                    e.setMonthTotalTarget(e.getMonthTotalBusTarget() + e.getMonthTotalTruckTarget());
                    //计算月累计开票
                    e.setMonthTotalInvoice(e.getMonthTotalBusInvoice() + e.getMonthTotalTruckInvoice());
                    //计算月目标差距
                    e.setMonthTotalDifference(e.getMonthTotalTarget() - e.getMonthTotalInvoice());
                    //计算月完成率
                    double monthTotalCompletionRate = e.getYearTotalTruckInvoice() / e.getYearTotalBusTarget();
                    if (!new Double(monthTotalCompletionRate).isNaN() && !new Double(monthTotalCompletionRate).isInfinite()) {
                        e.setMonthTotalCompletionRate(monthTotalCompletionRate);
                    }
                    //计算月缺口数
                    e.setMonthSurplus(e.getMonthBusSurplus() + e.getMonthTruckSurplus());
                    //计算客车月目标差距
                    e.setMonthTotalBusDifference(e.getMonthTotalBusTarget() - e.getMonthTotalBusInvoice());
                    //计算客车月完成率
                    double monthTotalBusCompletionRate = e.getMonthTotalBusInvoice() / e.getMonthTotalBusTarget();
                    if (!new Double(monthTotalBusCompletionRate).isNaN()) {
                        e.setMonthTotalBusCompletionRate(monthTotalBusCompletionRate);
                    }
                    //计算货车月目标差距
                    e.setMonthTotalTruckDifference(e.getMonthTotalTruckTarget() - e.getMonthTotalTruckInvoice());

                    //计算货车月完成率
                    double monthTotalTruckCompletionRate = e.getMonthTotalTruckInvoice() / e.getMonthTotalTruckTarget();
                    if (!new Double(monthTotalTruckCompletionRate).isNaN()) {
                        e.setMonthTotalTruckCompletionRate(monthTotalTruckCompletionRate);
                    }
                    BalanceEntity balanceEntity = null;
                    //获取客户余额
                    if (balanceEntities.size() > 0) {
                        balanceEntity = balanceEntities.stream().filter(balance -> e.getDistributorCode().equals(balance.getSgmwCode())).findFirst().orElse(null);
                    }
                    if (balanceEntity != null) {
                        e.setCreditBalance(balanceEntity.getCreditBalance());
                    }
                    //分车条件
                    if (e.getCreditBalance() / e.getMonthBusSurplus() >= 38000) {
                        e.setDistribution("足款");
                    } else {
                        e.setDistribution("缺款");
                    }

                }
        );
        return distributorCompleteReportViews;
    }


    /**
     * 获取缺口数
     *
     * @param distributorCompleteReportViews
     * @param infoWhere
     * @return
     */
    private List<DistributorCompleteReportView> getSurplusNum(List<DistributorCompleteReportView> distributorCompleteReportViews, DistributorCompleteReportView condition) {
        //复制属性
        RowMapper rowMapper = new RowMapper() {
            @Override
            public SalesOrderSurplusInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalesOrderSurplusInfo salesOrderSurplusInfo = SalesOrderSurplusInfo.builder().build();
                salesOrderSurplusInfo.setCustomer_code_(rs.getString("customer_code_"));
                salesOrderSurplusInfo.setDistributor_name_(rs.getString("distributor_name_"));
                salesOrderSurplusInfo.setBus_surplus_num_(rs.getDouble("bus_surplus_num_"));
                salesOrderSurplusInfo.setTruck_surplus_num_(rs.getDouble("truck_surplus_num_"));
                salesOrderSurplusInfo.setCreated_time_(rs.getDate("created_time_"));
                return salesOrderSurplusInfo;
            }
        };


        //获取月需求缺口
        List<SalesOrderSurplusInfo> monthSalesOrderSurplusInfos = jdbcTemplate.query("select * from   sales_order_surplus_ where year(created_time_) = '" + condition.getYear() + "' and month(created_time_) = '" + condition.getMonth() + "' ", rowMapper);
        //月缺口赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            monthSalesOrderSurplusInfos.forEach(e -> {
                if (e.getCustomer_code_().equals(distributorCompleteReportView.getCode())) {
                    distributorCompleteReportView.setMonthBusSurplus(e.getBus_surplus_num_());
                    distributorCompleteReportView.setMonthTruckSurplus(e.getTruck_surplus_num_());
                }
            });
        }
        return distributorCompleteReportViews;
    }


    /**
     * 获取目标统计
     *
     * @param distributorCompleteReportViews
     * @param condition
     * @return
     */
    private List<DistributorCompleteReportView> getTarget(List<DistributorCompleteReportView> distributorCompleteReportViews, DistributorCompleteReportView condition) {
        //复制属性
        RowMapper rowMapper = new RowMapper() {
            @Override
            public TargetInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                TargetInfo targetInfo = TargetInfo.builder().build();
                targetInfo.setDistributor_sgmw_code_(rs.getString("distributor_sgmw_code_"));
                targetInfo.setDistributor_name_(rs.getString("distributor_name_"));
                targetInfo.setTarget_(rs.getDouble("target_"));
                targetInfo.setCreated_time_(rs.getDate("created_time_"));
                return targetInfo;
            }
        };
        //获取年度目标合计
        List<TargetInfo> yearTargetInfos = jdbcTemplate.query("select * from   target_order_year_view_ where year(created_time_) = '" + condition.getYear() + "' ", rowMapper);
        //获取年度目标客改车
        List<TargetInfo> yearTargetBusInfos = jdbcTemplate.query("select * from   target_order_bus_view_ where year(created_time_) = '" + condition.getYear() + "' ", rowMapper);
        //获取年度目标货改车
        List<TargetInfo> yearTargetTruckInfos = jdbcTemplate.query("select * from   target_order_truck_view_ where year(created_time_) = '" + condition.getYear() + "' ", rowMapper);
        //获取月度目标客改
        List<TargetInfo> monthTargetBusInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='客厢车' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);
        //获取月度目标货改
        List<TargetInfo> monthTargetTruckInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='货改' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);
        //年度目标赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            yearTargetInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setYearTotalTarget(e.getTarget_());
                }
            });
        }
        //年度客改车目标赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            yearTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setYearTotalBusTarget(e.getTarget_());
                }
            });
        }
        //年度货改车目标赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            yearTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setYearTotalTruckTarget(e.getTarget_());
                }
            });
        }
        //月度客改车目标赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            monthTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setMonthTotalBusTarget(e.getTarget_());
                }
            });
        }
        //月度货改车目标赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            monthTargetTruckInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setMonthTotalTruckTarget(e.getTarget_());
                }
            });
        }

        return distributorCompleteReportViews;
    }


    /**
     * 获取开票信息
     *
     * @param distributorCompleteReportViews
     * @param condition
     * @return
     */
    private List<DistributorCompleteReportView> getInvoice(List<DistributorCompleteReportView> distributorCompleteReportViews, DistributorCompleteReportView condition) {
        //复制属性
        RowMapper rowMapper = new RowMapper() {
            @Override
            public InvoiceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                InvoiceInfo invoiceInfo = InvoiceInfo.builder().build();
                invoiceInfo.setDistributor_sgmw_code_(rs.getString("distributor_sgmw_code_"));
                invoiceInfo.setDistributor_name_(rs.getString("distributor_name_"));
                invoiceInfo.setBus_invoice_(rs.getDouble("bus_invoice_"));
                invoiceInfo.setTruck_invoice_(rs.getDouble("truck_invoice_"));
                invoiceInfo.setTotal_invoice_(rs.getDouble("total_invoice_"));
                invoiceInfo.setInvoice_date_(rs.getDate("invoice_date_"));
                return invoiceInfo;
            }
        };
        //获取年度目标合计
        List<InvoiceInfo> yearInvoice = jdbcTemplate.query("select * from  invoice_year_view_ where year(invoice_date_) = '" + condition.getYear() + "' ", rowMapper);
        List<InvoiceInfo> monthInvoice = jdbcTemplate.query("select * from  invoice_year_view_ where year(invoice_date_) = '" + condition.getYear() + "' and month(invoice_date_) = '" + condition.getMonth() + "' ", rowMapper);

        //年开票赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            yearInvoice.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setYearTotalInvoice(e.getTotal_invoice_());
                    distributorCompleteReportView.setYearTotalBusInvoice(e.getBus_invoice_());
                    distributorCompleteReportView.setYearTotalTruckInvoice(e.getTruck_invoice_());
                }
            });
        }
        //月开票赋值
        for (DistributorCompleteReportView distributorCompleteReportView : distributorCompleteReportViews) {
            monthInvoice.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setMonthTotalInvoice(e.getBus_invoice_() + e.getTruck_invoice_());
                    distributorCompleteReportView.setMonthTotalBusInvoice(e.getBus_invoice_());
                    distributorCompleteReportView.setMonthTotalTruckInvoice(e.getTruck_invoice_());
                }
            });
        }


        return distributorCompleteReportViews;
    }

    /**
     * @Author: wushi
     * @description: 获取所有
     * @Date: Created in 14:12 2019/4/18
     * @Modify by: wushi
     * @ModifyDate by: 14:12 2019/4/18
     */
    public List<DistributorCompleteReportView> findAll(DistributorCompleteReportView infoWhere) {
        List<DistributorEntity> distributorEntities = distributorRepository.findAll();
        return getData(distributorEntities, infoWhere);
    }
}
