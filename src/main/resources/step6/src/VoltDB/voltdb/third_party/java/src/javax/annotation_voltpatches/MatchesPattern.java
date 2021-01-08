package javax.annotation_voltpatches; // (rank 282) copied from https://github.com/VoltDB/voltdb/blob/0f2993cb9e1efe7c2c95cf68b83f10903e2697d3/third_party/java/src/javax/annotation_voltpatches/MatchesPattern.java

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

import javax.annotation_voltpatches.meta.TypeQualifier;
import javax.annotation_voltpatches.meta.TypeQualifierValidator;
import javax.annotation_voltpatches.meta.When;

@Documented
@TypeQualifier(applicableTo = String.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesPattern {
    @RegEx
    String value();

    int flags() default 0;

    static class Checker implements TypeQualifierValidator<MatchesPattern> {
        public When forConstantValue(MatchesPattern annotation, Object value) {
            Pattern p = Pattern.compile(annotation.value(), annotation.flags());
            if (p.matcher(((String) value)).matches())
                return When.ALWAYS;
            return When.NEVER;
        }

    }
}
