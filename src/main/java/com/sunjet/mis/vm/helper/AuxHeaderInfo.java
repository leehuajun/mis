package com.sunjet.mis.vm.helper;

import lombok.*;

/**
 * @Author: wushi
 * @description: 合并居中分类表头
 * @Date: Created in 9:45 2019/4/17
 * @Modify by: wushi
 * @ModifyDate by: 9:45 2019/4/17
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuxHeaderInfo {

    /**
     * 标题
     */
    private String title;

    /**
     * 起始行坐标(0开始)
     */
    private int firstRow = 0;
    /**
     * 结束行坐标(0开始)
     */
    private int lastRow = 0;
    /**
     * 起始列坐标(0开始)
     */
    private int firstCol = 0;
    /**
     * 起始列坐标(0开始)
     */
    private int lastCol = 0;

    private boolean isMerge = false;

}
