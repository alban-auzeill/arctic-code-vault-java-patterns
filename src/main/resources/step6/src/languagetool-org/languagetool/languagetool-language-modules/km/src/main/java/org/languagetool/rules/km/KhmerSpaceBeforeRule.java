/* LanguageTool, a natural language style checker  (rank 249) copied from https://github.com/languagetool-org/languagetool/blob/e8a7454efa2c7846a6655cd7156de74a1676e4ec/languagetool-language-modules/km/src/main/java/org/languagetool/rules/km/KhmerSpaceBeforeRule.java
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules.km;

import org.languagetool.Language;
import org.languagetool.rules.AbstractSpaceBeforeRule;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * A Khmer rule that checks if there is a missing space before some conjunctions.
 * 
 * @author Jaume Ortolà
 */
public class KhmerSpaceBeforeRule extends AbstractSpaceBeforeRule {

  private static final Pattern CONJUNCTIONS = 
          Pattern.compile("ដើម្បី|និង|ពីព្រោះ", Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);

  public KhmerSpaceBeforeRule(ResourceBundle messages, Language language) {
    super(messages, language);
  }

  @Override
  public Pattern getConjunctions() {
    return CONJUNCTIONS;
  }

  @Override
  public String getId() {
    return "KM_SPACE_BEFORE_CONJUNCTION";
  }
}