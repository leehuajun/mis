package com.sunjet.mis.exception;

/**
 * 出现此异常，意味着用户试图打开一个id已经存在的tab页签
 * 无需处理此异常，直接返回即可，不要继续打开tabpanel
 */
@SuppressWarnings("serial")
public class TabDuplicateException extends Exception {

    public TabDuplicateException(String errorMsg) {
        super(errorMsg);
    }

}
