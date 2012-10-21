package org.forzaverita.iverbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TranslatePopulator {

	private static final String URL = "http://www.syslang.com/frengly/controller?" +
			"action=translateREST&src={src}&dest={dest}&" +
			"text={text}&username=andrey&password=maldini";
	
	private static RestTemplate restTemplate = new RestTemplate();
	private static Map<String, String> params = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {
		Connection conn = ConnectionFactory.createConnection();
		/*fillLang(conn, "it");
		fillLang(conn, "fr");
		fillLang(conn, "de");*/
		fillLang(conn, "es");
		/*fillLang(conn, "cz");
		fillLang(conn, "ja");
		fillLang(conn, "pt");
		fillLang(conn, "zh");
		fillLang(conn, "ko");
		fillLang(conn, "uk");*/
		conn.close();
	}
	
	private static void fillLang(Connection conn, String lang) throws Exception {
		//conn.createStatement().execute("alter table verb add " + lang + " text");
		PreparedStatement stmt = conn.prepareStatement("update verb set " + lang + " = ? where id = ?");
		ResultSet rs = conn.prepareStatement("select id, form_1 from verb").executeQuery();
		while (rs.next()) {
			Long id = rs.getLong(1);
			String text = rs.getString(2);
			String translation = translate(text, lang);
			stmt.setString(1, translation);
			stmt.setLong(2, id);
			stmt.executeUpdate();
			System.out.println(lang + " : " + text + " : " + translation);
		}
		stmt.close();
		rs.close();
	}
	
	private static String translate(String text, String lang) throws Exception {
		Thread.sleep(3000);
		params.put("src", "en");
		params.put("dest", lang);
		params.put("text", text);
		ResponseEntity<Pair> response = restTemplate.getForEntity(URL, Pair.class, params);
		return response.getBody().getTranslation();
	}
	
}
