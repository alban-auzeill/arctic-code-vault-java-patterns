package com.xuexiang.xui.widget.edittext.materialedittext.validation; // (rank 837) copied from https://github.com/xuexiangjys/XUI/blob/a534a4bba9f10c28ee7a2be4b71fd47d60fef930/xui_lib/src/main/java/com/xuexiang/xui/widget/edittext/materialedittext/validation/RegexpValidator.java

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * 正则表达式验证
 *
 * @author xuexiang
 * @since 2018/11/26 下午5:06
 */
public class RegexpValidator extends METValidator {

    private Pattern pattern;

    public RegexpValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage);
        pattern = Pattern.compile(regex);
    }

    public RegexpValidator(@NonNull String errorMessage, @NonNull Pattern pattern) {
        super(errorMessage);
        this.pattern = pattern;
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        return pattern.matcher(text).matches();
    }
}
