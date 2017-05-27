package com.ssh.common.web.security;

import com.ssh.common.subject.ActiveUser;
import com.ssh.common.subject.Permission;
import com.ssh.common.subject.SecurityService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SecurityService securityService;

    /**
     * 用于认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        // 根据用户名从数据库中查询对应密码的散列值和盐
        ActiveUser activeUser = securityService.getActiveUser(principal);
        if (activeUser == null) {
            return null;
        }
        activeUser.setMenus(securityService.getMenuList(activeUser.getId()));
        return new SimpleAuthenticationInfo(activeUser, activeUser.getPass(), ByteSource.Util.bytes(activeUser.getSalt()), getName());
    }

    /**
     * 用于授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ActiveUser activeUser = (ActiveUser) principalCollection.getPrimaryPrincipal();
        activeUser.setFunctions(securityService.getFunctionList(activeUser.getId()));
        // 根据用户名从数据库中查询对应的权限数据
        // 基于角色的权限控制(由于角色名称可能会发生改变, 程序代码也要随之修改, 因此基于角色的权限控制不灵活, 可扩展性不强)
        // 基于资源的权限控制(即使角色名称发生改变, 程序代码也无需修改, 能很好适应变化, 建议采用此种方案)
        List<String> permissionList = new ArrayList<>();
        for (Permission permission : activeUser.getFunctions()) {
            permissionList.add(permission.getCode());
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissionList);
        return simpleAuthorizationInfo;
    }

    /**
     * 功能描述：清空缓存
     * 使用场景：当权限数据修改后需要立即生效
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}
