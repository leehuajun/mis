package com.sunjet.mis.utils.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/8/18.
 */
public class BeanUtils {


    /**
     * Bean转Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = null;
                    //如果是时间格式转换格式
                    if ("Date".equals(getter.getReturnType().getSimpleName())) {
                        Date date = (Date) getter.invoke(obj);
                        if (date != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
                            value = sdf.format(date);
                        } else {
                            value = getter.invoke(obj);
                        }
                    } else {
                        value = getter.invoke(obj);
                    }


                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            //  System.out.println("transBean2Map Error " + e);
        }

        return map;

    }


}
