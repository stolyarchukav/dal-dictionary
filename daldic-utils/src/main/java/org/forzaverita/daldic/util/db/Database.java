package org.forzaverita.daldic.util.db;

import org.forzaverita.daldic.util.initial.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Database implements AutoCloseable {

	public enum DatabaseEngine {
		H2("org.h2.Driver", "jdbc:h2:huge_data/daldich2"),
		SQLITE("org.sqlite.JDBC", "jdbc:sqlite:daldic-utils\\src\\main\\resources\\daldic.db");

		DatabaseEngine(String driverClass, String connectionString) {
			this.driverClass = driverClass;
			this.connectionString = connectionString;
		}

		private final String driverClass;
		private final String connectionString;
	}

	private static final String SELECT_WORD_IDS = "select word_id from word";
	private static final String SELECT_WORD_IDS_BY_FILTER = "select word_id from word where description like ?";
	private static final String SELECT_MAX_WORD_ID = "select max(word_id) from word";
	private static final String SELECT_WORD_BY_ID = "select word_id, word, description, first_letter," +
			" word_ref, accent_position from word where word_id = ?";
	private static final String SELECT_WORD_BY_NAME = "select word_id, word, description, first_letter," +
			" word_ref, accent_position from word where upper(word) = ?";
	private static final String SELECT_WORD_BY_NAME_CONTAINS = "select word_id, word, description, first_letter," +
			" word_ref, accent_position from word where word like ?";
	private static final String UPDATE_WORD = "update word set word = ?, description = ?," +
			" first_letter = ?, word_ref = ?, accent_position = ? where word_id = ?";
	private static final String INSERT_WORD = "insert into word (word_id, word, description, first_letter," +
			" word_ref, accent_position) values (?, ?, ?, ?, ?, ?)";
	private static final String DELETE_WORD = "delete from word where word_id = ?";

	private static final Map<DatabaseEngine, Database> INSTANCE = new ConcurrentHashMap<>();
	
	private Connection conn;

	private PreparedStatement psSelectWordIds;
	private PreparedStatement psSelectWordIdsByFilter;
	private PreparedStatement psSelectMaxWordId;
	private PreparedStatement psSelectWordById;
	private PreparedStatement psSelectWordByName;
	private PreparedStatement psSelectWordByNameContains;
	private PreparedStatement psUpdateWord;
	private PreparedStatement psInsertWord;
	private PreparedStatement psDeleteWord;
	
	public static Database getInstance() {
		return getInstance(DatabaseEngine.SQLITE);
	}
	
	public static Database getInstance(DatabaseEngine databaseEngine) {
		if (INSTANCE.get(databaseEngine) == null) {
			synchronized (Database.class) {
				if (INSTANCE.get(databaseEngine) == null) {
					INSTANCE.put(databaseEngine, new Database(databaseEngine));
				}
			}
		}
		return INSTANCE.get(databaseEngine);
	}
	
	private Database(DatabaseEngine databaseEngine) {
		try {
			Class.forName(databaseEngine.driverClass);
			conn = DriverManager.getConnection(databaseEngine.connectionString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public Integer getMaxWordId() {
		try {
			if (psSelectMaxWordId == null) {
				psSelectMaxWordId = conn.prepareStatement(SELECT_MAX_WORD_ID);
			}
			try (ResultSet rs = psSelectMaxWordId.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Word getWord(int id) {
		Word word = null;
		try {
			if (psSelectWordById == null) {
				psSelectWordById = conn.prepareStatement(SELECT_WORD_BY_ID);
			}
			psSelectWordById.setInt(1, id);
			ResultSet rs = psSelectWordById.executeQuery();
			if (rs.next()) {
				word = new Word();
				word.setId(rs.getInt(1));
				word.setWord(rs.getString(2));
				word.setDescription(rs.getString(3));
				word.setFirstLetter(rs.getString(4));
				word.setWordReference(rs.getInt(5));
				word.setAccentPosition(rs.getInt(6));
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return word;
	}

	public List<Word> getWordsContains(String wordNameContains) {
		return getWord("%" + wordNameContains + "%", SELECT_WORD_BY_NAME_CONTAINS, psSelectWordByNameContains);
	}

	public Word getWord(String wordName) {
		return getWord(wordName, SELECT_WORD_BY_NAME, psSelectWordByName).stream().findFirst().orElse(null);
	}

	private List<Word> getWord(String searchValue, String query, PreparedStatement statement) {
		List<Word> words = new ArrayList<>();
		try {
			if (statement == null) {
				statement = conn.prepareStatement(query);
			}
			statement.setString(1, searchValue.toUpperCase());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Word word = new Word();
				word.setId(rs.getInt(1));
				word.setWord(rs.getString(2));
				word.setDescription(rs.getString(3));
				word.setFirstLetter(rs.getString(4));
				word.setWordReference(rs.getInt(5));
				word.setAccentPosition(rs.getInt(6));
				words.add(word);
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return words;
	}
	
	public boolean createWord(Word word) {
		boolean result = false;
		try {
			if (psUpdateWord == null) {
				psUpdateWord = conn.prepareStatement(INSERT_WORD);
			}
			psUpdateWord.setInt(1, word.getId());
			psUpdateWord.setString(2, word.getWord());
			psUpdateWord.setString(3, word.getDescription());
			psUpdateWord.setString(4, word.getFirstLetter());
			if (word.getWordReference() == null) {
				psUpdateWord.setNull(5, Types.INTEGER);
			}
			else {
				psUpdateWord.setInt(5, word.getWordReference());
			}
			if (word.getAccentPosition() == null) {
				psUpdateWord.setNull(6, Types.INTEGER);
			} else {
				psUpdateWord.setInt(6, word.getAccentPosition());
			}
			psUpdateWord.executeUpdate();
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean saveWord(Word word) {
		boolean result = false;
		try {
			if (psUpdateWord == null) {
				psUpdateWord = conn.prepareStatement(UPDATE_WORD);
			}
			psUpdateWord.setString(1, word.getWord());
			psUpdateWord.setString(2, word.getDescription());
			psUpdateWord.setString(3, word.getFirstLetter());
			if (word.getWordReference() == null) {
				psUpdateWord.setNull(4, Types.INTEGER);
			}
			else {
                psUpdateWord.setInt(4, word.getWordReference());
			}
			if (word.getAccentPosition() == null) {
				psUpdateWord.setNull(5, Types.INTEGER);
			} else {
				psUpdateWord.setInt(5, word.getAccentPosition());
			}
			psUpdateWord.setInt(6, word.getId());
			psUpdateWord.executeUpdate();
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean createReferred(String word, Word refTo) {
		boolean result = false;
		Integer maxId = getMaxWordId();
		if (maxId != null) {
			try {
				if (psInsertWord == null) {
					psInsertWord = conn.prepareStatement(INSERT_WORD);
				}
				psInsertWord.setInt(1, ++maxId);
				psInsertWord.setString(2, word.toUpperCase());
				psInsertWord.setString(4, String.valueOf(word.charAt(0)));
				psInsertWord.setInt(5, refTo.getId());
				psInsertWord.executeUpdate();
				result = true;
				System.out.println("Created new word '" + word + "' with id = " + maxId + 
						", refered to id = " + refTo.getId());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean updateRefTo(Word word, String wordSelected) {
		boolean result = false;
		Word wordRef = getWord(wordSelected);
		if (wordRef != null) {
			word.setWordReference(wordRef.getId());
			result = saveWord(word);
		}
		return result;
	}
	
	public boolean deleteWord(int id) {
		boolean result = false;
		try {
			if (psDeleteWord == null) {
				psDeleteWord = conn.prepareStatement(DELETE_WORD);
			}
			psDeleteWord.setInt(1, id);
			psDeleteWord.executeUpdate();
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Integer> getIds(String text) {
		List<Integer> result = new ArrayList<>();
		try {
			if (psSelectWordIdsByFilter == null) {
				psSelectWordIdsByFilter = conn.prepareStatement(SELECT_WORD_IDS_BY_FILTER);
			}
			psSelectWordIdsByFilter.setString(1, "%" + text + "%");
			ResultSet rs = psSelectWordIdsByFilter.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Integer> getIds() {
		List<Integer> result = new ArrayList<>();
		try {
			if (psSelectWordIds == null) {
				psSelectWordIds = conn.prepareStatement(SELECT_WORD_IDS);
			}
			ResultSet rs = psSelectWordIds.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Integer> getSuspectedTitleIds() {
		List<Integer> result = new ArrayList<>();
		List<Integer> ids = getIds();
		for (int id : ids) {
			Word word = getWord(id);
			String title = word.getWord();
			String desc = word.getDescription();
			if (desc != null) {
				StringBuilder rTitleBuilder = new StringBuilder();
				for (int q = 0; q < desc.length(); q++) {
					char ch = desc.charAt(q);
					if (Character.isUpperCase(ch) || Character.isWhitespace(ch) || ch == '-') {
						rTitleBuilder.append(ch);
					}
					else {
						break;
					}
				}
				String rTitle = rTitleBuilder.toString().trim();
				if (! rTitle.equals(title)) {
					result.add(word.getId());
				}
			}
		}
		return result;
	}

	@Override
	public void close() throws Exception {
		conn.close();
	}

}
