package com.sunjet.mis.base.vm;

import com.sunjet.mis.exception.TabDuplicateException;
import com.sunjet.mis.helper.CommonHelper;
import com.sunjet.mis.helper.DocStatus;
import com.sunjet.mis.utils.dto.Order;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import com.sunjet.mis.utils.zk.ZkTabboxUtil;
import com.sunjet.mis.utils.zk.ZkUtils;
import com.sunjet.mis.vm.helper.OpenMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wfb on 17-7-24.
 * VM 基类：
 * 1.封装分页参数
 * 2.封装新增/修改请求路径
 * 3.封装用户权限
 * <p>
 * 2018-10-27 lhj ListVM 列表基类只保留一些列表窗体使用的属性，不能包含业务逻辑
 */
public class ListVM<T> extends BaseVM {
    /**
     * 页码
     */
    @Getter
    @Setter
    private Integer pageSize = 50;
    /**
     * 分页参数
     */
    @Getter
    @Setter
    private PageParam<T> pageParam = new PageParam<T>();
    /**
     * 分页返回
     */
    @Getter
    @Setter
    private PageResult<T> pageResult;
    /**
     * 新增/修改页面请求地址
     */
    @Getter
    @Setter
    private String formUrl;

    /**
     * 流程定义名称
     */
    @Getter
    @Setter
    private String entityName;

    /**
     * 定义一个 Window 对话框
     */
    @Getter
    @Setter
    private Window dialog;

    /**
     * 所有单据状态List，主要用于List页面查询中，供用户选择状态
     */
    @Getter
    @Setter
    private List<DocStatus> documentStatuses = DocStatus.getListWithAll();   //服务单状态
    /**
     * 所有List页面查询的单据状态为ALL，就是所有
     */
    @Getter
    @Setter
    protected DocStatus selectedStatus = DocStatus.ALL;    //单据全选状态

    @Getter
    @Setter
    protected OpenMode mode = OpenMode.TAB;

    @Getter
    @Setter
    private Boolean readonly = false;

    @Getter
    @Setter
    private String keyword;
    @Getter
    @Setter
    private Integer searchFormHeight = 0;

    /**
     * 根据状态索引，获取状态的名称
     *
     * @param index
     * @return
     */
    public String getStatusName(Integer index) {
        if (index != null) {
            return DocStatus.getName(index);
        }
        return null;
    }

    /**
     * 刷新--当前页
     *
     * @param item 实体对象
     */
    public void refreshPage(T item) {
        //当前页
        pageParam.setPage(pageResult.getPage());
        pageParam.setPageSize(calculatePageSize());
//        pageParam.setPageSize(50);
        //实体参数
        pageParam.setInfoWhere(item);
    }

    /**
     * 刷新--当前页
     *
     * @param item     实体对象
     * @param order    type: asc desc
     * @param orderKey 排序字段
     */
    public void refreshPage(T item, Order order, String orderKey) {
        //当前页
        pageParam.setPage(pageResult.getPage());
        pageParam.setPageSize(calculatePageSize());
//        pageParam.setPageSize(50);
        //排序
        setSort(order, orderKey);
        //实体参数
        pageParam.setInfoWhere(item);
    }

    /**
     * 刷新--首页
     *
     * @param view 实体对象
     */
    public void refreshFirstPage(T view) {
        //当前页
        pageParam = new PageParam<>();
        pageParam.setPageSize(calculatePageSize());
//        pageParam.setPageSize(50);
        //实体参数
        pageParam.setInfoWhere(view);
    }

    /**
     * 刷新--首页
     *
     * @param view     实体对象
     * @param order    type: asc desc
     * @param orderKey 排序字段
     */
    public void refreshFirstPage(T view, Order order, String orderKey) {
        //当前页
        pageParam = new PageParam<>();

        pageParam.setPageSize(calculatePageSize());
//        pageParam.setPageSize(50);
        //排序
        setSort(order, orderKey);
        //实体参数
        pageParam.setInfoWhere(view);
    }


    protected void setSort(Order order, String orderKey) {
        if (StringUtils.isNotBlank(orderKey) && StringUtils.isNotBlank(orderKey)) {
            pageParam.setOrder(order.toString());
            pageParam.setOrderName(orderKey);
        }
    }



    /**
     * 弹窗
     *
     * @param id
     * @param url
     * @param title
     */
    @Command
    public void openForm(@BindingParam("id") String id, @BindingParam("url") String url, @BindingParam("title") String title, @BindingParam("readonly") Boolean readonly) {
        this.readonly = readonly;
        if (mode == OpenMode.TAB) {
            openTab(id, url, title);
        } else {
            openDialog(id);
        }
    }


    public void openTab(String id, String url, String title) {
        Map<String, Object> paramMap = new HashMap<>();
        if (id != null) {
            paramMap.put("id", id);
        }
        try {
//            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title + "-" + (objId == null ? "新增" : "编辑"), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            ZkTabboxUtil.newTab(id == null ? URLEncoder.encode(title, "UTF-8") : id, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 流程定义窗口
     *
     * @param name
     * @param url
     */
    @Command
    public void openFlowDefineForm(@BindingParam("name") String name, @BindingParam("url") String url) {


//        if (url == null || url == "") {
//            ZkUtils.showError("URL为空！", "系统异常");
//            return;
//        }

        if (StringUtils.isBlank(name) || StringUtils.isBlank(url)) {
            ZkUtils.showError("系统错误，请联系管理员", "系统异常");
            return;
        }

        Map<String, Object> paramMap = new HashMap<>();
        if (name != null) {
            paramMap.put("name", name);
            paramMap.put("url", url);
        }

        try {
//            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title + "-" + (objId == null ? "新增" : "编辑"), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
//            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            ZkTabboxUtil.newTab(name, "流程定义", "", true, ZkTabboxUtil.OverFlowType.AUTO, "/views/flow/flow_define.zul", paramMap);

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 状态颜色
     *
     * @param colorIdx
     * @return
     */
    public String getColor(Integer colorIdx) {
        if (colorIdx == null) {
            return "";
        } else if (colorIdx == DocStatus.WAITING_SETTLE.getIndex()) {
            return "color:#F2DA0C;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLING.getIndex()) {
            return "color:#D47616 ;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLED.getIndex()) {
            return "color:#7BC144;font-weight:700";
        } else if (colorIdx == DocStatus.DRAFT.getIndex()) {
            return "color:#C7DD0E;font-weight:700";
        } else if (colorIdx == DocStatus.AUDITING.getIndex()) {
            return "color:#FF9900;font-weight:700";
        } else if (colorIdx == DocStatus.CLOSED.getIndex()) {
            return "color:#cccccc;font-weight:700";
        } else if (colorIdx == DocStatus.REJECT.getIndex()) {
            return "color:#FF3333;font-weight:700";
        } else if (colorIdx == DocStatus.WITHDRAW.getIndex()) {
            return "color:#FF3333;font-weight:700";
        } else if (colorIdx == DocStatus.SUSPEND.getIndex()) {
            return "color:#B20825;font-weight:700";
        } else if (colorIdx == DocStatus.OBSOLETE.getIndex()) {
            return "color:#999999;font-weight:700";
        } else {
            return "";
        }
    }


    private int calculatePageSize() {
        int h_navbar = 40;
        int h_tabs = 32;
        int h_toolbar = 41 + 10;
        int h_grid_header = 30;
        int h_grid_paging = 22;
        int h_border_bottom = 10;
        int rows = (CommonHelper.windowHeight - h_navbar - h_tabs - h_toolbar - h_grid_header - h_grid_paging - h_border_bottom - searchFormHeight) / 27;
        int pageSize = 10;
        for (int i = rows; i >= 0; i--) {
            if (i % 5 == 0) {
                pageSize = i;
                break;
            }
        }
        if (pageSize == 0) {
            pageSize = 5;
        }
//
//        if (pageSize < 10) {
//            pageSize = 10;
//        } else {
//            double v = pageSize / 10.0;
//            pageSize = (int) Math.floor(v);
//        }
        // 返回页面能显示的最大值
        return (int) Math.floor(rows) - 1;
        // 返回页面能显示的能被5整除的最大值（如果为0，则返回5）
//        return pageSize;

//        return 30;
    }

    // 下面是子类需要继承实现的方法

    @Command
    protected void simpleSearch(){

    }

    protected void openDialog(String id) {
    }

    @Command
    protected void showSearchDialog() {
    }

    @Command
    protected void refreshList() {
    }


    @Command
    public void export(@BindingParam("type") String type) {
        ZkUtils.showInformation("功能待开发", "提示");
    }


    // 上面是子类需要继承实现的方法
}
