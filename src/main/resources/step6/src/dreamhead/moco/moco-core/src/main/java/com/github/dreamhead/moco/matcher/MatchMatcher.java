package com.github.dreamhead.moco.matcher; // (rank 766) copied from https://github.com/dreamhead/moco/blob/598b4c12a5f4cc1bcdb39a7c6ca865fe2d3fc5dd/moco-core/src/main/java/com/github/dreamhead/moco/matcher/MatchMatcher.java

import com.github.dreamhead.moco.RequestExtractor;
import com.github.dreamhead.moco.RequestMatcher;
import com.github.dreamhead.moco.resource.Resource;

import java.util.regex.Pattern;

public final class MatchMatcher<T> extends AbstractOperatorMatcher<T> {
    public MatchMatcher(final RequestExtractor<T> extractor, final Resource expected) {
        super(extractor, expected, input -> {
            Pattern pattern = Pattern.compile(expected.readFor(null).toString());
            return pattern.matcher(input).matches();
        });
    }

    @Override
    protected RequestMatcher newMatcher(final RequestExtractor<T> extractor, final Resource resource) {
        return new MatchMatcher<>(extractor, resource);
    }
}
