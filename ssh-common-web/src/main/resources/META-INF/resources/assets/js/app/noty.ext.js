define([
    'jquery',
    'noty'
], function ($, noty) {

    $.extend($.noty.defaults, {
        layout: 'bottomRight',  // top(Left|Center|Right) center(Left|Right) bottom(Left|Center|Right)
        theme: 'relax',         // defaultTheme|bootstrapTheme|relax
        type: 'success',        // alert|success|error|warning|information|confirmation
        text: '',               // can be html or string
        dismissQueue: true,     // If you want to use queue feature set this true
        timeout: false,         // delay for closing event. Set false for sticky notifications
        force: false,           // adds notification to the beginning of queue when set to true
        modal: false,           // modal dialog
        maxVisible: 5,          // you can set max visible notification for dismissQueue true option,
        killer: true,           // for close all notifications before show
        closeWith: ['click'],   // ['click', 'button', 'hover', 'backdrop'] // backdrop click will close all notifications
        buttons: false          // an array of buttons
    });

    noty.processing = function () {
        return noty({
            text: '<i class="fa fa-spinner fa-spin fa-lg"></i> Loading...',
            type: 'alert',
            layout: 'center',
            modal: true,
            closeWith: []
        });
    };

    noty.alert = function (msg, msgType, callback) {
        var args = Array.prototype.slice.call(arguments);
        msg = args.shift();
        if (typeof args[args.length - 1] === 'function') {
            callback = args.pop();
        }
        msgType = args.length > 0 ? args.shift() : null;
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
            text = icon + ' <strong class="h4">' + title + ':' + msg + '</strong>';
        } else {
            text = '<strong class="h4">' + title + ':' + msg + '</strong>';
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
                    callback && callback();
                }
            }]
        });
    };

    noty.confirm = function (msg, options, callback) {
        var args = Array.prototype.slice.call(arguments);
        msg = args.shift();
        if (typeof args[args.length - 1] === 'function') {
            callback = args.pop();
        }
        options = args.length > 0 ? args.shift() : null;
        var icon = '<i class="fa fa-question-circle fa-2x"></i>';
        var title = options && options['title'] ? options['title'] : '确认提示';
        var text = icon + ' <strong class="h4">' + title + ':' + msg + '</strong>';
        return noty({
            text: text,
            type: 'alert',
            layout: 'topCenter',
            modal: true,
            buttons: [{
                addClass: 'btn btn-default btn-sm',
                text: options && options['buttonCancel'] ? options['buttonCancel'] : '取消',
                onClick: function ($noty) {
                    $noty.close();
                    callback && callback(false);
                }
            }, {
                addClass: 'btn btn-primary btn-sm',
                text: options && options['buttonOk'] ? options['buttonOk'] : '确定',
                onClick: function ($noty) {
                    $noty.close();
                    callback && callback(true);
                }
            }]
        });
    };

    noty.message = function (msg, success) {
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
    };

    return noty;
});
