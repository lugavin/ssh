package com.ssh.common.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Arrays;
import java.util.List;

public class SecurityRealm extends AuthorizingRealm {

    /**
     * 用于认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        // 根据用户名从数据库中查询对应密码的散列值和盐
        String password = "3986874b369863a88de3f82728004977";
        String salt = "10086";

        return new SimpleAuthenticationInfo(username, password, ByteSource.Util.bytes(salt), this.getName());
    }

    /**
     * 用于授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        // 根据用户名从数据库中查询对应的权限数据
        // 基于角色的权限控制(由于角色名称可能会发生改变, 程序代码也要随之修改, 因此基于角色的权限控制不灵活, 可扩展性不强)
        List<String> roleList = Arrays.asList("role1", "role2");
        // 基于资源的权限控制(即使角色名称发生改变, 程序代码也无需修改, 能很好适应变化, 建议采用此种方案)
        List<String> permissionList = Arrays.asList("user:create", "user:update");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleList);
        simpleAuthorizationInfo.addStringPermissions(permissionList);
        return simpleAuthorizationInfo;
    }

}
