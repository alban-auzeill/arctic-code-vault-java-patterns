package org.robolectric.annotation.processing; // (rank 128) copied from https://github.com/robolectric/robolectric/blob/69afdea73b25c1479c254dc8358775b48f131cee/processor/src/main/java/org/robolectric/annotation/processing/DocumentedElement.java

import java.util.regex.Pattern;

public abstract class DocumentedElement {
  private static final Pattern START_OR_NEWLINE_SPACE = Pattern.compile("(^|\n) ");

  private final String name;
  private String documentation;

  protected DocumentedElement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{name='" + name + '\'' + '}';
  }

  public void setDocumentation(String docStr) {
    if (docStr != null) {
      this.documentation = START_OR_NEWLINE_SPACE.matcher(docStr).replaceAll("$1");
    }
  }

  public String getDocumentation() {
    return documentation;
  }
}
