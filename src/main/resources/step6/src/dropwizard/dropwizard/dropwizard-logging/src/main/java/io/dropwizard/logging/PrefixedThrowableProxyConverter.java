package io.dropwizard.logging; // (rank 105) copied from https://github.com/dropwizard/dropwizard/blob/05bdecbe59366d4c747aa2f0256dcd768bd6b6e6/dropwizard-logging/src/main/java/io/dropwizard/logging/PrefixedThrowableProxyConverter.java

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

import java.util.regex.Pattern;

/**
 * A {@link ThrowableProxyConverter} which prefixes stack traces with {@code !}.
 */
public class PrefixedThrowableProxyConverter extends ThrowableProxyConverter {

    static final Pattern PATTERN = Pattern.compile("^\\t?", Pattern.MULTILINE);
    static final String PREFIX = "! ";

    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return PATTERN.matcher(super.throwableProxyToString(tp)).replaceAll(PREFIX);
    }
}
