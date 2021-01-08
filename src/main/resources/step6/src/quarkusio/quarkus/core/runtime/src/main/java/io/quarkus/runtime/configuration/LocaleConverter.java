package io.quarkus.runtime.configuration; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/core/runtime/src/main/java/io/quarkus/runtime/configuration/LocaleConverter.java

import static io.quarkus.runtime.configuration.ConverterSupport.DEFAULT_QUARKUS_CONVERTER_PRIORITY;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * A converter to support locales.
 */
@Priority(DEFAULT_QUARKUS_CONVERTER_PRIORITY)
public class LocaleConverter implements Converter<Locale>, Serializable {

    private static final Pattern NORMALIZE_LOCALE_PATTERN = Pattern.compile("_");

    public LocaleConverter() {
    }

    @Override
    public Locale convert(final String value) {
        String localeValue = value.trim();

        if (localeValue.isEmpty()) {
            return null;
        }

        Locale locale = Locale.forLanguageTag(NORMALIZE_LOCALE_PATTERN.matcher(localeValue).replaceAll("-"));
        if (locale != Locale.ROOT && (locale.getLanguage() == null || locale.getLanguage().isEmpty())) {
            throw new IllegalArgumentException("Unable to resolve locale: " + value);
        }

        return locale;
    }
}
