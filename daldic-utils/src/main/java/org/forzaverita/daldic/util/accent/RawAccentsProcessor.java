package org.forzaverita.daldic.util.accent;

import org.forzaverita.daldic.util.SpecialCharacters;

import java.io.*;

public class RawAccentsProcessor {

    public static void main(String[] args) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("words_result.txt")))) {
            try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
                while(br.ready()) {
                    String line = br.readLine();
                    int start = line.lastIndexOf(">");
                    if (start != -1) {
                        line = line.substring(start + 1).toLowerCase();
                    }
                    String[] words = line.split(",");
                    for (String word : words) {
                        word = word.replace("(", "");
                        word = word.replace(")", "");
                        word = word.replace("[", "");
                        word = word.replace("]", "");
                        word = word.trim().toLowerCase();
                        word = word.split(" ")[0];
                        if (word.contains(SpecialCharacters.ACCENT)) {
                            writer.println(word);
                        }
                    }
                }
            }
        }
    }

}
