package cn.iocoder.common.framework.util; // (rank 734) copied from https://github.com/YunaiV/onemall/blob/671d4ad2461cd404c5d154d84115e12786edcb3f/common/common-framework/src/main/java/cn/iocoder/common/framework/util/ValidationUtil.java

import java.util.regex.Pattern;

/**
 * 校验工具类
 */
public class ValidationUtil {

    private static Pattern PATTERN_URL = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return false;
        }
        // TODO 芋艿，后面完善手机校验
        return true;
    }

    public static boolean isURL(String url) {
        return StringUtils.hasText(url)
                && PATTERN_URL.matcher(url).matches();
    }

    public static void main(String[] args) {
        System.out.println(isURL("http://www.iocoder.cn"));
    }

}
