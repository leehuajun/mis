package com.sunjet.mis.utils.zk;


import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

import java.util.Map;

@SuppressWarnings("serial")
public class BaseTabpanel extends Tabpanel {
    private String url;
    private Tab tab;

    public BaseTabpanel(String url, Tab tab) {
        super();
        this.url = url;
        this.tab = tab;
    }

    @SuppressWarnings("rawtypes")
    public void render(Map map) {
        Executions.createComponents(url, this, map);
    }

    @SuppressWarnings("rawtypes")
    public void render() {
        Executions.createComponents(url, this, null);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

}
