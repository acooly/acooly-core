/**
 * acooly verify
 */
var acooly_verify = {

    regExp: function (value, pattern) {
        var re = new RegExp(pattern);
        var result = re.test(value);
        return re.test(value);
    },

    byteLength: function (value, minlen, maxlen) {
        if (value == null) {
            return false;
        }
        var l = value.length, blen = 0;
        for (i = 0; i < l; i++) {
            if ((value.charCodeAt(i) & 65280) != 0) {
                blen++;
            }
            blen++;
        }
        return !(blen > maxlen || blen < minlen);
    },

    cert: function (value) {
        return /^\d{6}[1,2]{1}\d{3}(0+[1-9]|10|11|12)([0,1,2]+[1-9]|30|31)\d{3}[\d,X,x]{1}$/.test(value);
    },

    phone: function (value) {
        return this.regExp(value, "(^0\\d{2}-?\\d{8}$)|(^0\\d{3}-?\\d{7}$)|(^0\\d2-?\\d{8}$)|(^0\\d3-?\\d{7}$)");
    },

    mobile: function (value) {
        var reg = /^1[2|3|4|5|6|7|8|9]\d{9}$/;
        return reg.test(value);
    },

    account: function (value) {
        return !/^[a-zA-Z][\w]+$/.test(value);
    },

    chs: function (value) {
        return /^[\u0391-\uFFE5]+$/.test(value);
    },

    csv: function (value) {
        return /^[\w,\$,\{,\},_,\u4e00-\u9fa5]+(,[\w,\$,\{,\},_,\u4e00-\u9fa5]+)*$/.test(value)
    },
    money: function (value) {
        return /^-?(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(value);
    }
};


(function ($) {
    if (!$.acooly) {
        $.extend({acooly: {}});
    }

    $.extend($.acooly, {
        verify: acooly_verify
    });

})(jQuery);
