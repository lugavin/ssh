<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="com.google.gson.GsonBuilder" %>
<%@ page import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ page import="org.apache.struts2.views.jsp.TagUtils" %>
<%@ page import="com.ssh.common.web.data.ResultBean" %>
<%
    ValueStack valueStack = TagUtils.getStack(pageContext);
    ResultBean resultBean = (ResultBean) valueStack.findValue("resultBean");
    response.setContentType("application/json; charset=utf-8");
    if (resultBean != null) {
        out.print(new GsonBuilder().serializeNulls().create().toJson(resultBean));
    }
    out.flush();
%>
