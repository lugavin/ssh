<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <base href="<c:url value="/"/>">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title><sitemesh:write property='title'/></title>
    <link rel="shortcut icon" href="favicon.ico">
    <link rel="stylesheet" href="assets/css/app.css">
    <script type="text/javascript" src="assets/js/app.js"></script>
    <script type="text/javascript" src="assets/js/config.js"></script>
    <script type="text/javascript" src="assets/js/require.js"></script>
    <script type="text/javascript">
        Base.contextPath = '<c:url value="/"/>';
    </script>
    <sitemesh:write property='head'/>
</head>
<body>
<sitemesh:write property='body'/>
</body>
</html>
