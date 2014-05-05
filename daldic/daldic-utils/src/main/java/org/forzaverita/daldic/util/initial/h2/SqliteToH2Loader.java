package org.forzaverita.daldic.util.initial.h2;

import org.forzaverita.daldic.util.initial.Database;
import org.forzaverita.daldic.util.initial.Word;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.forzaverita.daldic.util.initial.Database.DatabaseEngine;

public class SqliteToH2Loader {
	
	public static void main(String[] args) throws Exception {
		Database database = Database.getInstance(DatabaseEngine.H2);
		System.out.println(database.getMaxWordId());
		/*ResultSet rs = database.getConnecction().createStatement().executeQuery("select word, description, length(description) from word order by length(description) desc");
		if (rs.next()) {
			System.out.println(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getLong(3));
		}*/
		
		createSchema(database);
		
		int saved = 0;
		Set<Integer> notSaved = new HashSet<Integer>();
		Database source = Database.getInstance(DatabaseEngine.SQLITE);
		List<Integer> ids = source.getIds();
		for (int id : ids) {
			Word word = source.getWord(id);
			if (word.getWordReference() == 0) {
				word.setWordReference(null);
			}
			boolean result = database.createWord(word);
			if (result) {
				saved++;
			}
			else {
				notSaved.add(word.getId());
			}
		}
		
		System.out.println("saved = " + saved + ", not saved = " + notSaved);
		ResultSet rs = database.getConnecction().createStatement().executeQuery("select count(*) from word");
		rs.next();
		System.out.println("Words count = " + rs.getInt(1));
		
		database.getConnecction().commit();
		database.getConnecction().close();
	}

	private static void createSchema(Database database) throws SQLException {
		database.getConnecction().createStatement().execute("CREATE TABLE word (" +
				"word_id INT PRIMARY KEY, word VARCHAR_IGNORECASE(30) NOT NULL," +
				"description VARCHAR_IGNORECASE(20000), first_letter CHAR(1) NOT NULL, word_ref INT)");
	}

}
