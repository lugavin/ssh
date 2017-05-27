<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>认证失败</title>
</head>
<body>
<div class="alert alert-danger" role="alert">
    <p>认证失败！</p>
    <p>失败原因: ${requestScope.exception}</p>
</div>
</body>
</html>
