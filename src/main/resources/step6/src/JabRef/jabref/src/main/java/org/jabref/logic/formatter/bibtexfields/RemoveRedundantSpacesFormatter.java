package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/RemoveRedundantSpacesFormatter.java

import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

/**
 * Finds any occurrence of consecutive spaces and replaces it with a single space
 */
public class RemoveRedundantSpacesFormatter extends Formatter {

    private static final Pattern MULTIPLE_SPACES = Pattern.compile(" {2,}");

    @Override
    public String getName() {
        return Localization.lang("Remove redundant spaces");
    }

    @Override
    public String getKey() {
        return "remove_redundant_spaces";
    }

    @Override
    public String format(String value) {
        Objects.requireNonNull(value);
        return MULTIPLE_SPACES.matcher(value).replaceAll(" ");
    }

    @Override
    public String getDescription() {
        return Localization.lang("Replaces consecutive spaces with a single space in the field content.");
    }

    @Override
    public String getExampleInput() {
        return "In   CDMA";
    }
}
