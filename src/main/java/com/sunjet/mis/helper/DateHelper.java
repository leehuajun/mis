package com.sunjet.mis.helper;

import org.zkoss.zhtml.S;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lhj on 16/9/16.
 */
public class DateHelper {
    public static Date getFirstOfMonth() {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();

//        LocalDate localDate = LocalDate.now();
    }

    public static Date getFirstOfYear() {
        //获取当前年1月1日
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getEndDateOfYear() {
        //获取当前年1月1日
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DATE, 31);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getEndDateTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static Date getEndDateTime(Date endDate) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static Date getStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    public static Date getEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    public static String getTimeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
        return sdf.format(date);
    }

    public static String getTime(Date date) {
        return "";
    }


    public static String dateToString(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (time != null) {
            String ctime = formatter.format(time);
            return ctime;
        }
        return "";

    }

    //获取下下个月的10号
    public static Date nextMonthTenthDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        calendar.add(Calendar.MONTH, 2);
        return calendar.getTime();
    }

    public static Date getFirstOfNextMonth() {


        //获取当前年1月1日
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.SECOND, 0);

//        c.set(Calendar.MONTH, 0);
//        c.set(Calendar.DATE, 1);
//        c.set(Calendar.HOUR, 0);
//        c.set(Calendar.MINUTE, 0);
//        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getFirstDayOfThisMonth(){
        LocalDate localDate = LocalDate.now();
//        System.out.println(localDate.getYear());
//        System.out.println(localDate.getMonthValue());
        LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), 1, 0, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    public static Date getFirstDayOfNextMonth(){
        LocalDate localDate = LocalDate.now().plusMonths(1);
//        System.out.println(localDate.getYear());
//        System.out.println(localDate.getMonthValue());
        LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), 1, 0, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    public static Date getFirstDayByYeanAndMonth(Integer year,Integer month){
        LocalDateTime localDateTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    public static Date getFirstDayOfNextMonthByYeanAndMonth(Integer year,Integer month){
        LocalDate localDate = LocalDate.of(year, month, 1);
        LocalDate localDate1 = localDate.plusMonths(1);
        LocalDateTime ldt= LocalDateTime.of(localDate1.getYear(), localDate1.getMonthValue(), 1, 0, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = ldt.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

}
