package org.gridkit.jvmtool.stacktrace.analytics; // (rank 810) copied from https://github.com/aragozin/jvm-tools/blob/84f20a20334b48a06a285e27974c49c636e52145/sjk-stacktrace/src/main/java/org/gridkit/jvmtool/stacktrace/analytics/CachingFilterFactory.java

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gridkit.jvmtool.stacktrace.StackFrame;

/**
 * This filter factory is introducing hit cache on frame
 * matcher level. It also reusing matcher instance.
 * <br/>
 * These optimizations are not thread safe.
 *
 * @author Alexey Ragozin (alexey.ragozin@gmail.com)
 */
public class CachingFilterFactory extends BasicFilterFactory {

    @Override
    public StackFrameMatcher patternFrameMatcher(Collection<String> patterns) {
        if (patterns.isEmpty()) {
            throw new IllegalArgumentException("Pattern list is empty");
        }
        return new CachingPatternFrameMatcher(patterns);
    }

    protected static class CachingPatternFrameMatcher implements StackFrameMatcher {

        private final Matcher regEx;
        private final Map<StackFrame, Boolean> hitCahce = new HashMap<StackFrame, Boolean>();

        CachingPatternFrameMatcher(Collection<String> patterns) {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for(String pattern: patterns) {
                sb.append(wildCardTranslate(pattern));
                sb.append('|');
            }
            sb.setCharAt(sb.length() - 1, ')');
            regEx = Pattern.compile(sb.toString()).matcher("");
        }

        @Override
        public boolean evaluate(StackFrame frame) {
            Boolean hit =  hitCahce.get(frame);
            if (hit != null) {
                return hit;
            }
            else {
                regEx.reset(frame);
                hit = regEx.lookingAt();
                hitCahce.put(frame, hit);
            }
            return hit;
        }
    }
}
