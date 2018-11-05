<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>審計日誌</title>
</head>
<body>
<div class="container-fluid">
    <form id="form" method="post" class="form-horizontal form-group-sm">
        <fieldset class="bs-content" data-content="查询条件">
            <div class="form-group">
                <%--
                <label class="control-label col-sm-1" for="funcCode">功能名稱</label>
                <div class="col-sm-3">
                    <select id="funcCode" name="funcCode" class="form-control"></select>
                </div>
                --%>
                <label class="control-label col-sm-1" for="actorId">經辦人員</label>
                <div class="col-sm-3">
                    <select id="actorId" name="actorId" class="form-control"></select>
                </div>
                <label class="control-label col-sm-1" for="actionStartTime">經辦時間</label>
                <div class="col-sm-3">
                    <div class="input-group">
                        <input id="actionStartTime" name="actionStartTime" type="text" class="input-sm form-control"
                               data-provide="datepicker" placeholder="开始时间">
                        <span class="input-group-addon">一</span>
                        <input id="actionEndTime" name="actionEndTime" type="text" class="input-sm form-control"
                               data-provide="datepicker" placeholder="结束时间">
                    </div>
                </div>
                <div class="col-sm-4">
                    <button id="btnQuery" type="button" class="btn btn-sm btn-primary" accesskey="enter">
                        <i class="glyphicon glyphicon-search"></i> 查询
                    </button>
                    <button type="reset" class="btn btn-sm btn-default">
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
                <th>流水號</th>
                <th>功能名稱</th>
                <th>功能地址</th>
                <th>主機IP</th>
                <th>主機名</th>
                <th>經辦時間</th>
                <th>經辦人員</th>
                <th>操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<script id="actionTemplate" type="text/html">
    <a href="${contextPath}/audit/detail?auditLogId={0}"
       class="btn btn-xs btn-default fancybox" role="button"
       data-fancybox-type="iframe"
       data-fancybox-width="100%"
       data-fancybox-height="100%"><i class="glyphicon glyphicon-search"></i> 詳細
    </a>
</script>

<script type="text/javascript">

    require([
        'jquery',
        'bootstrap',
        'datatables.net',
        'datatables.net-bs',
        'datepicker',
        'select2',
        'fancybox'
    ], function ($) {

        $('#actorId').select2Remote({
            url: '${contextPath}/select2/queryActor'
        });

        $('#funcCode').select2Remote({
            url: '${contextPath}/select2/queryFunc'
        });

        var $table = $('#table').DataTable({
            ordering: false,
            searching: false,
            serverSide: true,
            lengthChange: false,
            deferLoading: 0,
            rowId: 'auditLogId',
            ajax: {
                url: '${contextPath}/audit/getList',
                type: 'POST',
                data: function (data, settings) {
                    // https://datatables.net/reference/option/ajax
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
                {data: 'auditLogId'},
                {data: 'funcCode'},
                {data: 'requestURL'},
                {data: 'hostIP'},
                {data: 'hostName'},
                {data: 'actionTime'},
                {data: 'actorId'},
                {
                    data: 'auditLogId',
                    render: function (data, type, row) {
                        return Base.format($('#actionTemplate').html(), data);
                    }
                }
            ]
        });

        $('#btnQuery').click(function () {
            $table.ajax.reload();
        });

    });
</script>
</body>
</html>
