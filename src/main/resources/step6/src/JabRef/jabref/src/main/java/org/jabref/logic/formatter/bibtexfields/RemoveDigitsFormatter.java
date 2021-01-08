package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/RemoveDigitsFormatter.java

import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

public class RemoveDigitsFormatter extends Formatter {

    private static final Pattern DIGITS = Pattern.compile("[ ]\\d+");

    @Override
    public String getName() {
        return Localization.lang("Remove digits");
    }

    @Override
    public String getKey() {
        return "remove_digits";
    }

    @Override
    public String format(String value) {
        Objects.requireNonNull(value);

        return DIGITS.matcher(value).replaceAll("");
    }

    @Override
    public String getDescription() {
        return Localization.lang("Removes digits.");
    }

    @Override
    public String getExampleInput() {
        return "In 012 CDMA";
    }
}
