package org.forzaverita.daldic.util.accent;

import static org.forzaverita.daldic.util.SpecialCharacters.ACCENT;

import org.forzaverita.daldic.util.db.Database;
import org.forzaverita.daldic.util.initial.Word;

import java.util.List;

public class AccentsFounder {

    public static void main(String[] args) {
        List<Word> words = Database.getInstance().getWordsContains(ACCENT);
        words.forEach(word -> {
            System.out.println(word);
            String name = word.getWord();
            int accent = name.indexOf(ACCENT);
            word.setWord(name.replaceAll(ACCENT, ""));
            System.out.println(accent);
            System.out.println(word.getWord());
            word.setAccentPosition(accent);
            System.out.println(WordFormatter.formattedWord(word));
            System.out.println(word);
            if (!Database.getInstance().saveWord(word)) {
                throw new RuntimeException();
            }
        });
        System.out.println(words.size());
    }

}
