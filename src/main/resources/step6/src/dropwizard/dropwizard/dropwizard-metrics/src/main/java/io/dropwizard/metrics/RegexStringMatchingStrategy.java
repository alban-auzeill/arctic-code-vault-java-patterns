package io.dropwizard.metrics; // (rank 105) copied from https://github.com/dropwizard/dropwizard/blob/05bdecbe59366d4c747aa2f0256dcd768bd6b6e6/dropwizard-metrics/src/main/java/io/dropwizard/metrics/RegexStringMatchingStrategy.java

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.util.Set;
import java.util.regex.Pattern;

class RegexStringMatchingStrategy implements StringMatchingStrategy {
    private final LoadingCache<String, Pattern> patternCache;

    RegexStringMatchingStrategy() {
        patternCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .build(Pattern::compile);
    }

    @Override
    public boolean containsMatch(Set<String> matchExpressions, String metricName) {
        for (String regexExpression : matchExpressions) {
            final Pattern pattern = patternCache.get(regexExpression);
            if (pattern != null && pattern.matcher(metricName).matches()) {
                // just need to match on a single value - return as soon as we do
                return true;
            }
        }
        return false;
    }
}
