<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <title>权限管理</title>
    <style type="text/css">
        .ztree li span.button.add {
            margin-left: 2px;
            margin-right: -1px;
            background-position: -144px 0;
            vertical-align: top;
            *vertical-align: middle
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-xs-12 col-sm-6">
                    <ul id="tree" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    <!--
    require([
        'jquery',
        'noty',
        'bootstrap',
        'ztree',
        'fancybox'
    ], function ($, noty) {

        var setting = {
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
                    rootPid: 0
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
                onClick: function (e, treeId, node) {
                    e.preventDefault();
                },
                beforeEditName: function (treeId, treeNode) {
                    var zTree = $.fn.zTree.getZTreeObj(treeId);
                    zTree.selectNode(treeNode);
                    // setTimeout(function () {
                    //     if (confirm('进入节点【' + treeNode.name + '】的编辑状态吗?')) {
                    //         setTimeout(function () {
                    //             zTree.editName(treeNode);
                    //         }, 0);
                    //     }
                    // }, 0);
                    $.fancybox({
                        minWidth: '80%',
                        minHeight: '100%',
                        type: 'iframe',
                        href: '${contextPath}/permission/edit?id=' + treeNode.id,
                        afterClose: function () {
                            ajaxReload(treeId);
                        }
                    });
                    return false;
                },
                beforeRemove: function (treeId, treeNode) {
                    noty.confirm('确认删除【' + treeNode.name + '】吗?', function (yes) {
                        if (yes) {
                            $.post('${contextPath}/permission/deleteSubmit', {id: treeNode.id}, function () {
                                ajaxReload(treeId);
                            }, 'json');
                        }
                    });
                    return false;
                }
            },
            view: {
                showIcon: true,
                showLine: true,
                selectedMulti: false,
                dblClickExpand: false,
                addHoverDom: function (treeId, treeNode) {
                    if (treeNode.editNameFlag || $('#' + treeNode.tId + '_add').length > 0) {
                        return;
                    }
                    var addStr = '<span class="button add" id="{0}_add" title="create" onfocus="this.blur();"></span>';
                    $('#' + treeNode.tId + '_span').after(Base.format(addStr, treeNode.tId));
                    var $btn = $('#' + treeNode.tId + '_add');
                    $btn && $btn.bind('click', function () {
                        $.fancybox({
                            minWidth: '80%',
                            minHeight: '100%',
                            type: 'iframe',
                            href: '${contextPath}/permission/add?parentId=' + treeNode.id,
                            afterClose: function () {
                                ajaxReload(treeId);
                            }
                        });
                    });

                },
                removeHoverDom: function (treeId, treeNode) {
                    $('#' + treeNode.tId + '_add').unbind().remove();
                }
            },
            edit: {
                enable: true
            }
        };

        $.fn.zTree.init($('#tree'), setting);

        var ajaxReload = function (treeId) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            var parentNode = zTree.getNodeByTId(treeId);
            zTree.selectNode(parentNode);
            zTree.reAsyncChildNodes(parentNode, 'refresh', false);
        };

    });
    //-->
</script>

</body>
</html>
