package org.forzaverita.iverbs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseCreator {

	public static void main(String[] args) {
		try {
			Connection conn = ConnectionFactory.createConnection();
			conn.createStatement().execute("create table android_metadata (" +
					"locale text" +
					")");
			conn.createStatement().execute("insert into android_metadata values ('ru_RU')");
			
			conn.createStatement().execute("create table iverbs_metadata (" +
					"data_version integer not null" +
					")");
			conn.createStatement().execute("insert into iverbs_metadata values (2)");
			
			conn.createStatement().execute("create table verb (" +
					"id integer primary key," +
					"form_1 text not null," +
					"form_1_transcription text not null," +
					"form_2 text not null," +
					"form_2_transcription text not null," +
					"form_3 text not null," +
					"form_3_transcription text not null," +
					"ru text" +
					")");
			PreparedStatement stmt = conn.prepareStatement("insert into verb values (" +
					"?, ?, ?, ?, ?, ?, ?, ?)");
			BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/verbs.csv"));
			int q = 0;
			while (reader.ready()) {
				String line = reader.readLine();
				String[] values = line.split("[|]");
				stmt.setInt(1, ++q);
				String[] form1 = parseWordTranscription(values[0]);
				stmt.setString(2, form1[0]);
				stmt.setString(3, form1[1]);
				String[] form2 = parseWordTranscription(values[1]);
				stmt.setString(4, form2[0]);
				stmt.setString(5, form2[1]);
				String[] form3 = parseWordTranscription(values[2]);
				stmt.setString(6, form3[0]);
				stmt.setString(7, form3[1]);
				String[] rus = parseWordTranscription(values[3]);
				stmt.setString(8, rus[0]);
				stmt.executeUpdate();
				System.out.println(q);
			}
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String[] parseWordTranscription(String item) {
		String word = "";
		String transcription = "";
		String[] elements = item.split("[,]");
		for (String elem : elements) {
			String[] pair = elem.split("[+]");
			if (! word.isEmpty()) {
				word += ", ";
			}
			word += pair[0];
			if (pair.length > 1) {
				if (! transcription.isEmpty()) {
					transcription += ", ";
				}
				transcription += pair[1];
			}
		}
		return new String[] {word, transcription};
	}
	
}
