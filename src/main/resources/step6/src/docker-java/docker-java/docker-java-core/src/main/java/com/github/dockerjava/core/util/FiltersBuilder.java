package com.github.dockerjava.core.util; // (rank 445) copied from https://github.com/docker-java/docker-java/blob/c3661f3da85ebf4f9caea5a75d1c9224d2937876/docker-java-core/src/main/java/com/github/dockerjava/core/util/FiltersBuilder.java

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representation of Docker filters.
 *
 * @author Carlos Sanchez <carlos@apache.org>
 *
 */
public class FiltersBuilder {

    private static final Pattern UNTIL_TIMESTAMP_PATTERN =
            Pattern.compile("^\\d{1,10}$");
    private static final Pattern UNTIL_DATETIME_PATTERN =
            Pattern.compile("^([0-9]+)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])([Tt]([01][0-9]|2[0-3]):([0-5][0-9]):"
                          + "([0-5][0-9]|60)(\\.[0-9]+)?)?(([Zz])|([\\+|\\-]([01][0-9]|2[0-3]):[0-5][0-9]))?$");
    private static final Pattern UNTIL_GO_PATTERN =
            Pattern.compile("^([1-9][0-9]*h)?([1-9][0-9]*m)?([1-9][0-9]*s)?$");

    private Map<String, List<String>> filters = new HashMap<>();

    public FiltersBuilder() {
    }

    public FiltersBuilder withFilter(String key, String... value) {
        filters.put(key, Arrays.asList(value));
        return this;
    }

    public FiltersBuilder withFilter(String key, Collection<String> value) {
        filters.put(key, value instanceof List ? (List<String>) value : new ArrayList<>(value));
        return this;
    }

    public List<String> getFilter(String key) {
        return filters.get(key);
    }

    public FiltersBuilder withImages(String... image) {
        withFilter("image", image);
        return this;
    }

    public List<String> getImage() {
        return getFilter("image");
    }

    public FiltersBuilder withContainers(String... container) {
        withFilter("container", container);
        return this;
    }

    public List<String> getContainer() {
        return getFilter("container");
    }

    /**
     * Filter by event types
     *
     * @param eventTypes an array of event types
     */
    public FiltersBuilder withEventTypes(String... eventTypes) {
        withFilter("type", Stream.of(eventTypes).collect(Collectors.toList()));
        return this;
    }

    /**
     * Filter by labels
     *
     * @param labels
     *            string array in the form ["key"] or ["key=value"] or a mix of both
     */
    public FiltersBuilder withLabels(String... labels) {
        withFilter("label", labels);
        return this;
    }

    /**
     * Filter by labels
     *
     * @param labels
     *            {@link Map} of labels that contains label keys and values
     */
    public FiltersBuilder withLabels(Map<String, String> labels) {
        withFilter("label", labelsMapToList(labels).toArray(new String[labels.size()]));
        return this;
    }

    public FiltersBuilder withUntil(String until) throws NumberFormatException {
        if (!isValidUntil(until)) {
            throw new NumberFormatException("Not valid format of 'until': " + until);
        }

        return withFilter("until", until);
    }

    private static List<String> labelsMapToList(Map<String, String> labels) {
        List<String> result = new ArrayList<>();
        for (Entry<String, String> entry : labels.entrySet()) {
            String rest = (entry.getValue() != null & !entry.getValue().isEmpty()) ? "=" + entry.getValue() : "";

            String label = entry.getKey() + rest;

            result.add(label);
        }
        return result;
    }

    private boolean isValidUntil(String until) {
        if (UNTIL_DATETIME_PATTERN.matcher(until).matches()) {
            return true;
        } else if (!Strings.isNullOrEmpty(until) && UNTIL_GO_PATTERN.matcher(until).matches()) {
            return true;
        } else if (UNTIL_TIMESTAMP_PATTERN.matcher(until).matches()) {
            return true;
        }

        return false;
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FiltersBuilder filters1 = (FiltersBuilder) o;

        return filters.equals(filters1.filters);

    }

    // CHECKSTYLE:ON

    @Override
    public int hashCode() {
        return filters.hashCode();
    }

    public Map<String, List<String>> build() {
        return filters;
    }
}
