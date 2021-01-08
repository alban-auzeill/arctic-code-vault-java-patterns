package io.dropwizard.logging; // (rank 105) copied from https://github.com/dropwizard/dropwizard/blob/05bdecbe59366d4c747aa2f0256dcd768bd6b6e6/dropwizard-logging/src/main/java/io/dropwizard/logging/PrefixedRootCauseFirstThrowableProxyConverter.java

import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

import java.util.regex.Pattern;

import static io.dropwizard.logging.PrefixedThrowableProxyConverter.PATTERN;
import static io.dropwizard.logging.PrefixedThrowableProxyConverter.PREFIX;

/**
 * A {@link RootCauseFirstThrowableProxyConverter} that prefixes stack traces with {@code !}.
 */
public class PrefixedRootCauseFirstThrowableProxyConverter
        extends RootCauseFirstThrowableProxyConverter {

    private static final String CAUSING = PREFIX + "Causing:";
    private static final Pattern CAUSING_PATTERN = Pattern.compile("^" + Pattern.quote(PREFIX) + "Wrapped by:",
            Pattern.MULTILINE);

    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        final String prefixed = PATTERN.matcher(super.throwableProxyToString(tp)).replaceAll(PREFIX);
        return CAUSING_PATTERN.matcher(prefixed).replaceAll(CAUSING);
    }
}
