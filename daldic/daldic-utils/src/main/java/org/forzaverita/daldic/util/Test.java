package org.forzaverita.daldic.util;

import org.apache.commons.lang3.StringUtils;

public class Test {

    public static void main(String[] args) {
        String s = "Аба́ка";
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));
        }
        System.out.println(s);
        System.out.printf("Аба" + "́" + "ка");
    }

}
