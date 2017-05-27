<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>用户管理</title>
</head>
<body>
<div class="container-fluid">
    <div class="page-header">
        <label class="h4">用户管理</label>
        <label class="h4 pull-right">
            <small>SSM-USER-LIST</small>
        </label>
    </div>
    <form id="form" class="form-horizontal form-group-sm" role="form">
        <fieldset class="bs-content" data-content="查询条件">
            <div class="form-group">
                <label class="control-label col-sm-1" for="code">用户帐号:</label>
                <div class="col-sm-3">
                    <input id="code" name="code" type="text" class="form-control" placeholder="用户帐号">
                </div>
                <label class="control-label col-sm-1" for="name">用户名:</label>
                <div class="col-sm-3">
                    <input id="name" name="name" type="text" class="form-control" placeholder="用户名">
                </div>
                <div class="col-sm-4">
                    <%-- 一定要指明type属性的值(否则可能向服务端发送两次请求) --%>
                    <shiro:hasPermission name="user:query">
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
                <th>用戶编号</th>
                <th>用户帐号</th>
                <th>用户名</th>
                <th>密码</th>
                <th>状态</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<script id="template" type="text/x-handlebars-template">
    <div class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <label class="modal-title">{{title}}</label>
                </div>
                <div class="modal-body">
                    <div class="container-fluid">
                        <form id="form_{{index}}" class="form-horizontal form-group-sm" role="form">{{{html}}}</form>
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
</script>

<template id="userTemplate">
    <div class="form-group">
        <label class="control-label" for="code_{{index}}">用户帐号</label>
        <input id="code_{{index}}" name="code" value="{{code}}" type="text" class="form-control" placeholder="用户帐号"
               data-bv-notempty="true"
               data-bv-stringlength="true"
               data-bv-stringlength-min="2"
               data-bv-stringlength-max="10"
               data-bv-remote="true"
               data-bv-remote-url="${contextPath}/user/checkCode"
               data-bv-remote-message="该帐号已在系统中存在，请使用其他帐号！">
    </div>
    <div class="form-group">
        <label class="control-label" for="name_{{index}}">用户名</label>
        <input id="name_{{index}}" name="name" value="{{name}}" type="text" class="form-control" placeholder="用户名"
               data-bv-notempty="true">
    </div>
    {{#if id}}
    <div class="form-group">
        <label class="control-label" for="status_{{index}}">状态</label>
        <select id="status_{{index}}" name="status" class="form-control" data-bv-notempty="true">
            <option value="1">未锁定</option>
            <option value="0">锁定</option>
        </select>
        <script>
            $('#code_' + '{{index}}').attr({disabled: 'disabled'});
            $('#status_' + '{{index}}').select2().val('{{status}}').change();
        </script>
    </div>
    {{/if}}
</template>

<template id="roleTemplate">
    <div class="form-group">
        <label class="control-label" for="role_{{index}}">角色</label>
        <select id="role_{{index}}" name="role" class="form-control" multiple="multiple">
            {{#each roles}}
            <option value="{{id}}" selected="selected">{{name}}</option>
            {{/each}}
        </select>
        <script>
            $('#role_' + '{{index}}').select2Remote({
                url: '${contextPath}/select2/getRoleList',
                data: function (params) {
                    return {keyword: params.term};
                },
                processResults: function (result) {
                    return {results: result};
                }
            });
        </script>
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
        'fancybox',
        'select2'
    ], function ($, noty, Handlebars) {

        var template = Handlebars.compile($('#template').html());
        var userTemplate = Handlebars.compile($('#userTemplate').html());
        var roleTemplate = Handlebars.compile($('#roleTemplate').html());

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
            searching: false,
            lengthChange: false,
            serverSide: true,
            rowId: 'id',
            select: {
                style: 'multi' // {none, single, multi, os}
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
                            title: '用户管理-新增',
                            index: index,
                            html: userTemplate({index: index})
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
                                        url: '${contextPath}/user/addSubmit',
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
                        $(template({
                            title: '用户管理-更新',
                            index: index,
                            html: userTemplate($.extend({}, rowData, {index: index}))
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
                                        url: '${contextPath}/user/editSubmit',
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
                                    url: '${contextPath}/user/deleteSubmit',
                                    type: 'POST',
                                    data: {ids: String(data)},
                                    dataType: 'json',
                                    success: function (result) {
                                        $table.ajax.reload(function () {
                                            noty.message(result.message, result.success);
                                            $table.buttons(['edit:name', 'auth:name', 'delete:name', 'reset:name']).enable(!result.success);
                                        });
                                    }
                                });
                            }
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-key"></i>',
                    name: 'auth',
                    className: 'btn-sm',
                    titleAttr: 'Click To Assign Roles',
                    action: function () {
                        var userId = $table.row({selected: true}).id();
                        $.ajax({
                            url: '${contextPath}/role/getListByUserId',
                            type: 'POST',
                            data: {userId: userId},
                            dataType: 'json',
                            success: function (result) {
                                var index = Base.generateRandomNumber(), isSuccess = false;
                                $(template({
                                    title: '角色分配',
                                    index: index,
                                    html: roleTemplate({index: index, roles: result})
                                })).insertBefore('#template').modal({
                                    backdrop: 'static',
                                    show: true
                                }).on('shown.bs.modal', function () {
                                    $('#btnSave_' + index).click(function () {
                                        var $this = $(this);
                                        $.ajax({
                                            url: '${contextPath}/user/authSubmit',
                                            type: 'POST',
                                            data: {
                                                userId: userId,
                                                roleIds: String($('#role_' + index).val() || '')
                                            },
                                            dataType: 'json',
                                            success: function (result) {
                                                isSuccess = result.success;
                                                isSuccess && $this.attr({disabled: true});
                                                noty.message(result.message, isSuccess);
                                            }
                                        });
                                    });
                                }).on('hidden.bs.modal', function () {
                                    $(this).remove();
                                    isSuccess && $table.ajax.reload();
                                });
                            }
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-lock"></i>',
                    name: 'reset',
                    className: 'btn-sm',
                    titleAttr: 'Click To Reset Password',
                    action: function () {
                        noty.confirm('确认重置密码吗?', function (yes) {
                            if (yes) {
                                var data = [];
                                var ids = $table.rows({selected: true}).ids();
                                $.each(ids, function (index, id) {
                                    data.push(id);
                                });
                                $.ajax({
                                    url: '${contextPath}/user/resetPass',
                                    type: 'POST',
                                    data: {ids: String(data)},
                                    dataType: 'json',
                                    success: function (result) {
                                        $table.ajax.reload(function () {
                                            noty.message(result.message, result.success);
                                            $table.buttons(['edit:name', 'auth:name', 'delete:name']).enable(!result.success);
                                        });
                                    }
                                });
                            }
                        });
                    }
                },
                {
                    enabled: true,
                    text: '<i class="fa fa-file-excel-o"></i>',
                    name: 'export',
                    className: 'btn-sm',
                    titleAttr: 'Click To Export',
                    action: function () {
                        $('#form').attr({action: '${contextPath}/user/export', method: 'POST'}).get(0).submit();
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
                url: '${contextPath}/user/getList',
                type: 'POST',
                data: function (data, settings) {
                    return JSON.stringify({
                        dtArgs: data,
                        dto: $('#form').serializeObject()
                    });
                },
                contentType: 'application/json',        // 发送信息至服务器时内容编码类型
                dataType: 'json',                       // 预期服务器返回的数据类型
                dataSrc: function (result) {
                    return result.data;
                }
            },
            columns: [
                {data: 'id'},
                {data: 'code'},
                {data: 'name'},
                {
                    data: 'pass',
                    render: function (data, type, row) {
                        return '*****';
                    }
                },
                {
                    data: 'status',
                    render: function (data, type, row) {
                        var dataMap = {
                            '0': '<span class="label label-danger">不可用</span>',
                            '1': '<span class="label label-info">可用</span>'
                        };
                        return dataMap[data] || '';
                    }
                }
            ],
            fnInitComplete: function (settings, json) {
                $table.buttons().container().appendTo($('.col-sm-6:eq(0)', $table.table().container()));
                $table.buttons().nodes().attr({'data-toggle': 'tooltip'}).bind('click', $.noop());
            }
        }).on('click', 'tbody tr', function () {
            var rows = $table.rows({selected: true}).data();
            $table.buttons(['edit:name', 'auth:name']).enable(rows.length === 1);
            $table.buttons(['delete:name', 'reset:name']).enable(rows.length > 0);
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
