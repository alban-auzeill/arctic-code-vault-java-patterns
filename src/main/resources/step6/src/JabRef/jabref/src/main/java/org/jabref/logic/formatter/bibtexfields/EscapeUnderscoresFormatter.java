package org.jabref.logic.formatter.bibtexfields; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/formatter/bibtexfields/EscapeUnderscoresFormatter.java

import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;

public class EscapeUnderscoresFormatter extends Formatter {

    private static final Pattern UNDERSCORES = Pattern.compile("_");

    @Override
    public String getName() {
        return Localization.lang("Escape underscores");
    }

    @Override
    public String getKey() {
        return "escapeUnderscores";
    }

    @Override
    public String format(String value) {
        Objects.requireNonNull(value);

        return UNDERSCORES.matcher(value).replaceAll("\\\\_");
    }

    @Override
    public String getDescription() {
        return Localization.lang("Escape underscores");
    }

    @Override
    public String getExampleInput() {
        return "Text_with_underscores";
    }
}
