package edu.stanford.nlp.util; // (rank 38) copied from https://github.com/stanfordnlp/CoreNLP/blob/f362675e891b720668e90cf774e480fc38ff8c85/src/edu/stanford/nlp/util/RegexStringFilter.java

import java.io.Serializable;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Filters Strings based on whether they match a given regex.
 *
 * @author John Bauer
 */
public class RegexStringFilter implements Predicate<String>, Serializable {
  final Pattern pattern;

  public RegexStringFilter(String pattern) {
    this.pattern = Pattern.compile(pattern);
  }

  public boolean test(String text) {
    return pattern.matcher(text).matches();
  }

  @Override
  public int hashCode() {
    return pattern.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof RegexStringFilter)) {
      return false;
    }
    return ((RegexStringFilter) other).pattern.equals(pattern);
  }
  
}

