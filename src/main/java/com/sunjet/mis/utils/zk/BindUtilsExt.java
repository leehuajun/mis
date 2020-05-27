package com.sunjet.mis.utils.zk;

import org.zkoss.bind.BindUtils;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-10-18 17:41
 * @description: 说明
 */
public class BindUtilsExt {
    public static void postNotifyChange(String queueName, String queueScope, Object bean, List<String> properties) {
        for (String property : properties) {
            BindUtils.postNotifyChange(queueName, queueScope, bean, property);
        }
    }

    public static void postNotifyChange(String queueName, String queueScope, Object bean, String property) {
        BindUtils.postNotifyChange(queueName, queueScope, bean, property);
    }
}
