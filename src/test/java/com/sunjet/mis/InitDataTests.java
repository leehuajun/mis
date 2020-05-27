package com.sunjet.mis;

import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.basic.entity.*;
import com.sunjet.mis.module.basic.repository.*;
import com.sunjet.mis.module.plan.entity.SalesPlanEntity;
import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.module.report.entity.PlanEntity;
import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
import com.sunjet.mis.module.system.entity.*;
import com.sunjet.mis.module.system.repository.*;
import com.sunjet.mis.module.warehouse.entity.VehicleAllotEntity;
import com.sunjet.mis.module.warehouse.entity.VehicleEntryEntity;
import com.sunjet.mis.module.warehouse.entity.VehicleInvoiceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDataTests {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IconRepository iconRepository;
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private DictionaryRepository dictionaryRepository;
    @Autowired
    private DictionaryEntryRepository dictionaryEntryRepository;

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;


    @Test
    public void contextLoads() {
        initMenu();
        initOperation();
        initOrgs();
        initResource();
        initRoles();
        initUsers();
        initPermission();
        initConfig();
        initBasicData();
        initVehicleModel();
        initVehicleType();
    }


    @Test
    public void initMenu() {
        List<MenuEntity> subMenus = new ArrayList<>();
//        MenuEntity parent = new MenuEntity("订单管理", null, 100, null);
        MenuEntity parent = MenuEntity.builder().name("订单管理").seq(100).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("订单录入").url("/views/order/order_form.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("订单导入").url("/views/order/order_import.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("订单查询").url("/views/order/order_list.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("订单汇总报表").url("/views/order/order_report.zul").seq(40).parent(parent).build()
        ));
        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("计划管理").seq(110).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("计划录入").url("/views/plan/plan_form.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("计划导入").url("/views/plan/plan_import.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("计划查询").url("/views/plan/plan_list.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("计划执行汇总报表").url("/views/plan/plan_report.zul").seq(40).parent(parent).build()
        ));
        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("仓库管理").seq(120).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("车辆下线入库").url("/views/warehouse/vehicle_in_form.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("按订单配车").url("/views/warehouse/allot_vehicle_form.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("仓库发货").url("/views/warehouse/dispatch_vehicle.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("车辆查询").url("/views/warehouse/vehicle_list.zul").seq(40).parent(parent).build(),
                MenuEntity.builder().name("库存台账报表").url("/views/warehouse/standing_report.zul").seq(50).parent(parent).build()
        ));
        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("经销商管理").seq(130).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("订单录入").url("/views/plan/plan_form.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("订单导入").url("/views/plan/plan_import.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("接车确认").url("/views/warehouse/receive_vehicle_form.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("销售录入").url("/views/sales/sales_form.zul").seq(40).parent(parent).build(),
                MenuEntity.builder().name("仓库车辆查询").url("/views/warehouse/vehicle_list.zul").seq(50).parent(parent).build(),
                MenuEntity.builder().name("仓库汇总报表").url("/views/warehouse/vehicle_report.zul").seq(60).parent(parent).build()
        ));
        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("报表管理").seq(140).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("35实销").url("/views/report/35_report.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("改装车在途汇总报表").url("/views/report/in_transit_report.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("社会库存报表").url("/views/report/social_report.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("经销商销售指标完成报表").url("/views/report/complete_report.zul").seq(40).parent(parent).build(),
                MenuEntity.builder().name("销售订单明细报表").url("/views/report/order_report.zul").seq(50).parent(parent).build(),
                MenuEntity.builder().name("周产销平衡报表").url("/views/report/weekly_balanced_report.zul").seq(60).parent(parent).build(),
                MenuEntity.builder().name("客户余额").url("/views/report/balance/balance_report.zul").seq(70).parent(parent).build(),
                MenuEntity.builder().name("配车单").url("/views/report/allot_vehicle/allot_vehicle_report.zul").seq(80).parent(parent).build(),
                MenuEntity.builder().name("车辆状态").url("/views/report/vehicle_inv/vehicle_inv_report.zul").seq(90).parent(parent).build(),
                MenuEntity.builder().name("销售计划").url("/views/report/plan/plan_report.zul").seq(100).parent(parent).build(),
                MenuEntity.builder().name("计划执行汇总").url("/views/report/plan_exec_summary/plan_exec_summary_report.zul").seq(110).parent(parent).build()
        ));

        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("基础数据管理").seq(990).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("区域管理").url("/views/basic/region/region_list.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("类型管理").url("/views/basic/mold/mold_list.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("车型管理").url("/views/basic/vehicle_model/vehicle_model_list.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("经销商").url("/views/basic/distributor/distributor_list.zul").seq(40).parent(parent).build(),
                MenuEntity.builder().name("经销商指标管理").url("/views/basic/distributor_target/distributor_target_list.zul").seq(50).parent(parent).build()
        ));

        menuRepository.saveAll(subMenus);

        parent = MenuEntity.builder().name("系统管理").seq(1000).build();
        parent = menuRepository.save(parent);
        subMenus.clear();
        subMenus.addAll(Arrays.asList(
                MenuEntity.builder().name("组织管理").url("/views/system/org_list.zul").seq(5).parent(parent).build(),
                MenuEntity.builder().name("资源管理").url("/views/system/resource_list.zul").seq(10).parent(parent).build(),
                MenuEntity.builder().name("操作管理").url("/views/system/operation_list.zul").seq(15).parent(parent).build(),
                MenuEntity.builder().name("用户管理").url("/views/system/user_list.zul").seq(20).parent(parent).build(),
                MenuEntity.builder().name("角色管理").url("/views/system/role_list.zul").seq(30).parent(parent).build(),
                MenuEntity.builder().name("参数配置").url("/views/system/config_list.zul").seq(40).parent(parent).build(),
                MenuEntity.builder().name("菜单配置").url("/views/system/menu_list.zul").seq(50).parent(parent).build(),
                MenuEntity.builder().name("数据字典").url("/views/system/dictionary_list.zul").seq(60).parent(parent).build(),
                MenuEntity.builder().name("参考图标").url("/views/system/icon_list.zul").seq(70).parent(parent).build()
        ));
        menuRepository.saveAll(subMenus);
    }

    @Test
    public void initUsers() {
        List<UserEntity> userEntities = Arrays.asList(
                UserEntity.builder().logId("admin").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("系统管理员").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("wlgly").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("管理员").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("wljhy").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("计划员").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("sgmw").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("SGMW").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("xinming").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("辛明").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("litao").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("李涛").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("wanghaijing").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("王海菁").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("wangjianbo").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("王健波").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("jindezhi").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("金德智").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("guowei").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("郭伟").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("yuanchao").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("袁超").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("weihouyong").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("韦厚勇").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("huoying").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("霍颖").userType(UserType.INTERNAL.getIndex()).phone("").email("").build(),
                UserEntity.builder().logId("liuxianjin").password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").name("刘显进").userType(UserType.INTERNAL.getIndex()).phone("").email("").build()
        );
        userRepository.saveAll(userEntities);
    }

    @Test
    public void initOrgs() {

        OrgEntity parent = OrgEntity.builder().code("001").name("XX集团").seq(100).build();
        parent = orgRepository.save(parent);
        List<OrgEntity> children = Arrays.asList(
                OrgEntity.builder().code("001001").name("子公司01").seq(100100).parent(parent).build(),
                OrgEntity.builder().code("001002").name("子公司02").seq(100110).parent(parent).build(),
                OrgEntity.builder().code("001003").name("子公司03").seq(100120).parent(parent).build()
        );
        children = orgRepository.saveAll(children);

    }

    @Test
    public void initRoles() {
        List<RoleEntity> roles = Arrays.asList(
                RoleEntity.builder().code("role001").name("管理员").build(),
                RoleEntity.builder().code("role002").name("计划员").build(),
                RoleEntity.builder().code("role003").name("SGMW").build(),
                RoleEntity.builder().code("role004").name("区域经理").build(),
                RoleEntity.builder().code("role005").name("计划经理").build(),
                RoleEntity.builder().code("role006").name("计划总监").build(),
                RoleEntity.builder().code("role007").name("经销商").build()
        );
        roleRepository.saveAll(roles);
    }

    @Test
    public void initResource() {
        List<ResourceEntity> resources = Arrays.asList(
                ResourceEntity.builder().code(UserEntity.class.getSimpleName()).name("用户").build(),
                ResourceEntity.builder().code(RoleEntity.class.getSimpleName()).name("角色").build(),
                ResourceEntity.builder().code(OrgEntity.class.getSimpleName()).name("组织").build(),
                ResourceEntity.builder().code(ResourceEntity.class.getSimpleName()).name("资源").build(),
                ResourceEntity.builder().code(ConfigEntity.class.getSimpleName()).name("参数").build(),
                ResourceEntity.builder().code(MenuEntity.class.getSimpleName()).name("菜单").build(),
                ResourceEntity.builder().code(DictionaryEntity.class.getSimpleName()).name("字典").build(),
                ResourceEntity.builder().code(OperationEntity.class.getSimpleName()).name("操作").build(),
                ResourceEntity.builder().code(RegionEntity.class.getSimpleName()).name("区域").build(),
                ResourceEntity.builder().code(VehicleModelEntity.class.getSimpleName()).name("车型").build(),
                ResourceEntity.builder().code(DistributorEntity.class.getSimpleName()).name("经销商").build(),
                ResourceEntity.builder().code(SalesTargetEntity.class.getSimpleName()).name("销售指标").build(),
                ResourceEntity.builder().code(BalanceEntity.class.getSimpleName()).name("客户余额表").build(),
                ResourceEntity.builder().code(AllotVehicleEntity.class.getSimpleName()).name("配车单明细").build(),
                ResourceEntity.builder().code(VehicleInvEntity.class.getSimpleName()).name("库存车辆状态表").build(),
                ResourceEntity.builder().code(PlanEntity.class.getSimpleName()).name("计划执行表").build(),

                ResourceEntity.builder().code(SalesOrderEntity.class.getSimpleName()).name("销售订单").build(),
                ResourceEntity.builder().code(SalesPlanEntity.class.getSimpleName()).name("销售计划").build(),

                ResourceEntity.builder().code(VehicleEntryEntity.class.getSimpleName()).name("入库单").build(),
                ResourceEntity.builder().code(VehicleAllotEntity.class.getSimpleName()).name("配车单").build(),
                ResourceEntity.builder().code(VehicleInvoiceEntity.class.getSimpleName()).name("发货单").build()
        );
        resourceRepository.saveAll(resources);
    }

    @Test
    public void initOperation() {
        List<OperationEntity> operations = Arrays.asList(
                OperationEntity.builder().code("read").name("查看").seq(10).build(),
                OperationEntity.builder().code("create").name("创建").seq(20).build(),
                OperationEntity.builder().code("modify").name("修改").seq(30).build(),
                OperationEntity.builder().code("delete").name("删除").seq(40).build(),
                OperationEntity.builder().code("search").name("搜索").seq(50).build(),
                OperationEntity.builder().code("audit").name("审核").seq(60).build(),
                OperationEntity.builder().code("disaudit").name("弃审").seq(70).build(),
                OperationEntity.builder().code("import").name("导入").seq(80).build(),
                OperationEntity.builder().code("export").name("导出").seq(90).build(),
                OperationEntity.builder().code("print").name("打印").seq(100).build()
        );
        operationRepository.saveAll(operations);
    }

    @Test
    public void initPermission() {
        List<OperationEntity> operations = operationRepository.findAll();
        List<ResourceEntity> resources = resourceRepository.findAll();
        for (ResourceEntity resource : resources) {
            for (OperationEntity operation : operations) {
                PermissionEntity entity = PermissionEntity.builder().resource(resource).operation(operation).seq(operation.getSeq()).build();
                permissionRepository.save(entity);
            }
        }
    }

    @Test
    public void initConfig() {
        List<ConfigEntity> list = Arrays.asList(
                ConfigEntity.builder().key("title").initialValue("销售订单跟踪系统").value("订单跟踪系统").comment("系统Title").build(),
                ConfigEntity.builder().key("pageSize").initialValue("20").value("20").comment("每页显示数量").build()
        );

        configRepository.saveAll(list);

    }

    @Test
    public void initDictionary() {
        DictionaryEntity dictionaryEntity = dictionaryRepository.save(DictionaryEntity.builder().name("season").comment("季节").build());
        dictionaryEntryRepository.saveAll(Arrays.asList(
                DictionaryEntryEntity.builder().key("spring").value("春季").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("summer").value("夏季").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("autumn").value("秋季").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("winter").value("冬季").dictionary(dictionaryEntity).build()
        ));

        dictionaryEntity = dictionaryRepository.save(DictionaryEntity.builder().name("brand").comment("汽车品牌").build());
        dictionaryEntryRepository.saveAll(Arrays.asList(
                DictionaryEntryEntity.builder().key("wuling").value("五菱汽车").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("changan").value("长安汽车").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("saic").value("上海汽车").dictionary(dictionaryEntity).build(),
                DictionaryEntryEntity.builder().key("honda").value("本田汽车").dictionary(dictionaryEntity).build()
        ));
    }

    @Test
    public void initBasicData() {
        try {

            String fileName = "/Users/lhj/project/mis/src/main/resources/data/distributor.csv";
            File file = new File(fileName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            RegionEntity region = null;
            ProvinceEntity province = null;
            List<RegionEntity> regions = new ArrayList<>();
            List<ProvinceEntity> provinces = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                region = checkRegion(regions, arr[0]);
                if (region == null) {
                    region = RegionEntity.builder().name(arr[0]).build();
                    region = regionRepository.save(region);
                    regions.add(region);
                }

                province = checkProvice(provinces, arr[1]);
                if (province == null) {
                    province = ProvinceEntity.builder().name(arr[1]).region(region).build();
                    province = provinceRepository.save(province);
                    provinces.add(province);
                }

                DistributorEntity distributor = DistributorEntity.builder().province(province).code(arr[5]).sgmwCode(arr[3]).name(arr[4]).level(arr[2]).build();
                distributorRepository.save(distributor);

                UserEntity user = UserEntity.builder().logId(arr[3]).name(arr[4]).password("5dfdd6a7ec1924ca2ac2898a570587fc").salt("dacfw").userType(UserType.DISTRIBUTOR.getIndex()).build();
                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RegionEntity checkRegion(List<RegionEntity> entities, String name) {
        List<RegionEntity> list = entities.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (list.size() <= 0)
            return null;
        return list.get(0);
    }

    private ProvinceEntity checkProvice(List<ProvinceEntity> entities, String name) {
        List<ProvinceEntity> list = entities.stream().filter(r -> r.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        if (list.size() <= 0)
            return null;
        return list.get(0);
    }

    @Test
    public void initVehicleModel() {
        try {

            String fileName = "/Users/lhj/project/mis/src/main/resources/data/类型清单_1204.csv";
            File file = new File(fileName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                System.out.println(line);
                VehicleModelEntity entity = null;
                if(arr.length == 4){
                    entity = VehicleModelEntity.builder().code(arr[0]).name(arr[1]).vehicleSeries(arr[2]).effluent(arr[3]).build();
                }else if(arr.length == 5){
                    entity = VehicleModelEntity.builder().code(arr[0]).name(arr[1]).vehicleSeries(arr[2]).effluent(arr[3]).displacement(arr[4]).build();
                } else if (arr.length ==6 ){
                    entity = VehicleModelEntity.builder().code(arr[0]).name(arr[1]).vehicleSeries(arr[2]).effluent(arr[3]).displacement(arr[4]).comment(arr[5]).build();
                }

                vehicleModelRepository.save(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author: wushi
     * @description: 初始化车辆类型
     * @Date: Created in 15:48 2019/3/8
     * @Modify by: wushi
     * @ModifyDate by: 15:48 2019/3/8
     */
    @Test
    public void initVehicleType() {
        List<VehicleTypeEntity> vehicleTypeEntities = Arrays.asList(
                VehicleTypeEntity.builder().key("福祉车").vehicleType("特种车").build(),
                VehicleTypeEntity.builder().key("新能源").vehicleType("特种车").build(),
                VehicleTypeEntity.builder().key("客").vehicleType("客改车").build(),
                VehicleTypeEntity.builder().key("自卸").vehicleType("环卫车").build(),
                VehicleTypeEntity.builder().key("可卸").vehicleType("环卫车").build(),
                VehicleTypeEntity.builder().key("桶装").vehicleType("环卫车").build(),
                VehicleTypeEntity.builder().key("冷藏车").vehicleType("冷藏车").build(),
                VehicleTypeEntity.builder().key("警车").vehicleType("客改车").build(),
                VehicleTypeEntity.builder().key("救护车").vehicleType("客改车").build(),
                VehicleTypeEntity.builder().key("TS28").vehicleType("原型客车").build(),
                VehicleTypeEntity.builder().key("TS26").vehicleType("原型货车").build()

        );
        vehicleTypeRepository.saveAll(vehicleTypeEntities);
    }
}
