package com.app.mvc.util; // (rank 1002) copied from https://github.com/kanwangzjm/funiture/blob/cc1382fec0a6b2dae7b1124a47686c6a378abfae/src/main/java/com/app/mvc/util/RegexUtil.java

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 系统中经常使用正则表达式工具，比如判断字符串是否含有中文，
 * 电话号码是否正确等等.因此改类封装了常用正则表达式
 * Created by jimin on 16/3/10
 */
public class RegexUtil {

    //pre-compile
    private static final Pattern isPostCode = Pattern.compile("[1-9]\\d(?!\\d)");
    private static final Pattern isIPv4 = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
    private static final Pattern isEmail = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static final Pattern isCEN_ = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$");
    private static final Pattern isCEN = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]+$");
    private static final Pattern isEN_ = Pattern.compile("^\\w{3,20}$");
    private static final Pattern isEN = Pattern.compile("^[A-Za-z0-9]+$");
    private static final Pattern isTelephone = Pattern.compile("\\d{3}-\\d{8}|\\d{4}-\\d{7}");
    private static final Pattern isMobile = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");

    /**
     * 顾名思义，是否是中文char
     *
     * @param ch 单个字符
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isChinese(char ch) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 是否是正确的邮政编码
     *
     * @param postcode 邮政编码
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isPostCode(String postcode) {
        checkArgument(!isNullOrBlank(postcode));
        return isPostCode.matcher(postcode).matches();
    }

    /**
     * 是否是点分十进制的ip v4地址
     *
     * @param ip ip地址
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isIPv4(String ip) {
        checkArgument(!isNullOrBlank(ip));
        return isIPv4.matcher(ip).matches();
    }

    /**
     * 是否是email地址
     *
     * @param email 电子邮箱
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isEmail(String email) {
        checkArgument(!isNullOrBlank(email));
        return isEmail.matcher(email).matches();
    }

    /**
     * 中文、英文、数字包括下划线
     *
     * @param input
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isCEN_(String input) {
        checkArgument(!isNullOrBlank(input));
        return isCEN_.matcher(input).matches();
    }

    /**
     * 中文、英文、数字但不包括下划线等符号
     *
     * @param input 输入
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isCEN(String input) {
        checkArgument(!isNullOrBlank(input));
        return isCEN.matcher(input).matches();
    }

    /**
     * 由数字、26个英文字母或者下划线组成的字符串
     *
     * @param input 输入
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isEN_(String input) {
        checkArgument(!isNullOrBlank(input));
        return isEN_.matcher(input).matches();
    }

    /**
     * 由数字和26个英文字母组成的字符串
     *
     * @param input 输入
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isEN(String input) {
        checkArgument(!isNullOrBlank(input));
        return isEN.matcher(input).matches();
    }

    /**
     * 国内电话号码
     *
     * @param telephone 国内电话
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isTelephone(String telephone) {
        checkArgument(!isNullOrBlank(telephone));
        return isTelephone.matcher(telephone).matches();
    }

    /**
     * 手机号码，判断86 | 0 |17951 的形式。
     *
     * @param mobile 国内手机号
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isMobile(String mobile) {
        checkArgument(!isNullOrBlank(mobile));
        return isMobile.matcher(mobile).matches();
    }

    /**
     * 身份证号(15位、18位数字)
     *
     * @param icard 身份证号
     * @return true: 符合条件 false: 不复合条件
     */
    public static boolean isIDCard(String icard) {
        checkArgument(!isNullOrBlank(icard));
        return icard.matches("^\\d{15}|\\d{18}$");
    }

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isEmpty();
    }
}
