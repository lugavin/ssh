define([
    'jquery',
    'datepicker'
], function ($) {

    'use strict';

    $.extend($.fn.datetimepicker.defaults, {
        format: 'YYYY/MM/DD'
    });

    $('input[data-provide="datepicker"]').datetimepicker();

    $('input[data-provide="datetimepicker"]').datetimepicker({
        format: 'YYYY/MM/DD HH:mm'
    });

});