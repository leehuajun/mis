package com.sunjet.mis.module.basic.service;

import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.basic.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wushi
 * @description:
 * @Date: Created in 15:07 2019/1/10
 * @Modify by: wushi
 * @ModifyDate by: 15:07 2019/1/10
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("provinceService")
public class ProvinceService {


    @Autowired
    private ProvinceRepository provinceRepository;


    /**
     * 查询所有
     *
     * @return
     */
    public List<ProvinceEntity> findAll() {
        return provinceRepository.findAll();
    }

    public ProvinceEntity findById(String id) {
        return provinceRepository.findById(id).orElse(null);
    }

//    /**
//     * @Description: 删除省份
//     * @Author: YFS
//     * @CreateDate: 2019/1/11 13:50
//     */
//    public boolean delete(ProvinceEntity provinceEntity) {
//        try {
//            provinceRepository.delete(provinceEntity);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * @Description: 根据id删除
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:00
     */
    public boolean deleteById(String id) {
        try {
            provinceRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 保存
     *
     * @param provinceEntity
     * @return
     */
    public ProvinceEntity save(ProvinceEntity provinceEntity) {
        return provinceRepository.save(provinceEntity);
    }

}
