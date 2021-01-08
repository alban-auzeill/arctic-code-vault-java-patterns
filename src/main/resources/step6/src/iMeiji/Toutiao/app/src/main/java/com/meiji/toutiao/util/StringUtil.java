package com.meiji.toutiao.util; // (rank 1011) copied from https://github.com/iMeiji/Toutiao/blob/e8be6039b09d008a8f06eb7a9a9388354ab0bd48/app/src/main/java/com/meiji/toutiao/util/StringUtil.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Meiji on 2017/7/6.
 */

public class StringUtil {

    public static String getStringNum(String s) {
        String regex = "[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("").trim();
    }
}
