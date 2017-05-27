/*
 * https://github.com/requirejs/r.js/blob/master/build/example.build.js
 */
({
    //By default, all modules are located relative to this path. If baseUrl
    //is not explicitly set, then all modules are loaded relative to
    //the directory that holds the build file. If appDir is set, then
    //baseUrl should be specified as relative to the appDir.
    baseUrl: "./",

    //Allow CSS optimizations. Allowed values:
    //- "standard": @import inlining and removal of comments, unnecessary whitespace and line returns.
    //Removing line returns may have problems in IE, depending on the type of CSS.
    //- "standard.keepLines": like "standard" but keeps line returns.
    //- "none": skip CSS optimizations.
    //- "standard.keepComments": keeps the file comments, but removes line returns.  (r.js 1.0.8+)
    //- "standard.keepComments.keepLines": keeps the file comments and line returns. (r.js 1.0.8+)
    //- "standard.keepWhitespace": like "standard" but keeps unnecessary whitespace.
    optimizeCss: "standard",

    //If optimizeCss is in use, a list of files to ignore for the @import
    //inlining. The value of this option should be a string of comma separated
    //CSS file names to ignore (like 'a.css,b.css'. The file names should match
    //whatever strings are used in the @import calls.
    cssImportIgnore: null,

    //cssIn is typically used as a command line option. It can be used
    //along with out to optimize a single CSS file.
    cssIn: "app.css",
    out: "app.min.css",

    //If "out" is not in the same directory as "cssIn", and there is a relative
    //url() in the cssIn file, use this to set a prefix URL to use. Only set it
    //if you find a problem with incorrect relative URLs after optimization.
    cssPrefix: ""
})