package com.ssh.common.exception;

import org.junit.Before;
import org.junit.Test;

public class ExceptionTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetMessage() throws Exception {
        DatabaseException exception = new DatabaseException("数据库异常.....");
        System.out.println(exception.getErrorCode());
        System.out.println(exception.getMessage());
    }

}
