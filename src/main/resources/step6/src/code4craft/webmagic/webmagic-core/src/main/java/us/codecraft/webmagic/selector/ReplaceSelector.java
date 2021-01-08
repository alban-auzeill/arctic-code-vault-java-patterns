package us.codecraft.webmagic.selector; // (rank 679) copied from https://github.com/code4craft/webmagic/blob/50c4e8ccfe827b50efefc7aeff76fb71ad3c5803/webmagic-core/src/main/java/us/codecraft/webmagic/selector/ReplaceSelector.java

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Replace selector.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class ReplaceSelector implements Selector {

    private String regexStr;

    private String replacement;

    private Pattern regex;

    public ReplaceSelector(String regexStr, String replacement) {
        this.regexStr = regexStr;
        this.replacement = replacement;
        try {
            regex = Pattern.compile(regexStr);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("invalid regex", e);
        }
    }

    @Override
    public String select(String text) {
        Matcher matcher = regex.matcher(text);
        return matcher.replaceAll(replacement);
    }

    @Override
    public List<String> selectList(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return regexStr + "_" + replacement;
    }

}
