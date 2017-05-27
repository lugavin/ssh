package com.ssh.common;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalTest {

    private static final Map<Thread, String> map = new HashMap<>();

    @BeforeClass
    public static void beforeClass() throws Exception {

    }

    @Test
    public void testThreadLocal() throws Exception {
        for (int i = 1; i <= 5; i++) {
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

    class TestA {
        public void get() {
            System.out.println("TestA: " + map.get(Thread.currentThread()));
        }
    }

    class TestB {
        public void get() {
            System.out.println("TestB: " + map.get(Thread.currentThread()));
        }
    }


}
