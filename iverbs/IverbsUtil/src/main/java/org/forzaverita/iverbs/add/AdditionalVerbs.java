package org.forzaverita.iverbs.add;

import org.forzaverita.iverbs.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdditionalVerbs {

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionFactory.createConnection();

        PreparedStatement search = conn.prepareStatement("select id " +
                " from verb where form_1 = ?");
        PreparedStatement insert = conn.prepareStatement("insert into verb (id, form_1, form_1_transcription, " +
                "form_2, form_2_transcription, form_3, form_3_transcription, ru) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" +
                "full_list.csv"));

        int num = 127;
        while (reader.ready()) {
            String line = reader.readLine();
            String[] values = line.split("\t");

            String[] value = values[1].split(" ");
            String form1 = value[0].toLowerCase();
            String form1Tr = value[1].toLowerCase();

            value = values[2].split(" ");
            String form2 = value[0].toLowerCase();
            String form2Tr = value[1].toLowerCase();

            value = values[3].split(" ");
            String form3 = value[0].toLowerCase();
            String form3Tr = value[1].toLowerCase();

            String ru = values[4].toLowerCase();

            search.setString(1, form1);
            ResultSet rs = search.executeQuery();
            if (! rs.next()) {
                insert.setInt(1, ++num);
                insert.setString(2, form1);
                insert.setString(3, form1Tr);
                insert.setString(4, form2);
                insert.setString(5, form2Tr);
                insert.setString(6, form3);
                insert.setString(7, form3Tr);
                insert.setString(8, ru);
                insert.executeUpdate();
                System.out.println("Inserted: [" + form1 + "], [" + ru + "]");
            }
            rs.close();
        }
        insert.close();
        search.close();
        conn.close();
    }

}
