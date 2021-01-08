package io.quarkus.runtime.configuration; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/core/runtime/src/main/java/io/quarkus/runtime/configuration/RegexConverter.java

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * A converter to support regular expressions.
 */
@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class RegexConverter implements Converter<Pattern>, Serializable {

    private static final long serialVersionUID = -2627801624423530576L;

    /**
     * Construct a new instance.
     */
    public RegexConverter() {
    }

    public Pattern convert(final String value) {
        return value.isEmpty() ? null : Pattern.compile(value);
    }
}
