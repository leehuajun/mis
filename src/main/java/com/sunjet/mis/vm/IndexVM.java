package com.sunjet.mis.vm;

import com.sunjet.mis.base.vm.BaseVM;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.module.system.entity.MenuEntity;
import com.sunjet.mis.module.system.service.MenuService;
import com.sunjet.mis.utils.zk.CustomTreeNode;
import com.sunjet.mis.utils.zk.MenuTreeUtil;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.theme.Themes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
public class IndexVM extends BaseVM {

    @WireVariable
    private MenuService menuService;

    @Getter
    @Setter
    private TreeModel treeModel;

    @Getter
    @Setter
    private String now;

    @Init
    public void init() throws IOException {

        // 设置 主题样式 为 iceblue_c 紧凑型的 iceblue(这是默认的主题）
        Themes.setTheme(Executions.getCurrent(), "iceblue_c");
//        String theme = Themes.getCurrentTheme();
//        boolean isNull = Sessions.getCurrent().hasAttribute("currentTheme");
//        if ((!theme.equals("iceblue_c")) && (!isNull)) {
//            Themes.setTheme(Executions.getCurrent(), "iceblue_c");
//            Executions.sendRedirect(null);
//        }

        System.out.println(this.hasPermission("user:search"));
//        List<MenuEntity> menus = menuService.findAll();
        List<MenuEntity> menus = this.getActiveUser().getMenus();
        System.out.println(menus.size());

//        if(menus!=null) {
//            menus.forEach(menuInfo -> {
//                if (menuInfo.getParent() == null) {
//                    mapMenuIcon.put(menuInfo.getObjId(), "z-icon-chevron-down");
//                    mapMenuStatus.put(menuInfo.getObjId(), false);
//                }
//            });
//
            this.treeModel = new DefaultTreeModel(MenuTreeUtil.getRoot(menus));
//        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws InterruptedException {
        Selectors.wireComponents(view, this, false);
//        view.getPage().setStyle("background-color:rgb(78,116,149)");
        showIndex();
    }

    /**
     * 显示首页
     */
    @Command
    public void showIndex() {
        try {
//            ZkTabboxUtil.newTab("welcome", "欢迎", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/welcome.zul", null);
            ZkTabboxUtil.newTab("portal", "首页", "z-icon-home", false, ZkTabboxUtil.OverFlowType.AUTO, "/portal.zul", null);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开标签
     *
     * @param e
     */
    @NotifyChange({"mapMenuStatus", "mapMenuIcon"})
    @Command
    public void openTab(@BindingParam("e") Event e) {
        Treeitem treeitem = (Treeitem) e.getTarget();
//        System.out.println("树节点状态：" + treeitem.isOpen());
//        通过treeitem.getValue()方法也可以获得TreeNode对象
        CustomTreeNode node = treeitem.getValue();

//        System.out.println("子节点个数:" + node.getChildCount());


        // 更改导航树的图标样式
        if (node.getChildCount() > 0) {
            if (treeitem.isOpen()) {
                treeitem.setOpen(false);
            } else {
                Collection<Treeitem> items = treeitem.getTree().getItems();

                items.forEach(t->t.setOpen(false));

                treeitem.setOpen(true);
            }
        } else {
            MenuEntity menuInfo = (MenuEntity) node.getData();
            try {
                String url = (menuInfo.getUrl() == null || menuInfo.getUrl().trim().equalsIgnoreCase("null") || menuInfo.getUrl().trim().equals("")) ?
                        "/sorry.zul"
                        : menuInfo.getUrl();
                if (url.contains("http://")) {
                    ZkUtils.sendRedirect(url, "_blank");
                    return;
                }
                String iconSclass = menuInfo.getIcon();
                ZkTabboxUtil.newTab(menuInfo.getId(), menuInfo.getName(), iconSclass, true, ZkTabboxUtil.OverFlowType.AUTO, url, null);
            } catch (TabDuplicateException ex) {
                ex.printStackTrace();
            }
        }
    }

//    @Command
//    public void test(@BindingParam("data") Object obj){
//        CustomTreeNode treeNode = (CustomTreeNode) obj;
//        System.out.println(treeNode.getChildCount());
//    }

    @NotifyChange("now")
    @Command
    public void renewal() {
//        LocalDateTime localDateTime = LocalDateTime.now().get
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = sdf.format(new Date());
        log.info("用户Session续约, 当前时间: " + sdf.format(new Date()));
    }

    /**
     * 获取客户端信息
     *
     * @param event
     */
    @Command
    public void showClientInfo(@BindingParam("evt") ClientInfoEvent event) {
        CommonHelper.windowHeight = event.getDesktopHeight();
        CommonHelper.windowWidth = event.getDesktopWidth();
        CommonHelper.screenHeight = event.getScreenHeight();
        // baseGridHeight，用于列表视图，windowHeight为视窗内的高度
        // 高度为：windowHeight - navbar的高度 - tab的高度 - 分页的高度 - 各个padding的高度
        CommonHelper.baseGridHeight = CommonHelper.windowHeight - 234 - 81;
        log.info(String.format("Desktop: width:%s x height:%s", event.getDesktopWidth(), event.getDesktopHeight()));
        log.info(String.format("Screen: width:%s x height:%s", event.getScreenWidth(), event.getScreenHeight()));
    }

    @Command
    public void closeOne(@BindingParam("tabNow") Tab tabNow) {
        ZkTabboxUtil.closeOne(tabNow);
    }

    @Command
    public void closeAll(@BindingParam("tabs") List<Tab> tabList) {
        ZkTabboxUtil.closeAll(tabList);
    }

    @Command
    public void closeOther(@BindingParam("tabs") List<Tab> tabList,@BindingParam("tabNow") Tab tabNow) {
        ZkTabboxUtil.closeOther(tabList,tabNow);
    }

    /**
     * 退出登录
     */
    @Command
    public void logout() {
        Executions.sendRedirect("/logout.zul");
    }

}
