/* (rank 72) copied from https://github.com/spring-projects/spring-security/blob/7ef3f619242816683a72b35a1f8b4fb4f32d5203/samples/xml/gae/src/main/java/samples/gae/validation/ForenameValidator.java
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package samples.gae.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Luke Taylor
 */
public class ForenameValidator implements ConstraintValidator<Forename, String> {
	private static final Pattern VALID = Pattern.compile("[\\p{L}'\\-,.]+");

	public void initialize(Forename constraintAnnotation) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		return VALID.matcher(value).matches();
	}
}
