package com.sunjet.mis.vm;

import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zul.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class PortalVM {

    @Getter
    private List<CustomButton> buttons = new ArrayList<>();

    private int no = 0;

    @Init
    public void init() throws IOException {
//        Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
//        toolbar = (Toolbar) Selectors.find(currentPage, "#toolbar").get(0);

        buttons = Arrays.asList(
                CustomButton.builder().name("Button01").build(),
                CustomButton.builder().name("Button02").build(),
                CustomButton.builder().name("Button03").build()
        );

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


    @Command
    public void changeToolbarButtons(@BindingParam("comp") Toolbar toolbar) {

//        List<Component> children = toolbar.getChildren();
        toolbar.getChildren().clear();
//        if(children.size()>0) {
//            for (Component child : children) {
//                if(child instanceof Toolbarbutton) {
//                    toolbar.removeChild(child);
//                }
//            }
//        }
        int idx = no;
        for(int i=idx;i<idx+3;i++){
            Button btn = new Button();
            btn.setId("button0" + i);
            btn.setLabel("button0" + i);
            btn.setSclass("btn-default");
            btn.setStyle("margin-right:10px");
//            btn.setAttribute("style", "margin-right:10px");
            btn.addEventListener("onClick", new EventListener<Event>() {
                @Override
                public void onEvent(Event event) throws Exception {
                    ZkUtils.showInformation(btn.getLabel(), btn.getId());
//                    commit(btn.getLabel());
                }
            });
            toolbar.appendChild(btn);
            no++;
        }
//        Button button01 = new Button();
//        button01.setId("button01");
//        button01.setLabel("button01");
//        button01.setSclass("btn-default");
//        toolbar.appendChild(button01);
//
//        Button button02 = new Button();
//        button02.setId("button02");
//        button02.setLabel("button02");
//        button02.setSclass("btn-default");
//        toolbar.appendChild(button02);
    }
    @Command
    public void hello(){
        ZkUtils.showInformation("test", "test");
    }

//    private Button getButton(int i){
//        if(i==1){
//            Button btn = new Button();
//            btn.setId("button0" + i);
//            btn.setLabel("button0" + i);
//            btn.setSclass("btn-default");
//            btn.addEventListener("onClick", new EventListener<Event>() {
//                @Override
//                public void onEvent(Event event) throws Exception {
//                    ZkUtils.showInformation(btn.getLabel(), btn.getId());
//                }
//            });
//            return btn;
//        } else if(i==2){
//            Button btn = new Button();
//            btn.setId("button0" + i);
//            btn.setLabel("button0" + i);
//            btn.setSclass("btn-default");
//            btn.addEventListener("onClick", new EventListener<Event>() {
//                @Override
//                public void onEvent(Event event) throws Exception {
//                    ZkUtils.showInformation(btn.getLabel(), btn.getId());
//                }
//            });
//            return btn;
//        }
//    }

}

