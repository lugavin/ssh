define([
    'jquery',
    'datepicker'
], function ($) {

    $('input[data-provide="datepicker"]').datetimepicker({
        format: 'YYYY/MM/DD'
    });

    $('input[data-provide="datetimepicker"]').datetimepicker({
        format: 'YYYY/MM/DD HH:mm'
    });

});