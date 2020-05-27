package com.sunjet.mis.vm;

import com.sunjet.mis.auth.ActiveUser;
import com.sunjet.mis.config.Variables;
import com.sunjet.mis.module.system.service.UserService;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Date;

/**
 * @author: lhj
 * @create: 2017-07-02 21:50
 * @description: 说明
 */
@Slf4j
public class LoginVM {

    @Getter
    @Setter
    private String technicalSupportandversion;


    @WireVariable
    private UserService userService;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    private boolean loginFailure = false;

    @WireVariable
    public Execution execution;


    @Init
    @NotifyChange("*")
    public void init() {

        String logId = ZkUtils.getCookie("", Variables.COOKIE_LOG_ID);
        if(StringUtils.isNotBlank(logId)){
            this.username = logId;
        }
        //获取url的传值（特殊处理）
        String orgId = null;
        if(execution!=null){
            orgId = execution.getParameter("orgId");
            //System.out.println("=======================================orgId:"+orgId);
        }


        log.info("启动登录界面！" + new Date());
//        String technicalSupport = configService.getValueByKey("technicalSupport");
//        String version = configService.getValueByKey("app_version");
//        technicalSupportandversion = technicalSupport + "         " + version;
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            Executions.sendRedirect("/logout.zul");
        }
    }


    @Command
    @NotifyChange("*")
    public void login(){
        loginFailure = false;

        try {

            if(StringUtils.isBlank(username)){
                ZkUtils.showError("用户名不能空!", "提示");
                return;
            }

            if(StringUtils.isBlank(password)){
                ZkUtils.showError("密码不能空!", "提示");
                return;
            }

//            if(selectedOrg==null){
//                ZkUtils.showError("请选择组织!", "提示");
//                return;
//            }

            UsernamePasswordToken token = new UsernamePasswordToken();

            token.setUsername(this.username);
            token.setPassword(this.password.toCharArray());

            // 开始登录
            SecurityUtils.getSubject().login(token);
            System.out.println("登录验证成功");

            //获取登录用户
            ActiveUser principal = (ActiveUser)SecurityUtils.getSubject().getPrincipal();
//            principal.setOrg(this.selectedOrg);

            //获取登录用户绑定的合作商、服务站、角色、菜单信息（根据用户登录所选组织获取绑定信息）
//            UserEntity userEntity = userService.findByLogId(principal.getUserId());

//            boolean checkSelectedOrg = false;
//            for(OrganizationEntity org :userEntity.getOrganizations()){
//                if(org.getObjId().equalsIgnoreCase(this.selectedOrg.getObjId())){
//                    checkSelectedOrg = true;
//                }
//            }

//            if(userEntity==null)
//                throw new Exception("用户不存在！");

//            if(!checkSelectedOrg){
//                ZkUtils.showError("组织选择错误，请重新选择!", "提示");
//                return;
//            }

            //给登录用户绑定合作商、服务站、角色、菜单信息
//            principal.setRoles(userEntity.getRoles());
//            principal.setMenus(userEntity.getMenuEntityList());
//            System.out.println("绑定用户信息成功");
            ZkUtils.setCookie("", Variables.COOKIE_LOG_ID, username);
            Executions.sendRedirect("/index.zul");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("登录失败");
            this.loginFailure =true;
        }
    }




//    @Command
//    @NotifyChange("*")
//    public void getOrgListByLogId(){
//        this.orgList = orgService.findAllByLogId(this.username);
//
//        //默认选中一个
//        if(this.orgList!=null&&this.orgList.size()>0){
//            //页面选中是默认最后一个，这里需要同步
//            this.selectedOrg = this.orgList.get(this.orgList.size()-1);
//        }
//    }
}
