package com.baidu.dsp.common.constraint; // (rank 729) copied from https://github.com/knightliao/disconf/blob/d413cbce9334fe38a5a24982ce4db3a6ed8e98ea/disconf-web/src/main/java/com/baidu/dsp/common/constraint/UserNameConstraint.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author liaoqiqi
 * @version 2014-1-14
 */
@NotNull
@Size(min = 3, max = 20)
@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username.error")
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface UserNameConstraint {

    String message() default "username.error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
