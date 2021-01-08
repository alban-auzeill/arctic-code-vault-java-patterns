/* (rank 114) copied from https://github.com/zaproxy/zaproxy/blob/267e8156f904d87a103dadcd0778138cfbfe3870/zap/src/main/java/org/zaproxy/zap/network/DomainMatcher.java
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2017 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.network;

import java.util.regex.Pattern;
import org.zaproxy.zap.utils.Enableable;

/**
 * Class that contains rules to match domains.
 *
 * <p>It supports both plain text and regular expression checks.
 *
 * @since 2.6.0
 * @see #matches(String)
 */
public class DomainMatcher extends Enableable {

    private final Pattern pattern;
    private final String domain;
    private final boolean regex;

    public DomainMatcher(Pattern pattern) {
        super(true);

        if (pattern == null) {
            throw new IllegalArgumentException("Parameter pattern must not be null.");
        }

        this.pattern = pattern;
        this.regex = true;
        this.domain = null;
    }

    public DomainMatcher(String domain) {
        super(true);

        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("Parameter domain must not be null or empty.");
        }

        this.domain = domain;
        this.regex = false;
        this.pattern = null;
    }

    public DomainMatcher(DomainMatcher other) {
        super(other.isEnabled());

        this.domain = other.domain;
        this.regex = other.regex;
        this.pattern = other.pattern;
    }

    public String getValue() {
        if (isRegex()) {
            return pattern.pattern();
        }

        return domain;
    }

    public boolean isRegex() {
        return regex;
    }

    /**
     * Tells whether or not the given domain matches.
     *
     * @param domain the domain that will be checked
     * @return {@code true} if the domain matches, {@code false} otherwise.
     */
    public boolean matches(String domain) {
        if (pattern != null) {
            return pattern.matcher(domain).matches();
        }

        return this.domain.equals(domain);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + ((pattern == null) ? 0 : pattern.pattern().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        DomainMatcher other = (DomainMatcher) object;
        if (domain == null) {
            if (other.domain != null) {
                return false;
            }
        } else if (!domain.equals(other.domain)) {
            return false;
        }
        if (pattern == null) {
            if (other.pattern != null) {
                return false;
            }
        } else if (!pattern.pattern().equals(other.pattern.pattern())) {
            return false;
        }
        return true;
    }

    public static Pattern createPattern(String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
}