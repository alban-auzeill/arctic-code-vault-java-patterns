package com.codahale.metrics.graphite; // (rank 164) copied from https://github.com/dropwizard/metrics/blob/1ca6391043830a03520b98d92bdfeb3a7da4d6b6/metrics-graphite/src/main/java/com/codahale/metrics/graphite/GraphiteSanitize.java

import java.util.regex.Pattern;

class GraphiteSanitize {

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final String DASH = "-";

    /**
     * Trims the string and replaces all whitespace characters with the provided symbol
     */
    static String sanitize(String string) {
        return WHITESPACE.matcher(string.trim()).replaceAll(DASH);
    }
}
