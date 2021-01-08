package us.codecraft.webmagic.handler; // (rank 679) copied from https://github.com/code4craft/webmagic/blob/50c4e8ccfe827b50efefc7aeff76fb71ad3c5803/webmagic-extension/src/main/java/us/codecraft/webmagic/handler/PatternRequestMatcher.java

import us.codecraft.webmagic.Request;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: April 03, 2014
 * Time: 10:00
 * <p>
 * A PatternHandler is in charge of both page extraction and data processing by implementing
 * its two abstract methods.
 */
public abstract class PatternRequestMatcher implements RequestMatcher {

    /**
     * match pattern. only matched page should be handled.
     */
    protected String pattern;

    private Pattern patternCompiled;

    /**
     * @param pattern url pattern to handle
     */
    public PatternRequestMatcher(String pattern) {
        this.pattern = pattern;
        this.patternCompiled = Pattern.compile(pattern);
    }

    @Override
    public boolean match(Request request) {
        return patternCompiled.matcher(request.getUrl()).matches();
    }
}
