package com.ssh.common;

import org.junit.*;

/**
 * 执行顺序: @BeforeClass --> @Before <--> @After --> @AfterClass
 */
public class JunitTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.err.println("BeforeClass(初始化资源): 针对所有方法,只执行一次且方法必须为static!");
    }

    @Before
    public void before() throws Exception {
        System.err.println("Before(初始化资源): 针对每个方法,都执行一次!");
    }

    @After
    public void after() throws Exception {
        System.err.println("After(释放资源): 针对每个方法,都执行一次!!");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        System.err.println("AfterClass(释放资源): 针对所有方法,只执行一次且方法必须为static!");
    }

    @Test
    public void test() throws Exception {
        System.err.println();
    }

}
