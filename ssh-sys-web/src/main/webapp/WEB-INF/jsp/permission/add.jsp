<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>权限管理-新增</title>
</head>
<body>
<div class="container-fluid">
    <div class="page-header">
        <label class="h4">权限管理-新增</label>
        <label class="h4 pull-right">
            <small>SSM-PERMISSION-ADD</small>
        </label>
    </div>
    <form id="form" method="post" class="form-horizontal form-group-sm">
        <fieldset>
            <div class="form-group">
                <label class="control-label" for="name">资源名称</label>
                <input id="name" name="name" type="text" class="form-control" placeholder="资源名称">
            </div>
            <div class="form-group">
                <label class="control-label" for="code">资源编码<span class="text-warning">(新增后此项不允许修改)</span></label>
                <input id="code" name="code" type="text" class="form-control" placeholder="资源编码">
            </div>
            <div class="form-group">
                <label class="control-label" for="url">资源地址</label>
                <input id="url" name="url" type="text" class="form-control" placeholder="资源地址">
            </div>
            <div class="form-group">
                <label class="control-label" for="type">资源类型</label>
                <select id="type" name="type" class="form-control" data-provide="select2">
                    <option value="FUNC">功能</option>
                    <option value="MENU">菜单</option>
                </select>
            </div>
            <div class="form-group">
                <label class="control-label" for="available">是否可用</label>
                <select id="available" name="available" class="form-control" data-provide="select2">
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
            </div>
            <div class="form-group">
                <label class="control-label" for="seq">排序号</label>
                <input id="seq" name="seq" type="number" class="form-control" placeholder="排序号">
            </div>
            <div class="form-group">
                <label class="control-label" for="parent">上级资源</label>
                <input id="parentId" name="parentId" type="hidden">
                <input id="parent" name="parent" type="text" class="form-control"
                       readonly="readonly" placeholder="请选择...">
            </div>
            <div class="form-group">
                <div class="text-right">
                    <button id="btnSave" type="button" class="btn btn-sm btn-primary">
                        <i class="glyphicon glyphicon-saved"></i> 保存
                    </button>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<div id="menuContent" style="display:none; position:absolute;">
    <ul id="tree" class="ztree" style="margin-top:0; width:180px; height:300px;"></ul>
</div>
<script type="text/javascript">

    require([
        'jquery',
        'noty',
        'bootstrap',
        'ztree',
        'select2'
    ], function ($, noty) {

        var $parent = $('#parent');
        var $parentId = $('#parentId');

        var setting = {
            check: {
                enable: true,
                chkStyle: 'radio',
                radioType: 'all'
            },
            view: {
                showIcon: true,
                showLine: true,
                selectedMulti: false,
                dblClickExpand: false
            },
            async: {
                enable: true,
                url: '${contextPath}/permission/getList',
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
                    $.fn.zTree.getZTreeObj(treeId).checkNode(treeNode, !treeNode.checked, null, true);
                },
                onCheck: function (e, treeId, treeNode) {
                    var nodes = $.fn.zTree.getZTreeObj(treeId).getCheckedNodes(true);
                    if (nodes.length === 1) {
                        $parent.attr('value', nodes[0].name);
                        $parentId.attr('value', nodes[0].id);
                    } else {
                        $parent.attr('value', '');
                        $parentId.attr('value', '');
                    }
                }
            }
        };

        $.fn.zTree.init($('#tree'), setting);

        var onBodyDown = function (event) {
            if (!(event.target.id == 'parent'
                    || event.target.id == 'menuContent'
                    || $(event.target).parents('#menuContent').length > 0)) {
                hideMenu();
            }
        };

        var hideMenu = function () {
            $('#menuContent').fadeOut('fast');
            $('body').unbind('mousedown', onBodyDown);
        };

        var showMenu = function () {
            var cityOffset = $parent.offset();
            $('#menuContent').css({
                left: cityOffset.left + 'px',
                top: cityOffset.top + $parent.outerHeight() + 'px'
            }).slideDown('fast');
            $('body').bind('mousedown', onBodyDown);
        };

        $parent.click(showMenu);

        $('#btnSave').click(function () {
            var $this = $(this);
            $.ajax({
                url: '${contextPath}/permission/addSubmit',
                type: 'POST',
                data: JSON.stringify({permissionDTO: $('#form').serializeObject()}),
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
