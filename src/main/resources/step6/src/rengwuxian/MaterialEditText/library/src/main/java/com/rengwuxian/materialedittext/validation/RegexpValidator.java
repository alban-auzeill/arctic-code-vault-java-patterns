package com.rengwuxian.materialedittext.validation; // (rank 899) copied from https://github.com/rengwuxian/MaterialEditText/blob/f6e9fa42213cda552764c2a63f93a1464fa91d04/library/src/main/java/com/rengwuxian/materialedittext/validation/RegexpValidator.java

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * Custom validator for Regexes
 */
public class RegexpValidator extends METValidator {

  private Pattern pattern;

  public RegexpValidator(@NonNull String errorMessage, @NonNull String regex) {
    super(errorMessage);
    pattern = Pattern.compile(regex);
  }

  public RegexpValidator(@NonNull String errorMessage, @NonNull Pattern pattern) {
    super(errorMessage);
    this.pattern = pattern;
  }

  @Override
  public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
    return pattern.matcher(text).matches();
  }
}
