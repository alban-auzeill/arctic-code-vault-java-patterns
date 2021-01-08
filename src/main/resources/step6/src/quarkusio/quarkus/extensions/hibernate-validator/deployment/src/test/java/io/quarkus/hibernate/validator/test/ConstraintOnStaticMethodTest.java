package io.quarkus.hibernate.validator.test; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/extensions/hibernate-validator/deployment/src/test/java/io/quarkus/hibernate/validator/test/ConstraintOnStaticMethodTest.java

import javax.inject.Inject;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

public class ConstraintOnStaticMethodTest {

    @Inject
    ValidatorFactory validatorFactory;

    @RegisterExtension
    static final QuarkusUnitTest test = new QuarkusUnitTest().setArchiveProducer(() -> ShrinkWrap
            .create(JavaArchive.class).addClasses(MyUtil.class));

    @Test
    public void testStaticMethodIsIgnored() {
        MyUtil.validateName("My name");
    }

    public static class MyUtil {

        public static void validateName(@Pattern(regexp = "A.*") String name) {
            // do nothing
        }
    }
}
