<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>異常信息</title>
</head>
<body>
<div class="alert alert-danger" role="alert">
    <p>出错啦！</p>
    <p>错误信息: ${requestScope.exception}</p>
</div>
</body>
</html>
