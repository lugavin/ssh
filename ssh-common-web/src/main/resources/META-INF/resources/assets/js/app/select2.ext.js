define([
    'jquery',
    'select2'
], function ($) {

    'use strict';

    $.fn.select2.defaults.set('language', 'zh-TW');
    $.fn.select2.defaults.set('placeholder', '請選擇.....');
    $.fn.select2.defaults.set('width', '100%');
    $.fn.select2.defaults.set('tags', false);
    $.fn.select2.defaults.set('multiple', false);
    $.fn.select2.defaults.set('allowClear', true);
    $.fn.select2.defaults.set('closeOnSelect', true);
    $.fn.select2.defaults.set('minimumInputLength', 0);
    $.fn.select2.defaults.set('theme', 'bootstrap');

    $(':reset').click(function () {
        $.initSelection('.select2-hidden-accessible');
    });

    $('select[data-provide="select2"]').select2();

    $.extend({
        initSelection: function (selector, text, value) {
            var option = new Option(text || $.fn.select2.defaults.placeholder, value || '');
            $(option).attr('selected', true);
            $(selector).append(option).trigger('change');
        }
    });

    $.fn.select2Remote = function (options) {
        var opts = $.extend({}, $.fn.select2Remote.defaults, options);  // 将两个或多个对象的内容合并到第一个对象
        return this.select2({
            ajax: {
                url: opts.url,
                data: opts.data,
                type: opts.type,
                delay: opts.delay,
                cache: opts.cache,
                dataType: opts.dataType,
                processResults: opts.processResults
            },
            escapeMarkup: opts.escapeMarkup,
            templateResult: opts.templateResult,
            templateSelection: opts.templateSelection
        });
    };

    $.fn.select2Remote.defaults = {
        url: '',
        type: 'GET',
        delay: 1000,
        cache: true,
        dataType: 'json',
        data: function (params) {
            return {
                'params.keyword': params.term,
                'pageNumber': params.page || 1,
                'pageSize': 15
            };
        },
        processResults: function (result, params) {
            /********************************************************
             * return {
             *   results: $.map(result.data, function (item) {
             *     return {id: item.id, text: item.text}
             *   })
             * };
             ********************************************************/
            return {
                results: result.content,
                pagination: {
                    more: (params.page || 1) * 30 < (result['totalElements'] || 10)
                }
            };
        },
        escapeMarkup: function (markup) {   // Let custom formatter work
            return markup;
        },
        templateResult: function (repo) {
            if (repo.loading) {
                return repo.text;
            }
            return repo.id + ' - ' + repo.text;
        },
        templateSelection: function (repo) {
            // return repo.id || repo.text;
            return repo.text;
        }
    };

    return $.fn.select2Remote;

});
