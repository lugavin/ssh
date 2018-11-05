<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>登录页面</title>
    <style type="text/css">
        body {
            margin: 0;
            width: 100%;
            height: 100vh;
            min-width: 480px;
            min-height: 600px;
            /*background: url('assets/img/bg.jpg') no-repeat 50% 50%;*/
            background-color: #f8f8f8;
            background-size: cover;
        }

        #captcha {
            width: 50%;
            display: inline;
        }

        #captchaImg {
            width: 120px;
            height: 34px;
            cursor: pointer;
        }
    </style>
    <script type="text/javascript">
        // 解决session过期跳转到登陆页面并跳出iframe框架的问题
        if (top != window.self) {
            top.location.href = location.href;
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default" style="margin-top: 30%">
                <div class="panel-heading">
                    <div class="panel-title">请登录</div>
                </div>
                <div class="panel-body">
                    <form id="form" action="${contextPath}/security/login" method="post" role="form">
                        <fieldset>
                            <c:if test="${not empty requestScope.exception}">
                            <div class="form-group">
                                <div class="label label-danger" style="font-size: 1.5rem;">
                                    <i class="fa fa-warning fa-lg"></i>&nbsp;${requestScope.exception}
                                </div>
                            </div>
                            </c:if>
                            <div class="form-group">
                                <label class="control-label sr-only" for="username">用户名</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                    <input id="username" name="username" type="text" class="form-control" placeholder="用户名" autofocus="autofocus"
                                           data-bv-notempty="true"
                                           data-bv-stringlength="true"
                                           data-bv-stringlength-min="5"
                                           data-bv-stringlength-max="10">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label sr-only" for="password">密码</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                    <input id="password" name="password" type="password" class="form-control" placeholder="密码"
                                           data-bv-notempty="true">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label sr-only" for="password">验证码</label>
                                <input id="captcha" name="captcha" type="text" class="form-control" placeholder="验证码"
                                       data-bv-notempty="true">
                                <img id="captchaImg" src="${contextPath}/captcha/getCaptchaImage" alt="kaptcha.jpg"
                                     data-toggle="tooltip" title="点击刷新">
                            </div>
                            <div class="checkbox">
                                <label><input name="rememberMe" type="checkbox">记住我</label>
                            </div>
                            <input type="submit" value="登录" class="btn btn-success btn-block">
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    require([
        'jquery',
        'bootstrap',
        'bootstrapValidator'
    ], function ($) {

        // $.backstretch('${contextPath}/assets/img/bg.jpg');

        $('#form').bootstrapValidator({
            <%--
            fields: {
                captcha: {
                    validators: {
                        notEmpty: {},
                        callback: {
                            message: '验证码不匹配!',
                            callback: function (value, validator) {
                                return /^[0-9]{4}$/.test(value);
                            }
                        },
                        remote: {
                            message: '验证码不匹配!',
                            url: '${contextPath}/captcha/validateCaptcha'
                        }
                    }
                }
            }
            --%>
        });

        $('#captchaImg').click(function () {
            $(this).attr('src', '${contextPath}/captcha/getCaptchaImage?' + Math.floor(Math.random() * 100)).fadeIn();
        });

    });
</script>
</body>
</html>
