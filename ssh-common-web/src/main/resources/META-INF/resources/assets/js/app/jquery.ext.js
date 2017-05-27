define([
    'jquery'
], function ($) {

    $.fn.extend({
        serializeObject: function () {
            var result = {};
            $.each(this.serializeArray(), function (i, element) {
                var node = result[element.name];
                // If node with same name exists already, need to convert it to an array as it
                // is a multi-value field (i.e., checkboxes)
                if (typeof node !== 'undefined' && node !== null) {
                    if ($.isArray(node)) {
                        node.push(element.value);
                    } else {
                        result[element.name] = [node, element.value];
                    }
                } else {
                    result[element.name] = element.value;
                }
            });
            return result;
        }
    });

    $('form').keypress(function (event) {
        if (event.keyCode === 13) {
            var $this = $(this);
            if ($this.find('[type="submit"]').length > 0) {
                return true;
            } else {
                var $btn = $this.find('[type="button"][accesskey="enter"]');
                $btn.length > 0 && $($btn.get(0)).click();
                return false;
            }
        }
    });

    return $;

});
