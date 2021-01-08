package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/RemoveHyphenatedNewlinesFormatter.java

import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

/**
 * Removes all hyphenated line breaks in the string.
 */
public class RemoveHyphenatedNewlinesFormatter extends Formatter {
    private static final Pattern HYPHENATED_WORDS = Pattern.compile("-\\R");

    @Override
    public String getName() {
        return Localization.lang("Remove hyphenated line breaks");
    }

    @Override
    public String getKey() {
        return "remove_hyphenated_newlines";
    }

    @Override
    public String format(String value) {
        Objects.requireNonNull(value);

        value = HYPHENATED_WORDS.matcher(value).replaceAll("");
        return value.trim();
    }

    @Override
    public String getDescription() {
        return Localization.lang("Removes all hyphenated line breaks in the field content.");
    }

    @Override
    public String getExampleInput() {
        return "Gimme shel-\nter";
    }
}
