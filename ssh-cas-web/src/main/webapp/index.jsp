<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.jasig.cas.client.util.AssertionHolder" %>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%
    // AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
    AttributePrincipal principal = AssertionHolder.getAssertion().getPrincipal();
    String loginName = principal.getName();
    out.println("loginName:" + loginName);
%>
