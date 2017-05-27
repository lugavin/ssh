/*!
 * jQuery TreeMenu Plugin
 * version: v0.1-2016.12.05
 * Requires jQuery v1.5 or later
 * Copyright (c) 2016 Gavin
 * https://github.com/lugavin
 *
 * Uses CommonJS, AMD or browser globals to create a jQuery plugin.
 * https://github.com/umdjs/umd/blob/master/templates/jqueryPlugin.js
 *
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof module === 'object' && module.exports) {
        // Node/CommonJS
        module.exports = function (root, jQuery) {
            if (jQuery === undefined) {
                // require('jQuery') returns a factory that requires window to
                // build a jQuery instance, we normalize how we use modules
                // that require this pattern but the window provided is a noop
                // if it's defined (how jquery works)
                if (typeof window !== 'undefined') {
                    jQuery = require('jquery');
                } else {
                    jQuery = require('jquery')(root);
                }
            }
            factory(jQuery);
            return jQuery;
        };
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    /**
     * @type {{_rebuild: Function, _bindEvent: Function}}
     * @private
     */
    var _methods = {
        _rebuild: function (data, settings) {
            var dataMap = {},
                pidKey = settings.pidKey,
                idKey = settings.idKey,
                target = settings.target;
            $.each(data, function (idx, obj) {
                if (dataMap[obj[pidKey]]) {
                    dataMap[obj[pidKey]].push(obj);
                } else {
                    dataMap[obj[pidKey]] = [];
                    dataMap[obj[pidKey]].push(obj);
                }
            });
            var buildTree = function (group) {
                if (!group) { // Recursive termination condition
                    return;
                }
                var $root = $('<ul>');
                $.each(group, function (idx, obj) {
                    $root.append($('<li>')
                        .append($('<a>').data('data', obj).attr({target: target, href: obj.url}).text(obj.name))
                        .append(buildTree(dataMap[obj[idKey]])));
                });
                return $root;
            };
            return buildTree(dataMap[settings.rootPid]);
        },
        _bindEvent: function (dom, settings) {
            dom.on('click', 'ul>li>a', function (e) {
                var $this = $(this);
                if ($this.parent().hasClass('active')) {
                    $this.next().slideToggle();
                } else {
                    $this.parent().addClass('active');
                    $this.next().slideDown();
                }
                $this.parent().siblings().removeClass('active').find('ul').slideUp().children().removeClass('active');
                if (typeof(settings.onClick) === "function") {
                    settings.onClick.apply($this, [e, this, $this.data('data')]);
                }
            });
            return dom;
        }
    };

    /**
     * Public Method
     */
    var methods = {
        setContainer: function (container) {
            return $(this).data('treeMenu', container);
        },
        getContainer: function () {
            return $(this).data('treeMenu');
        },
        destroyContainer: function () {
            return $(this).removeData('treeMenu');
        },
        setSettings: function (settings) {
            $(this).treeMenu('getContainer').data('settings', settings);
        },
        getSettings: function () {
            if (!$(this).treeMenu('getContainer')) {
                return null;
            }
            return $(this).treeMenu('getContainer').data('settings');
        },
        getSetting: function (name) {
            return $(this).treeMenu('getSettings')[name];
        },
        async: function (settings) {
            var _this = this;
            $.ajax({
                url: settings.url,
                type: 'post',
                data: {},
                dataType: 'json',
                success: function (data) {
                    // console.time('treeMenu');
                    if (typeof(settings.dataFilter) === "function") {
                        data = settings.dataFilter.apply(_this, [data]);
                    }
                    _this.append(_methods._rebuild(data, settings));
                    // console.timeEnd('treeMenu');
                }
            });
        },
        destroy: function () {
            return this.each(function () {
                var $this = $(this);
                $this.treeMenu('destroyContainer');
            });
        },
        init: function (options) {
            var settings = $.extend({}, this.treeMenu.defaults, options);
            return this.each(function () {
                var $this = $(this);
                $this.treeMenu('setContainer', $this);
                $this.treeMenu('setSettings', settings);
                $this.treeMenu('async', settings);
                _methods._bindEvent($this, settings);
            });
        }
    };

    $.fn.treeMenu = function () {
        var method = arguments[0];
        if (methods[method]) {
            // this.foo(arg1, arg2, arg3) == foo.call(this, arg1, arg2, arg3) == foo.apply(this, arguments)
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (!method || typeof(method) == 'object') {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.treeMenu');
            return this;
        }
    };

    $.fn.treeMenu.defaults = {
        url: '',
        idKey: 'id',
        pidKey: 'pid',
        rootPid: null,
        target: '_blank',
        dataFilter: null,
        onClick: null
    };

}));