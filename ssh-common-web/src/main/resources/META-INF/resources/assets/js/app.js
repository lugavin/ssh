/**
 * @namespace Base
 */
(function (window, undefined) {

    'use strict';

    var Base = {};

    Base.version = '1.0';
    Base.locale = navigator.language || navigator.userLanguage;

    Base.format = function () {
        if (arguments.length == 0) {
            return null;
        }
        var str = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            str = str.replace(new RegExp('\\{' + (i - 1) + '\\}', 'gm'), arguments[i]);
        }
        return str;
    };

    Base.formatObject = function (str, obj) {
        /**
         * \s 匹配任何空白字符
         * /g 直接量语法(/regexp/g) 用于执行全局匹配
         */
        return str.replace(/\{\s*([^}\s]+)\s*\}/g, function (match, p1, offset, string) {
            return obj[p1]
        });
    };

    if (typeof window === 'object' && typeof window.document === 'object') {
        window.Base = Base;
    }

})(window);
