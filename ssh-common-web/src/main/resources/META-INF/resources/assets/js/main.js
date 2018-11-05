define(function(require, exports, module) {

    var $ = require('jquery');
    require('select2');
    require('noty');
    require('datepicker');
    require('datatables.net');

    $.extend($.fn.dataTable.defaults, {
        serverSide: false,
        paging: true,
        lengthChange: true,
        pagingType: 'full_numbers',
        lengthMenu: [10, 20, 50, 100, 200, 500],
        displayStart: 0,
        displayLength: 10,
        ordering: true,
        searching: true,
        processing: true,
        deferRender: true
    });

    return $;

});
