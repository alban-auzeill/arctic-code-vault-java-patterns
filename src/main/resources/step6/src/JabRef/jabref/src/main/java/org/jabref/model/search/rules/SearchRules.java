package org.jabref.model.search.rules; // (rank 370) copied from https://github.com/JabRef/jabref/blob/6f35c3682b73ecb44ce9263b11946644f3908551/src/main/java/org/jabref/model/search/rules/SearchRules.java

import java.util.regex.Pattern;

public class SearchRules {

    private static final Pattern SIMPLE_EXPRESSION = Pattern.compile("[^\\p{Punct}]*");

    private SearchRules() {
    }

    /**
     * Returns the appropriate search rule that fits best to the given parameter.
     */
    public static SearchRule getSearchRuleByQuery(String query, boolean caseSensitive, boolean regex) {
        if (isSimpleQuery(query)) {
            return new ContainBasedSearchRule(caseSensitive);
        }

        // this searches specified fields if specified,
        // and all fields otherwise
        SearchRule searchExpression = new GrammarBasedSearchRule(caseSensitive, regex);
        if (searchExpression.validateSearchStrings(query)) {
            return searchExpression;
        } else {
            return getSearchRule(caseSensitive, regex);
        }
    }

    private static boolean isSimpleQuery(String query) {
        return SIMPLE_EXPRESSION.matcher(query).matches();
    }

    static SearchRule getSearchRule(boolean caseSensitive, boolean regex) {
        if (regex) {
            return new RegexBasedSearchRule(caseSensitive);
        } else {
            return new ContainBasedSearchRule(caseSensitive);
        }
    }
}
