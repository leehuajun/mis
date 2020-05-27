package com.sunjet.mis.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wfb on 17-5-4.
 * 查询分页返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private List<T> rows;//数据集合
    private long total;//共几条数据
    private int page;//第几页
    private int pageSize;//页显示几行数据

}
