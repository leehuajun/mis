package com.sunjet.mis;

import com.sunjet.mis.module.basic.entity.VehicleModelEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.repository.PermissionRepository;
import com.sunjet.mis.module.system.repository.RoleRepository;
import com.sunjet.mis.utils.common.EncodingDetect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    public void contextLoads() {
    }


    @Test
    public void initIcons() {
//        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
//            /* 读入TXT文件 */
//
//            List<IconEntity> iconEntities = new ArrayList<>();
//            String pathname = "/Volumes/hd/demo/sb-demos/mis/src/main/webapp/resource/awesome_4.7.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
////            String pathname = "/Volumes/hd/demo/sb-demos/mis/src/main/webapp/resource/awesome_5.5_type.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
//            File filename = new File(pathname); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(filename)); // 建立一个输入流对象reader
//            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
//            String line = "";
//            line = br.readLine();
//            String category = "";
//            while (line != null) {
//                if(line.trim().contains("@")){
//                    category = line.replaceAll("@","").trim();
//                } else {
//                    iconEntities.add(IconEntity.builder().category(category).name("z-icon-" + line.trim()).build());
//                }
//                line = br.readLine(); // 一次读入一行数据
//            }
//
//            iconRepository.saveAll(iconEntities);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void checkCharset() {
        System.out.println(EncodingDetect.getJavaEncodeFile("/Users/lhj/Desktop/客户发车余额查询--界面.csc"));
    }

    @Test
    public void checkPermissions(){
        RoleEntity role001 = roleRepository.findById("402881ed67ed948e0167ed94aadd0044").get();
        RoleEntity role002 = roleRepository.findById("402881ed67ed948e0167ed94aadd0045").get();
        List<RoleEntity> roles = Arrays.asList(role001,role002);
        List<PermissionEntity> list = permissionRepository.findPermissionEntitiesByRolesContains(roles);
        System.out.println(list.size());
    }

    @Test
    public void readCSV() {
        try {


            String fileName = "/Users/lhj/project/mis/src/main/resources/data/配车单明细表utf.csv";
            System.out.println(EncodingDetect.getJavaEncodeFile(fileName));
            DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
            BufferedReader br = new BufferedReader(new InputStreamReader(in,EncodingDetect.getJavaEncodeFile(fileName)));

//            File file = new File(fileName);
//            InputStreamReader reader = new InputStreamReader(new FileInputStream(file,"GBK"));
//            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
//                System.out.println(arr.length);
                for(int i=0;i<arr.length;i++){
                    System.out.print(i+":" + arr[i] + "\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
