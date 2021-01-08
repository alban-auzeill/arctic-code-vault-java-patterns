package io.quarkus.devtools.codestarts.core; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/independent-projects/tools/codestarts/src/main/java/io/quarkus/devtools/codestarts/core/CodestartPathProcessor.java

import io.quarkus.devtools.codestarts.CodestartStructureException;
import io.quarkus.devtools.codestarts.utils.NestedMaps;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CodestartPathProcessor {

    private static final String PACKAGE_NAME_KEY = "package-name";
    private static final String PACKAGE_NAME_DIR_EXPRESSION = "package-name.dir";
    private static Pattern EXPRESSION_PATTERN = Pattern.compile("\\{([^}]+)}");

    private CodestartPathProcessor() {
    }

    static String process(String relativePath, Map<String, Object> data) {
        if (!relativePath.contains("{")) {
            return relativePath;
        }
        Matcher m = EXPRESSION_PATTERN.matcher(relativePath);
        StringBuffer result = new StringBuffer();
        while (m.find()) {
            String found = m.group(1);
            String key = found.replace(PACKAGE_NAME_DIR_EXPRESSION, PACKAGE_NAME_KEY);
            String value = NestedMaps.<String> getValue(data, key)
                    .orElseThrow(() -> new CodestartStructureException("Missing required data for PathGen: {" + key + "}"));
            if (found.contains(PACKAGE_NAME_DIR_EXPRESSION) && value.contains(".")) {
                value = value.replace(".", "/");
            }
            m.appendReplacement(result, value);
        }
        m.appendTail(result);
        return result.toString();
    }

}
