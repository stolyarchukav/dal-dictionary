package org.forzaverita.iverbs.translate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.forzaverita.iverbs.ConnectionFactory;
import org.forzaverita.iverbs.translate.impl.TranslateRestSyslang;

public class TranslatePopulator {
	
	private static Translator translator = new TranslateRestSyslang();
	//private static Translator translator = new TranslateWs();
	
	public static void main(String[] args) throws Exception {
		Connection conn = ConnectionFactory.createConnection();
		/*fillLang(conn, "it");
		fillLang(conn, "fr");
		fillLang(conn, "de");
		fillLang(conn, "es");*/
		fillLang(conn, "cz");
		fillLang(conn, "ja");
		fillLang(conn, "pt");
		fillLang(conn, "zh");
		fillLang(conn, "ko");
		fillLang(conn, "ua");
		conn.close();
	}
	
	private static void fillLang(Connection conn, String lang) throws Exception {
		//conn.createStatement().execute("alter table verb add " + lang + " text");
		PreparedStatement stmt = conn.prepareStatement("update verb set " + lang + " = ? where id = ?");
		ResultSet rs = conn.prepareStatement("select id, form_1 from verb").executeQuery();
		while (rs.next()) {
			Long id = rs.getLong(1);
			String text = rs.getString(2);
			String translation = translator.translate(text, lang);
			stmt.setString(1, translation);
			stmt.setLong(2, id);
			stmt.executeUpdate();
			System.out.println(lang + " : " + text + " : " + translation);
		}
		stmt.close();
		rs.close();
	}
	
}
