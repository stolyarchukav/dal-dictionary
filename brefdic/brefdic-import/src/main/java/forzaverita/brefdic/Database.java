package forzaverita.brefdic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import forzaverita.brefdic.model.Image;
import forzaverita.brefdic.model.Word;

public final class Database {
	
	private static final String SELECT_WORD_COUNT = "select count(*) from word";
	private static final String SELECT_WORD_IDS = "select word_id from word";
	private static final String SELECT_WORD_IDS_BY_FILTER = "select word_id from word where description like ?";
	private static final String SELECT_MAX_WORD_ID = "select max(word_id) from word";
	private static final String SELECT_WORD_BY_ID = "select word_id, word, description, first_letter" +
			" from word where word_id = ?";
	private static final String SELECT_WORD_BY_NAME = "select word_id, word, description, first_letter" +
			" from word where upper(word) = ?";
	private static final String UPDATE_WORD = "update word set word = ?, description = ?," +
			" first_letter = ? where word_id = ?";
	private static final String INSERT_WORD = "insert into word (word_id, word, description, first_letter)" +
			" values (?, ?, ?, ?)";
	private static final String DELETE_WORD = "delete from word where word_id = ?";
	private static final String INSERT_IMAGE = "insert into word_image (word_id, image_id, position)" +
			" values (?, ?, ?)";
	
	private static Database INSTANCE;
	
	private Connection conn;

	private PreparedStatement psSelectWordIds;
	private PreparedStatement psSelectWordIdsByFilter;
	private PreparedStatement psSelectMaxWordId;
	private PreparedStatement psSelectWordById;
	private PreparedStatement psSelectWordByName;
	private PreparedStatement psUpdateWord;
	private PreparedStatement psInsertWord;
	private PreparedStatement psDeleteWord;
	private PreparedStatement psInsertImage;
	
	public static Database getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Database();
		}
		return INSTANCE;
	}
	
	private Database() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/brefdic.sqlite");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer getMaxWordId() {
		Integer maxId = null;
		try {
			if (psSelectMaxWordId == null) {
				psSelectMaxWordId = conn.prepareStatement(SELECT_MAX_WORD_ID);
			}
			ResultSet rs = psSelectMaxWordId.executeQuery();
			if (rs.next()) {
				maxId = rs.getInt(1);
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return maxId;
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
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return word;
	}
	
	public Word getWord(String wordName) {
		Word word = null;
		try {
			if (psSelectWordByName == null) {
				psSelectWordByName = conn.prepareStatement(SELECT_WORD_BY_NAME);
			}
			psSelectWordByName.setString(1, wordName.toUpperCase());
			ResultSet rs = psSelectWordByName.executeQuery();
			if (rs.next()) {
				word = new Word();
				word.setId(rs.getInt(1));
				word.setWord(rs.getString(2));
				word.setDescription(rs.getString(3));
				word.setFirstLetter(rs.getString(4));
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return word;
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
			psUpdateWord.setInt(4, word.getId());
			psUpdateWord.executeUpdate();
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean createWord(Word word) {
		boolean result = false;
		Integer maxId = getMaxWordId();
		if (maxId != null) {
			try {
				if (psInsertWord == null) {
					psInsertWord = conn.prepareStatement(INSERT_WORD);
				}
				psInsertWord.setInt(1, ++maxId);
				psInsertWord.setString(2, word.getWord());
				psInsertWord.setString(3, word.getDescription());
				psInsertWord.setString(4, word.getFirstLetter());
				psInsertWord.executeUpdate();
				if (word.getImages() != null && ! word.getImages().isEmpty()) {
					if (psInsertImage == null) {
						psInsertImage = conn.prepareStatement(INSERT_IMAGE);
					}
					for (Image image : word.getImages()) {
						psInsertImage.setInt(1, word.getId());
						psInsertImage.setInt(2, image.getImageId());
						psInsertImage.setInt(3, image.getPosition());
						psInsertImage.executeUpdate();
					}
				}
				result = true;
				System.out.println("Created new word '" + word);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
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
		List<Integer> result = new ArrayList<Integer>();
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
		List<Integer> result = new ArrayList<Integer>();
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
		List<Integer> result = new ArrayList<Integer>();
		List<Integer> ids = INSTANCE.getIds();
		for (int id : ids) {
			Word word = INSTANCE.getWord(id);
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
	
	public int getCount() {
		int result = -1;
		try {
			if (psSelectWordIds == null) {
				psSelectWordIds = conn.prepareStatement(SELECT_WORD_COUNT);
			}
			ResultSet rs = psSelectWordIds.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
