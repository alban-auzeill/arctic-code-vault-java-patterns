package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/ReplaceTabsBySpaceFormater.java

import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

/**
 * Replaces any tab with a space
 */
public class ReplaceTabsBySpaceFormater extends Formatter {

    private static final Pattern TAB = Pattern.compile("\t+");

    @Override
    public String getName() {
        return Localization.lang("Replace tabs with space");
    }

    @Override
    public String getKey() {
        return "remove_tabs";
    }

    @Override
    public String format(String value) {
        Objects.requireNonNull(value);
        return TAB.matcher(value).replaceAll(" ");
    }

    @Override
    public String getDescription() {
        return Localization.lang("Replace tabs with space in the field content.");
    }

    @Override
    public String getExampleInput() {
        return "In \t\t CDMA";
    }
}
