package net.lightbody.bmp.proxy; // (rank 389) copied from https://github.com/JZ-Darkal/AndroidHttpCapture/blob/7e7833b0b3e0c3ce3073d72a79f03e46abe10323/app/src/main/java/net/lightbody/bmp/proxy/RewriteRule.java

import java.util.regex.Pattern;

/**
 * Container for a URL rewrite rule pattern and replacement string.
 */
public class RewriteRule {
    private final Pattern pattern;
    private final String replace;

    public RewriteRule(String pattern, String replace) {
        this.pattern = Pattern.compile(pattern);
        this.replace = replace;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getReplace() {
        return replace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewriteRule that = (RewriteRule) o;

        if (!pattern.equals(that.pattern)) return false;
        if (!replace.equals(that.replace)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pattern.hashCode();
        result = 31 * result + replace.hashCode();
        return result;
    }
}
