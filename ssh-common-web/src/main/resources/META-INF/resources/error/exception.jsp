<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ssh.common.exception.AbstractException" %>
<%@ page import="com.ssh.common.exception.UnknownException" %>
<%@ page import="com.ssh.common.util.JsonUtils" %>
<%@ page import="com.ssh.common.web.data.ResultBean" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%
    Exception ex = (Exception) ActionContext.getContext().getValueStack().findValue("exception");
    AbstractException exception = ex instanceof AbstractException ? (AbstractException) ex : new UnknownException(ex);
    response.setContentType("application/json; charset=utf-8");
    out.print(JsonUtils.serialize(new ResultBean()
                    .setSuccess(false)
                    .setCode(exception.getErrorCode())
                    .setMessage(exception.getMessage()))
    );
    out.flush();
%>
