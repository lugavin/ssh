define([
    'jquery',
    'select2'
], function ($) {

    $.fn.select2.defaults.set('language', 'zh-TW');
    $.fn.select2.defaults.set('placeholder', '請選擇.....');
    $.fn.select2.defaults.set('width', '100%');
    $.fn.select2.defaults.set('tags', false);
    $.fn.select2.defaults.set('multiple', false);
    $.fn.select2.defaults.set('allowClear', true);
    $.fn.select2.defaults.set('closeOnSelect', true);
    $.fn.select2.defaults.set('minimumInputLength', 0);
    $.fn.select2.defaults.set('theme', 'bootstrap');

    $('select[data-provide="select2"]').select2();

    $(':reset').click(function () {
        $('select.select2-hidden-accessible').val('').change();
    });

    $.fn.select2Remote = function (options) {
        var settings = $.extend({}, this.select2Remote.defaults, options);
        return this.select2({
            ajax: {
                url: settings.url,
                data: settings.data,
                type: settings.type,
                delay: settings.delay,
                cache: settings.cache,
                dataType: settings.dataType,
                processResults: settings.processResults
            },
            escapeMarkup: settings.escapeMarkup,
            templateResult: settings.templateResult,
            templateSelection: settings.templateSelection
        });
    };

    $.fn.select2Remote.defaults = {
        url: '',
        type: 'POST',
        delay: 1000,
        cache: true,
        dataType: 'json',
        data: function (params) {
            return {
                'keyword': params.term,
                'page': params.page || 1,
                'limit': 10
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
                results: result['records'],
                pagination: {
                    more: (params.page || 1) * 30 < (result['totalRecords'] || 10)
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

    return $;

});
