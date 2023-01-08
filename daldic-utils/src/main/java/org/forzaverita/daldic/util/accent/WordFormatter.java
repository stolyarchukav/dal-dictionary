package org.forzaverita.daldic.util.accent;

import org.forzaverita.daldic.util.SpecialCharacters;
import org.forzaverita.daldic.util.initial.Word;

public class WordFormatter {

    public static String formattedWord(Word word) {
        String name = word.getWord();
        Integer accentPosition = word.getAccentPosition();
        if (accentPosition != null) {
            return new StringBuilder().append(name).insert(accentPosition, SpecialCharacters.ACCENT).toString();
        }
        return name;
    }

}
