<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.struts2.views.jsp.TagUtils" %>
<%@ page import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ page import="com.ssh.common.util.JsonMapper" %>
<%@ page import="com.ssh.common.web.data.ResultBean" %>
<%
    ValueStack valueStack = TagUtils.getStack(pageContext);
    response.setContentType("application/json; charset=utf-8");
    response.setHeader("pragma", "no-cache");
    response.setHeader("cache-control", "no-cache");
    out.println(JsonMapper.nonEmptyMapper().serialize(new ResultBean()
            .setSuccess(false)
            .setCode("F-0001")
            .setMsg(valueStack.findString("fieldErrors"))));
    out.flush();
%>

