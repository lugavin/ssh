define([
    'jquery',
    'cookie'
], function ($) {

    'use strict';

    var log = function (s) {
        if (console !== undefined) {
            console.log("store.js: " + s);
        }
        return null;
    };

    if ($.cookie === undefined) {
        throw "Missing jquery.cookie.js";
    }

    if (JSON === undefined) {
        throw "Missing json2.js";
    }

    var IS_SET = "___isSet___";

    var methods = {
        get: function (key) {
            var o = $.cookie(key);
            if (!o) {
                return null;
            }
            if ($.isPlainObject(o) && o[IS_SET] === true) {
                var vals = [];
                for (var val in o) if (val !== IS_SET) vals.push(val);
                return vals;
            }
            return o;
        },
        set: function (key, keyval, val) {
            if (val === undefined) {
                return $.cookie(key, keyval), keyval;
            }
            var o = $.cookie(key) || {};
            if (!$.isPlainObject(o)) {
                return log("Setting key on a non-object");
            }
            if (!val) {
                delete o[keyval];
            }
            else {
                o[keyval] = val;
            }
            $.cookie(key, o);
            return val;
        },
        push: function (key, val) {
            var a = $.cookie(key) || [];
            if (!$.isArray(a)) {
                return log("Pushing to a non-array");
            }
            a.push(val);
            $.cookie(key, a);
            return a;
        },
        pop: function (key) {
            var a = $.cookie(key);
            if (!a) {
                return null;
            }
            if (!$.isArray(a)) {
                return log("Popping a non-array");
            }
            if (a.pop === undefined) {
                return;
            }
            var val = a.pop();
            $.cookie(key, a);
            return val;
        },
        add: function (key, val) {
            if (!$.cookie(key)) {
                methods.set(key, IS_SET, true);
            }
            return methods.set(key, val, val);
        },
        remove: function (key, val) {
            return methods.set(key, val, null);
        }
    };

    $.store = methods;

    return methods;
});