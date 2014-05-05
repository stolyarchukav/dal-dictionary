package org.forzaverita.daldic.util.accent;

import org.forzaverita.daldic.util.initial.Database;
import org.forzaverita.daldic.util.initial.Word;

import java.util.List;

public class DataUpdater {

    public static void main(String[] args) {
        Database db = Database.getInstance();
        List<Integer> ids = db.getIds();
        for (Integer id : ids) {
            Word word = db.getWord(id);

        }

    }

}
