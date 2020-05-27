package com.sunjet.mis.utils.common;

import java.util.UUID;

/**
 * Created by lhj on 16/6/17.
 */
public class UUIDUtil {
    public static String newUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
