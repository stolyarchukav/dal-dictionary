package org.forzaverita.daldic.util.initial;

import org.forzaverita.daldic.util.db.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class DataLoader {

	public static void main(String[] args) throws Exception {
		// Postgres
		/*Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/daldic",
				"postgres", "postgres");*/
		
		// SQLite
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/daldic.sqlite");
		
		//importFromFile(conn);
		//deleteQuestionSymbols(conn);
		//convertToUpperCase(conn);
		//testSearchById(conn);
		//delete(conn, findDuplicates(conn));
		//createRefToSm(conn);
		//searchById(conn);
		//createRefToSm(conn);
		//fixFirstLetter(conn);
		//fixReferences(conn);
		//findDuplicates(conn);
		findLowerCaseWord(conn);
	}
	
	private static void findLowerCaseWord(Connection conn) {
		List<Integer> ids = Database.getInstance().getIds();
		for (int id : ids) {
			Word word = Database.getInstance().getWord(id);
			for (char ch : word.getWord().toCharArray()) {
				if (Character.isLowerCase(ch)) {
					System.out.println(word);
					break;
				}
			}
		}
	}

	private static void fixReferences(Connection conn) throws SQLException {
		List<Integer> ids = Database.getInstance().getIds();
		for (int id : ids) {
			Word word = Database.getInstance().getWord(id);
			Integer ref = word.getWordReference();
			if (ref != 0) {
				Word refWord = Database.getInstance().getWord(ref);
				if (refWord == null) {
					System.out.println(word + " none");
				}
				else if (word.getDescription() != null && ! word.getDescription().isEmpty() && word.getDescription().toUpperCase().indexOf(refWord.getWord().toUpperCase()) == -1) {
					System.out.println(refWord.getWord() + " " + word);
					word.setWordReference(0);
					Database.getInstance().saveWord(word);
				}
			}
		}
	}
	
	private static void fixFirstLetter(Connection conn) throws SQLException {
		List<Integer> ids = Database.getInstance().getIds();
		for (int id : ids) {
			Word word = Database.getInstance().getWord(id);
			String letter = word.getFirstLetter();
			String title = word.getWord();
			if (letter.length() != 1 || letter.charAt(0) != title.charAt(0)) {
				System.out.println(title + " <> " + letter);
				word.setFirstLetter(String.valueOf(title.charAt(0)));
				Database.getInstance().saveWord(word);
			}
		}
	}

	private static void createRefToSm(Connection conn) throws SQLException {
		String sm = " см. ";
		List<Integer> ids = Database.getInstance().getIds(sm);
		for (int id : ids) {
			Word word = Database.getInstance().getWord(id);
			System.out.println("---");
			System.out.println(word);
			String desc = word.getDescription();
			if (desc.length() < 100) {
				int first = desc.indexOf(sm) + sm.length();
				int last = desc.indexOf(" ", first);
				if (last == -1) last = Integer.MAX_VALUE;
				int last2 = desc.indexOf(".", first);
				if (last2 == -1) last2 = Integer.MAX_VALUE;
				int last3 = desc.indexOf(",", first);
				if (last3 == -1) last3 = Integer.MAX_VALUE;
				int min = Math.min(Math.min(last, last2), last3);
				String ref = null;
				if (min != Integer.MAX_VALUE) {
					ref = desc.substring(first, min);
				}
				else {
					ref = desc.substring(first);
				}
				if (ref != null && ! ref.toUpperCase().equals("см.".toUpperCase()) && 
						! ref.toUpperCase().equals("см.".toUpperCase())) {
					Word wordRef = Database.getInstance().getWord(ref);
					if (word.getWordReference() == 0) {
						if (wordRef != null) {
							System.out.println(wordRef);
							word.setWordReference(wordRef.getId());
							Database.getInstance().saveWord(word);
						}
					}
				}
			}
		}
	}
	
	private static void delete(Connection conn, Set<Integer> ids) throws Exception {
		PreparedStatement psDelete = conn.prepareStatement("delete from word where word_id = ?");
		System.out.print("Deleted ");
		for (int id : ids) {
			psDelete.setInt(1, id);
			psDelete.executeUpdate();
			System.out.print(id + " ");
		}
		System.out.println();
	}
	
	private static Set<Integer> findDuplicates(Connection conn) throws SQLException {
		Set<Integer> duplicates = new HashSet<Integer>();
		PreparedStatement ps = conn.prepareStatement("select word from word group by word, word_ref having count(*) > 1");
		PreparedStatement psIn = conn.prepareStatement("select word_id, description, word_ref from word where word = ?");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			boolean duplicate = false;
			String word = rs.getString(1);
			psIn.setString(1, word);
			ResultSet rsIn = psIn.executeQuery();
			while (rsIn.next()) {
				if (rsIn.getInt(3) != 0) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				System.out.println(word);
				rsIn = psIn.executeQuery();
				while (rsIn.next()) {
					System.out.println("\t ---");
					System.out.println("\t" + rsIn.getInt(1));
					System.out.println("\t" + rsIn.getString(2));
					System.out.println("\t" + rsIn.getInt(3));
					if (rsIn.getString(2) == null) {
						duplicates.add(rsIn.getInt(1));
						break;
					}
				}
			}
		}
		return duplicates;
	}
	
	private static void searchById(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select description from word where word_id = ?");
		ps.setInt(1, 675);
		ResultSet rs = ps.executeQuery();
		rs.next();
		System.out.println(rs.getString(1));
	}

	private static void testSearchById(Connection conn) throws SQLException {
		Random rand = new Random();
		PreparedStatement ps = conn.prepareStatement("select word from word where word_id = ?");
		long time = System.currentTimeMillis();
		for (int q = 0; q < 100; q++) {
			ps.setInt(1, rand.nextInt(44652));
			ResultSet rs = ps.executeQuery();
			rs.next();
			System.out.println(rs.getString(1));
		}
		System.out.println(System.currentTimeMillis() - time);
	}

	private static void convertToUpperCase(Connection conn) throws Exception {
		Set<String> words = new HashSet<String>();
		PreparedStatement ps = conn.prepareStatement("select word from word");
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			String word = rs.getString(1);
			words.add(word);
		}
		for (String word : words) {
			PreparedStatement psUpdate = conn.prepareStatement("UPDATE WORD SET WORD = ? where WORD = ?");
			psUpdate.setString(1, word.toUpperCase());
			psUpdate.setString(2, word);
			psUpdate.executeUpdate();
		}
		
	}
	
	private static void deleteQuestionSymbols(Connection conn) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		PreparedStatement ps = conn.prepareStatement("select word from word where word like ?");
		ps.setString(1, "%'%");
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			String word = rs.getString(1);
			System.out.println(word);
			//String wordNew = word.replaceAll("\\?", "");
			String wordNew = word.replaceAll("\\'", "");
			System.out.println(" " + wordNew);
			map.put(word, wordNew);
		}
		
		for (String key : map.keySet()) {
			PreparedStatement psUpdate = conn.prepareStatement("UPDATE WORD SET WORD = ? where WORD = ?");
			psUpdate.setString(2, key);
			psUpdate.setString(1, map.get(key));
			psUpdate.executeUpdate();
		}
	}
	
	private static void importFromFile(Connection conn) throws Exception {
		conn.createStatement().execute("CREATE TABLE word (" +
				"word_id INTEGER PRIMARY KEY, word TEXT NOT NULL," +
				"description TEXT NOT NULL, first_letter TEXT NOT NULL)");
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO WORD VALUES (?, ?, ?, ?)");
		
		BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/tolkovyi_slovar_dalya.txt"));
		StringBuilder description = new StringBuilder();
		String word = null;
		int q = 0;
		while (reader.ready()) {
			String line = reader.readLine();
			if (line.startsWith("   ")) {
				System.out.println("Word = " + word + ", description = " + description.toString());
				if (word != null) {
					ps.setInt(1, ++q);
					ps.setString(2, word);
					ps.setString(3, description.toString());
					ps.setString(4, word.substring(0, 1));
					ps.executeUpdate();
				}
				line = line.substring(3).trim();
				int space = line.indexOf(" ");
				int comma = line.indexOf(",");
				int pos = Math.min(space, comma);
				if (pos == -1) {
					pos = (space != -1) ? space : comma; 
				}
				if (pos != -1) {
					word = line.substring(0, pos);
				}
				description = new StringBuilder();
				description.append(line);
			}
			else {
				description.append(" ");
				description.append(line);
			}
		}
	}
}
