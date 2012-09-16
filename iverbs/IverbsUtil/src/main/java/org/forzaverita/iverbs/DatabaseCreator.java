package org.forzaverita.iverbs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseCreator {

	public static void main(String[] args) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/iverbs.sqlite");
			conn.createStatement().execute("create table verb (" +
					"id integer primary key," +
					"form_1 text not null," +
					"form_2 text not null," +
					"form_3 text not null," +
					"rus text," +
					"ita text," +
					"ukr text" +
					")");
			PreparedStatement stmt = conn.prepareStatement("insert into verb values (?, ?, ?, ?, ?, ?, ?)");
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/verbs.csv"));
			int q = 0;
			while (reader.ready()) {
				String line = reader.readLine();
				String[] values = line.split("[|]");
				stmt.setInt(1, ++q);
				stmt.setString(2, values[0]);
				stmt.setString(3, values[1]);
				stmt.setString(4, values[2]);
				stmt.setString(5, values[3]);
				stmt.setString(6, null);
				stmt.setString(7, null);
				stmt.executeUpdate();
				System.out.println(q);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
