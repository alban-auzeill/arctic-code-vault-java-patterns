/* (rank 523) copied from https://github.com/hibernate/hibernate-validator/blob/5f27bf7aba181f42c91adba7e306505acaa36d99/engine/src/main/java/org/hibernate/validator/internal/engine/messageinterpolation/util/InterpolationHelper.java
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.engine.messageinterpolation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities used for message interpolation.
 *
 * @author Guillaume Smet
 */
public class InterpolationHelper {

	public static final char BEGIN_TERM = '{';
	public static final char END_TERM = '}';
	public static final char EL_DESIGNATOR = '$';
	public static final char ESCAPE_CHARACTER = '\\';

	private static final Pattern ESCAPE_MESSAGE_PARAMETER_PATTERN = Pattern.compile( "([\\" + ESCAPE_CHARACTER + BEGIN_TERM + END_TERM + EL_DESIGNATOR + "])" );

	private InterpolationHelper() {
	}

	public static String escapeMessageParameter(String messageParameter) {
		if ( messageParameter == null ) {
			return null;
		}
		return ESCAPE_MESSAGE_PARAMETER_PATTERN.matcher( messageParameter ).replaceAll( Matcher.quoteReplacement( String.valueOf( ESCAPE_CHARACTER ) ) + "$1" );
	}

}
