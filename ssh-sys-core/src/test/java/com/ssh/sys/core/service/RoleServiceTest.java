package com.ssh.sys.core.service;

import com.ssh.common.util.Constant;
import com.ssh.sys.api.service.RoleService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.List;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleServiceTest {

    @Inject
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        Assert.notNull(roleService);
        Assert.isTrue(AopUtils.isAopProxy(roleService));
        Assert.isTrue(!AopUtils.isCglibProxy(roleService));
        Assert.isTrue(AopUtils.isJdkDynamicProxy(roleService));
    }

    @Test
    public void test1GetPermissions() throws Exception {
        List<Long> permissionIds = roleService.getPermissionIds(1001L);
        System.out.println(permissionIds);
    }

}
