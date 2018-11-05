define(function (require, exports, module) {

    var jQuery = require('jquery');

    jQuery.fn.serializeObject = function () {
        var obj = {};
        var arr = this.serializeArray();
        jQuery.each(arr, function () {
            if (obj[this.name]) {
                if (!obj[this.name].push) {
                    obj[this.name] = [obj[this.name]];
                }
                obj[this.name].push(this.value || '');
            } else {
                obj[this.name] = this.value || '';
            }
        });
        return obj;
    };

    jQuery('form').keypress(function (event) {
        if (event.keyCode === 13) {
            var $this = jQuery(this);
            if ($this.find('[type="submit"]').length > 0) {
                return true;
            } else {
                var $btn = $this.find('[type="button"][accesskey="enter"]');
                $btn.length > 0 && $($btn.get(0)).click();
                return false;
            }
        }
    });

    return jQuery;

});
