package org.forzaverita.daldic.util.accent;

import org.forzaverita.daldic.util.db.Database;
import org.forzaverita.daldic.util.initial.Word;

import java.util.List;

public class AccentsFounder {

    public static void main(String[] args) {
        List<Word> words = Database.getInstance().getWordsContains("́");
        words.forEach(System.out::println);
        System.out.println(words.size());
    }

}
