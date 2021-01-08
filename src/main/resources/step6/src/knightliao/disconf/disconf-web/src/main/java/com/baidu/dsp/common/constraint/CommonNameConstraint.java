package com.baidu.dsp.common.constraint; // (rank 729) copied from https://github.com/knightliao/disconf/blob/d413cbce9334fe38a5a24982ce4db3a6ed8e98ea/disconf-web/src/main/java/com/baidu/dsp/common/constraint/CommonNameConstraint.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * 中文正则匹配校验
 *
 * @author zhangbi
 * @date 2014年1月16日下午4:50:15
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "^[a-zA-Z0-9\u4e00-\u9fa5\ufe30-\uffa0_-]+$", message = "chinese.pattern.error")
public @interface CommonNameConstraint {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
