<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>日誌詳細信息</title>
</head>
<body>
<div class="container-fluid">
    <form id="form" method="post" class="form-horizontal form-group-sm">
        <fieldset>
            <legend>
                <label class="h4">日誌詳細信息</label>
                <label class="h4 pull-right">
                    <small>SSH-AUDIT-002</small>
                </label>
            </legend>
        </fieldset>
    </form>
    <table id="table" class="table table-bordered table-hover table-condensed" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>記錄ID</th>
            <th>操作</th>
            <th>实体</th>
            <th>表名</th>
            <th>属性</th>
            <th>字段</th>
            <th>旧值</th>
            <th>新值</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/javascript">
    require([
        'jquery',
        'bootstrap',
        'datatables.net',
        'datatables.net-bs'
    ], function ($) {

        $('#table').DataTable({
            paging: false,
            ordering: false,
            searching: false,
            lengthChange: false,
            ajax: {
                url: '${contextPath}/audit/getDetail',
                type: 'POST',
                data: {auditLogId: '${requestScope.auditLogId}'},
                dataType: 'json',
                dataSrc: function (result) {
                    return result.data;
                }
            },
            columns: [
                {data: 'rowId'},
                {data: 'action'},
                {data: 'entityName', visible: false},
                {data: 'tableName'},
                {data: 'propertyName', visible: false},
                {data: 'columnName'},
                {data: 'beforeValue'},
                {data: 'afterValue'}
            ]
        });
    });
</script>
</body>
</html>
