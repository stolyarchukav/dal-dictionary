package org.forzaverita.iverbs.lang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.forzaverita.iverbs.ConnectionFactory;

public class LangImporter {

	public static void importLang(String lang, int engIdx, int langIdx, String fileName) 
			throws Exception {
		importLang(lang, engIdx, langIdx, fileName, "[|]");
	}
	
	public static void importLang(String lang, int engIdx, int langIdx, String fileName, 
			String separator) throws Exception {
		Connection conn = ConnectionFactory.createConnection();
		PreparedStatement search = conn.prepareStatement("select id, " + 
				lang + " from verb where form_1 = ?");
		PreparedStatement update = conn.prepareStatement("update verb set " + 
				lang + " = ? where id = ?");
		BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + 
				fileName + ".csv"));
		while (reader.ready()) {
			String line = reader.readLine();
			String[] values = line.split(separator);
			if (values.length > 1) {
				String form1 = values[engIdx].trim().toLowerCase();
				String translation = values[langIdx].trim().replaceAll("[*]", "");
				search.setString(1, form1);
				ResultSet rs = search.executeQuery();
				while (rs.next()) {
					update.setInt(2, rs.getInt(1));
					update.setString(1, translation);
					update.executeUpdate();
					System.out.println("Updated: [" + form1 + "], [" + translation + "]");
				}
				rs.close();
			}
		}
		update.close();
		search.close();
		conn.close();
	}
	
}
