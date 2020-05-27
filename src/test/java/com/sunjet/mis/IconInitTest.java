package com.sunjet.mis;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.helper.DateHelper;
import com.sunjet.mis.module.report.entity.BalanceEntity;
import com.sunjet.mis.utils.common.CSVUtil;
import com.sunjet.mis.utils.common.EncodingDetect;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class IconInitTest {

//    @Test
//    public void initIcons() {
//        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
//            /* 读入TXT文件 */
//            String pathname = "/Users/lhj/demos/sb-demos/mis/src/main/webapp/resource/awesome_4.7.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
//            File filename = new File(pathname); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(filename)); // 建立一个输入流对象reader
//            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
//            String line = "";
//            line = br.readLine();
//            System.out.println(line);
//            while (line != null) {
//                line = br.readLine(); // 一次读入一行数据
//                System.out.println(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void checkCharset() throws Exception {
        System.out.println(EncodingDetect.getJavaEncodeFile("/Users/lhj/Desktop/客户发车余额查询--界面.csv"));
        System.out.println(EncodingDetect.getJavaEncodeFile("/Users/lhj/Desktop/test02.csv"));
        System.out.println(EncodingDetect.getJavaEncodeFile("/Volumes/hd/demo/sb-demos/mis/src/main/webapp/uploads/other/c08684ceca954103b76433352bbd1e2e.csv"));

        Set<String> headerSet = new HashSet<>();
        Class<BalanceEntity> clazz = BalanceEntity.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn header = field.getAnnotation(ExcelColumn.class);
            if (header != null) {
                headerSet.add(header.value());
            }
        }

//        Object[] headerArr = headerList.toArray();

        String[] headerArr=headerSet.toArray(new String[headerSet.size()]);
        List<CSVRecord> csvRecords = CSVUtil.readCSV("/Users/lhj/Desktop/客户发车余额查询--界面.csv", headerArr);
        for(CSVRecord record : csvRecords){
            System.out.println(record);
        }
    }

    @Test
    public void testDate(){
        System.out.println(DateHelper.getFirstDayByYeanAndMonth(2019, 1));
    }
}
