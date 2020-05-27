package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.repository.BalanceRepository;
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
@Service("balanceService")
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private DistributorRepository distributorRepository;

    public PageResult<BalanceEntity> getPageList(PageParam<BalanceEntity> pageParam) {
        //1.查询条件
        BalanceEntity balanceEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<BalanceEntity> specification = null;
        Specification<BalanceEntity> specification2 = null;
        Specification<BalanceEntity> specification3 = null;

        //页面查询条件

        if (pageParam.getUserType() == UserType.DISTRIBUTOR.getIndex()) {
            if (balanceEntity != null && StringUtils.isNotBlank(balanceEntity.getSgmwCode())) {
                specification2 = Specifications.<BalanceEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .eq(StringUtils.isNotEmpty(balanceEntity.getSgmwCode()), "sgmwCode", balanceEntity.getSgmwCode())
                        .build();
            }
        } else {
            if (balanceEntity != null && StringUtils.isNotBlank(balanceEntity.getSgmwCode()) || StringUtils.isNotBlank(balanceEntity.getDistributorCode()) || StringUtils.isNotBlank(balanceEntity.getDistributorName())) {
                specification2 = Specifications.<BalanceEntity>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotEmpty(balanceEntity.getSgmwCode()), "sgmwCode", "%" + balanceEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(balanceEntity.getDistributorCode()), "distributorCode", "%" + balanceEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(balanceEntity.getDistributorName()), "distributorName", "%" + balanceEntity.getDistributorName() + "%")
                        .build();
            }
        }

//        if(pageParam.getUserType()== UserType.DISTRIBUTOR.getIndex() && balanceEntity!=null&& StringUtils.isNotBlank(balanceEntity.getSgmwCode())){
//                specification2 = Specifications.<BalanceEntity>or()
//                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                        .eq(StringUtils.isNotEmpty(balanceEntity.getSgmwCode()), "sgmwCode", balanceEntity.getSgmwCode())
//                        .build();
//        }else{
//            specification2 = Specifications.<BalanceEntity>or()
////                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .like(StringUtils.isNotEmpty(balanceEntity.getSgmwCode()), "sgmwCode", "%" + balanceEntity.getSgmwCode()+ "%")
//                    .like(StringUtils.isNotEmpty(balanceEntity.getDistributorCode()), "distributorCode", "%" + balanceEntity.getDistributorCode() + "%")
//                    .like(StringUtils.isNotEmpty(balanceEntity.getDistributorName()), "distributorName", "%" + balanceEntity.getDistributorName()+ "%")
//                    .build();
//        }

        //组合查询条件
        specification = Specifications.<BalanceEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<BalanceEntity> pages = balanceRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 每次覆盖对应的客户数据
     *
     * @param entities
     * @return
     */
    public List<BalanceEntity> saveAll(List<BalanceEntity> entities) {
        List<BalanceEntity> resultEntities = new ArrayList<>();
        List<BalanceEntity> addEntities = new ArrayList<>();
        try {
            //1. 用SGMW的客户编号替换五菱工业的客户编号
            List<DistributorEntity> listDistributor = distributorRepository.findAll();
            for (BalanceEntity entity : entities) {
                List<DistributorEntity> lst = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
                if (lst.size() == 1) {
//                    entity.setCustomerCode(lst.get(0).getSgmwCode());
                    entity.setSgmwCode(lst.get(0).getSgmwCode());
                    addEntities.add(entity);
                } else {
                    resultEntities.add(entity);
                }
            }
            //2. 删除对应的客户编码的余额表数据
            List<String> list = addEntities.stream().map(e -> e.getDistributorCode()).collect(Collectors.toList());
            balanceRepository.deleteAllByDistributorCodeIn(list);

            //3. 保存新导入的客户余额数据
            balanceRepository.saveAll(addEntities);
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
    public List<BalanceEntity> findAll() {
        return balanceRepository.findAll();
    }
    /**
     * @Description: 保存客户余额
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:56
     */
    public BalanceEntity save(BalanceEntity balanceEntity) {
        try {
            return balanceRepository.save(balanceEntity);
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

    public boolean delete(BalanceEntity balanceEntity) {
        try {
            balanceRepository.delete(balanceEntity);
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
            balanceRepository.deleteById(id);
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

    public BalanceEntity findOne(String id) {
        try {
            return balanceRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
