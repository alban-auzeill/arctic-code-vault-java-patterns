package org.geoserver.taskmanager.util; // (rank 119) copied from https://github.com/geoserver/geoserver/blob/051d7a91c3dae51e18b71a344f3867687c722b2b/src/community/taskmanager/core/src/main/java/org/geoserver/taskmanager/util/PlaceHolderUtil.java

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderUtil {

    private PlaceHolderUtil() {}

    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile("\\$\\{([^\\}]*)\\}");

    public static String replacePlaceHolders(String pattern, Map<String, Serializable> map) {
        Matcher matcher = PATTERN_PLACEHOLDER.matcher(pattern);
        while (matcher.find()) {
            pattern =
                    pattern.substring(0, matcher.start())
                            + map.get(matcher.group(1)).toString()
                            + pattern.substring(matcher.end());
            matcher = PATTERN_PLACEHOLDER.matcher(pattern);
        }
        return pattern;
    }
}
