package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import com.sunjet.mis.module.report.repository.VehicleInvRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("vehicleInvService")
public class VehicleInvService {

    @Autowired
    private VehicleInvRepository vehicleInvRepository;
    @Autowired
    private DistributorRepository distributorRepository;

    public PageResult<VehicleInvEntity> getPageList(PageParam<VehicleInvEntity> pageParam) {
        //1.查询条件
        VehicleInvEntity vehicleInvEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<VehicleInvEntity> specification = null;
        Specification<VehicleInvEntity> specification2 = null;
        Specification<VehicleInvEntity> specification3 = null;

        //页面查询条件
        if(pageParam.getUserType()== UserType.DISTRIBUTOR.getIndex()){
            if(vehicleInvEntity!=null&& (StringUtils.isNotBlank(vehicleInvEntity.getSgmwCode()) || StringUtils.isNotBlank(vehicleInvEntity.getDistributorCode()) || StringUtils.isNotBlank(vehicleInvEntity.getDistributorName()) || StringUtils.isNotBlank(vehicleInvEntity.getVin()) || StringUtils.isNotBlank(vehicleInvEntity.getVsn()))){
                specification2 = Specifications.<VehicleInvEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(StringUtils.isNotEmpty(vehicleInvEntity.getSgmwCode()), "sgmwCode", vehicleInvEntity.getSgmwCode())
                        .eq(StringUtils.isNotEmpty(vehicleInvEntity.getDistributorCode()), "distributorCode", vehicleInvEntity.getDistributorCode())
                        .eq(StringUtils.isNotEmpty(vehicleInvEntity.getDistributorName()), "distributorName", vehicleInvEntity.getDistributorName())
                        .eq(StringUtils.isNotEmpty(vehicleInvEntity.getVin()), "vin", vehicleInvEntity.getVin())
                        .eq(StringUtils.isNotEmpty(vehicleInvEntity.getVsn()), "vsn", vehicleInvEntity.getVsn())
                        .build();
            }
        } else {
            if(vehicleInvEntity!=null&& (StringUtils.isNotBlank(vehicleInvEntity.getSgmwCode()) || StringUtils.isNotBlank(vehicleInvEntity.getDistributorCode()) || StringUtils.isNotBlank(vehicleInvEntity.getDistributorName()) || StringUtils.isNotBlank(vehicleInvEntity.getVin()) || StringUtils.isNotBlank(vehicleInvEntity.getVsn()))){
                specification3 = Specifications.<VehicleInvEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotEmpty(vehicleInvEntity.getSgmwCode()), "sgmwCode", "%" + vehicleInvEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(vehicleInvEntity.getDistributorCode()), "distributorCode", "%" + vehicleInvEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(vehicleInvEntity.getDistributorName()), "distributorName", "%" + vehicleInvEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(vehicleInvEntity.getVin()), "vin", "%"+vehicleInvEntity.getVin())
                        .like(StringUtils.isNotEmpty(vehicleInvEntity.getVsn()), "vsn", "%"+vehicleInvEntity.getVsn())
                        .build();
            }
        }

        //组合查询条件
        specification = Specifications.<VehicleInvEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();

        //3.执行查询
        Page<VehicleInvEntity> pages = vehicleInvRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<VehicleInvEntity> saveAll(List<VehicleInvEntity> importEntities) {

        List<VehicleInvEntity> resultEntities = new ArrayList<>();
        List<VehicleInvEntity> addEntities = new ArrayList<>();
        try{
            //1. 用SGMW的客户编号替换五菱工业的客户编号
            List<DistributorEntity> listDistributor = distributorRepository.findAll();
            for (VehicleInvEntity entity : importEntities) {
                List<DistributorEntity> list = listDistributor.stream().filter(e -> e.getName().equalsIgnoreCase(entity.getDistributorName())).collect(Collectors.toList());
                if(list.size()==1){
                    entity.setDistributorCode(list.get(0).getCode());
                    entity.setSgmwCode(list.get(0).getSgmwCode());
                    addEntities.add(entity);
                }else{
                    resultEntities.add(entity);
                }
            }
            //2. 删除对应vin的数据
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            vehicleInvRepository.deleteAllByVinIn(list);

            //3. 保存数据
            vehicleInvRepository.saveAll(addEntities);

        }catch (Exception e){
            e.printStackTrace();
            throw new MisException(e.getMessage());
        }finally{
            return resultEntities;
        }
    }

    /**
     * 查询所有
     * @return
     */
    public List<VehicleInvEntity> findAll() {
        return vehicleInvRepository.findAll();
    }

    /**
     * @Description: 库存车辆状态跟踪   保存
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:56
     */
    public VehicleInvEntity save(VehicleInvEntity vehicleInvEntity) {
        try {
            return vehicleInvRepository.save(vehicleInvEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:59
     */

    public boolean delete(VehicleInvEntity vehicleInvEntity) {
        try {
            vehicleInvRepository.delete(vehicleInvEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: 通过id删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:02
     */
    public boolean deleteById(String id) {
        try {
            vehicleInvRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @Description: 通过id查询对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:04
     */

    public VehicleInvEntity findById(String id) {
        try {
               return   vehicleInvRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
