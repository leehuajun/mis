package com.sunjet.mis.vm.helper;

import lombok.*;

/**
 * @Author: wushi
 * @description: 自定义表头
 * @Date: Created in 15:55 2019/4/12
 * @Modify by: wushi
 * @ModifyDate by: 15:55 2019/4/12
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableHeaderInfo {

    /**
     * 表头名称
     */
    private String label;
    /**
     * 宽度 单位px
     */
    private String width;
    /**
     * 对齐方式(center)
     */
    private String align = "center";

    private String hflex;

}
