package com.lou.springboot.utils; // (rank 728) copied from https://github.com/ZHENFENG13/spring-boot-projects/blob/8cbf09ba09726ca3798ba6c846dbf402524688d9/SpringBoot%E5%89%8D%E5%90%8E%E7%AB%AF%E5%88%86%E7%A6%BB%E5%AE%9E%E6%88%98%E9%A1%B9%E7%9B%AE%E6%BA%90%E7%A0%81/spring-boot-project-front-end&back-end/src/main/java/com/lou/springboot/utils/NumberUtil.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
public class NumberUtil {

    private NumberUtil() {
    }


    /**
     * 判断是否为11位电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 生成指定长度的随机数
     *
     * @param length
     * @return
     */
    public static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 生成订单流水号
     *
     * @return
     */
    public static String genOrderNo() {
        StringBuffer buffer = new StringBuffer(String.valueOf(System.currentTimeMillis()));
        int num = genRandomNum(4);
        buffer.append(num);
        return buffer.toString();
    }

    public static String formatMoney2Str(Double money) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        return df.format(money);
    }

    public static String formatMoney2Str(float money) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        return df.format(money);
    }

}
