package org.forzaverita.daldic.util.accent;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("words.txt", true)))) {
            try(BufferedReader links = new BufferedReader(new FileReader("links.txt"))) {
                while(links.ready()) {
                    String link = links.readLine();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(link).
                            openConnection().getInputStream()))) {
                        while(br.ready()) {
                            String line = br.readLine();
                            String word = StringUtils.substringBetween(line, "<b>", "</b>");
                            if (word == null) {
                                word = StringUtils.substringBetween(line, ">", "</span>");
                            }
                            if (word != null && word.contains("́")) {
                                System.out.println(word);
                                writer.println(word);
                            }
                        }
                    }
                    Thread.sleep(1000);
                }
            }
        }
    }

}
