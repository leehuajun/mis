package com.sunjet.mis.utils.zk;


import com.sunjet.mis.module.system.entity.MenuEntity;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class MenuTreeUtil {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<MenuEntity> menuEntities) {
        CustomTreeNode<MenuEntity> root = new CustomTreeNode<MenuEntity>(null, new ArrayList<TreeNode<MenuEntity>>());
        for (MenuEntity menu : menuEntities) {
            if (menu.getParent() == null) {    // 表示是根节点
                //if (menuEntity.getIcon().trim().equals(""))
                //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<MenuEntity> node;
                if (isLeaf(menu, menuEntities)) {     // 叶节点
                    node = new CustomTreeNode<MenuEntity>(menu);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<MenuEntity>(menu, new ArrayList<TreeNode<MenuEntity>>());
                }
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);
//                menuEntity.setChildrenCount(childrenCount);
//                if(childrenCount==0) {     // 叶节点
//                    node = new CustomTreeNode<MenuEntity>(menuEntity);
//                    //menuEntity.setIcon("");
//                }else{   // 非 页节点
//                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
//                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(),menuEntity.getOpen());
//                }
                root.add(node);
                getNode(node, menuEntities);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param menuInfo
     * @param menuEntities
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(MenuEntity menuInfo, List<MenuEntity> menuEntities) {

        for (MenuEntity menu : menuEntities) {
            if (menu.getParent() != null) {
                if (menu.getParent().getId().equals(menuInfo.getId())) {
                    return false;
                }
            }
        }

        return true;


    }
    private static void getNode(CustomTreeNode<MenuEntity> parent, List<MenuEntity> menuEntities) {
        MenuEntity parentEntity = (MenuEntity) parent.getData();
        for (MenuEntity menu : menuEntities) {
            if (menu.getParent() != null) {
                menu.setIcon(null);
                if (menu.getParent().getId().equals(parentEntity.getId())) {
                    //if (menuEntity.getIcon().trim().equals(""))
                    //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                    CustomTreeNode<MenuEntity> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                    if (isLeaf(menu, menuEntities)) {
                        child = new CustomTreeNode<MenuEntity>(menu);
                    } else {
                        child = new CustomTreeNode<MenuEntity>(menu, new ArrayList<TreeNode<MenuEntity>>());
                    }
                    parent.add(child);
                    if (!isLeaf(menu, menuEntities))
                        getNode(child, menuEntities);
                }
            }

        }
    }
}
