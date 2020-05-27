package com.sunjet.mis.auth;

import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.module.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: lhj
 * @create: 2017-07-02 19:52
 * @description: 说明
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;

//    @Autowired
//    private RestUtils restUtils;

//    //用户登录次数计数  redisKey 前缀
//    private String SHIRO_LOGIN_COUNT = "shiro_login_count_";
//
//    //用户登录是否被锁定    一小时 redisKey 前缀
//    private String SHIRO_IS_LOCK = "shiro_is_lock_";

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        String name = token.getUsername();
//        String password = String.valueOf(token.getPassword());
//        //访问一次，计数一次
////        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
////        opsForValue.increment(SHIRO_LOGIN_COUNT + name, 1);
////        //计数大于5时，设置用户被锁定一小时
////        if (Integer.parseInt(opsForValue.get(SHIRO_LOGIN_COUNT + name)) >= 5) {
////            opsForValue.set(SHIRO_IS_LOCK + name, "LOCK");
////            stringRedisTemplate.expire(SHIRO_IS_LOCK + name, 1, TimeUnit.HOURS);
////        }
////        if ("LOCK".equals(opsForValue.get(SHIRO_IS_LOCK + name))) {
////            throw new DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录！");
////        }
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("nickname", name);
//        //密码进行加密处理  明文为  password+name
//        String paw = password + name;
//        String pawDES = MyDES.encryptBasedDes(paw);
//        map.put("pswd", pawDES);
//        SysUser user = null;
//        // 从数据库获取对应用户名密码的用户
//        List<SysUser> userList = sysUserService.selectByMap(map);
//        if (userList.size() != 0) {
//            user = userList.get(0);
//        }
//        if (null == user) {
//            throw new AccountException("帐号或密码不正确！");
//        } else if ("0".equals(user.getStatus())) {
//            /**
//             * 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
//             */
//            throw new DisabledAccountException("此帐号已经设置为禁止登录！");
//        } else {
//            //登录成功
//            //更新登录时间 last login time
//            user.setLastLoginTime(new Date());
//            sysUserService.updateById(user);
//            //清空登录计数
//            opsForValue.set(SHIRO_LOGIN_COUNT + name, "0");
//        }
//        Logger.getLogger(getClass()).info("身份认证成功，登录用户：" + name);
//        return new SimpleAuthenticationInfo(user, password, getName());

        // token 是用户输入的用户名和密码
        // 第一步从 token 中取出用户身份/凭证,即用户名/密码
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String logId = usernamePasswordToken.getUsername();
//        log.info(logId);

//        ResponseEntity<IdModle> entity = restUtils.exchange(String.format("http://localhost:9000/user/%s", HttpMethod.GET, User.class));
//        ResponseEntity<UserInfo> responseEntity = restTemplate.exchange(String.format("http://localhost:9000/user/%s", logId), HttpMethod.GET, AuthHelper.getHttpEntity(), UserInfo.class);
//        ResponseEntity<UserEntity> responseEntity = restClient.get(String.format("/user/%s", logId), UserEntity.class);

//        HttpEntity requestEntity = new HttpEntity<>(logId, null);
//        ResponseEntity<UserEntity> responseEntity  = restClient.post(RequestPath.User.findOneByLogId, requestEntity, UserEntity.class);

        UserEntity userEntity = userService.findByLogId(logId);
        // 第二步：根据用户输入的 logId 从数据库查询
//        UserEntity userEntity = null;
        SimpleAuthenticationInfo simpleAuthenticationInfo = null;
        List<String> permissions = new ArrayList<>();
        for (PermissionEntity permission : userEntity.getPermissions()) {
            permissions.add(permission.getResource().getCode()+":" + permission.getOperation().getCode());
        }

        if(userEntity==null){
            throw new UnknownAccountException(); //如果用户名错误
        } else {
            //activeUser就是用户身份信息
            ActiveUser activeUser = ActiveUser.builder()
                    .userId(userEntity.getId())
                    .logId(userEntity.getLogId())
                    .username(userEntity.getName())
                    .userType(userEntity.getUserType())
//                    .dealer(userEntity.getDealer())
//                    .agency(userEntity.getAgency())
                    .roles(new ArrayList<>(userEntity.getRoles()))
                    .permissions(permissions)
                    .menus(userEntity.getMenus())
//                    .menus(userEntity.getMenuInfoList())
                    .build();
            simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    activeUser, userEntity.getPassword(), ByteSource.Util.bytes(userEntity.getSalt()), this.getName());

        }

//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            if (responseEntity.getBody() == null) {
//                throw new UnknownAccountException(); //如果用户名错误
//            } else {
//                //activeUser就是用户身份信息
//                ActiveUser activeUser = ActiveUser.builder()
//                        .userId(responseEntity.getBody().getObjId())
//                        .logId(responseEntity.getBody().getLogId())
//                        .username(responseEntity.getBody().getName())
//                        .userType(responseEntity.getBody().getUserType())
//                        .dealer(responseEntity.getBody().getDealer())
//                        .agency(responseEntity.getBody().getAgency())
//                        .roles(responseEntity.getBody().getRoles())
//                        .menus(responseEntity.getBody().getMenuInfoList())
//                        .build();
//                simpleAuthenticationInfo = new SimpleAuthenticationInfo(
//                        activeUser, responseEntity.getBody().getPassword(), ByteSource.Util.bytes(responseEntity.getBody().getSalt()), this.getName());
//            }
//
//        } else {
//            throw new RemoteAccessException(); //远程服务访问异常
//        }
        //将 currentUser 设置 simpleAuthenticationInfo
        //如果身份认证验证成功，返回一个认证信息 AuthenticationInfo
//        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
//                activeUser, password, ByteSource.Util.bytes(salt), this.getName());
        return simpleAuthenticationInfo;
//        return new SimpleAuthenticationInfo(activeUser, password, getName());

    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //从 principals获取主身份信息
        //将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
        ActiveUser activeUser = (ActiveUser) principals.getPrimaryPrincipal();

//        List<String> permissionList = permissionService.findAllByUserId(activeUser.getUserId());
//        activeUser.setPermissions(permissionList);
//        List<MenuEntity> menus = menuService.findAllByUserId(activeUser.getUserId());
//
//        Set<MenuEntity> userMenus = new HashSet<>();
//        for(MenuEntity me : menus){
//            if(permissionList.contains(me.getPermissionCode())){
//                userMenus.add(me);
////                userMenus.add(me.getParent());
//                chkParetMenu(userMenus,me);
//            }
//        }
//        List<MenuEntity> menuList = new ArrayList<>(userMenus);
//        Collections.sort(menuList, new Comparator<MenuEntity>(){
//            /*
//             * int compare(MenuEntity o1, MenuEntity o2) 返回一个基本类型的整型，
//             * 返回负数表示：o1 小于o2，
//             * 返回0 表示：o1和o2相等，
//             * 返回正数表示：o1大于o2。
//             */
//            public int compare(MenuEntity o1, MenuEntity o2) {
//
//                //按照菜单序号进行升序排列
//                if(o1.getSeq() > o2.getSeq()){
//                    return 1;
//                }
//                if(o1.getSeq() == o2.getSeq()){
//                    return 0;
//                }
//                return -1;
//            }
//        });
//        activeUser.setMenus(menuList);
//        log.info(activeUser.toString());

//            currentUser.setPermissions(permissionList);

//        List<String> permissions = new ArrayList<String>();
//        if (permissionList != null) {
//            for (PermissionEntity permissionEntity : permissionList) {
//                //将数据库中的权限标签 符放入集合
//                permissions.add(permissionEntity.getPermissionCode());
//            }
//        }

        //单独定一个集合对象

        Set<String> permissions = new HashSet<>();
//        permissions.add("user:create");//用户的创建
//        permissions.add("item:query");//商品查询权限
//        permissions.add("item:add");//商品添加权限
//        permissions.add("item:edit");//商品修改权限

        //授权

        for(RoleEntity role : activeUser.getRoles()){
            for (PermissionEntity permission : role.getPermissions()) {
                permissions.add(permission.getResource().getCode()+":"+permission.getOperation().getCode());
            }
        }
//        List<RoleEntity> roleEntityList = activeUser.getRoles();
//        if (roleEntityList != null && roleEntityList.size() > 0) {
//            for (RoleEntity entity : roleEntityList) {
//                List<PermissionEntity> permissionList = entity.getPermissionList(); //.getPermissionList();
//                if (permissionList != null && permissionList.size() > 0) {
//                    for (PermissionEntity permissionEntity : permissionList) {
//                        permissions.add(permissionEntity.getPermissionCode());
//                    }
//                }
//            }
//        }

//        activeUser.getPermissions();

        activeUser.setPermissions(new ArrayList<>(permissions));
//        System.out.println(activeUser.getPermissions());
        //查到权限数据，返回授权信息(要包括 上边的permissions)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }
}
