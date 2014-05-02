package org.forzaverita.iverbs.add;

import org.forzaverita.iverbs.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TranslateUpdaterIfEmpty {

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionFactory.createConnection();

        PreparedStatement update = conn.prepareStatement("update verb set ro = ? where id = ? and ro is null");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" +
                "tr_ro.csv"));

        int id = 0;

        while (reader.ready()) {
            String line = reader.readLine().trim().toLowerCase();
            String value = line.substring(line.indexOf(" ") + 1);
            update.setString(1, value);
            update.setInt(2, ++id);
            update.executeUpdate();
            System.out.println("Updated: [" + id + "], [" + value + "]");
        }
        update.close();
        conn.close();
    }

}
