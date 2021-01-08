package io.quarkus.hibernate.validator.test; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/extensions/hibernate-validator/deployment/src/test/java/io/quarkus/hibernate/validator/test/ConstraintValidatorLocalesTest.java

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Pattern;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

public class ConstraintValidatorLocalesTest {

    @Inject
    ValidatorFactory validatorFactory;

    @RegisterExtension
    static final QuarkusUnitTest test = new QuarkusUnitTest().setArchiveProducer(() -> ShrinkWrap
            .create(JavaArchive.class).addClasses(MyBean.class)
            .addAsResource("application.properties")
            .addAsResource("ValidationMessages.properties")
            .addAsResource("ValidationMessages_fr_FR.properties"));

    @Test
    public void testConstraintLocale() {
        assertThat(validatorFactory.getValidator().validate(new MyBean("INVALID"))).asString().contains("Non conforme");
    }

    static class MyBean {

        public MyBean(String name) {
            super();
            this.name = name;
        }

        @Pattern(regexp = "A.*", message = "{pattern.message}")
        private String name;
    }
}
