package com.sunjet.mis.config;

import com.sunjet.mis.auth.MyShiroRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author: lhj
 * @create: 2017-07-02 18:42
 * @description: 说明
 */
@Configuration
public class ShiroConfig {

    /**
     * 自定义form认证过虑器
     * 基于Form表单的身份验证过滤器，不配置将也会注册此过虑器，表单中的用户账号、密码及loginurl将采用默认值，建议配置
     */
//    @Bean(name = "formAuthFilter")
//    public FormAuthenticationFilter formAuthenticationFilter() {
//        FormAuthenticationFilter filter = new FormAuthenticationFilter();
////        filter.setUsernameParam("logId");
////        filter.setPasswordParam("password");
////        filter.setRememberMeParam("rememberMe");
//        filter.setFailureKeyAttribute("loginFailure");
//        return filter;
//    }
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setFailureKeyAttribute("loginFailure");

        Map<String, Filter> filters = bean.getFilters();
        filters.put("authc", formAuthenticationFilter);

        bean.setSecurityManager(manager);

        //配置登录的url和登录成功的url
        bean.setLoginUrl("/login.zul");
        bean.setSuccessUrl("/index.zul");

//        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
////        filters.put("perms", urlPermissionsFilter());
////        filters.put("anon", new AnonymousFilter());
//        filters.put("authc", new DelegatingFilterProxy("formAuthFilter"));
//        bean.setFilters(filters);

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //filterChainDefinitionMap.put();
        filterChainDefinitionMap.put("/403.zul", "anon");  //表示可以匿名访问
        filterChainDefinitionMap.put("/zkau/**", "anon");
        filterChainDefinitionMap.put("/resource/**", "anon");
        filterChainDefinitionMap.put("/logout.zul", "logout");
        filterChainDefinitionMap.put("/*", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/*.*", "authc");


        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthorizingRealm authRealm) {
//        System.err.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    //配置自定义的权限登录器
    @Bean(name = "authRealm")
    public AuthorizingRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        MyShiroRealm authRealm = new MyShiroRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    //配置自定义的密码比较器
//    @Bean(name="credentialsMatcher")
//    public CredentialsMatcher credentialsMatcher() {
//        return new CredentialsMatcher();
//    }
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}
