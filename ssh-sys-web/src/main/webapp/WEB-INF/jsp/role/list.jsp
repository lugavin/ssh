<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>角色管理</title>
</head>
<body>
<div class="container-fluid">
    <div class="page-header">
        <label class="h4">角色管理</label>
        <label class="h4 pull-right"><small>SSM-ROLE-LIST</small></label>
    </div>
    <form id="form" class="form-horizontal form-group-sm" role="form">
        <fieldset class="bs-content" data-content="查询条件">
            <div class="form-group">
                <label class="control-label col-sm-1" for="name">角色名称:</label>
                <div class="col-sm-3">
                    <input id="name" name="name" type="text" class="form-control" placeholder="角色名称">
                </div>
                <label class="control-label col-sm-1" for="status">角色状态</label>
                <div class="col-sm-3">
                    <select id="status" name="status" class="form-control" data-provide="select2">
                        <option value=""></option>
                        <option value="1">可用</option>
                        <option value="0">不可用</option>
                    </select>
                </div>
                <div class="col-sm-4">
                    <%-- 一定要指明type属性的值(否则可能向服务端发送两次请求) --%>
                    <shiro:hasPermission name="role:query">
                    <button id="btnQuery" type="button" class="btn btn-primary btn-sm" accesskey="enter">
                        <i class="glyphicon glyphicon-search"></i> 查询
                    </button>
                    </shiro:hasPermission>
                    <button id="btnReset" type="reset" class="btn btn-default btn-sm">
                        <i class="glyphicon glyphicon-refresh"></i> 重置
                    </button>
                </div>
            </div>
        </fieldset>
    </form>
    <div class="bs-content" data-content="数据列表">
        <table id="table" class="table table-hover table-condensed" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>角色编号</th>
                <th>角色名称</th>
                <th>角色状态</th>
                <th>备注</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<div id="modal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <label class="modal-title">资源列表</label>
            </div>
            <div class="modal-body">
                <ul class="ztree"></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary">
                    <i class="fa fa-key"></i> 授权
                </button>
            </div>
        </div>
    </div>
</div>

<template id="template">
    <div class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <label class="modal-title">{{title}}</label>
                </div>
                <div class="modal-body">
                    <div class="container-fluid">
                        <form id="form_{{index}}" class="form-horizontal form-group-sm" role="form">
                            <div class="form-group">
                                <label class="control-label" for="name_{{index}}">角色名称</label>
                                <input id="name_{{index}}" name="name" value="{{name}}" type="text" class="form-control" placeholder="角色名称"
                                       data-bv-notempty="true"
                                       data-bv-stringlength="true"
                                       data-bv-stringlength-min="2"
                                       data-bv-stringlength-max="15">
                            </div>
                            {{#if id}}
                            <div class="form-group">
                                <label class="control-label" for="status_{{index}}">角色状态</label>
                                <select id="status_{{index}}" name="status" class="form-control" data-bv-notempty="true">
                                    <option value="1">可用</option>
                                    <option value="0">不可用</option>
                                </select>
                                <script>$('#status_' + '{{index}}').select2().val('{{status}}').change();</script>
                            </div>
                            {{/if}}
                            <div class="form-group">
                                <label class="control-label" for="remark_{{index}}">備註</label>
                                <textarea id="remark_{{index}}" name="remark" class="form-control" placeholder="備註">{{remark}}</textarea>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="btnSave_{{index}}" type="button" class="btn btn-sm btn-primary">
                        <i class="glyphicon glyphicon-saved"></i> 保存
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script type="text/javascript">

    var fnUpdate, fnDelete;

    require([
        'jquery',
        'noty',
        'handlebars',
        'bootstrap',
        'bootstrapValidator',
        'datatables.net',
        'datatables.net-bs',
        'datatables.net-buttons',
        'datatables.net-buttons-bs',
        'datatables.net-buttons-colVis',
        'datatables.net-select',
        'select2',
        'ztree'
    ], function ($, noty, Handlebars) {

        var template = Handlebars.compile($('#template').html());

        $.extend($.fn.dataTable.ext.buttons, {
            reload: {
                text: 'Reload',
                action: function (e, dt, node, config) {
                    // 刷新当前页
                    dt.ajax.reload($.noop, false);
                }
            }
        });

        var $table = $('#table').DataTable({
            ordering: false,
            searching: true,
            serverSide: false,
            lengthChange: false,
            rowId: 'id',
            select: {
                style: 'multi'
            },
            language: {
                select: {
                    rows: {
                        _: '(已選擇 %d 行)',
                        0: '(點擊選擇行)'
                    }
                }
            },
            buttons: [
                {
                    enabled: true,
                    text: '<i class="fa fa-plus"></i>',
                    name: 'add',
                    className: 'btn-sm',
                    titleAttr: 'Click To Add',
                    action: function () {
                        var index = Base.generateRandomNumber(), isSuccess = false;
                        $(template({
                            title: '角色管理-新增',
                            index: index
                        })).insertBefore('#template').modal({
                            backdrop: 'static',
                            show: true
                        }).on('shown.bs.modal', function () {
                            var $form = $('#form_' + index);
                            $form.bootstrapValidator();
                            $('#btnSave_' + index).click(function () {
                                if ($form.validate()) {
                                    var $this = $(this);
                                    $.ajax({
                                        url: '${contextPath}/role/addSubmit',
                                        type: 'POST',
                                        data: $form.serialize(),
                                        dataType: 'json',
                                        success: function (result) {
                                            isSuccess = result.success;
                                            isSuccess && $this.attr({disabled: true});
                                            noty.message(result.message, isSuccess);
                                        }
                                    });
                                }
                            });
                        }).on('hidden.bs.modal', function () {
                            $(this).remove();
                            isSuccess && $table.ajax.reload();
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-edit"></i>',
                    name: 'edit',
                    className: 'btn-sm',
                    titleAttr: 'Click To Edit',
                    action: function () {
                        var id = $table.row({selected: true}).id();
                        var rowData = $table.row({selected: true}).data();
                        var index = Base.generateRandomNumber(), isSuccess = false;
                        $(template($.extend({}, rowData, {
                            title: '角色管理-更新',
                            index: index
                        }))).insertBefore('#template').modal({
                            backdrop: 'static',
                            show: true
                        }).on('shown.bs.modal', function () {
                            var $form = $('#form_' + index);
                            $form.bootstrapValidator();
                            $('#btnSave_' + index).click(function () {
                                if ($form.validate()) {
                                    var $this = $(this);
                                    $.ajax({
                                        url: '${contextPath}/role/editSubmit',
                                        type: 'POST',
                                        data: $.extend({}, $form.serializeObject(), {id: id}),
                                        dataType: 'json',
                                        success: function (result) {
                                            isSuccess = result.success;
                                            isSuccess && $this.attr({disabled: true});
                                            noty.message(result.message, isSuccess);
                                        }
                                    });
                                }
                            });
                        }).on('hidden.bs.modal', function () {
                            $(this).remove();
                            isSuccess && $table.ajax.reload();
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-key"></i>',
                    name: 'auth',
                    className: 'btn-sm',
                    titleAttr: 'Click To Assign Permissions',
                    action: function () {
                        var roleId = $table.row({selected: true}).id();
                        var $clone = $('#modal').clone(true).removeAttr('id').insertBefore('#modal').modal({
                            backdrop: 'static',
                            show: true
                        }).on('hidden.bs.modal', function () {
                            $(this).remove();
                        });
                        $clone.find('ul.ztree').attr({id: 'ztree'});
                        $clone.find('div.modal-footer>button').bind('click', {id: roleId}, function (event) {
                            var $this = $(this);
                            var ids = $.map(zTree.getCheckedNodes(true), function (node) {
                                return node.id;
                            });
                            $.ajax({
                                url: '${contextPath}/role/authSubmit',
                                type: 'POST',
                                data: {
                                    id: event.data.id,
                                    permissionIds: ids.toString()
                                },
                                dataType: 'json',
                                success: function (result) {
                                    result.success && $this.attr({disabled: true});
                                    noty.message(result.message, result.success);
                                }
                            });
                        });
                        var setting = {
                            check: {enable: true},
                            view: {
                                showIcon: true,
                                showLine: true,
                                selectedMulti: false,
                                dblClickExpand: true
                            },
                            async: {
                                enable: true,
                                url: '${contextPath}/role/getPermissionList?id=' + roleId,
                                dataFilter: function (treeId, parentNode, responseData) {
                                    return responseData['permissionList'];
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
                                onAsyncSuccess: function (event, treeId, treeNode, data) {
                                    var permissionIds = JSON.parse(data)['permissionIds'];
                                    $.each(permissionIds, function (idx, permissionId) {
                                        var node = zTree.getNodeByParam('id', permissionId);
                                        node && !node.isParent && zTree.checkNode(node, true, true);
                                    });
                                    zTree.expandNode(zTree.getNodes()[0], true, true, true);
                                },
                                onClick: function (event) {
                                    event.preventDefault();
                                }
                            }
                        };
                        var zTree = $.fn.zTree.init($('#ztree'), setting);
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-trash"></i>',
                    name: 'delete',
                    className: 'btn-sm',
                    titleAttr: 'Click To Delete',
                    action: function () {
                        noty.confirm('确认删除吗?', function (yes) {
                            if (yes) {
                                var data = [];
                                var ids = $table.rows({selected: true}).ids();
                                $.each(ids, function (index, id) {
                                    data.push(id);
                                });
                                $.ajax({
                                    url: '${contextPath}/role/deleteSubmit',
                                    type: 'POST',
                                    data: {ids: String(data)},
                                    dataType: 'json',
                                    success: function (result) {
                                        $table.ajax.reload(function () {
                                            noty.message(result.message, result.success);
                                            $table.buttons(['edit:name', 'delete:name', 'auth:name']).enable(!result.success);
                                        });
                                    }
                                });
                            }
                        });
                    }
                },
                {
                    extend: 'reload',
                    enabled: true,
                    text: '<i class="fa fa-refresh"></i>',
                    name: 'reload',
                    className: 'btn-sm',
                    titleAttr: 'Click To Reload'
                },
                {
                    extend: 'colvis',
                    enabled: true,
                    text: '<i class="fa fa-columns"></i>',
                    name: 'colvis',
                    className: 'btn-sm',
                    titleAttr: 'Click To Hidden Column'
                }
            ],
            ajax: {
                url: '${contextPath}/role/getList',
                type: 'POST',
                data: function () {
                    return $('#form').serialize();
                },
                dataType: 'json',
                dataSrc: function (data) {
                    return data;
                }
            },
            columns: [
                {data: 'id'},
                {data: 'name'},
                {
                    data: 'status',
                    render: function (data, type, row) {
                        var dataMap = {
                            '0': '<span class="label label-danger">不可用</span>',
                            '1': '<span class="label label-info">可用</span>'
                        };
                        return dataMap[data] || '';
                    }
                },
                {data: 'remark'}
            ],
            fnInitComplete: function (settings, json) {
                $table.buttons().container().appendTo($('.col-sm-6:eq(0)', $table.table().container()));
                $table.buttons().nodes().attr({'data-toggle': 'tooltip'}).bind('click', $.noop());
            }
        }).on('click', 'tbody tr', function () {
            var rows = $table.rows({selected: true}).data();
            $table.buttons(['edit:name', 'auth:name']).enable(rows.length === 1);
            $table.buttons(['delete:name']).enable(rows.length > 0);
        });

        $('#btnQuery').click(function () {
            var $btn = $(this).button('loading');
            $table.ajax.reload(function () {
                $btn.button('reset');
            });
        });

    });
</script>
</body>
</html>
