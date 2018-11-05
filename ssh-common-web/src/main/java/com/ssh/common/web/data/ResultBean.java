package com.ssh.common.web.data;

import com.ssh.common.enums.StatusCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ResultBean implements Serializable {

    private static final long serialVersionUID = 20160130L;

    private boolean success = Boolean.TRUE;
    private String code = Integer.toString(StatusCode.SUCCESS.getCode());
    private String message = StatusCode.SUCCESS.getReasonPhrase();
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public ResultBean setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ResultBean setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResultBean setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
