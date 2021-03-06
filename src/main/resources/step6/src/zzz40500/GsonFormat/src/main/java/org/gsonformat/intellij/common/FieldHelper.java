package org.gsonformat.intellij.common; // (rank 990) copied from https://github.com/zzz40500/GsonFormat/blob/23c0f5f6f1d24d96d369c58aacdda05e5a871eb3/src/main/java/org/gsonformat/intellij/common/FieldHelper.java


import org.gsonformat.intellij.config.Constant;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dim on 17/1/21.
 */
public class FieldHelper {


    public static String generateLuckyFieldName(String name) {

        if (name == null) {
            return Constant.DEFAULT_PREFIX + new Random().nextInt(333);
        }
        Matcher matcher = Pattern.compile("(\\w+)").matcher(name);
        StringBuilder sb = new StringBuilder("_$");
        while (matcher.find()) {
            sb.append(StringUtils.captureName(matcher.group(1)));
        }
        return sb.append(new Random().nextInt(333)).toString();
    }


}
