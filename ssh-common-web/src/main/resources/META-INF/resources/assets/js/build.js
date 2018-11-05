/*
 * https://github.com/requirejs/r.js/blob/master/build/example.build.js
 */
({
    baseUrl: './',
    mainConfigFile: 'config.js',
    optimizeCss: 'standard',
    include: [
        'select2',
        'datatables.net',
        'noty',
        'datepicker'
    ],
    out: 'app-built.js'
})