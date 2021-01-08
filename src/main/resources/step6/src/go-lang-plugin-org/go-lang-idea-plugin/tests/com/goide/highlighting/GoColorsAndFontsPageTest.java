package com.goide.highlighting; // (rank 279) copied from https://github.com/go-lang-plugin-org/go-lang-idea-plugin/blob/e73dab44213ffa76b3d6a853aecb109929c3e2b5/tests/com/goide/highlighting/GoColorsAndFontsPageTest.java

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoColorsAndFontsPageTest extends TestCase {

  public void testDemoText() {
    GoColorsAndFontsPage testee = new GoColorsAndFontsPage();
    String demoText = testee.getDemoText();
    Set<String> knownElements = testee.getAdditionalHighlightingTagToDescriptorMap().keySet();

    Matcher m = Pattern.compile("</?(\\w+)>").matcher(demoText);
    while (m.find()) {
      String name = m.group(1);
      if (!knownElements.contains(name)) {
        Assert.fail("Unknown element \"" + m.group() + "\".");
      }
    }
  }

}
