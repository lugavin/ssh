define([
    'jquery',
    'datatables.net'
], function ($) {

    $.extend($.fn.dataTable.defaults, {
        paging: true,
        ordering: true,
        searching: true,
        processing: true,
        serverSide: false,
        deferRender: true,
        lengthChange: true,
        deferLoading: null, // Delay the loading of server-side data until second draw
        pagingType: 'full_numbers',
        lengthMenu: [10, 20, 50, 100, 200, 500],
        displayStart: 0,
        displayLength: 10
    });

});
