package com.sunjet.mis.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/10/19.
 */
public enum UserType {
    INTERNAL("内部用户", 0),
    DISTRIBUTOR("经销商", 1);


    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private UserType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (UserType c : UserType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static List<UserType> getList() {
        List<UserType> list = new ArrayList<>();
        for (UserType c : UserType.values()) {
                list.add(c);
        }
        return list;
    }

    public static UserType getUserType(int idx) {
        for (UserType c : UserType.values()) {
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
        return "UserType{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }}
