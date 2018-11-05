// define(function (require, exports, module) {
//     var $ = require('jquery');
// });
define([
    'jquery',
    'noty',
    'string.ext'
], function ($, noty) {
    'use strict';
    $.extend($.noty.defaults, {
        layout: 'bottomRight', // top(Left|Center|Right) center(Left|Right) bottom(Left|Center|Right)
        theme: 'relax',        // defaultTheme|bootstrapTheme|relax
        type: 'success',       // alert|success|error|warning|information|confirmation
        text: '',              // can be html or string
        dismissQueue: true,    // If you want to use queue feature set this true
        timeout: false,        // delay for closing event. Set false for sticky notifications
        force: false,          // adds notification to the beginning of queue when set to true
        modal: false,          // modal dialog
        maxVisible: 5,         // you can set max visible notification for dismissQueue true option,
        killer: true,          // for close all notifications before show
        closeWith: ['click'],  // ['click', 'button', 'hover', 'backdrop'] // backdrop click will close all notifications
        buttons: false         // an array of buttons
    });

    var methods = {
        processing: function () {
            return noty({
                text: '<i class="fa fa-spinner fa-spin fa-lg"></i> Loading...',
                type: 'alert',
                layout: 'center',
                modal: true,
                closeWith: []
            });
        },
        alert: function (msg, msgType, callback) {
            var title = '提示';
            var icon = '';
            var text = '';
            switch (msgType) {
                case 'success':
                    icon = '<i class="fa fa-info-circle fa-2x"></i>';
                    title = '成功提示';
                    break;
                case 'error':
                    icon = '<i class="fa fa-times-circle fa-2x"></i>';
                    title = '失败提示';
                    break;
                case 'warn':
                    icon = '<i class="fa fa-warning fa-2x"></i>';
                    title = '警告';
                    break;
                case 'question':
                    icon = '<i class="fa fa-question-circle fa-2x"></i>';
                    title = '确认';
                    break;
                default:
                    break;
            }
            if (msgType) {
                text = String.format('{0}<strong class="h4">{1}：{2}</strong>', icon, title, msg);
            } else {
                text = String.format('<strong class="h4">{0}：{1}</strong>', title, msg);
            }
            return noty({
                text: text,
                type: 'alert',
                layout: 'topCenter',
                modal: true,
                buttons: [{
                    addClass: 'btn btn-default btn-sm',
                    text: '确定',
                    onClick: function ($noty) {
                        $noty.close();
                        if (typeof callback === 'function') {
                            callback();
                        }
                    }
                }]
            });
        },
        confirm: function (msg, callback, options) {
            var icon = '<i class="fa fa-question-circle fa-2x"></i>';
            var title = options && options.title ? options.title : '确认提示';
            var text = String.format('{0}<strong class="h4">{1}：{2}</strong>', icon, title, msg);
            return noty({
                text: text,
                type: 'alert',
                layout: 'topCenter',
                modal: true,
                buttons: [{
                    addClass: 'btn btn-primary btn-sm',
                    text: options && options.buttonOk ? options.buttonOk : '确定',
                    onClick: function ($noty) {
                        $noty.close();
                        if (typeof callback === 'function') {
                            callback(true);
                        }
                    }
                }, {
                    addClass: 'btn btn-default btn-sm',
                    text: options && options.buttonCancel ? options.buttonCancel : '取消',
                    onClick: function ($noty) {
                        $noty.close();
                        if (typeof callback === 'function') {
                            callback(false);
                        }
                    }
                }]
            });
        },
        message: function (msg, success) {
            if (typeof success !== 'boolean') {
                success = true;
            }
            return noty({
                text: msg,
                type: success ? 'success' : 'error',
                layout: 'bottomRight',
                modal: false,
                timeout: 0
            });
        }
    };

    $.processing = noty.processing = methods['processing'];
    $.alert = noty.alert = methods['alert'];
    $.confirm = noty.confirm = methods['confirm'];
    $.message = noty.message = methods['message'];

    return noty;
});
