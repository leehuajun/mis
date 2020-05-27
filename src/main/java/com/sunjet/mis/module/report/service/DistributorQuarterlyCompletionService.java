package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.view.DistributorQuarterlyCompletionView;
import com.sunjet.mis.module.report.view.InvoiceInfo;
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
 * @description: 经销商季度完成奖励
 * @Date: Created in 10:03 2019/4/1
 * @Modify by: wushi
 * @ModifyDate by: 10:03 2019/4/1
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("distributorQuarterlyCompletionService")
public class DistributorQuarterlyCompletionService {

    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PageResult<DistributorQuarterlyCompletionView> getPageList(PageParam<DistributorQuarterlyCompletionView> pageParam) {

        //1.查询条件
        DistributorQuarterlyCompletionView infoWhere = pageParam.getInfoWhere();

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
        Page<DistributorEntity> pages = distributorRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        List<DistributorEntity> content = pages.getContent();
        List<DistributorQuarterlyCompletionView> distributorQuarterlyCompletionViews = new ArrayList<>();
        //4.设置省份信息
        for (DistributorEntity allDistributor : content) {
            DistributorQuarterlyCompletionView info = DistributorQuarterlyCompletionView.builder().build();
            //省份信息不全
            if(allDistributor.getProvince()!=null){
                info.setRegionName(allDistributor.getProvince().getRegion().getName());
                info.setProvinceName(allDistributor.getProvince().getName());
                info.setCode(allDistributor.getCode());
                info.setDistributorCode(allDistributor.getSgmwCode());
                info.setDistributorName(allDistributor.getName());
                info.setLevel(allDistributor.getLevel());
                distributorQuarterlyCompletionViews.add(info);

            }
        }
        //获取指标数据
        distributorQuarterlyCompletionViews = getTaget(distributorQuarterlyCompletionViews, infoWhere);

        //获取实际完成
        distributorQuarterlyCompletionViews = getinvoice(distributorQuarterlyCompletionViews, infoWhere);

        //计算奖励

        distributorQuarterlyCompletionViews.forEach(e -> {
            //货改车指标合计
            e.setTruckTargetTotal(e.getFirstMonthTruckTarget() + e.getSecondMonthTruckTarget() + e.getThirdMonthTruckTarget());
            //完成合计
            e.setTruckCompleteTotal(e.getFirstMonthTruckComplete() + e.getSecondMonthTruckComplete() + e.getThirdMonthTruckComplete());
            //指标差距
            e.setTruckTargetDifference(e.getTruckTargetTotal() - e.getTruckCompleteTotal());
            //完成率
            double truckCompleteRate = e.getTruckCompleteTotal() / e.getTruckTargetTotal() * 100;
            if (!new Double(truckCompleteRate).isNaN() && !new Double(truckCompleteRate).isInfinite()) {
                e.setTruckCompletionRate(truckCompleteRate);
            }
            //计算奖励标准
            if (e.getTruckCompletionRate() >= 100 && e.getTruckCompletionRate() < 110) {
                e.setTruckRewardCriterion(100);
            } else if (e.getTruckCompletionRate() >= 110) {
                e.setTruckRewardCriterion(300);
            }
            //计算小计
            e.setTruckSubtotal(e.getTruckCompleteTotal() * e.getTruckRewardCriterion());


            //货客车指标合计
            e.setBusTargetTotal(e.getFirstMonthBusTarget() + e.getSecondMonthBusTarget() + e.getThirdMonthBusTarget());
            //完成合计
            e.setBusCompleteTotal(e.getFirstMonthBusComplete() + e.getSecondMonthBusComplete() + e.getThirdMonthBusComplete());
            //指标差距
            e.setBusTargetDifference(e.getBusTargetTotal() - e.getBusCompleteTotal());
            //完成率
            double busCompleteRate = e.getBusCompleteTotal() / e.getBusTargetTotal() * 100;
            if (!new Double(busCompleteRate).isNaN() && !new Double(busCompleteRate).isInfinite()) {
                e.setBusCompletionRate(busCompleteRate);
            }
            //计算奖励标准
            if (e.getBusCompletionRate() >= 100 && e.getBusCompletionRate() < 110) {
                e.setBusRewardCriterion(50);
            } else if (e.getBusCompletionRate() >= 110) {
                e.setBusRewardCriterion(100);
            }
            //计算小计
            e.setBusSubtotal(e.getBusCompleteTotal() * e.getBusRewardCriterion());
            //计算奖励合计
            e.setRewardTotal(e.getBusSubtotal() + e.getTruckSubtotal());

        });

        return PageUtil.getPageResult(distributorQuarterlyCompletionViews, pages, pageParam);
    }


    /**
     * 获取指标数据
     *
     * @param distributorQuarterlyCompletionViews
     * @param infoWhere
     * @return
     */
    private List<DistributorQuarterlyCompletionView> getTaget(List<DistributorQuarterlyCompletionView> distributorQuarterlyCompletionViews, DistributorQuarterlyCompletionView condition) {
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


        //获取季度第一个月目标客改
        List<TargetInfo> firstMonthTargetBusInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='客厢车' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterStartMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);
        //获取季度第一个月目标货改
        List<TargetInfo> firstMonthTargetTruckInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='货改' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterStartMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);

        //获取季度第二个月目标客改
        List<TargetInfo> secondMonthTargetBusInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='客厢车' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterStartMonth() + 1 + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);
        //获取季度第二个月目标货改
        List<TargetInfo> secondMonthTargetTruckInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='货改' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterStartMonth() + 1 + "' )GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);

        //获取季度第三个月目标客改
        List<TargetInfo> thirdMonthTargetBusInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='客厢车' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterEndMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);
        //获取季度第三个月目标货改
        List<TargetInfo> thirdMonthTargetTruckInfos = jdbcTemplate.query("SELECT `rto`.`distributor_sgmw_code_`AS`distributor_sgmw_code_`,`rto`.`distributor_name_`AS`distributor_name_`,sum(`rto`.`target_`)AS`target_`,`rto`.`created_time_`AS`created_time_`FROM`rpt_target_order` `rto`WHERE(`rto`.`vehicle_type_`='货改' AND year(created_time_) = '" + condition.getYear() + "' AND month(created_time_) = '" + condition.getQuarterEndMonth() + "' ) GROUP BY`rto`.`distributor_sgmw_code_`", rowMapper);

        //第一个月客改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            firstMonthTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setFirstMonthBusTarget(e.getTarget_());
                }
            });
        }
        //第一个月货改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            firstMonthTargetTruckInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setFirstMonthTruckTarget(e.getTarget_());
                }
            });
        }


        //第二个月客改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            secondMonthTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setSecondMonthBusTarget(e.getTarget_());
                }
            });
        }
        //第二个月货改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            secondMonthTargetTruckInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setSecondMonthTruckTarget(e.getTarget_());
                }
            });
        }


        //第三个月客改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            thirdMonthTargetBusInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setThirdMonthBusTarget(e.getTarget_());
                }
            });
        }
        //第三个月货改目标
        for (DistributorQuarterlyCompletionView distributorQuarterlyCompletionView : distributorQuarterlyCompletionViews) {
            thirdMonthTargetTruckInfos.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorQuarterlyCompletionView.getDistributorCode())) {
                    distributorQuarterlyCompletionView.setThirdMonthTruckTarget(e.getTarget_());
                }
            });
        }


        return distributorQuarterlyCompletionViews;
    }


    /**
     * 获取销售订单
     *
     * @param distributorQuarterlyCompletionViews
     * @param infoWhere
     * @return
     */
    private List<DistributorQuarterlyCompletionView> getinvoice(List<DistributorQuarterlyCompletionView> distributorQuarterlyCompletionViews, DistributorQuarterlyCompletionView condition) {
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
        List<InvoiceInfo> firstMonthInvoice = jdbcTemplate.query("select * from  invoice_year_view_ where year(invoice_date_) = '" + condition.getYear() + "' and month(invoice_date_) = '" + condition.getQuarterStartMonth() + "' ", rowMapper);
        List<InvoiceInfo> secondMonthInvoice = jdbcTemplate.query("select * from  invoice_year_view_ where year(invoice_date_) = '" + condition.getYear() + "' and month(invoice_date_) = '" + condition.getQuarterStartMonth() + 1 + "' ", rowMapper);
        List<InvoiceInfo> thirdMonthInvoice = jdbcTemplate.query("select * from  invoice_year_view_ where year(invoice_date_) = '" + condition.getYear() + "' and month(invoice_date_) = '" + condition.getQuarterEndMonth() + "' ", rowMapper);

        //季度第一个月开票赋值
        for (DistributorQuarterlyCompletionView distributorCompleteReportView : distributorQuarterlyCompletionViews) {
            firstMonthInvoice.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setFirstMonthBusComplete(e.getBus_invoice_());
                    distributorCompleteReportView.setFirstMonthTruckComplete(e.getTruck_invoice_());
                }
            });
        }
        //季度第二个月开票赋值
        for (DistributorQuarterlyCompletionView distributorCompleteReportView : distributorQuarterlyCompletionViews) {
            secondMonthInvoice.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setSecondMonthBusComplete(e.getBus_invoice_());
                    distributorCompleteReportView.setSecondMonthTruckComplete(e.getTruck_invoice_());
                }
            });
        }
        //季度第三个月开票赋值
        for (DistributorQuarterlyCompletionView distributorCompleteReportView : distributorQuarterlyCompletionViews) {
            thirdMonthInvoice.forEach(e -> {
                if (e.getDistributor_sgmw_code_().equals(distributorCompleteReportView.getDistributorCode())) {
                    distributorCompleteReportView.setThirdMonthBusComplete(e.getBus_invoice_());
                    distributorCompleteReportView.setThirdMonthTruckComplete(e.getTruck_invoice_());
                }
            });
        }

        return distributorQuarterlyCompletionViews;
    }


}
