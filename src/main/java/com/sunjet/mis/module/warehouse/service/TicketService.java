package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.AllotVehicleRepository;
import com.sunjet.mis.module.report.repository.BalanceRepository;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.VehicleInvRepository;
import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import com.sunjet.mis.module.warehouse.entity.TicketEntity;
import com.sunjet.mis.module.warehouse.repository.InventoryRepository;
import com.sunjet.mis.module.warehouse.repository.TicketRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("ticketService")
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;


    public List<TicketEntity> findAll() {
        try {
            return this.ticketRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数
     * @return result 包含 List<Entity> 和分页数据
     */
    public PageResult<TicketEntity> getPageList(PageParam<TicketEntity> pageParam) {
        //1.查询条件
        TicketEntity ticketEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<TicketEntity> specification1 = null;
        Specification<TicketEntity> specification2 = null;
        Specification<TicketEntity> specification3 = null;

        //页面条件
        if (ticketEntity != null && (StringUtils.isNotBlank(ticketEntity.getVin()) || StringUtils.isNotBlank(ticketEntity.getVsn()) || StringUtils.isNotBlank(ticketEntity.getVehicleType()))) {
            specification2 = Specifications.<TicketEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(ticketEntity.getVin()), "vin", "%"+ticketEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(ticketEntity.getVsn()), "vsn", "%"+ticketEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(ticketEntity.getVehicleType()), "vehicleType", "%"+ticketEntity.getVehicleType()+"%")
                    .build();
        }

        //组织
//        if(authObject.getOrg().getOrgHierarchy().intValue()!=1) {
//            specification3 = Specifications.<ConfigEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .eq("orgId", authObject.getOrg().getObjId())
//                    .eq("orgType", "1")
//                    .build();
//        }

        //组合查询条件
        specification1 = Specifications.<TicketEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<TicketEntity> pages = ticketRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        //4.数据转换
//        List<ConfigEntity> rows = new ArrayList<>();
//        for (ConfigEntity entity : pages.getContent()) {
//            rows.add(entity);
//        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        System.out.println(pages.getContent().size());
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public TicketEntity findById(String id) {
        Optional<TicketEntity> entityOptional = ticketRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public TicketEntity save(TicketEntity entity) {
        return ticketRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            ticketRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<TicketEntity> saveAll(List<TicketEntity> importEntities) {
        //List<TicketEntity> resultEntities = new ArrayList<>();
        List<TicketEntity> addEntities = new ArrayList<>();
        HashSet<InventoryEntity> resultEntities =new HashSet<>();
        List<TicketEntity> ticketEntities = ticketRepository.findAll();
        for(TicketEntity entity:ticketEntities){
            for(TicketEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVsn()) && StringUtils.isNotBlank(importEntity.getVsn())) {
                    if (entity.getVin().equals(importEntity.getVin())) {
                        addEntities.add(entity);
                    }
                }else{
                    addEntities.add(entity);
                }
            }
        }

        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            ticketRepository.deleteAllByVinIn(list);
            ticketRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;


//        try {
            //1. 用SGMW的客户编号替换五菱工业的客户编号
//            List<OrderPlanEntity> listDistributor = orderPlanRepository.findAll();
//            for (OrderPlanEntity entity : importEntities) {
//                List<OrderPlanEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
//                if (list.size() == 1) {
////                    entity.setCustomerCode(list.get(0).getSgmwCode());
//                    entity.setSgmwCode(list.get(0).getSgmwCode());
//                    addEntities.add(entity);
//                } else {
//                    resultEntities.add(entity);
//                }
//            }
            //2. 删除对应vin的数据
//            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
//            allotVehicleRepository.deleteAllByVinIn(list);

            //3. 保存数据
//            orderPlanRepository.saveAll(addEntities);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MisException(e.getMessage());
//        } finally {
//            return resultEntities;
//        }



    }

}
