<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>系统首页</title>
    <style type="text/css">
        @media (min-width: 768px) {
            .sidebar {
                position: fixed;
                top: 50px;
                bottom: 0;
                left: 0;
                width: 220px;
                z-index: 99;
                display: block;
                padding: 0;
                overflow-x: hidden;
                overflow-y: auto;
            }

            .main-content {
                position: inherit;
                margin-left: 220px;
                border-left: 1px solid #e5e5e5;
            }
        }

        #wrapper {
            width: 100%;
        }

        #page-wrapper {
            margin-top: 50px;
        }

        #iframe {
            width: 100%;
            height: calc(100vh - 70px);
        }

        #navbar {
            position: absolute;
            top: 0;
            right: 0;
        }

        #navbar > ul > li {
            line-height: 50px;
            height: 50px;
            border-left: 1px solid #f1f1f1;
            padding: 0;
            position: relative;
            float: left;
        }

        #navbar > ul > li > a {
            color: #5e5e5e;
            display: block;
            line-height: inherit;
            text-align: center;
            height: 100%;
            width: auto;
            min-width: 50px;
            padding: 0 8px;
            position: relative;
        }

        #navbar > ul > li > a:hover,
        #navbar > ul > li > a:focus,
        #navbar > ul > li > a:active {
            color: #f9f9f9;
            background-color: #999;
        }

        #navbar .user-info {
            max-width: 100px;
            display: inline-block;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: left;
            vertical-align: middle;
            line-height: 15px;
            position: relative;
        }

        #navbar .user-info small {
            display: block;
        }

        #navbar ul.dropdown-navbar {
            padding: 0;
            width: 240px;
            -webkit-box-shadow: 0 2px 4px rgba(30, 30, 100, 0.25);
            box-shadow: 0 2px 4px rgba(30, 30, 100, 0.25);
            border-color: #bcd4e5;
        }

        #navbar ul.dropdown-navbar > li {
            border-bottom: 1px solid #f3f3f3;
        }

        #navbar ul.dropdown-navbar > li.dropdown-header {
            text-shadow: none;
            padding-top: 0;
            padding-bottom: 0;
            line-height: 34px;
            font-size: 13px;
            font-weight: bold;
            text-transform: none;
        }
    </style>
</head>
<body>
<div id="wrapper">
    <nav class="nav navbar-fixed-top navbar-default" role="navigation">
        <button type="button" class="navbar-toggle pull-left" data-toggle="collapse" data-target="#sidebar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header">
            <a href="#" class="navbar-brand">
                <i class="fa fa-html5 fa-lg"></i>
                <small>Struts2 + Spring + JPA + Shiro + Dubbo</small>
            </a>
        </div>
        <div id="navbar" class="navbar-header" role="navigation">
            <ul class="nav">
                <li class="purple">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <i class="fa fa-bell"></i>
                        <span class="badge">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right dropdown-navbar">
                        <li class="dropdown-header">
                            <i class="fa fa-exclamation-triangle"></i>
                            8 Notifications
                        </li>
                        <li>
                            <a href="#">
                                <div class="clearfix">
                                    <i class="btn btn-xs btn-success fa fa-comment"></i> New Comments
                                    <span class="pull-right badge">+12</span>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <div class="clearfix">
                                    <i class="btn btn-xs btn-info fa fa-twitter pull-left"></i> Followers
                                    <span class="pull-right badge">+11</span>
                                </div>
                            </a>
                        </li>
                        <li><a href="#">See all notifications <i class="fa fa-arrow-right"></i></a></li>
                    </ul>
                </li>
                <li>
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img src="${contextPath}/assets/img/user.jpg" class="img-circle" style="max-width: 30px;">
                        <span class="user-info"><small>欢迎您</small>${sessionScope.activeUser.name}</span>
                        <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right" role="menu">
                        <li><a href="#"><i class="ace-icon fa fa-cog"></i> Settings</a></li>
                        <li><a href="#"><i class="ace-icon fa fa-user"></i> Profile</a></li>
                        <li class="divider"></li>
                        <li><a id="logout" href="javascript:void(0)"><i class="ace-icon fa fa-power-off"></i> Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div id="sidebar" class="collapse navbar-collapse sidebar" role="navigation">
            <ul id="tree" class="ztree"></ul>
        </div>
    </nav>
    <div id="page-wrapper">
        <div class="main-content">
            <iframe id="iframe" name="iframe" src="${contextPath}/portal/welcome" class="embed-responsive-item" frameborder="0" scrolling="auto"></iframe>
        </div>
    </div>
    <footer class="nav navbar-fixed-bottom navbar-inverse">
        <div class="text-center">
            <address class="navbar-link" style="display: inline;">
                <small>Copyright © 2016 Bootstrap All Rights Reserved</small>
            </address>
        </div>
    </footer>
</div>

<script type="text/javascript">
    require([
        'jquery',
        'bootstrap',
        'noty',
        'ztree'
    ], function ($) {

        var setting = {
            view: {
                showIcon: true,
                showLine: true,
                selectedMulti: false,
                dblClickExpand: false
            },
            async: {
                enable: true,
                url: '${contextPath}/portal/getMenus',
                dataFilter: function (treeId, parentNode, responseData) {
                    return responseData.data;
                }
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: 'parentId',
                    rootPid: null
                }
            },
            callback: {
                onNodeCreated: this.onNodeCreated,
                beforeClick: this.beforeClick,
                onAsyncSuccess: function (e, treeId, treeNode, msg) {
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    var nodes = zTree.getNodes();
                    zTree.expandNode(nodes[0], true, true, true);
                },
                onClick: function (e, treeId, treeNode) {
                    e.preventDefault();
                    if (treeNode && treeNode.url) {
                        $('#iframe').on('load', function () {
                            // console.info('iframe完成加载内容');
                        }).attr('src', treeNode.url);
                    }
                }
            }
        };

        $.fn.zTree.init($('#tree'), setting);

        $('#logout').click(function () {
            $.confirm('确认退出吗？', function (yes) {
                if (yes) {
                    location.href = '${contextPath}/security/logout';
                }
            });
        });

    });
</script>
</body>
</html>
