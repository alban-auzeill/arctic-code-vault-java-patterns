package io.cucumber.core.options; // (rank 310) copied from https://github.com/cucumber/cucumber-jvm/blob/a64adbe2264dab2d4a5ee0f2d92d1b193b800a18/core/src/main/java/io/cucumber/core/options/OptionsFileParser.java

import io.cucumber.core.exception.CucumberException;
import io.cucumber.core.feature.FeatureWithLines;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.nio.file.Files.readAllLines;

class OptionsFileParser {

    private static final Pattern RERUN_PATH_SPECIFICATION = Pattern.compile("(?m:^| |)(.*?\\.feature(?:(?::\\d+)*))");

    private OptionsFileParser() {

    }

    static Collection<FeatureWithLines> parseFeatureWithLinesFile(Path path) {
        try {
            List<FeatureWithLines> featurePaths = new ArrayList<>();
            readAllLines(path).forEach(line -> {
                Matcher matcher = RERUN_PATH_SPECIFICATION.matcher(line);
                while (matcher.find()) {
                    featurePaths.add(FeatureWithLines.parse(matcher.group(1)));
                }
            });
            return featurePaths;
        } catch (Exception e) {
            throw new CucumberException(format("Failed to parse '%s'", path), e);
        }
    }

}
