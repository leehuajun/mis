package com.sunjet.mis.utils.zk;

import com.sunjet.mis.exception.TabCloseFailureException;
import com.sunjet.mis.exception.TabDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zul.*;

import java.util.*;

//import org.apache.log4j.Logger;

/**
 * @author lhj
 * @create 2015-12-23 下午11:31
 */
@Slf4j
public class ZkTabboxUtil {
    public static final String MAIN_TABPANELS_ID = "#mainTabpanels";
    public static final String MAIN_TABS_ID = "#mainTabs";
    public static final String TOP_DIV_ID = "#mainScreen";

//    private static Logger logger = LoggerUtil.getLogger();


    public enum OverFlowType {
        AUTO("overflow:auto"), SCROLL("overflow:scroll"), HIDDEN("overflow:hidden"), VISIBLE("overflow:visible");
        private final String text;

        private OverFlowType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * @param tabNew
     * @param topPage 当前Page
     * @throws TabDuplicateException 如果已经存在与tabNew的id相同的Tab，会导致该页签被选中，并抛出此异常 捕获此异常直接返回即可，不要继续打开Tabpanel
     *                               <p>
     *                               return 存在，返回true，不存在，返回false
     */
    protected static Boolean newTab(Tab tabNew, Page topPage) throws TabDuplicateException {
//        Assert.notNull(tabNew);
//        Assert.notNull(tabNew.getId());
        Div topDiv = (Div) Selectors.find(topPage, TOP_DIV_ID).get(0);
        Tabs mainTabs = (Tabs) Selectors.find(topDiv, MAIN_TABS_ID).get(0);
        if (mainTabs.getChildren() != null || mainTabs.getChildren().size() > 0) {
            for (Component comp : mainTabs.getChildren()) {
                Tab tab = (Tab) comp;

//                logger.info("已存在的页签【" + tab.getId() + "|" + tab.getLabel() + "】");
                if (tab.getId().equals(tabNew.getId())) {
//                    log.warn("打开已存在页签【" + tabNew.getId() + "|" + tab.getLabel() + "】！");
                    tab.setSelected(true);
                    return true;
//                    throw new TabDuplicateException(errorMsg);
//                    return;
                }
            }
        }
        tabNew.setSelected(true);
        mainTabs.appendChild(tabNew);
//        if(mainTabs.getChildren().size()>10){
//            String message = String.format("当前打开的tab窗体数量：%d，请关闭不必要的窗体，谢谢！",mainTabs.getChildren().size());
//            ZkUtils.showExclamation(message,"系统警告!");
//        }
        return false;
    }

    protected static void newTabpanel(Tabpanel tabpanel, Page topPage) {
        Div topDiv = (Div) Selectors.find(topPage, TOP_DIV_ID).get(0);
        Tabpanels mainTabpanels = (Tabpanels) Selectors.find(topDiv, MAIN_TABPANELS_ID).get(0);
//        log.info("创建新tabpanel,ID为: " + tabpanel.getId() + "  索引为: " + tabpanel.getIndex());
        mainTabpanels.appendChild(tabpanel);
    }

//    protected static Boolean checkTabExist(Tab tabNew, Page topPage) {
//        // 如果 tabNew 为 null, 或者 tabNew 的 id 为 null 或 "" ,则返回 false
//        if (tabNew == null || (tabNew.getId() == null || tabNew.getId().toString().equals(""))) {
//            return false;
//        }
//
//        Div topDiv = (Div) Selectors.find(topPage, TOP_DIV_ID).get(0);
//        Tabs mainTabs = (Tabs) Selectors.find(topDiv, MAIN_TABS_ID).get(0);
//        if (mainTabs.getChildren() != null || mainTabs.getChildren().size() > 0) {
//            for (Component comp : mainTabs.getChildren()) {
//                Tab tab = (Tab) comp;
//                logger.debug("已存在的页签【" + tab.getId() + "|" + tab.getLabel() + "】");
//                if (tab.getId().equals(tabNew.getId())) {
//                    String errorMsg = "打开已存在页签【" + tabNew.getId() + "|" + tab.getLabel() + "】！";
//                    logger.warn(errorMsg);
//                    tab.setSelected(true);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 打开一个新页签
     *
     * @param tabId
     * @param tabLable
     * @param closeable
     * @param overFlowType
     * @param tabPanelUrl
     * @param map
     * @return
     * @throws TabDuplicateException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static BaseTabpanel newTab(String tabId, String tabLable, String image, boolean closeable,
                                      OverFlowType overFlowType, String tabPanelUrl, Map map) throws TabDuplicateException {
        Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
        Tab tabNew = new Tab();
        tabNew.setId("tab_" + tabId);
//        if (StringUtils.isNotEmpty(image)) {
//            tabNew.setImage("images/menu/" + Config.THEME_NAME + "/" + image);
//        }
        tabNew.setImage("");
//        tabNew.setIconSclass(image);
//        tabNew.setImage("images/menu/" + Config.THEME_NAME + "/" + image);
        tabNew.setClosable(closeable);

//        tabNew.setStyle("height:" + TAB_HEIGHT + ";margin-right:" + TAB_MARGIN + ";padding-top:" + TAB_PADDING_TOP);
        tabNew.setLabel(tabLable);
        /* 创建一个 tab 标签,并添加到 tabs 里面, 设置为 当前选择项 */
        // 检查是否存在 同样 的tabNew,如果存在,则激活它,然后返回.
//        if(checkTabExist(tabNew,currentPage)){
//            return null;
//        }
        Boolean isExists = newTab(tabNew, currentPage);

        if (isExists)
            return null;


        //因部分zul url需要传递参数，故构建TabPanel时需要将url后的参数截断
        if (map == null) {
            map = new HashMap();
        }
        int indexOfMark = tabPanelUrl.indexOf("?");
        if (indexOfMark > -1) {
            String paramString = tabPanelUrl.substring(indexOfMark + 1);
            tabPanelUrl = tabPanelUrl.substring(0, indexOfMark);

            String[] params = paramString.split("&");
            for (String param : params) {
                String[] paramSplit = param.split("=");
//                log.info("param标识符为【" + paramSplit[0] + "】，param值为【" + paramSplit[1] + "】");
                map.put(paramSplit[0], paramSplit[1]);
            }

//            String[] paramSplit = paramString.split("=");
//            logger.debug("param标识符为【" + paramSplit[0] + "】，param值为【" + paramSplit[1] + "】");
//            map.put(paramSplit[0], paramSplit[1]);
        }

//        System.out.println("导向的页面为: " + tabPanelUrl);
        BaseTabpanel tabpanel = new BaseTabpanel(tabPanelUrl, tabNew);;
//        try {
//            tabpanel = new BaseTabpanel(tabPanelUrl, tabNew);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            tabpanel = new BaseTabpanel("/sorry.zul", tabNew);
//        }
//        tabpanel.setId(tabNew.getId());
        tabpanel.setTab(tabNew);
        tabpanel.setId("tabpanel_" + tabId);
//        tabpanel.setStyle("overflow:hidden;padding:0px 0px;margin:0px 0px;border:0px");
        newTabpanel(tabpanel, currentPage);

//        tabpanel.render(map);

        try {
//            tabpanel = new BaseTabpanel(tabPanelUrl, tabNew);
            tabpanel.render(map);
        } catch (Exception e) {
            e.printStackTrace();
//            log.error(e.getMessage());
//            tabpanel = new BaseTabpanel("/sorry.zul", tabNew);
            tabpanel.setUrl("/sorry.zul");
            tabpanel.render(map);
        }

        return tabpanel;
    }

    public static BaseTabpanel newTab(String tabId, String tabLable, boolean closeable, OverFlowType overFlowType,
                                      String tabPanelUrl, Map map) throws TabDuplicateException {
        return newTab(tabId, tabLable, null, closeable, overFlowType, tabPanelUrl, map);
    }

    /**
     * 关闭Tab页签及其Tabpanel
     *
     * @param tabpanelDiv 要关闭的Tab对应Tabpanel对应的页面的顶层Div
     * @throws TabCloseFailureException
     */
    public static void closeTab(Div tabpanelDiv) throws TabCloseFailureException {
//        Assert.notNull(tabpanelDiv);
        Component tabpanelComp = tabpanelDiv.getParent();
        if (!BaseTabpanel.class.isAssignableFrom(tabpanelComp.getClass())) {
            String errorMsg = "触发关闭页签的组件所在容器【" + tabpanelComp.getClass() + "】非法，应该为" + BaseTabpanel.class;
            log.error(errorMsg);
            throw new TabCloseFailureException(errorMsg);
        }
        BaseTabpanel tabpanelToClose = (BaseTabpanel) tabpanelComp;
        Tab tabToClose = tabpanelToClose.getTab();
//        log.debug("正在关闭页签【" + tabToClose.getId() + "|" + tabToClose.getLabel() + "】");
        int tabIndex = tabToClose.getIndex();
        Tabs tabs = (Tabs) tabToClose.getParent();
        Tab nextTab = null;
        if (tabs.getLastChild() != tabToClose) {// 非最后的页签，激活右边下一个页签
            nextTab = (Tab) tabs.getChildren().get(tabIndex + 1);
        } else {
            if (tabs.getChildren().size() > 1) {// 是最后一个页签且左边有页签
                nextTab = (Tab) tabs.getChildren().get(tabIndex - 1);
            }
        }
        if (nextTab != null) {
            nextTab.setSelected(true);
        }
        tabpanelDiv.detach();
        tabToClose.detach();
        tabpanelToClose.detach();
    }

    public static void closeOne(Tab tabNow) {
        if (!tabNow.getId().equalsIgnoreCase("tab_portal")) {
            tabNow.close();
        }

    }

    /**
     * 关闭其他
     *
     * @param tabList
     * @param tabNow
     */
    public static void closeOther(List<Tab> tabList, Tab tabNow) {
        List<Tab> tabs = new ArrayList<>();
        for (Tab tab : tabList) {
            if (!tab.getId().equalsIgnoreCase("tab_portal")
                    && !tab.getId().equalsIgnoreCase(tabNow.getId())) {
                tabs.add(tab);
            }
        }
        for (Tab tab : tabs) {
            tab.close();
        }
    }


    /**
     * 关闭指定页签
     *
     * @param tabList 当前打开的所有页签
     * @param objId   单据Id
     */
    public static void closeOneByObjId(List<Tab> tabList, String objId) {
        Iterator<Tab> iterator = tabList.iterator();

        while (iterator.hasNext()) {
            Tab tab = iterator.next();
            String[] split = tab.getId().split("_");
            if (split[1].equals(objId)) {
                ZkTabboxUtil.closeOne(tab);
            }
        }

    }

    public static void closeAll(List<Tab> tabList) {
        List<Tab> tabs = new ArrayList<>();
        for (Tab tab : tabList) {
            if (!tab.getId().equalsIgnoreCase("tab_portal")) {
                tabs.add(tab);
            }
        }
        for (Tab tab : tabs) {
            tab.close();
        }
    }
}
