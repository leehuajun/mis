package com.sunjet.mis.utils.zk;

import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:19
 */
public class CustomTreeNode<T> extends DefaultTreeNode {
    private static final long serialVersionUID = -7012663776755277499L;

    public CustomTreeNode(T data, List<TreeNode<T>> children) {
        super(data, children);
    }


    public CustomTreeNode(T data) {
        super(data);

    }

}