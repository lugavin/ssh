/**
 * Declare this variable before loading RequireJS JavaScript library
 * To config RequireJS after it’s loaded, pass the below object into require.config();
 * Configure loading modules from the lib directory.
 * For any third party dependencies, like jQuery, place them in the lib folder.
 */
var require = {
    'baseUrl': 'assets/js',
    'paths': {
        'jquery': 'lib/jquery/jquery',
        'bootstrap': 'lib/bootstrap/bootstrap',
        'jquery.ui': 'lib/jquery-ui/jquery-ui',
        'moment': 'lib/moment/moment',
        'datepicker': 'lib/bootstrap-datetimepicker/bootstrap-datetimepicker',
        'fileinput': 'lib/bootstrap-fileinput/fileinput',
        'maxlength': 'lib/bootstrap-maxlength/bootstrap-maxlength',
        'typeahead': 'lib/bootstrap-typeahead/bootstrap-typeahead',
        'wizard': 'lib/bootstrap-wizard/jquery.bootstrap.wizard',
        'bootstrapValidator': 'lib/bootstrapValidator/bootstrapValidator',
        'backstretch': 'lib/jquery-backstretch/jquery.backstretch',
        'colorbox': 'lib/jquery-colorbox/jquery.colorbox',
        'jquery.grid': 'lib/jquery-jqGrid/jquery.jqGrid',
        'datatables.net': 'lib/jquery-dataTables/jquery.dataTables',
        'datatables.net-bs': 'lib/jquery-dataTables/dataTables.bootstrap',
        'datatables.net-buttons': 'lib/jquery-dataTables-ext/buttons/dataTables.buttons',
        'datatables.net-buttons-bs': 'lib/jquery-dataTables-ext/buttons/buttons.bootstrap',
        'datatables.net-buttons-colVis': 'lib/jquery-dataTables-ext/buttons/buttons.colVis',
        'datatables.net-select': 'lib/jquery-dataTables-ext/select/dataTables.select',
        'easing': 'lib/jquery-easing/jquery.easing',
        'fancybox': 'lib/jquery-fancyBox/jquery.fancybox',
        'fullpage': 'lib/jquery-fullpage/jquery.fullpage',
        'metisMenu': 'lib/jquery-metisMenu/metisMenu',
        'noty': 'lib/jquery-noty/packaged/jquery.noty.packaged',
        'nprogress': 'lib/nprogress/nprogress',
        'select2': 'lib/jquery-select2/select2.full',
        'ztree': 'lib/jquery-zTree/jquery.ztree.all',
        'cookie': 'lib/jquery-cookie/jquery.cookie',
        'jsrender': 'lib/jsrender/jsrender',
        'jsviews': 'lib/jsviews/jsviews',
        'underscore': 'lib/underscore/underscore',
        'backbone': 'lib/backbone/backbone',
        'dust.full': 'lib/dustjs/dust-full',
        'dust.helpers': 'lib/dustjs-helpers/dust-helpers',
        'math.ext': 'app/math.ext',
        'string.ext': 'app/string.ext',
        'cookie.ext': 'app/cookie.ext'
    },
    /**
     * Remember: only use shim config for non-AMD scripts, scripts that do not already call define().
     * The shim config will not work correctly if used on AMD scripts,
     * in particular, the exports and init config will not be triggered,
     * and the deps config will be confusing for those cases.
     */
    'shim': {
        'bootstrap': {
            /**
             * These script dependencies should be loaded before loading bootstrap.js
             */
            deps: ['jquery'],
            /**
             * Once loaded, use the global 'bootstrap' as the module value.
             */
            exports: 'bootstrap',
            /**
             * Using a function allows you to call noConflict for libraries that support it, and do other cleanup.
             * However, plugins for those libraries may still want a global.
             * "this" for the function will be the global object.
             * The dependencies will be passed in as function arguments.
             * If this function returns a value, then that value is used as the module export value instead of the object found via the 'exports' string.
             * Note: jQuery registers as an AMD module via define(), so this will not work for jQuery.
             * See notes section below for an approach for jQuery.
             */
            init: function ($) {
                $('body').tooltip({
                    selector: '[data-toggle="tooltip"]',
                    container: 'body'
                }).popover({
                    selector: '[data-toggle="popover"]',
                    container: 'body',
                    trigger: 'focus'
                });
                $(':input.form-control.required').each(function () {
                    $('label.control-label[for="' + this.id + '"]').addClass('control-label-required');
                });
            }
        },
        'bootstrapValidator': {
            deps: ['jquery'],
            exports: 'bootstrapValidator',
            init: function ($) {
                $(':input.form-control').closest('.form-group, [class*="col-"]').addClass('form-validator');
                $.fn.bootstrapValidator.DEFAULT_OPTIONS = $.extend({}, $.fn.bootstrapValidator.DEFAULT_OPTIONS, {
                    group: '.form-validator',   // '.form-group'
                    verbose: false,             // 多个验证器顺序执行,若前面的验证不通过则不再执行后续校验
                    container: null,            // ['tooltip', 'popover']
                    excluded: [':disabled', ':hidden', ':not(:visible)'],
                    message: 'The field is not valid',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    }
                });
                $.extend({
                    formValidator: function (form) {
                        var $validator = $(form).data('bootstrapValidator');
                        if (!$validator) {
                            return false;
                        }
                        var isValid = $validator.validate().isValid();
                        if (!isValid) {
                            $('.has-error:first :input').focus();
                        }
                        return isValid;
                    }
                });
                $('[data-provide="datepicker"][data-bv-notempty="true"]').on('dp.change dp.show', function () {
                    $(this).closest('form').bootstrapValidator('revalidateField', this.name);
                });
            }
        },
        'maxlength': ['jquery'],
        'colorbox': ['jquery'],
        'wizard': ['jquery'],
        'backstretch': ['jquery'],
        'easing': ['jquery'],
        'fancybox': {
            deps: ['jquery'],
            exports: 'fancybox',
            init: function ($) {
                $.extend($.fancybox.defaults, {
                    closeBtn: true,
                    closeClick: false,
                    helpers: {
                        overlay: {closeClick: false}
                    }
                });
                $('a.fancybox').fancybox({
                    afterLoad: function () {
                        this.minWidth = $(this.element).data('fancybox-width') || $.fancybox.defaults.width;
                        this.minHeight = $(this.element).data('fancybox-height') || $.fancybox.defaults.height;
                    }
                });
            }
        },
        'ztree': ['jquery']
    },
    'map': {
        '*': {
            // When any module does 'require("jquery")' it will get the 'jquery.ext.js' file.
            'jquery': 'app/jquery.ext',
            'select2': 'app/select2.ext',
            'datatables.net': 'app/datatables.ext',
            'noty': 'app/noty.ext',
            'datepicker': 'app/datepicker.ext'
        },
        'app/jquery.ext': {
            'jquery': 'jquery'
        },
        'app/select2.ext': {
            'select2': 'select2'
        },
        'app/datatables.ext': {
            'datatables.net': 'datatables.net'
        },
        'app/noty.ext': {
            'noty': 'noty'
        },
        'app/datepicker.ext': {
            'datepicker': 'datepicker'
        }
    },
    'urlArgs': 'v=1.0.0'
};
