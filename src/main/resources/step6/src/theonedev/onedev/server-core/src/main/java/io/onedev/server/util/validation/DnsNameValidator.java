package io.onedev.server.util.validation; // (rank 413) copied from https://github.com/theonedev/onedev/blob/ec22b9b803cfc2f8d450cf9e905d26e100d343f2/server-core/src/main/java/io/onedev/server/util/validation/DnsNameValidator.java

import java.util.function.Function;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.onedev.commons.utils.StringUtils;
import io.onedev.server.util.interpolative.Interpolative;
import io.onedev.server.util.validation.annotation.DnsName;

public class DnsNameValidator implements ConstraintValidator<DnsName, String> {

	private static final Pattern PATTERN = Pattern.compile("[a-zA-Z0-9]([-a-zA-Z0-9]*[a-zA-Z0-9])?");
	
	private boolean interpolative;
	
	private String message;
	
	@Override
	public void initialize(DnsName constaintAnnotation) {
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
		
		if (!PATTERN.matcher(value).matches()) {
			String message = this.message;
			if (message.length() == 0) {
				message = "Should only contain alphanumberic characters or '-', and can only "
						+ "start and end with alphanumeric characters";
			}
			constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
			return false;
		} else {
			return true;
		}
	}
	
}
