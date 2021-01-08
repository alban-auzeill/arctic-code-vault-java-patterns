package org.csploit.android.net.metasploit; // (rank 268) copied from https://github.com/cSploit/android/blob/70268d9cb8c231c48b671e06b577f89d11c617ee/cSploit/src/main/java/org/csploit/android/net/metasploit/Author.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * an Author of a MSF module
 */
public class Author {
  private static final Pattern EMAIL = Pattern.compile("\\s*< *([^>]+) *>");

  private final String name;
  private final String email;

  public Author(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public static Author fromString(String nameAndEmail) {
    Matcher matcher = EMAIL.matcher(nameAndEmail);
    String name, email;

    if(!matcher.find()) {
      name = nameAndEmail.trim();
      email = null;
    } else {
      name = nameAndEmail.substring(0, matcher.regionStart());
      email = matcher.group(1).replace(" [at] ", "@");
    }

    return new Author(name, email);
  }
}
