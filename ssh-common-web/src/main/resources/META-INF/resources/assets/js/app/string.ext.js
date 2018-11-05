define(function () {
    'use strict';
    var methods = {
        format: function () {
            if (arguments.length == 0) {
                return null;
            }
            var str = arguments[0];
            for (var i = 1; i < arguments.length; i++) {
                var reg = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
                str = str.replace(reg, arguments[i]);
            }
            return str;
        }
    };

    String.prototype.format = function () {
        var str = this;
        for (var i = 0; i < arguments.length; i++) {
            var regexp = new RegExp('\\{' + i + '\\}', 'gi');
            str = str.replace(regexp, arguments[i]);
        }
        return str;
    };

    if (!String.format) {
        String.format = methods['format'];
    }

    return methods;

});
