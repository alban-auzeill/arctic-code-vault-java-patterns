package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/OrdinalsToSuperscriptFormatter.java

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

/**
 * This class transforms ordinal numbers into LaTeX superscripts.
 */
public class OrdinalsToSuperscriptFormatter extends Formatter {

    // find possible superscripts on word boundaries
    private static final Pattern SUPERSCRIPT_DETECT_PATTERN = Pattern.compile("\\b(\\d+)(st|nd|rd|th)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static final String SUPERSCRIPT_REPLACE_PATTERN = "$1\\\\textsuperscript{$2}";

    @Override
    public String getName() {
        return Localization.lang("Ordinals to LaTeX superscript");
    }

    @Override
    public String getKey() {
        return "ordinals_to_superscript";
    }

    /**
     * Converts ordinal numbers to superscripts, e.g. 1st, 2nd or 3rd.
     * Will replace ordinal numbers even if they are semantically wrong, e.g. 21rd
     *
     * <example>
     * 1st Conf. Cloud Computing -> 1\textsuperscript{st} Conf. Cloud Computing
     * </example>
     */
    @Override
    public String format(String value) {
        Objects.requireNonNull(value);

        if (value.isEmpty()) {
            // nothing to do
            return value;
        }

        Matcher matcher = SUPERSCRIPT_DETECT_PATTERN.matcher(value);
        // replace globally

        // adds superscript tag
        return matcher.replaceAll(SUPERSCRIPT_REPLACE_PATTERN);
    }

    @Override
    public String getDescription() {
        return Localization.lang("Converts ordinals to LaTeX superscripts.");
    }

    @Override
    public String getExampleInput() {
        return "11th";
    }
}
