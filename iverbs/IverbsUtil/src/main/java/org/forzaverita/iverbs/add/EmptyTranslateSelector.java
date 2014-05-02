package org.forzaverita.iverbs.add;

import org.forzaverita.iverbs.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmptyTranslateSelector {

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionFactory.createConnection();

        PreparedStatement search = conn.prepareStatement("select form_1 " +
                " from verb");

        ResultSet rs = search.executeQuery();
        while (rs.next()) {
            System.out.println("to " + rs.getString(1));
        }

        search.close();
        conn.close();
    }

}
