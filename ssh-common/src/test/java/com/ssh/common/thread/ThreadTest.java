package com.ssh.common.thread;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;
import org.junit.Test;

/**
 * 当多线程访问公共数据时就有可能产生线程安全问题;
 * 当然大多数情况下, 我们根本不需要考虑线程安全问题, 比如 Service & DAO 等, 除非在 Bean 中声明了实例变量;
 * 因此对于线程不安全的对象最好定义在方法体内作为局部变量使用而不建议定义为成员变量使用!
 */
public class ThreadTest {

    /**
     * 在单例模式下可能产生线程安全问题(当多个线程调用时, 实例变量可能会发生窜数据问题)
     */
    private int ticket = 5;

    private class ExecuteThread extends TestRunnable {
        @Override
        public void runTest() throws Throwable {
            while (ticket > 0) {
                System.err.println(Thread.currentThread().getName() + ">>" + ticket--);
                Thread.sleep(20);
            }
        }
    }

    @Test
    public void testThread() throws Throwable {
        TestRunnable[] testRunnable = new TestRunnable[2];
        for (int i = 0; i < testRunnable.length; i++) {
            testRunnable[i] = new ExecuteThread();
        }
        new MultiThreadedTestRunner(testRunnable).runTestRunnables();
    }

}
