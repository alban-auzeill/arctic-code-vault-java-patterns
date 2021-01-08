package org.jeecg.common.util.filter; // (rank 601) copied from https://github.com/zhangdaiscott/jeecg-boot/blob/a004acee4b811c4b3e21c7414621968fea7c0092/jeecg-boot/jeecg-boot-base/jeecg-boot-base-core/src/main/java/org/jeecg/common/util/filter/StrAttackFilter.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 文件上传字符串过滤特殊字符
 */
public class StrAttackFilter {

    public static String filter(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = "[`_《》~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

//    public static void main(String[] args) {
//        String filter = filter("@#jeecg/《》【bo】￥%……&*（o）)))！@t<>,.,/?'\'~~`");
//        System.out.println(filter);
//    }
}
