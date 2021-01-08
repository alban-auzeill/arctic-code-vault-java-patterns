package com.intuit.karate.validator; // (rank 323) copied from https://github.com/intuit/karate/blob/2b45168df0d8ade7d609b83acf46a942a560fe1f/karate-core/src/main/java/com/intuit/karate/validator/RegexValidator.java

import com.intuit.karate.ScriptValue;
import com.intuit.karate.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pthomas3
 */
public class RegexValidator implements Validator {
    
    private final Pattern pattern;
    
    public RegexValidator(String regex) {
        regex = StringUtils.trimToEmpty(regex);
        pattern = Pattern.compile(regex);
    }

    @Override
    public ValidationResult validate(ScriptValue value) {
        if (!value.isStringOrStream()) {
            return ValidationResult.fail("not a string");
        }
        String strValue = value.getAsString();
        Matcher matcher = pattern.matcher(strValue);
        if (matcher.matches()) {
            return ValidationResult.PASS;
        }
        return ValidationResult.fail("regex match failed");
    }
    
}
