/* (rank 60) copied from https://github.com/google/j2objc/blob/a5e169535ade702338edc8d15ae8b655153ee848/translator/src/main/java/com/google/devtools/j2objc/util/ProGuardUsageParser.java
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.j2objc.util;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses dead code reports generated by ProGuard.
 *
 * Example ProGuard configuration file to generate an acceptable listing:
 *
 * <pre><code>
 * -injars <foo.jar>
 * -libraryjars <java.home>/lib/rt.jar
 *
 * -dontoptimize
 * -dontobfuscate
 * -dontpreverify
 * -printusage
 * -dontnote
 *
 * -keep public class com.google.foo.Bar {
 *     public static void main(java.lang.String[]);
 * }
 *
 * -keepclassmembers class * {
 *     static final % *;
 *     static final java.lang.String *;
 * }
 * </code></pre>
 *
 * @author Daniel Connelly
 */
public class ProGuardUsageParser {

  private static final Pattern proGuardMethodPattern = Pattern.compile(
      "    " + // leading indent
      "(\\d+:\\d+:)?" + // method line numbers (optional)
      "((public|private|protected|static|synchronized|varargs|bridge|" +
          "native|abstract|strictfp|final|synthetic)\\s)*" + // keywords
      "((\\S+)\\s)?(\\S+)\\((\\S*)\\)"); // return type, method name, arguments

  private ProGuardUsageParser() {
    // Don't instantiate.
  }

  private static String buildTypeSignature(String type) {
    if (type.equals("byte")) {
      return "B";
    }
    if (type.equals("char")) {
      return "C";
    }
    if (type.equals("double")) {
      return "D";
    }
    if (type.equals("float")) {
      return "F";
    }
    if (type.equals("int")) {
      return "I";
    }
    if (type.equals("long")) {
      return "J";
    }
    if (type.equals("short")) {
      return "S";
    }
    if (type.equals("boolean")) {
      return "Z";
    }
    if (type.equals("void")) {
      return "V";
    }
    if (type.endsWith("[]")) {
      return "[" + buildTypeSignature(type.substring(0, type.length() - 2));
    }
    if (type.length() == 0) {
      return "";
    }
    return "L" + type.replace('.', '/') + ";";
  }

  private static String buildMethodSignature(String returnType, String argumentList) {
    String[] arguments = argumentList.split(",");
    StringBuilder signature = new StringBuilder().append("(");
    for (String argument : arguments) {
      signature.append(buildTypeSignature(argument));
    }
    signature.append(")");
    signature.append(buildTypeSignature(returnType != null ? returnType : "void"));
    return signature.toString();
  }

  public static CodeReferenceMap parseDeadCodeFile(File file) {
    if (file != null) {
      try {
        return ProGuardUsageParser.parse(Files.asCharSource(file, Charset.defaultCharset()));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    return null;
  }

  public static CodeReferenceMap parse(CharSource listing) throws IOException {
    LineProcessor<CodeReferenceMap> processor = new LineProcessor<CodeReferenceMap>() {
      CodeReferenceMap.Builder dead = CodeReferenceMap.builder();
      String lastClass;

      @Override
      public CodeReferenceMap getResult() {
        return dead.build();
      }

      private void handleClass(String line) {
        if (line.endsWith(":")) {
          // Class, but not completely dead; save to read its dead methods
          lastClass = line.substring(0, line.length() - 1);
        } else {
          dead.addClass(line);
        }
      }

      private void handleMethod(String line) throws IOException {
        Matcher methodMatcher = proGuardMethodPattern.matcher(line);
        if (!methodMatcher.matches()) {
          throw new AssertionError("Line doesn't match expected ProGuard format!");
        }
        if (lastClass == null) {
          throw new IOException("Bad listing format: method not attached to a class");
        }
        String returnType = methodMatcher.group(5);
        String methodName = methodMatcher.group(6);
        String arguments = methodMatcher.group(7);
        String signature = buildMethodSignature(returnType, arguments);
        dead.addMethod(lastClass, methodName, signature);
      }

      private void handleField(String line) throws IOException {
        String name = line.substring(line.lastIndexOf(" ") + 1);
        dead.addField(lastClass, name);
      }

      @Override
      public boolean processLine(String line) throws IOException {
        if (line.startsWith("ProGuard, version")
            || line.startsWith("Reading ")
            || line.startsWith("    processed in")) {
          // ignore output header
        } else if (!line.startsWith("    ")) {
          handleClass(line);
        } else if (line.startsWith("    ") && !line.contains("(")) {
          handleField(line);
        } else {
          handleMethod(line);
        }
        return true;
      }
    };

    return listing.readLines(processor);
  }

  // Used for testing.
  public static void main(String[] args) throws IOException {
    ProGuardUsageParser.parse(Files.asCharSource(new File(args[0]), Charset.defaultCharset()));
  }

}
