<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>用户管理-新增</title>
</head>
<body>
<div class="container-fluid">
    <div class="page-header">
        <label class="h4">用户管理-新增</label>
        <label class="h4 pull-right">
            <small>SSM-USER-ADD</small>
        </label>
    </div>
    <form id="form" method="post" class="form-horizontal form-group-sm">
        <fieldset>
            <div class="form-group">
                <label class="control-label" for="code">用户帐号</label>
                <input id="code" name="code" type="text" class="form-control" placeholder="用户帐号">
            </div>
            <div class="form-group">
                <label class="control-label" for="name">用户名</label>
                <input id="name" name="name" type="text" class="form-control" placeholder="用户名">
            </div>
            <div class="form-group">
                <label class="control-label" for="pass">密码</label>
                <input id="pass" name="pass" type="password" class="form-control" placeholder="密码">
            </div>
            <div class="form-group">
                <button id="btnSave" type="button" class="btn btn-sm btn-primary">
                    <i class="glyphicon glyphicon-saved"></i> 保存
                </button>
            </div>
        </fieldset>
    </form>
</div>
<script type="text/javascript">

    require([
        'jquery',
        'noty'
    ], function ($, noty) {

        $('#btnSave').click(function () {
            var $this = $(this);
            $.ajax({
                url: '${contextPath}/user/addSubmit',
                type: 'POST',
                data: JSON.stringify({userDTO: $('#form').serializeObject()}),
                contentType: 'application/json',        // 发送信息至服务器时内容编码类型
                dataType: 'json',                       // 预期服务器返回的数据类型
                success: function (result) {
                    result.success && $this.attr({disabled: true});
                    noty.message(result.message, result.success);
                }
            });
        });

    });

</script>
</body>
</html>
