package com.ssh.common.thread;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Synchronized用于线程间数据的共享而ThreadLocal用于线程间数据的隔离
 */
public class ThreadLocalTest {

    private static final Map<Thread, String> map = new HashMap<>();

    private class TestA {
        public void get() {
            System.err.println("TestA: " + map.get(Thread.currentThread()));
        }
    }

    private class TestB {
        public void get() {
            System.err.println("TestB: " + map.get(Thread.currentThread()));
        }
    }

    @Test
    public void testThreadLocal() throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    map.put(Thread.currentThread(), Thread.currentThread().getName());
                    new TestA().get();
                    new TestB().get();
                }
            }).start();
        }
    }

}
