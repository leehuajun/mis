package com.sunjet.mis.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: lhj
 * @create: 2017-11-24 09:17
 * @description: 说明
 */
@Data
@AllArgsConstructor
public class ResponseStatusInfo implements Serializable {
    public String code;
    public String message;
    public String fullMessage;
}
