package com.sunjet.mis.module.plan.service;

import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.repository.ProvinceRepository;
import com.sunjet.mis.module.basic.service.DistributorService;
import com.sunjet.mis.module.plan.entity.TargetOrderEntity;
import com.sunjet.mis.module.plan.repository.TargetOrderRepository;
import com.sunjet.mis.module.plan.view.TargetOrderView;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wushi
 * @description: 目标管理
 * @Date: Created in 11:49 2019/3/5
 * @Modify by: wushi
 * @ModifyDate by: 11:49 2019/3/5
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("targetOrderService")
public class TargetOrderService {

    @Autowired
    private TargetOrderRepository targetOrderRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistributorService distributorService;


    /**
     * @Author: wushi
     * @description: 分页
     * @Date: Created in 11:53 2019/3/5
     * @Modify by: wushi
     * @ModifyDate by: 11:53 2019/3/5
     */
    public PageResult<TargetOrderView> getPageList(PageParam<TargetOrderView> pageParam) {

        //1.查询条
        TargetOrderView targetOrderEntity = pageParam.getInfoWhere();


        String combine = "";
        String vehicleType = "";
        String conn = "";
        String year = null;
        String distributorName = "";
        String sql = "SELECT rto.id_, " +
                "YEAR ( rto.created_time_ ) AS year," +
                "rto.province_name_, " +
                "rto.region_, " +
                "rto.distributor_sgmw_code_, " +
                "rto.vehicle_type_, " +
                "rto.distributor_name_, " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '01' THEN rto.target_ ELSE 0 END) AS 'january_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '02' THEN rto.target_ ELSE 0 END ) AS 'february_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '03' THEN rto.target_ ELSE 0 END ) AS 'march_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '04' THEN rto.target_ ELSE 0 END ) AS 'april_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '05' THEN rto.target_ ELSE 0 END ) AS 'may_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '06' THEN rto.target_ ELSE 0 END ) AS 'june_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '07' THEN rto.target_ ELSE 0 END ) AS 'july_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '08' THEN rto.target_ ELSE 0 END ) AS 'august_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '09' THEN rto.target_ ELSE 0 END ) AS 'september_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '10' THEN rto.target_ ELSE 0 END ) AS 'october_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '11' THEN rto.target_ ELSE 0 END ) AS 'november_', " +
                "SUM( CASE DATE_FORMAT(rto.created_time_, '%m') WHEN '12' THEN rto.target_ ELSE 0 END ) AS 'december_' " +
                "FROM `rpt_target_order` AS rto " +
                "WHERE  rto.distributor_sgmw_code_  ";

        if (targetOrderEntity != null) {
            //单查询客户名称
            if (targetOrderEntity.getDistributorName() != null && targetOrderEntity.getDistributorName() != "") {
                distributorName = "AND rto.distributor_name_ =" + "'" + targetOrderEntity.getDistributorName() + "'";
                conn = " GROUP BY rto.distributor_name_";
                combine = sql + distributorName + conn;
            }
            //单查询时间
            if (targetOrderEntity.getYear() != null) {
                String aa = "";
                try {
                    //时间格式转化成字符串
                    aa = new SimpleDateFormat("yyyy-MM-dd").format(targetOrderEntity.getYear());
                    String ss = aa.substring(0, 4);
                    //本质是字符串无需转换
                    year = "AND YEAR( rto.created_time_)=" + "'" + ss + "'";
                    conn = " GROUP BY rto.distributor_name_";
                    combine = sql + year + conn;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //单车辆类型查询
            if (targetOrderEntity.getVehicleType() != null && targetOrderEntity.getVehicleType() != "") {
                vehicleType = "AND rto.vehicle_type_ =" + "'" + targetOrderEntity.getVehicleType() + "'";
                conn = " GROUP BY rto.distributor_name_";
                combine = sql + vehicleType + conn;
            }
           //车辆类型跟客户名称查询
            if (targetOrderEntity.getDistributorName() != null && targetOrderEntity.getDistributorName() != ""
                    && targetOrderEntity != null && targetOrderEntity.getVehicleType() != null && targetOrderEntity.getVehicleType() != "") {
                distributorName = "AND rto.distributor_name_ =" + "'" + targetOrderEntity.getDistributorName() + "'";
                vehicleType = "AND rto.vehicle_type_ =" + "'" + targetOrderEntity.getVehicleType() + "'";
                conn = " GROUP BY rto.distributor_name_";
                combine = sql + vehicleType + distributorName + conn;
            }
            //时间和客户名称查询
            if (targetOrderEntity.getYear() != null&&targetOrderEntity.getDistributorName() != null && targetOrderEntity.getDistributorName() != "") {
                String aa = "";
                try {
                    //时间格式转化成字符串
                    aa = new SimpleDateFormat("yyyy-MM-dd").format(targetOrderEntity.getYear());
                    String ss = aa.substring(0, 4);
                    //本质是字符串无需转换
                    year = "AND YEAR( rto.created_time_)=" + "'" + ss + "'";

                } catch (Exception e) {
                    e.printStackTrace();
                }
                distributorName = "AND rto.distributor_name_ =" + "'" + targetOrderEntity.getDistributorName() + "'";
                conn = " GROUP BY rto.distributor_name_";
                combine = sql + year +distributorName+ conn;
            }

          //时间 客户名称 车辆类型查询

            if(targetOrderEntity.getDistributorName() != null && targetOrderEntity.getDistributorName() != ""&&targetOrderEntity.getYear() != null&&targetOrderEntity.getVehicleType() != null && targetOrderEntity.getVehicleType() != ""){
                String aa = "";
                try {
                    //时间格式转化成字符串
                    aa = new SimpleDateFormat("yyyy-MM-dd").format(targetOrderEntity.getYear());
                    String ss = aa.substring(0, 4);
                    //本质是字符串无需转换
                    year = "AND YEAR( rto.created_time_)=" + "'" + ss + "'";

                } catch (Exception e) {
                    e.printStackTrace();
                }
                distributorName = "AND rto.distributor_name_ =" + "'" + targetOrderEntity.getDistributorName() + "'";
                vehicleType = "AND rto.vehicle_type_ =" + "'" + targetOrderEntity.getVehicleType() + "'";
                conn = " GROUP BY rto.distributor_name_";
                combine = sql + year +distributorName+vehicleType +conn;
            }


        } else {
            //没有带参查询
            conn = "GROUP BY rto.distributor_name_";
            combine = sql + conn;
        }

       // System.out.print("语句输出=" + combine);

        /**
         * 赋值
         */
        List<TargetOrderView> targetOrderViews = jdbcTemplate.query(combine, new RowMapper<TargetOrderView>() {
            @Override
            public TargetOrderView mapRow(ResultSet rs, int rowNum) throws SQLException {
                TargetOrderView targetOrderView = TargetOrderView.builder().build();
                targetOrderView.setId(rs.getString("id_"));
                //targetOrderView.setYear(rs.getDate("year"));
                targetOrderView.setProvinceName(rs.getString("province_name_"));
                targetOrderView.setRegion(rs.getString("region_"));
                targetOrderView.setDistributorName(rs.getString("distributor_name_"));
                targetOrderView.setSgmwCode(rs.getString("distributor_sgmw_code_"));
                targetOrderView.setVehicleType(rs.getString("vehicle_type_"));
                targetOrderView.setJanuary(rs.getDouble("january_"));
                targetOrderView.setFebruary(rs.getDouble("february_"));
                targetOrderView.setMarch(rs.getDouble("march_"));
                targetOrderView.setApril(rs.getDouble("april_"));
                targetOrderView.setMay(rs.getDouble("may_"));
                targetOrderView.setJune(rs.getDouble("june_"));
                targetOrderView.setJuly(rs.getDouble("july_"));
                targetOrderView.setAugust(rs.getDouble("august_"));
                targetOrderView.setSeptember(rs.getDouble("september_"));
                targetOrderView.setOctober(rs.getDouble("october_"));
                targetOrderView.setNovember(rs.getDouble("november_"));
                targetOrderView.setDecember(rs.getDouble("december_"));
                targetOrderView.setYearTarget(
                        targetOrderView.getJanuary() +
                                targetOrderView.getFebruary() +
                                targetOrderView.getMarch() +
                                targetOrderView.getApril() +
                                targetOrderView.getMay() +
                                targetOrderView.getJune() +
                                targetOrderView.getJuly() +
                                targetOrderView.getAugust() +
                                targetOrderView.getSeptember() +
                                targetOrderView.getOctober() +
                                targetOrderView.getNovember() +
                                targetOrderView.getDecember());
                return targetOrderView;
            }
        });


        //5.返回
        return PageUtil.getPageResult(targetOrderViews, Page.empty(), pageParam);
    }

    /**
     * @Author: wushi
     * @description: 保存集合
     * @Date: Created in 11:54 2019/3/5
     * @Modify by: wushi
     * @ModifyDate by: 11:54 2019/3/5
     */
    public List<TargetOrderEntity> saveAll(List<TargetOrderEntity> importEntities) {
        return targetOrderRepository.saveAll(importEntities);
    }

    @Transactional(propagation = Propagation.NEVER)//不执行事务
    public List<TargetOrderEntity> importData(List<TargetOrderEntity> importEntities) {
        List<TargetOrderEntity> err = new ArrayList<>();
        List<DistributorEntity> distributorEntityList = distributorService.findAll();
        //获取省份信息
        List<ProvinceEntity> provinceEntityList = provinceRepository.findAll();
        //targetOrderRepository.saveAll(importEntities);
        importEntities.forEach(info -> {
            try {
                List<DistributorEntity> list = distributorEntityList.stream().filter(e -> e.getSgmwCode().equalsIgnoreCase(info.getSgmwCode())).collect(Collectors.toList());
                if (list.size() == 1) {
                    ProvinceEntity province = list.get(0).getProvince();
                    info.setProvinceName(province.getName());
                    info.setRegion(province.getRegion().getName());
                }
                targetOrderRepository.save(info);
            } catch (Exception e) {
                err.add(info);
            }
        });
        return err;
    }


}
