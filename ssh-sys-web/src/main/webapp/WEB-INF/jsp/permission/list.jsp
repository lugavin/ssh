<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>权限管理</title>
    <style type="text/css">
        .ui-inline-icon {
            margin-left: 8px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <table id="treeGrid"></table>
    <div id="pager"></div>
</div>
<script id="template" type="text/html">
    <a id="btnAdd" href="javascript:void(0)" class="ui-inline-icon">
        <i class="fa fa-plus"></i>
    </a>
    <a id="btnEdit" href="javascript:void(0)" class="ui-inline-icon ui-state-disabled">
        <i class="fa fa-edit"></i>
    </a>
    <a id="btnDel" href="javascript:void(0)" class="ui-inline-icon ui-state-disabled">
        <i class="fa fa-trash"></i>
    </a>
    <a id="btnRefresh" href="javascript:void(0)" class="ui-inline-icon">
        <i class="fa fa-refresh"></i>
    </a>
</script>
<script type="text/javascript">
    <!--
    require([
        'jquery',
        'noty',
        'jquery.ui',
        'jquery.grid',
        'fancybox'
    ], function ($, noty) {

        $(document).ready(function () {

            var $treeGrid = $('#treeGrid');

            $treeGrid.jqGrid({
                url: '${contextPath}/permission/getList',
                mtype: 'POST',
                datatype: 'json',
                jsonReader: {
                    root: 'data',
                    repeatitems: false
                },
                treeReader: {
                    level_field: 'level',
                    parent_id_field: 'parent',
                    leaf_field: 'isLeaf',
                    expanded_field: 'expanded',
                    loaded: 'loaded',
                    icon_field: 'icon'
                },
                height: 'auto',
                loadui: 'disable',
                colNames: ['资源编号', '资源名称', '资源编码', '资源类型', '资源地址', '上级资源编号', '资源路径', '排序号', '是否可用'],
                colModel: [
                    {name: 'id', width: 1, hidden: true, key: true},
                    {name: 'name', width: 200, resizable: false, editable: true},
                    {name: 'code', width: 100, resizable: false},
                    {
                        name: 'type', width: 100, resizable: false, align: 'center',
                        formatter: function (cellValue, options, rowObject) {
                            var map = {'MENU': '菜单', 'FUNC': '功能'};
                            return map[cellValue] || '';
                        }
                    },
                    {name: 'url', width: 100, resizable: false, editable: true},
                    {name: 'parentId', width: 100, resizable: false, editable: true, align: 'right'},
                    {name: 'parentIds', width: 100, resizable: false, editable: true, align: 'right'},
                    {name: 'seq', width: 50, resizable: false, editable: true, align: 'right', sortable: false},
                    {
                        name: 'available', width: 50, resizable: false, editable: true, align: 'center',
                        formatter: function (cellValue, options, rowObject) {
                            var map = {'1': '可用', '0': '不可用'};
                            return map[cellValue] || '';
                        }
                    }
                ],
                treeGrid: true,
                tree_root_level: 1,
                treeGridModel: 'adjacency',
                caption: '资源列表',
                ExpandColumn: 'name',
                rowNum: 10000,
                treeIcons: {
                    plus: 'ui-icon-triangle-1-e',
                    minus: 'ui-icon-triangle-1-s',
                    leaf: 'ui-icon-document'
                },
                gridview: true,
                autowidth: true,
                pager: '#pager',
                sortname: 'seq',
                sortorder: 'asc',
                enabletooltips: true,
                loadComplete: function (result) {
                    this.addJSONData($.map(result.data, function (obj) {
                        return $.extend({}, obj, {
                            expanded: true,
                            loaded: true,
                            isLeaf: obj['type'] === 'func',
                            parent: obj['parentId'],
                            level: String(obj['parentIds']).split('/').length
                        });
                    }));
                },
                onSelectRow: function (id) {
                    id && $('#btnEdit,#btnDel').removeClass('ui-state-disabled');
                }
            });

            // $treeGrid.jqGrid('inlineNav', '#pager', {addtext: '', edittext: ''});

            $('#pager_left').html($('#template').html());

            $('#btnAdd').click(function () {
                $.fancybox({
                    minWidth: '60%',
                    minHeight: '100%',
                    type: 'iframe',
                    href: '${contextPath}/permission/add',
                    afterClose: function () {
                        $treeGrid.jqGrid('resetSelection').trigger('reloadGrid');
                    }
                });
            });

            $('#btnEdit').click(function () {
                var rowId = $treeGrid.jqGrid('getGridParam', 'selrow');
                if (rowId) {
                    $.fancybox({
                        minWidth: '60%',
                        minHeight: '100%',
                        type: 'iframe',
                        href: '${contextPath}/permission/edit?id=' + rowId,
                        afterClose: function () {
                            $('#btnEdit,#btnDel').addClass('ui-state-disabled');
                            $treeGrid.jqGrid('resetSelection').trigger('reloadGrid');
                        }
                    });
                }
            });

            $('#btnDel').click(function () {
                var rowId = $treeGrid.jqGrid('getGridParam', 'selrow');
                if (rowId) {
                    var rowData = $treeGrid.getRowData(rowId);
                    noty.confirm('确认删除【' + rowData.name + '】吗?', function (yes) {
                        if (yes) {
                            $.post('${contextPath}/permission/deleteSubmit', {id: rowId}, function () {
                                $('#btnEdit,#btnDel').addClass('ui-state-disabled');
                                $treeGrid.jqGrid('resetSelection').trigger('reloadGrid');
                            }, 'json');
                        }
                    });
                }
            });

            $('#btnRefresh').click(function () {
                $treeGrid.jqGrid('resetSelection').trigger('reloadGrid');
            });

        });
    });
    //-->
</script>

</body>
</html>
