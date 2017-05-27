<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>权限管理-新增</title>
</head>
<body>
<div class="container-fluid">
    <%--
    <div class="page-header">
        <label class="h4">权限管理-新增</label>
        <label class="h4 pull-right">
            <small>SSM-PERMISSION-ADD</small>
        </label>
    </div>
    --%>
    <form id="form" method="post" class="form-horizontal form-group-sm" role="form">
        <div class="form-group">
            <label class="control-label col-sm-2" for="name">资源名称</label>
            <div class="col-sm-8">
                <input id="name" name="name" type="text" class="form-control" placeholder="资源名称"
                       data-bv-notempty="true"
                       data-bv-stringlength="true"
                       data-bv-stringlength-min="2"
                       data-bv-stringlength-max="15">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="code">资源编码</label>
            <div class="col-sm-8">
                <input id="code" name="code" type="text" class="form-control" placeholder="资源编码">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="url">资源地址</label>
            <div class="col-sm-8">
                <input id="url" name="url" type="text" class="form-control" placeholder="资源地址">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">资源类型</label>
            <div class="col-sm-8">
                <select id="type" name="type" class="form-control" data-provide="select2" data-bv-notempty="true">
                    <option value=""></option>
                    <option value="FUNC">功能</option>
                    <option value="MENU">菜单</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="seq">排序号</label>
            <div class="col-sm-8">
                <input id="seq" name="seq" type="number" class="form-control" placeholder="排序号"
                       data-bv-notempty="true">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="icon">资源图标</label>
            <div class="col-sm-8">
                <input id="icon" name="icon" type="text" class="form-control" placeholder="资源图标">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="parentId">上级资源</label>
            <div class="col-sm-8">
                <input id="parentId" class="form-control" readonly="readonly" placeholder="请选择...">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 text-right">
                <button id="btnSave" type="button" class="btn btn-sm btn-primary">
                    <i class="glyphicon glyphicon-saved"></i> 保存
                </button>
            </div>
        </div>
    </form>
</div>
<div id="menuContent" style="display:none;position:absolute;">
    <ul id="tree" class="ztree"></ul>
</div>
<script type="text/javascript">

    require([
        'jquery',
        'noty',
        'bootstrap',
        'bootstrapValidator',
        'ztree',
        'select2'
    ], function ($, noty) {

        $('#form').bootstrapValidator();

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
                type: 'POST',
                dataFilter: function (treeId, parentNode, responseData) {
                    return $.grep(responseData, function (obj, idx) {
                        return obj.type === 'MENU';
                    });
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
                    // var zTree = $.fn.zTree.getZTreeObj(treeId);
                    var nodes = zTree.getNodes();
                    zTree.expandNode(nodes[0], true, true, true);
                },
                onClick: function (e, treeId, treeNode) {
                    e.preventDefault();
                    // var zTree = $.fn.zTree.getZTreeObj(treeId);
                    zTree.checkNode(treeNode, !treeNode.checked, null, true);
                },
                onCheck: function (e, treeId, treeNode) {
                    // var zTree = $.fn.zTree.getZTreeObj(treeId);
                    var nodes = zTree.getCheckedNodes(true);
                    if (nodes.length === 1) {
                        $parentId.attr('value', nodes[0].name);
                    } else {
                        $parentId.attr('value', '');
                    }
                }
            }
        };

        var zTree = $.fn.zTree.init($('#tree'), setting);

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
            var offset = $parentId.offset();
            $('#menuContent').css({
                left: offset.left + 'px',
                top: offset.top + $parentId.outerHeight() + 'px'
            }).slideDown('fast');
            $('body').bind('mousedown', onBodyDown);
        };

        $parentId.click(showMenu);

        $('#btnSave').click(function () {
            var $form = $('#form');
            if ($form.validate()) {
                var $this = $(this);
                var formData = $form.serializeObject();
                var checkedNodes = zTree.getCheckedNodes(true);
                if (checkedNodes.length === 1) {
                    $.extend(formData, {parentId: checkedNodes[0].id});
                }
                $.ajax({
                    url: '${contextPath}/permission/addSubmit',
                    type: 'POST',
                    data: JSON.stringify(formData),
                    contentType: 'application/json',        // 发送信息至服务器时内容编码类型
                    dataType: 'json',                       // 预期服务器返回的数据类型
                    success: function (result) {
                        result.success && $this.attr({disabled: true});
                        noty.message(result.message, result.success);
                    }
                });
            }
        });

    });

</script>
</body>
</html>
