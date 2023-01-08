package org.forzaverita.daldic.util.accent;

import org.forzaverita.daldic.util.SpecialCharacters;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Test {

    public static void main(String[] args) {
        String s = "Аба́ка";
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));
        }
        System.out.println(s);
        System.out.println("Аба" + SpecialCharacters.ACCENT + "ка");
        System.out.println(Arrays.toString(SpecialCharacters.ACCENT.getBytes()));
        System.out.println(Arrays.toString(s.getBytes()));
        s.chars().forEach(ch -> System.out.println("" + ch));
        IntStream.range(0, 3000).forEach(i -> {
            System.out.println(i + ": " + (char) i);
        });
    }

}
