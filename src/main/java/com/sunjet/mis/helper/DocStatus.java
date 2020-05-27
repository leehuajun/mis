package com.sunjet.mis.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/10/19.
 */
public enum DocStatus {
    ALL("全部", 10),
    DRAFT("草稿", 0),
    AUDITING("审核中", 1),
    AUDITED("已审核", 2),
    CLOSED("已关闭", 3),
    WITHDRAW("已撤回", 4),
    REJECT("已退回", -1),
    SUSPEND("已中止", -2),
    OBSOLETE("已作废", -3),
    WAITING_SETTLE("待结算", 1000),
    SETTLING("结算中", 1001),
    SETTLED("已结算", 1002);


    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private DocStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (DocStatus c : DocStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static List<DocStatus> getListNotAll() {
        List<DocStatus> list = new ArrayList<>();
        for (DocStatus c : DocStatus.values()) {
            if (c != DocStatus.ALL && c != DocStatus.OBSOLETE)
                list.add(c);
        }
        return list;
    }

    public static List<DocStatus> getListWithAll() {
        List<DocStatus> list = new ArrayList<>();
        for (DocStatus c : DocStatus.values()) {
            if (c != DocStatus.OBSOLETE)
                list.add(c);
        }
        return list;
    }

    public static List<DocStatus> getListNotAllAndSettlement() {
        List<DocStatus> list = new ArrayList<>();
        for (DocStatus c : DocStatus.values()) {
            if (c != DocStatus.ALL && c != DocStatus.OBSOLETE && c != DocStatus.WAITING_SETTLE
                    && c != DocStatus.SETTLING && c != DocStatus.SETTLED)
                list.add(c);
        }
        return list;
    }

    public static List<DocStatus> getListWithAllNotSettlement() {
        List<DocStatus> list = new ArrayList<>();
        for (DocStatus c : DocStatus.values()) {
            if (c != DocStatus.OBSOLETE && c != DocStatus.WAITING_SETTLE
                    && c != DocStatus.SETTLING && c != DocStatus.SETTLED)
                list.add(c);
        }
        return list;
    }

    public static List<DocStatus> getListSettlementStatus() {
        List<DocStatus> list = new ArrayList<>();
        for (DocStatus c : DocStatus.values()) {
            if (c == DocStatus.ALL || c == DocStatus.WAITING_SETTLE || c == DocStatus.SETTLING || c == DocStatus.SETTLED)
                list.add(c);
        }
        return list;
    }

    public static DocStatus getDocStatus(int idx) {
        for (DocStatus c : DocStatus.values()) {
            if (c.getIndex() == idx) {
                return c;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "DocStatus{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}
