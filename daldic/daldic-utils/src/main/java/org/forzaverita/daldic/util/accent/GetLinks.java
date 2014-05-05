package org.forzaverita.daldic.util.accent;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class GetLinks {

    public static void main(String[] args) throws Exception {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("links.txt", true)))) {
            try (BufferedReader br = new BufferedReader(new FileReader("raw_links"))) {
                while(br.ready()) {
                    String line = br.readLine();
                    String[] words = StringUtils.substringsBetween(line, "<a href=\"", "\"");
                    for (String word : words) {
                        word = "http://ru.wikisource.org" + word;
                        writer.println(word);
                        System.out.println(word);
                    }
                }
            }
        }
    }

}
