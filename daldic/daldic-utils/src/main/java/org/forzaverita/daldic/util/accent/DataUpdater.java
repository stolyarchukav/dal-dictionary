package org.forzaverita.daldic.util.accent;

import org.apache.commons.lang3.StringUtils;
import org.forzaverita.daldic.util.initial.Database;
import org.forzaverita.daldic.util.initial.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUpdater {

    public static void main(String[] args) throws Exception {

        Map<String, String> words = getAccentWords();

        System.out.println("Accented words: " + words.size());

        Database db = Database.getInstance();
        List<Integer> ids = db.getIds();

        System.out.println("Word ids loaded: " + ids.size());

        for (Integer id : ids) {
            Word entity = db.getWord(id);
            boolean modified = false;
            for (String word : words.keySet()) {
                if (entity.getWord().trim().equalsIgnoreCase(word)) {
                    entity.setWord(words.get(word).toUpperCase());
                    System.out.println("Word: " + word);
                    modified = true;
                }
                String desc = entity.getDescription();
                if (desc != null) {
                    if (desc.startsWith(word.toUpperCase())) {
                        entity.setDescription(desc.replaceFirst(word.toUpperCase(), words.get(word).toUpperCase()));
                        System.out.println("Description start: " + entity.getWord() + " --- " + word);
                        modified = true;
                    }
                    int start = desc.indexOf(" " + word + " ");
                    if (start == -1) {
                        start = desc.indexOf(" " + word + ".");
                    }
                    if (start == -1) {
                        start = desc.indexOf(" " + word + ",");
                    }
                    if (start != -1) {
                        desc = desc.substring(0, start + 1) + words.get(word).toLowerCase() + desc.substring(start + 1 + word.length());
                        entity.setDescription(desc);
                        System.out.println("Description: " + entity.getWord() + " --- " + word + " --- " + start);
                        modified = true;
                    }
                }
            }
            if (modified) {
                System.out.println(entity);
                db.saveWord(entity);
            }
        }
        db.getConnection().close();
    }

    private static Map<String, String> getAccentWords() throws IOException {
        Map<String, String> words = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("words_result.txt"))) {
            while (br.ready()) {
                String word = br.readLine().toLowerCase().trim();
                words.put(word.replace("́", ""), word);
            }
        }
        return words;
    }

}
