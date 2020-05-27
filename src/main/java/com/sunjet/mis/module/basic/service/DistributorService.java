package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.basic.repository.ProvinceRepository;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
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
@Service("distributorService")
public class DistributorService {

    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    public List<DistributorEntity> findAll(){
        return distributorRepository.findAll();
    }

    /**
     * 经销商查询分页方法
     * @param pageParam
     * @return
     */
    public PageResult<DistributorEntity> getPageList(PageParam<DistributorEntity> pageParam){
        //1.查询条件
        DistributorEntity distributorEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DistributorEntity> specification = null;
        //页面查询条件
        if (distributorEntity != null &&(StringUtils.isNotBlank(distributorEntity.getName()) || StringUtils.isNotBlank(distributorEntity.getCode()) || distributorEntity.getProvince()!=null)) {
            specification = Specifications.<DistributorEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(distributorEntity.getCode()), "code",  "%" + distributorEntity.getCode() + "%")
                    .like(StringUtils.isNotEmpty(distributorEntity.getName()), "name",  "%" + distributorEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(distributorEntity.getSgmwCode()), "sgmwCode",  "%" + distributorEntity.getSgmwCode() + "%")
                    .eq(distributorEntity.getProvince() != null, "province.id",distributorEntity.getProvince()!=null ?  distributorEntity.getProvince().getId() : "")
                    .build();
        }

        //3.执行查询
        Page<DistributorEntity> pages = distributorRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 保存车型管理信息
     * @param vehicleModelEntity
     * @return
     */

    public DistributorEntity save(DistributorEntity distributorEntity){
        if(StringUtils.isNotBlank(distributorEntity.getProvinceName())){
            ProvinceEntity pi = provinceRepository.findByName(distributorEntity.getProvinceName());
            distributorEntity.setProvince(pi);
        }
        return distributorRepository.save(distributorEntity);
    }


    /**
     * 查询一个实体
     * @param id
     * @return
     */
    public DistributorEntity findById(String id) {
        return distributorRepository.findById(id).orElse(null);
    }


    /**
     * 删除对象
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            distributorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<DistributorEntity> saveAll(List<DistributorEntity> importEntities) {

        List<DistributorEntity> resultEntities = new ArrayList<>();
        List<DistributorEntity> addEntities = new ArrayList<>();
        try{
            List<ProvinceEntity> listProvince = provinceRepository.findAll();
            for(ProvinceEntity pe:listProvince){
                for(DistributorEntity entity : importEntities) {
                    if(pe.getName().equals(entity.getProvinceName())){
                        entity.setProvince(pe);
                    }
                }
            }
            List<DistributorEntity> listDistributor = distributorRepository.findAll();
            for (DistributorEntity entity : importEntities) {
                for (DistributorEntity de : listDistributor) {
                   if(entity.getCode().equals(de.getCode())){
                       addEntities.add(de);
                   }
               }
            }
            //2. 删除对应vin的数据
            List<String> list = addEntities.stream().map(e -> e.getCode()).collect(Collectors.toList());
            distributorRepository.deleteAllByCode(list);

            //3. 保存数据
            distributorRepository.saveAll(importEntities);

        }catch (Exception e){
            e.printStackTrace();
            throw new MisException(e.getMessage());
        }finally{
            return resultEntities;
        }
    }

}
