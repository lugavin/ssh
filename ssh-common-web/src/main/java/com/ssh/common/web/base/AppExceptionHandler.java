package com.ssh.common.web.base;

import com.ssh.common.exception.AbstractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 异常统一处理的三种方式:
 * (1)使用@ExceptionHandler注解: 异常处理方法必须与出错方法在同一个Controller里面, 每个Controller都要写一遍;
 * (2)实现HandlerExceptionResolver接口: 只要实现HandlerExceptionResolver接口就是全局异常解析器;
 * (3)使用@ControllerAdvice注解: @ControllerAdvice+@ExceptionHandler注解解决了异常处理方法必须与出错方法在同一个Controller里面的问题, 可以实现全局异常.
 */
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(AbstractException.class)
    public ResponseData handleApplicationException(AbstractException ex) {
        return new ResponseData()
                .setSuccess(Boolean.FALSE)
                .setCode(ex.getErrorCode())
                .setMessage(ex.getMessage());
    }

    // @ResponseBody
    // @ExceptionHandler(RuntimeException.class)
    // public ResponseData handleApplicationException(RuntimeException ex) {
    //     LOGGER.error(ex.getMessage(), ex);
    //     AbstractException exception = new UnknownException(ex);
    //     return new ResponseData()
    //             .setSuccess(Boolean.FALSE)
    //             .setCode(exception.getErrorCode())
    //             .setMessage(exception.getMessage());
    // }

    @ExceptionHandler(Exception.class)
    public String handleApplicationException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return "redirect:/error/exception.jsp?err=" + ex.getMessage();
    }

}
