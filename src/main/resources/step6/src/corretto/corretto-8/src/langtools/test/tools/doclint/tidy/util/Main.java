/* (rank 158) copied from https://github.com/corretto/corretto-8/blob/a6b2628f8074004f2c10bd7c276543a1acba412f/src/langtools/test/tools/doclint/tidy/util/Main.java
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


package tidystats;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate statistics from the files generated by tidy.sh.
 *
 * <p>The tidy.sh script is used to run tidy on all the HTML files
 * in a directory, creating files in a new directory, and for each
 * HTML file, it writes the console output from tidy into a file
 * beside the fixed up file, with an additional .tidy extension.
 *
 * <p>This program will scan a directory for *.tidy files and
 * analyze the messages reported by tidy, in order to generate a
 * report with statistics on the various messages that were
 * reported by tidy.
 *
 * <p>Typical usage:
 * <pre>
 * $ bash /path/to/tidy.sh /path/to/htmldir
 * $ javac -d /path/to/classes /path/to/Main.java
 * $ java -cp /path/to/classes tidystats.Main /path/to/htmldir.tidy
 * </pre>
 *
 * <p>Internally, the program works by matching lines in the *.tidy
 * files against a series of regular expressions that are used to
 * categorize the messages.  The set of regular expressions was
 * empirically determined by running the program on the output from
 * running tidy.sh on all the generated JDK documentation. It is
 * possible that tidy may generate more/different messages on other
 * doc sets, in which case, the set of regexes in the program should
 * be updated.
 */
public class Main {
    public static void main(String... args) throws IOException {
        new Main().run(args);
    }

    void run(String... args) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        List<Path> paths = new ArrayList<>();

        int i;
        for (i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-"))
                throw new IllegalArgumentException(arg);
            else
                break;
        }

        for ( ; i < args.length; i++) {
            Path p = fs.getPath(args[i]);
            paths.add(p);
        }

        for (Path p: paths) {
            scan(p);
        }

        print("%6d files read", files);
        print("%6d files had no errors or warnings", ok);
        print("%6d files reported \"Not all warnings/errors were shown.\"", overflow);
        print("%6d errors found", errs);
        print("%6d warnings found", warns);
        print("%6d recommendations to use CSS", css);
        print("");

        Map<Integer, Set<String>> sortedCounts = new TreeMap<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                });

        for (Map.Entry<Pattern, Integer> e: counts.entrySet()) {
            Pattern p = e.getKey();
            Integer n = e.getValue();
            Set<String> set = sortedCounts.get(n);
            if (set == null)
                sortedCounts.put(n, (set = new TreeSet<>()));
            set.add(p.toString());
        }

        for (Map.Entry<Integer, Set<String>> e: sortedCounts.entrySet()) {
            for (String p: e.getValue()) {
                if (p.startsWith(".*")) p = p.substring(2);
                print("%6d: %s", e.getKey(), p);
            }
        }
    }

    void scan(Path p) throws IOException {
        if (Files.isDirectory(p)) {
            for (Path c: Files.newDirectoryStream(p)) {
                scan(c);
            }
        } else if (isTidyFile(p)) {
            scan(Files.readAllLines(p, Charset.defaultCharset()));
        }
    }

    boolean isTidyFile(Path p) {
        return Files.isRegularFile(p) && p.getFileName().toString().endsWith(".tidy");
    }

    void scan(List<String> lines) {
        Matcher m;
        files++;
        for (String line: lines) {
            if (okPattern.matcher(line).matches()) {
                ok++;
            } else if ((m = countPattern.matcher(line)).matches()) {
                warns += Integer.valueOf(m.group(1));
                errs += Integer.valueOf(m.group(2));
                if (m.group(3) != null)
                    overflow++;
            } else if ((m = guardPattern.matcher(line)).matches()) {
                boolean found = false;
                for (Pattern p: patterns) {
                    if ((m = p.matcher(line)).matches()) {
                        found = true;
                        count(p);
                        break;
                    }
                }
                if (!found)
                    System.err.println("Unrecognized line: " + line);
            } else if (cssPattern.matcher(line).matches()) {
                css++;
            }
        }
    }

    Map<Pattern, Integer> counts = new HashMap<>();
    void count(Pattern p) {
        Integer i = counts.get(p);
        counts.put(p, (i == null) ? 1 : i + 1);
    }

    void print(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    Pattern okPattern = Pattern.compile("No warnings or errors were found.");
    Pattern countPattern = Pattern.compile("([0-9]+) warnings, ([0-9]+) errors were found!.*?(Not all warnings/errors were shown.)?");
    Pattern cssPattern = Pattern.compile("You are recommended to use CSS.*");
    Pattern guardPattern = Pattern.compile("line [0-9]+ column [0-9]+ - (Error|Warning):.*");

    Pattern[] patterns = {
        Pattern.compile(".*Error: <.*> is not recognized!"),
        Pattern.compile(".*Error: missing quote mark for attribute value"),
        Pattern.compile(".*Warning: <.*> anchor \".*\" already defined"),
        Pattern.compile(".*Warning: <.*> attribute \".*\" has invalid value \".*\""),
        Pattern.compile(".*Warning: <.*> attribute \".*\" lacks value"),
        Pattern.compile(".*Warning: <.*> attribute \".*\" lacks value"),
        Pattern.compile(".*Warning: <.*> attribute with missing trailing quote mark"),
        Pattern.compile(".*Warning: <.*> dropping value \".*\" for repeated attribute \".*\""),
        Pattern.compile(".*Warning: <.*> inserting \".*\" attribute"),
        Pattern.compile(".*Warning: <.*> is probably intended as </.*>"),
        Pattern.compile(".*Warning: <.*> isn't allowed in <.*> elements"),
        Pattern.compile(".*Warning: <.*> lacks \".*\" attribute"),
        Pattern.compile(".*Warning: <.*> missing '>' for end of tag"),
        Pattern.compile(".*Warning: <.*> proprietary attribute \".*\""),
        Pattern.compile(".*Warning: <.*> unexpected or duplicate quote mark"),
        Pattern.compile(".*Warning: <a> cannot copy name attribute to id"),
        Pattern.compile(".*Warning: <a> escaping malformed URI reference"),
        Pattern.compile(".*Warning: <blockquote> proprietary attribute \"pre\""),
        Pattern.compile(".*Warning: discarding unexpected <.*>"),
        Pattern.compile(".*Warning: discarding unexpected </.*>"),
        Pattern.compile(".*Warning: entity \".*\" doesn't end in ';'"),
        Pattern.compile(".*Warning: inserting implicit <.*>"),
        Pattern.compile(".*Warning: inserting missing 'title' element"),
        Pattern.compile(".*Warning: missing <!DOCTYPE> declaration"),
        Pattern.compile(".*Warning: missing <.*>"),
        Pattern.compile(".*Warning: missing </.*> before <.*>"),
        Pattern.compile(".*Warning: nested emphasis <.*>"),
        Pattern.compile(".*Warning: plain text isn't allowed in <.*> elements"),
        Pattern.compile(".*Warning: replacing <p> by <br>"),
        Pattern.compile(".*Warning: replacing invalid numeric character reference .*"),
        Pattern.compile(".*Warning: replacing unexpected .* by </.*>"),
        Pattern.compile(".*Warning: trimming empty <.*>"),
        Pattern.compile(".*Warning: unescaped & or unknown entity \".*\""),
        Pattern.compile(".*Warning: unescaped & which should be written as &amp;"),
        Pattern.compile(".*Warning: using <br> in place of <p>")
    };

    int files;
    int ok;
    int warns;
    int errs;
    int css;
    int overflow;
}

