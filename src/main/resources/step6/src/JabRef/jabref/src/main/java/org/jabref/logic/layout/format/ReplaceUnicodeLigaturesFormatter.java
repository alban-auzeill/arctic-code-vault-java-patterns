package org.jabref.logic.layout.format; // (rank 370) copied from https://github.com/JabRef/jabref/blob/c8f7be89ad704065474decb61851e42416caf88d/src/main/java/org/jabref/logic/layout/format/ReplaceUnicodeLigaturesFormatter.java

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jabref.logic.cleanup.Formatter;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.layout.LayoutFormatter;
import org.jabref.logic.util.strings.UnicodeLigaturesMap;

public class ReplaceUnicodeLigaturesFormatter extends Formatter implements LayoutFormatter {

    private final Map<Pattern, String> ligaturesMap;

    public ReplaceUnicodeLigaturesFormatter() {
        ligaturesMap = new HashMap<>();
        UnicodeLigaturesMap stringMap = new UnicodeLigaturesMap();
        for (String key : stringMap.keySet()) {
            ligaturesMap.put(Pattern.compile(key), stringMap.get(key));
        }
    }

    @Override
    public String getName() {
        return Localization.lang("Replace Unicode ligatures");
    }

    @Override
    public String getKey() {
        return "remove_unicode_ligatures";
    }

    @Override
    public String format(String fieldText) {
        String result = fieldText;

        for (Pattern key : ligaturesMap.keySet()) {
            result = key.matcher(result).replaceAll(ligaturesMap.get(key));
        }
        return result;
    }

    @Override
    public String getDescription() {
        return Localization.lang("Replaces Unicode ligatures with their expanded form");
    }

    @Override
    public String getExampleInput() {
        return "Æneas";
    }
}
