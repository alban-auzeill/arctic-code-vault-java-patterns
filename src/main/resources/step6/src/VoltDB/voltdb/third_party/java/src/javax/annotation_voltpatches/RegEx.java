package javax.annotation_voltpatches; // (rank 282) copied from https://github.com/VoltDB/voltdb/blob/0f2993cb9e1efe7c2c95cf68b83f10903e2697d3/third_party/java/src/javax/annotation_voltpatches/RegEx.java

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation_voltpatches.meta.TypeQualifierNickname;
import javax.annotation_voltpatches.meta.TypeQualifierValidator;
import javax.annotation_voltpatches.meta.When;

/**
 * This qualifier is used to denote String values that should be a Regular
 * expression.
 * 
 */
@Documented
@Syntax("RegEx")
@TypeQualifierNickname
@Retention(RetentionPolicy.RUNTIME)
public @interface RegEx {
    When when() default When.ALWAYS;

    static class Checker implements TypeQualifierValidator<RegEx> {

        public When forConstantValue(RegEx annotation, Object value) {
            if (!(value instanceof String))
                return When.NEVER;

            try {
                Pattern.compile((String) value);
            } catch (PatternSyntaxException e) {
                return When.NEVER;
            }
            return When.ALWAYS;

        }

    }

}
