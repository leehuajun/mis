package com.sunjet.mis.utils.zk;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.SimpleConstraint;

/**
 * Created by lhj on 16/10/12.
 */
public class NoEmptyConstraint extends SimpleConstraint implements CustomConstraint {

    public NoEmptyConstraint() {
        super(SimpleConstraint.NO_EMPTY);
    }

    @Override
    public void showCustomError(Component comp, WrongValueException ex) {
        //do nothing here -> no error message displayed
//        comp.setAttribute("style","border: 1px solid #a94442");
//        System.out.println("非空预警！");
//        ex.getComponent().setAttribute("style","border: 1px solid #a94442");
//        Clients.log(String.valueOf(ex)); //comment this out for really nothing
    }
}
