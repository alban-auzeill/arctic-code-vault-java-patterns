package org.jabref.logic.layout.format; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/layout/format/HTMLParagraphs.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jabref.logic.layout.LayoutFormatter;

/**
 * Will interpret two consecutive newlines as the start of a new paragraph and thus
 * wrap the paragraph in HTML-p-tags.
 */
public class HTMLParagraphs implements LayoutFormatter {

    private static final Pattern BEFORE_NEW_LINES_PATTERN = Pattern.compile("(.*?)\\n\\s*\\n");

    @Override
    public String format(String fieldText) {

        if (fieldText == null) {
            return fieldText;
        }

        String trimmedFieldText = fieldText.trim();

        if (trimmedFieldText.isEmpty()) {
            return trimmedFieldText;
        }

        Matcher m = HTMLParagraphs.BEFORE_NEW_LINES_PATTERN.matcher(trimmedFieldText);
        StringBuilder s = new StringBuilder();
        while (m.find()) {
            String middle = m.group(1).trim();
            if (!middle.isEmpty()) {
                s.append("<p>\n");
                m.appendReplacement(s, m.group(1));
                s.append("\n</p>\n");
            }
        }
        s.append("<p>\n");
        m.appendTail(s);
        s.append("\n</p>");

        return s.toString();
    }
}
