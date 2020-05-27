package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.VehicleTypeEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.basic.repository.VehicleTypeRepository;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.repository.AllotVehicleRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配车单
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("allotVehicleService")
public class AllotVehicleService {

    @Autowired
    private AllotVehicleRepository allotVehicleRepository;
    @Autowired
    private DistributorRepository distributorRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public PageResult<AllotVehicleEntity> getPageList(PageParam<AllotVehicleEntity> pageParam) {
        //1.查询条件
        AllotVehicleEntity allotVehicleEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AllotVehicleEntity> specification = null;
        Specification<AllotVehicleEntity> specification2 = null;
        Specification<AllotVehicleEntity> specification3 = null;

        //页面查询条件
        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (allotVehicleEntity != null && StringUtils.isNotBlank(allotVehicleEntity.getSgmwCode()) || StringUtils.isNotBlank(allotVehicleEntity.getDistributorCode()) ||StringUtils.isNotBlank(allotVehicleEntity.getDistributorName()) || StringUtils.isNotBlank(allotVehicleEntity.getVin())|| StringUtils.isNotBlank(allotVehicleEntity.getVsn())) {
                specification2 = Specifications.<AllotVehicleEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getSgmwCode()), "sgmwCode", "%" + allotVehicleEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getDistributorCode()), "distributorCode", "%" + allotVehicleEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getDistributorName()), "distributorName", "%" + allotVehicleEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getVin()), "vin", "%" + allotVehicleEntity.getVin() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getVsn()), "vsn", "%" + allotVehicleEntity.getVsn() + "%")
                        .build();
            }
        } else {
            if (allotVehicleEntity != null && StringUtils.isNotBlank(allotVehicleEntity.getSgmwCode()) || StringUtils.isNotBlank(allotVehicleEntity.getDistributorCode()) ||StringUtils.isNotBlank(allotVehicleEntity.getDistributorName()) || StringUtils.isNotBlank(allotVehicleEntity.getVin())|| StringUtils.isNotBlank(allotVehicleEntity.getVsn())) {
                specification2 = Specifications.<AllotVehicleEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getSgmwCode()), "sgmwCode", "%" + allotVehicleEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getDistributorCode()), "distributorCode", "%" + allotVehicleEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getDistributorName()), "distributorName", "%" + allotVehicleEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getVin()), "vin", "%" + allotVehicleEntity.getVin() + "%")
                        .like(StringUtils.isNotEmpty(allotVehicleEntity.getVsn()), "vsn", "%" + allotVehicleEntity.getVsn() + "%")
                        .build();
            }
        }


        //组合查询条件
        specification = Specifications.<AllotVehicleEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<AllotVehicleEntity> pages = allotVehicleRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<AllotVehicleEntity> saveAll(List<AllotVehicleEntity> importEntities) {
        List<AllotVehicleEntity> resultEntities = new ArrayList<>();
        List<AllotVehicleEntity> addEntities = new ArrayList<>();
        try {
            //1. 用SGMW的客户编号替换五菱工业的客户编号
            List<DistributorEntity> listDistributor = distributorRepository.findAll();
            //2. 获取车辆类型
            List<VehicleTypeEntity> vehicleTypeEntities = vehicleTypeRepository.findAll();

            Map<String, String> vehicleTypeMap = new HashMap<>();

            vehicleTypeEntities.forEach(e -> {
                vehicleTypeMap.put(e.getKey(), e.getVehicleType());
            });

            for (AllotVehicleEntity entity : importEntities) {
                if(entity.getVin()!=null && StringUtils.isNotBlank(entity.getVin())) {
                    List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
                    if (list.size() == 1) {
//                    entity.setCustomerCode(list.get(0).getSgmwCode());
                        entity.setSgmwCode(list.get(0).getSgmwCode());
                        //设置车辆类型
                        String vehicleType = getVehicleType(vehicleTypeMap, entity.getVin(), entity.getVehicleName());
                        entity.setVehicleType(vehicleType);

                        addEntities.add(entity);
                    } else {
                        resultEntities.add(entity);
                    }
                }else{
                    resultEntities.add(entity);
                }
            }
            if(resultEntities.size()>0){
                ZkUtils.showError("缺少经销商信息或缺少VIN！", "系统提示");
            }else{
                //2. 删除对应vin的数据
                List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
                allotVehicleRepository.deleteAllByVinIn(list);
                //3. 保存数据
                allotVehicleRepository.saveAll(addEntities);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return resultEntities;
        }
    }


    /**
     * 查询所有
     * @return
     */
    public List<AllotVehicleEntity> findAll() {
        return allotVehicleRepository.findAll();
    }

    /**
     * @Description: 保存配车单明细
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */

    public AllotVehicleEntity save(AllotVehicleEntity allotVehicleEntity) {
        try {
            return  allotVehicleRepository.save(allotVehicleEntity);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:24
     */
    public boolean delete(AllotVehicleEntity allotVehicleEntity) {
        try {
            allotVehicleRepository.delete(allotVehicleEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @Description: 通过ID删除实体
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:29
     */
    public boolean deleteById(String id) {
        try {
            // AllotVehicleEntity allotVehicleEntity=allotVehicleRepository.findOne(id);
            allotVehicleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: 通过ID查找实体
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:38
     */
    public AllotVehicleEntity findById(String id) {
        try {
            AllotVehicleEntity allotVehicleEntity=allotVehicleRepository.findById(id).orElse(null);
            return allotVehicleEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * map匹配车型
     *
     * @param map
     * @param vin
     * @return
     */
    public static String getVehicleType(Map<String, String> map, String vin, String vehicleName) {
        String value = "货改车";
        for (Map.Entry<String, String> entity : map.entrySet()) {
            if (vin.contains(entity.getKey())) {
                return entity.getValue();
            } else if (vehicleName.contains(entity.getKey())) {
                return entity.getValue();
            }
        }
        return value;
    }


}
