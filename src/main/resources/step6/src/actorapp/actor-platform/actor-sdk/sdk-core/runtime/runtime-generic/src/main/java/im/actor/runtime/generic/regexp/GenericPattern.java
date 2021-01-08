package im.actor.runtime.generic.regexp; // (rank 329) copied from https://github.com/actorapp/actor-platform/blob/5123c1584757c6eeea0ed2a0e7e043629871a0c6/actor-sdk/sdk-core/runtime/runtime-generic/src/main/java/im/actor/runtime/generic/regexp/GenericPattern.java

import java.util.regex.Pattern;

import im.actor.runtime.regexp.MatcherCompat;
import im.actor.runtime.regexp.PatternCompat;

public class GenericPattern extends PatternCompat {

    private Pattern pattern;

    public GenericPattern(String pattern) {
        super(pattern);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public MatcherCompat matcher(String input) {
        return new GenericMatch(pattern.matcher(input), input);
    }
}
