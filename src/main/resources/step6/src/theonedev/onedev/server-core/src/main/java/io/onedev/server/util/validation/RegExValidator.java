package io.onedev.server.util.validation; // (rank 413) copied from https://github.com/theonedev/onedev/blob/f34af86d0d952ec210e84e98fbd02102e6fe230a/server-core/src/main/java/io/onedev/server/util/validation/RegExValidator.java

import java.util.function.Function;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.onedev.commons.utils.StringUtils;
import io.onedev.server.util.interpolative.Interpolative;
import io.onedev.server.util.validation.annotation.RegEx;

public class RegExValidator implements ConstraintValidator<RegEx, String> {

	private boolean interpolative;
		
	private Pattern pattern;
	
	private String message;
	
	@Override
	public void initialize(RegEx constaintAnnotation) {
		interpolative = constaintAnnotation.interpolative();
		pattern = Pattern.compile(constaintAnnotation.pattern());
		message = constaintAnnotation.message();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
		if (value == null) 
			return true;
		
		if (interpolative && !Interpolated.get()) try {
			value = StringUtils.unescape(Interpolative.parse(value).interpolateWith(new Function<String, String>() {

				@Override
				public String apply(String t) {
					return "a";
				}
				
			}));
		} catch (Exception e) {
			return true; // will be handled by interpolative validator
		}
		if (pattern.matcher(value).matches()) {
			return true;
		} else {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
			return false;
		}
	}
	
}
