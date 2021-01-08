package com.baeldung.swagger2boot.plugin; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/spring-boot-modules/spring-boot-mvc/src/main/java/com/baeldung/swagger2boot/plugin/EmailAnnotationPlugin.java

import static springfox.bean.validators.plugins.Validators.annotationFromBean;

import java.util.Optional;

import javax.validation.constraints.Email;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import springfox.bean.validators.plugins.Validators;
import springfox.documentation.builders.StringElementFacetBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Component
@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class EmailAnnotationPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    /**
     * read Email annotation
     */
    @Override
    public void apply(ModelPropertyContext context) {
        Optional<Email> email = annotationFromBean(context, Email.class);
        if (email.isPresent()) {
            context.getSpecificationBuilder().facetBuilder(StringElementFacetBuilder.class)
              .pattern(email.get().regexp());
            context.getSpecificationBuilder().example("email@email.com");
        }
    }

}

