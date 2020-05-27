package com.sunjet.mis.utils.common;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.vm.helper.AuxHeaderInfo;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;
import org.zkoss.zul.Filedownload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 功能说明：Excel 导入/导出
 * 典型用法：无
 * 特殊用法：无
 * 创建者：phil
 * 创建时间： 2017年11月9日
 * 修改人：phil
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本号：1.0
 */
public class ExcelUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");// 格式化 number为整

    private static final DecimalFormat DECIMAL_FORMAT_PERCENT = new DecimalFormat("##.00%");//格式化分比格式，后面不足2位的用0补齐

//	private static final DecimalFormat df_per_ = new DecimalFormat("0.00%");//格式化分比格式，后面不足2位的用0补齐,比如0.00,%0.01%

//	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); // 格式化日期字符串

//    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyy/MM/dd");
//    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss");

    private static final DecimalFormat DECIMAL_FORMAT_NUMBER = new DecimalFormat("0.00E000"); //格式化科学计数器

    private static final Pattern POINTS_PATTERN = Pattern.compile("0.0+_*[^/s]+"); //小数匹配

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final NumberFormat nf = NumberFormat.getInstance();
    private static final DecimalFormat df2 = new DecimalFormat();

    /**
     * 对外提供读取excel 的方法
     *
     * @param fileName
     * @return
     * @throws IOException
     */
//    public static List<List<Object>> readExcel(String fileName) throws IOException {
//
////        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
////        if (Objects.equals("xls", extension) || Objects.equals("xlsx", extension)) {
////            return readExcel(file.getInputStream());
////        } else {
////            throw new IOException("不支持的文件类型");
////        }
//        return readExcel(fileName);
//    }

    /**
     * 对外提供读取excel 的方法
     *
     * @param fileName
     * @param cls
     * @return
     * @throws IOException
     */
//    public static <T> List<T> readExcel(String fileName, Class<T> cls) throws IOException {
////        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
////        if (Objects.equals("xls", extension) || Objects.equals("xlsx", extension)) {
////            return readExcel(file.getInputStream(), cls);
////        } else {
////            throw new IOException("不支持的文件类型");
////        }
//        return readExcel(fileName, cls);
//    }

    /**
     * 读取 office excel
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String fileName) throws IOException {
        File file = null;
        List<List<Object>> list = new LinkedList<>();
        Workbook workbook = null;
        try {
            file = new File(fileName);
            workbook = WorkbookFactory.create(file);
//            int sheetsNumber = workbook.getNumberOfSheets();
//            for (int n = 0; n < sheetsNumber; n++) {
            Sheet sheet = workbook.getSheetAt(0);
            Object value = null;
            Row row = null;
            Cell cell = null;
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) { // 从第二行开始读取
                row = sheet.getRow(i);
                if (StringUtils.isEmpty(row)) {
                    continue;
                }
                List<Object> linked = new LinkedList<>();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (StringUtils.isEmpty(cell)) {
                        continue;
                    }
                    value = getCellValue(cell);
//                    value = cell.getStringCellValue();
                    linked.add(value);
                }
                list.add(linked);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            file = null;
        }
        return list;
    }

    /**
     * 获取excel数据 将之转换成bean
     *
     * @param fileName
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(String fileName, Class<T> cls) throws IOException {
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        nf.setGroupingUsed(false);
        File file = null;
        List<T> dataList = new LinkedList<>();
        Workbook workbook = null;
        try {
            file = new File(fileName);
            workbook = WorkbookFactory.create(file);
//            Map<String, List<Field>> classMap = new HashMap<String, List<Field>>();
            Map<String, Field> fieldMap = new HashMap<>();

            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                if (annotation != null) {
                    String value = annotation.value();
//                    if (!classMap.containsKey(value)) {
//                        classMap.put(value, new ArrayList<Field>());
//                    }
                    field.setAccessible(true);
                    fieldMap.put(value, field);
//                    classMap.get(value).add(field);
                }
            }
//            Map<Integer, List<Field>> reflectionMap = new HashMap<>();
            Map<Integer, Field> reflectionMap = new HashMap<>();

            Sheet sheet = workbook.getSheetAt(0);
            // 获取第一列的标题，就是实体模板类中的 ExcelColumn 注解内容
            for (int j = sheet.getRow(0).getFirstCellNum(); j < sheet.getRow(0).getLastCellNum(); j++) { //首行提取注解
                String cellValue = getCellValue(sheet.getRow(0).getCell(j));
//                if (classMap.containsKey(cellValue)) {
//                    reflectionMap.put(j, classMap.get(cellValue));
//                }
                if (!StringUtils.isEmpty(cellValue) && fieldMap.containsKey(cellValue)) {
                    reflectionMap.put(j, fieldMap.get(cellValue));
                }
            }
            Row row = null;
            Cell cell = null;
            // 循环行
            for (int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                T t = cls.newInstance();
                // 循环列
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);

                    if (reflectionMap.containsKey(j)) {
                        String cellValue = getCellValue(cell);
//                        List<Field> fieldList = reflectionMap.get(j);
                        Field field = reflectionMap.get(j);
//                        for (Field field : fieldList) {
                        try {
                            if (cellValue == null) {
                                field.set(t, null);
                            } else {
                                Class<?> type = field.getType();
                                if (type == Integer.class) {
//                                    field.set(t, df.parse(cellValue).intValue());
                                    if (StringUtils.isEmpty(cellValue)) {
                                        field.set(t, 0);
                                    } else {
                                        field.set(t, Integer.parseInt(cellValue));
                                    }
                                } else if (type == Double.class || type == double.class) {
//                                    field.set(t, df.parse(cellValue).doubleValue());
                                    if (StringUtils.isEmpty(cellValue)) {
                                        field.set(t, 0.0);
                                    } else {
                                        field.set(t, Double.parseDouble(cellValue));
                                    }
                                } else if (type == String.class) {
                                    field.set(t, cellValue);
                                } else if (type == Boolean.class) {
                                    if (StringUtils.isEmpty(cellValue)) {
                                        field.set(t, false);
                                    } else {
                                        field.set(t, Boolean.parseBoolean(cellValue));
                                    }
                                } else if (type == Date.class) {
                                    field.set(t, sdf.parse(cellValue));
                                } else {
                                    field.set(t, cellValue);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                dataList.add(t);
            }
//            }
        } catch (Exception e) {
            dataList = null;
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            file = null;
        }
        return dataList;
    }

    /**
     * 获取excel 单元格数据
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {

        if (cell == null) {
//            System.out.println(String.format("Cell为Null，请检查数据！单元格[%d,%d]" ,i,j));
            return null;
        }

        String value = null;
        switch (cell.getCellTypeEnum()) {
            case _NONE:
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { //日期
//                    value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
//                    sdf.setNumberFormat(df);
                    value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    System.out.println(value);
//                    value = sdf.format(cell.getNumericCellValue());
//                    value = String.valueOf(cell.getNumericCellValue());
                } else {
//                    value = String.valueOf(cell.getNumericCellValue());
//                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
//                    nf.setGroupingUsed(false);
                    value = nf.format(cell.getNumericCellValue());
//                    value = String.valueOf(cell.getNumericCellValue()+"");
                }
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                //value = ",";
                break;
            default:
                value = cell.getStringCellValue();
        }
        return value;
    }


    /**
     * 通过List<Map>导出excel
     *
     * @param titleList 标题列表
     * @param keyList   key 列表
     * @param maps      map
     */
    public static void listMapToExcel(List<String> titleList, List<String> keyList, List<Map<String, Object>> maps) {

        //Clients.showBusy("正在生成数据,请稍候...");
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("Sheet1");
        //行号
        int rowIndex = 0;

        //for (int i = 0; i < titleList.size(); i++) {
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(rowIndex);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            //自动适应宽度
            //style.setShrinkToFit(true);
            // 创建一个居中格式
            style.setAlignment(HorizontalAlignment.CENTER);

            //从第1列开始
            HSSFCell cell = row.createCell(0);
            // 设置表头
            int titleIndex = 1;
        for (String title : titleList) {
            if (titleIndex > titleList.size()) {
                    continue;
                }
                cell.setCellValue(title);
                //style.setShrinkToFit(true);
                cell.setCellStyle(style);
                cell = row.createCell(titleIndex);
                titleIndex++;
            }
            rowIndex++;
        //}


        //rowIndex++;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : maps) {
            HSSFRow datarow = sheet.createRow(rowIndex);
            int size = 0;
            for (String key : keyList) {
                if (size > map.size()) {
                    continue;
                }
                datarow.createCell(size).setCellValue(map.get(key) == null ? "" : map.get(key).toString());

                size++;
            }
            rowIndex++;
        }

        //设置自动宽度
        //for (int i = 0; i < titleList.size(); i++) {
        //    sheet.autoSizeColumn(i);
        //
        //}
        //设置合并单元格
        //CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        //sheet.addMergedRegion(region);
        String fileName = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            fileName = UUIDUtil.newUuid() + ".xls";
            wb.write(out);
            Filedownload.save(out.toByteArray(), null, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param auxHeaders
     * @param titleList
     * @param keyList
     * @param dataSet
     * @Author: wushi
     * @description: 通过List<Map>导出excel
     * @Date: Created in 10:17 2019/4/17
     * @Modify by: wushi
     * @ModifyDate by: 10:17 2019/4/17
     */
    public static void listMapToExcel(List<AuxHeaderInfo> auxHeaders, List<String> titleList, List<String> keyList, List<Map<String, Object>> dataSet) {

        //Clients.showBusy("正在生成数据,请稍候...");
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("Sheet1");
        //行号
        int rowIndex = 0;

        if (auxHeaders != null && auxHeaders.size() > 0) {
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(rowIndex);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            //自动适应宽度
            //style.setShrinkToFit(true);
            // 创建一个居中格式
            style.setAlignment(HorizontalAlignment.CENTER);

            //从第1列开始
            HSSFCell cell = row.createCell(0);
            // 设置表头
            //int auxHeaderIndex = 1;
            for (AuxHeaderInfo auxHeaderInfo : auxHeaders) {
                //if (auxHeaderIndex > titleList.size()) {
                //    continue;
                //}
                cell.setCellValue(auxHeaderInfo.getTitle());
                //style.setShrinkToFit(true);
                //设置合并单元格

                cell.setCellStyle(style);
                if (auxHeaderInfo.isMerge()) {
                    CellRangeAddress region = new CellRangeAddress(auxHeaderInfo.getFirstRow(), auxHeaderInfo.getLastRow(), auxHeaderInfo.getFirstCol(), auxHeaderInfo.getLastCol());
                    sheet.addMergedRegion(region);
                }
                cell = row.createCell(auxHeaderInfo.getLastCol() + 1);
                //auxHeaderIndex++;
            }
            rowIndex++;
        }

        //for (int i = 0; i < titleList.size(); i++) {
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(rowIndex);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //自动适应宽度
        //style.setShrinkToFit(true);
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);

        //从第1列开始
        HSSFCell cell = row.createCell(0);
        // 设置表头
        int titleIndex = 1;
        for (String title : titleList) {
            if (titleIndex > titleList.size()) {
                continue;
            }
            cell.setCellValue(title);
            //style.setShrinkToFit(true);
            cell.setCellStyle(style);
            cell = row.createCell(titleIndex);
            titleIndex++;
        }
        rowIndex++;
        //}


        //rowIndex++;
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (Map<String, Object> map : dataSet) {
            HSSFRow datarow = sheet.createRow(rowIndex);
            int size = 0;
            for (String key : keyList) {
                if (size > map.size()) {
                    continue;
                }
                datarow.createCell(size).setCellValue(map.get(key) == null ? "" : map.get(key).toString());

                size++;
            }
            rowIndex++;
        }

        //设置自动宽度
        //for (int i = 0; i < titleList.size(); i++) {
        //    sheet.autoSizeColumn(i);
        //
        //}

        String fileName = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            fileName = UUIDUtil.newUuid() + ".xls";
            wb.write(out);
            Filedownload.save(out.toByteArray(), null, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}