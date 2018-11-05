<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>用户管理</title>
</head>
<body>
<div class="container-fluid">
    <form id="form" class="form-horizontal form-group-sm" role="form">
        <fieldset class="bs-content" data-content="查询条件">
            <div class="form-group">
                <label class="control-label col-sm-1" for="uid">用户编号</label>
                <div class="col-sm-3">
                    <select id="uid" name="uid" class="form-control"></select>
                </div>
                <label class="control-label col-sm-1" for="code">用户账号</label>
                <div class="col-sm-3">
                    <input id="code" name="code" type="text" class="form-control" placeholder="用户账号">
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
        <table id="table" class="table table-bordered table-hover table-condensed" cellspacing="0" width="100%">
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

<script type="text/javascript">

    require([
        'jquery',
        'noty',
        'bootstrap',
        'datatables.net',
        'datatables.net-bs',
        'datatables.net-buttons',
        'datatables.net-buttons-bs',
        'datatables.net-buttons-colVis',
        'datatables.net-select',
        'select2',
        'fancybox'
    ], function ($, noty) {

        $.extend($.fn.dataTable.ext.buttons, {
            reload: {
                text: 'Reload',
                action: function (e, dt, node, config) {
                    dt.ajax.reload();
                }
            }
        });

        $('#uid').select2Remote({
            url: '${contextPath}/select2/queryActor'
        });

        var $table = $('#table').DataTable({
            ordering: false,
            searching: false,
            serverSide: true,
            lengthChange: false,
            rowId: 'id',
            select: {
                style: 'multi'
            },
            buttons: [
                {
                    enabled: true,
                    text: '<i class="fa fa-plus"></i>',
                    name: 'addButton',
                    className: 'addButton',
                    titleAttr: 'Click To Add',
                    action: function () {
                        $.fancybox({
                            minWidth: '80%',
                            minHeight: '100%',
                            type: 'iframe',
                            href: '${contextPath}/user/add',
                            afterClose: function () {
                                $table.ajax.reload();
                            }
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-edit"></i>',
                    name: 'editButton',
                    className: 'editButton',
                    titleAttr: 'Click To Edit',
                    action: function () {
                        var id = $table.row({selected: true}).id();
                        $.fancybox({
                            minWidth: '80%',
                            minHeight: '100%',
                            type: 'iframe',
                            href: '${contextPath}/user/edit?id=' + id,
                            afterClose: function () {
                                $table.ajax.reload();
                            }
                        });
                    }
                },
                {
                    enabled: false,
                    text: '<i class="fa fa-trash"></i>',
                    name: 'deleteButton',
                    className: 'deleteButton',
                    titleAttr: 'Click To Delete',
                    action: function () {
                        noty.confirm('确认删除吗?', function (yes) {
                            if (yes) {
                                // var data = {};
                                // var rows = $table.rows({selected: true}).data();
                                // for (var i = 0; i < rows.length; i++) {
                                //     data['ids[' + i + ']'] = rows[i].id;
                                // }
                                // console.info('Object => JSON', JSON.stringify(data));
                                // console.info('JSON => Object', JSON.parse(JSON.stringify(data)));
                                // console.info('Object => String', $.param(data));
                                var data = [];
                                var ids = $table.rows({selected: true}).ids();
                                $.each(ids, function (index, id) {
                                    data.push(id);
                                });
                                $.ajax({
                                    url: '${contextPath}/user/deleteSubmit',
                                    type: 'POST',
                                    data: JSON.stringify({ids: data}),
                                    contentType: 'application/json',        // 发送信息至服务器时内容编码类型
                                    dataType: 'json',                       // 预期服务器返回的数据类型
                                    success: function (result) {
                                        $table.ajax.reload(function () {
                                            noty.message(result.message, result.success);
                                            $table.buttons(['.editButton', '.deleteButton']).enable(!result.success);
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
                    name: 'exportButton',
                    className: 'exportButton',
                    titleAttr: 'Click To Export',
                    action: function () {
                        $('#form').attr({action: '${contextPath}/user/export'}).get(0).submit();
                    }
                },
                {
                    extend: 'reload',
                    enabled: true,
                    text: '<i class="fa fa-refresh"></i>',
                    name: 'reloadButton',
                    className: 'reloadButton',
                    titleAttr: 'Click To Reload'
                },
                {
                    extend: 'colvis',
                    enabled: true,
                    text: '<i class="fa fa-columns"></i>',
                    name: 'colvisButton',
                    className: 'colvisButton',
                    titleAttr: 'Click To Hidden Column'
                }
            ],
            ajax: {
                url: '${contextPath}/user/getList',
                type: 'POST',
                data: function (data, settings) {
                    // $.extend({}, data, $('#form').serializeObject())
                    return JSON.stringify({
                        dtArgs: data,
                        params: $('#form').serializeObject()
                    });
                },
                contentType: 'application/json',        // 发送信息至服务器时内容编码类型
                dataType: 'json',                       // 预期服务器返回的数据类型
                dataFilter: function (data, type) {     // 对Ajax返回的原始数据进行预处理
                    var obj = $.parseJSON(data);
                    return JSON.stringify(obj.data);
                },
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
                        return "******";
                    }
                },
                {
                    data: 'locked',
                    render: function (data, type, row) {
                        var dataMap = {'1': '锁定', '0': '未锁定'};
                        return dataMap[data] || '';
                    }
                }
            ],
            fnInitComplete: function (settings, json) {
                $table.buttons().container().appendTo($('.col-sm-6:eq(0)', $table.table().container()));
                $table.buttons().nodes().attr({'data-toggle': 'tooltip'}).bind('click', function (e) {
                    // console.info($(this).data('original-title'));
                });
            }
        });

        $table.on('click', 'tbody tr', function () {
            var len = $table.rows({selected: true}).data().length;
            $table.button('.editButton').enable(len === 1);
            $table.buttons(['.deleteButton']).enable(len !== 0);
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
