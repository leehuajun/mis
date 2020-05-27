package com.sunjet.mis.vm.helper;

import lombok.Getter;
import lombok.Setter;

/**
 * UI界面控件对应的权限状态对象
 * 2018-10-30  lhj 每个 ListVM / FormVM 都有一个 PermissionStatus 对象，用户做业务操作后，需要更改该对象
 */
@Getter
@Setter
public class PermissionStatus {

    /**
     * 是否禁用桌面用户交互控件
     */
    private boolean permitted = false;

    private boolean canRead = false;
    private boolean canCreate = false;
    private boolean canModify = false;
    private boolean canDelete = false;
    private boolean canSearch = false;
    private boolean canAudit = false;
    private boolean canDisaudit = false;
    private boolean canImport = false;
    private boolean canExport = false;
    private boolean canPrint = false;

}
