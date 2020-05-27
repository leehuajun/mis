package com.sunjet.mis.utils.common;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;
import sun.nio.cs.ext.GBK;

public class CSVUtil {
    /**
     * 读取csv文件
     *
     * @param filePath 文件路径
     * @param headers  csv列头
     * @return CSVRecord 列表
     * @throws IOException
     **/
    public static List<CSVRecord> readCSV(String filePath, String[] headers) throws Exception {

        System.out.println(EncodingDetect.getJavaEncodeFile(filePath));
        DataInputStream in = new DataInputStream(new FileInputStream(new File(filePath)));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, EncodingDetect.getJavaEncodeFile(filePath)));

//        String fileEncode=EncodingDetect.getJavaEncodeFile(filePath);
//        String fileContent=FileUtils.readFileToString(new File(filePath),fileEncode);

//        File file = new File(filePath);
//        String fileEncode=EncodingDetect.getJavaEncode(filePath);
//        DataInputStream inputStream = new DataInputStream(new FileInputStream(filePath));
//        FileInputStream inputStream = new FileInputStream(filePath);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),fileEncode));
//
////        Reader in = new FileReader(filePath);
//        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(bufferedReader);
//        for (CSVRecord record : records) {
//            System.out.println(record.toString());
////            String lastName = record.get("Last Name");
////            String firstName = record.get("First Name");
//        }
//        return ((CSVParser) records).getRecords();

        //创建CSVFormat
        CSVFormat formator = CSVFormat.EXCEL.withHeader(headers).withQuoteMode(null);

        FileReader fileReader = new FileReader(filePath);
        //创建CSVParser对象
        CSVParser parser = new CSVParser(bufferedReader, formator);

        List<CSVRecord> records = parser.getRecords();

        parser.close();
        fileReader.close();
        bufferedReader.close();
        in.close();

        return records;


//        Reader in = new FileReader("path/to/file.csv");
//        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
//        ((CSVParser) records).getRecords().get(0).size();
//        for (CSVRecord record : records) {
//            String lastName = record.get("Last Name");
//            String firstName = record.get("First Name");
//        }
//
//        return null;
    }

//    public static String resolveCode(String path) throws Exception {
//        InputStream inputStream = new FileInputStream(path);
//        byte[] head = new byte[3];
//        inputStream.read(head);
//        String code = "gb2312";  //或GBK
//        if (head[0] == -1 && head[1] == -2)
//            code = "UTF-16";
//        else if (head[0] == -2 && head[1] == -1)
//            code = "Unicode";
//        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
//            code = "UTF-8";
//
//        inputStream.close();
//
//        System.out.println(code);
//        return code;
//    }

    /**
     * <div>
     * 利用第三方开源包cpdetector获取文件编码格式.<br/>
     * --1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来,
     * 如:ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector. <br/>
     * --2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则. <br/>
     * --3、cpDetector是基于统计学原理的,不保证完全正确.<br/>
     * </div>
     *
     * @param filePath
     * @return 返回文件编码类型：GBK、UTF-8、UTF-16BE、ISO_8859_1
     * @throws Exception
     */
//    public static String getFileCharset(String filePath) throws Exception {
//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//        /*ParsingDetector可用于检查HTML、XML等文件或字符流的编码,
//         * 构造方法中的参数用于指示是否显示探测过程的详细信息，为false不显示。
//         */
//        detector.add(new ParsingDetector(false));
//        /*JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码测定。
//         * 所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以再多加几个探测器，
//         * 比如下面的ASCIIDetector、UnicodeDetector等。
//         */
//        detector.add(JChardetFacade.getInstance());
//        detector.add(ASCIIDetector.getInstance());
//        detector.add(UnicodeDetector.getInstance());
//        Charset charset = null;
//        File file = new File(filePath);
//        try {
//            //charset = detector.detectCodepage(file.toURI().toURL());
//            InputStream is = new BufferedInputStream(new FileInputStream(filePath));
//            charset = detector.detectCodepage(is, 8);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//        String charsetName = "GBK";
//        if (charset != null) {
//            if (charset.name().equals("US-ASCII")) {
//                charsetName = "ISO_8859_1";
//            } else if (charset.name().startsWith("UTF")) {
//                charsetName = charset.name();// 例如:UTF-8,UTF-16BE.
//            }
//        }
//        return charsetName;
//    }
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


}
