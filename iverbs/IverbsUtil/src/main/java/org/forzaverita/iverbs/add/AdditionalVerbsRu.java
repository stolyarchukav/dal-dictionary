package org.forzaverita.iverbs.add;

import org.forzaverita.iverbs.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdditionalVerbsRu {

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionFactory.createConnection();

        PreparedStatement search = conn.prepareStatement("select id " +
                " from verb where form_1 = ?");
        PreparedStatement update = conn.prepareStatement("update verb set ru = ? where id = ?");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" +
                "full_list.csv"));

        while (reader.ready()) {
            String line = reader.readLine();
            String[] values = line.split("\t");

            String[] value = values[1].split(" ");
            String form1 = value[0].toLowerCase();
            String ru = values[4].toLowerCase();

            search.setString(1, form1);
            ResultSet rs = search.executeQuery();
            while (rs.next()) {
                update.setString(1, ru);
                update.setInt(2, rs.getInt(1));
                update.executeUpdate();
                System.out.println("Updated ru: [" + form1 + "], [" + ru + "]");
            }
            rs.close();
        }
        update.close();
        search.close();
        conn.close();
    }

}
