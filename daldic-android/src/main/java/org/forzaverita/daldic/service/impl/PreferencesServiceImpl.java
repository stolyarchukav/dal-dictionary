package org.forzaverita.daldic.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.preferences.TextFont;
import org.forzaverita.daldic.service.PreferencesService;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.forzaverita.daldic.data.Constants.NAME_PREF_TEXT_ALIGN;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_TEXT_CAPITAL_LETTERS;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_TEXT_FONT;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_WIDGET_REFRESH_AUTO;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_WIDGET_REFRESH_INTERVAL;
import static org.forzaverita.daldic.data.Constants.PREF_REFRESH_AUTO;
import static org.forzaverita.daldic.data.Constants.PREF_REFRESH_INTERVAL;
import static org.forzaverita.daldic.data.Constants.PREF_TEXT_CAPITAL_LETTERS;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String INTERNAL_STORAGE = "internalStorage";
	private static final String DATABASE_PATH = "databasePath";
	private static final String HISTORY_KEY = "searchHistory";
	private static final String BOOKMARK_KEY = "bookmark";
	private static final String STAT_SEARCH_KEY = "statSearch";
	private static final String STAT_FULL_SEARCH_KEY = "statFullSearch";
	private static final String STAT_OPEN_WORD_KEY = "statOpenWord";
	private static final String STAT_OPEN_WORD_WIDGET_KEY = "statOpenWordWidget";
	private static final String STAT_BOOKMARK_KEY = "statBookmark";
	private static final int MAX_WORDS_COUNT = 30;

	private SharedPreferences preferences;
	
	public PreferencesServiceImpl(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@Override
	public boolean isInternalStorage() {
		return preferences.getBoolean(INTERNAL_STORAGE, false);
	}
	
	@Override
	public void switchPreferredStorage() {
		preferences.edit().putBoolean(INTERNAL_STORAGE, ! isInternalStorage()).apply();
	}
	
	@Override
	public String getDatabasePath() {
		return preferences.getString(DATABASE_PATH, null);
	}
	
	@Override
	public void setDatabasePath(String databasePath) {
		preferences.edit().putString(DATABASE_PATH, databasePath).apply();
	}
	
	@Override
	public boolean isCapitalLetters() {
		return preferences.getBoolean(NAME_PREF_TEXT_CAPITAL_LETTERS, PREF_TEXT_CAPITAL_LETTERS);
	}
	
	@Override
	public int getRefreshInterval() {
		return preferences.getInt(NAME_PREF_WIDGET_REFRESH_INTERVAL, PREF_REFRESH_INTERVAL);
	}
	
	@Override
	public boolean isAutoRefresh() {
		return preferences.getBoolean(NAME_PREF_WIDGET_REFRESH_AUTO, PREF_REFRESH_AUTO);
	}
	
	@Override
	public TextAlignment getWordTextAlign() {
		return TextAlignment.valueOf(preferences.getString(NAME_PREF_TEXT_ALIGN, TextAlignment.JUSTIFY.name()));
	}
	
	@Override
	public TextFont getTextFont() {
		return TextFont.valueOf(preferences.getString(NAME_PREF_TEXT_FONT, TextFont.SANS_SERIF.name()));
	}
	
	@Override
	public Map<Integer, String> getHistory() {
		return loadWords(HISTORY_KEY);
	}
	
	@Override
	public void addToHistory(Integer id, String word) {
		addToWords(id, word, HISTORY_KEY);
	}
	
	@Override
	public Map<Integer, String> getBookmarks() {
		return loadWords(BOOKMARK_KEY);
	}
	
	@Override
	public void addBookmark(Integer id, String word) {
		addToWords(id, word, BOOKMARK_KEY);
	}
	
	@Override
	public void removeBookmark(Integer id) {
		removeFromWords(id);
	}
	
	@Override
	public boolean isBookmarked(Integer id) {
		return loadWords(BOOKMARK_KEY).containsKey(id);
	}

	private Map<Integer, String> loadWords(String prefKey) {
		Map<Integer, String> result = new LinkedHashMap<>();
		String wordsStr = preferences.getString(prefKey, "");
		String[] words = wordsStr.split(";");
		for (String token : words) {
			String[] pair = token.split(":");
			if (pair.length == 2) {
				result.put(Integer.parseInt(pair[0]), pair[1]);
			}
		}
		return result;
	}
	
	private void addToWords(Integer id, String word, String prefKey) {
		Map<Integer, String> words = loadWords(prefKey);
		if (words.size() >= MAX_WORDS_COUNT) {
			if  (words.keySet().iterator().hasNext()) {
				words.remove(words.keySet().iterator().next());
			}
		}
		words.remove(id);
		words.put(id, word);
		buildAndSaveWords(prefKey, words);
	}
	
	private void removeFromWords(Integer id) {
		Map<Integer, String> words = loadWords(PreferencesServiceImpl.BOOKMARK_KEY);
		words.remove(id);
		buildAndSaveWords(PreferencesServiceImpl.BOOKMARK_KEY, words);
	}

	private void buildAndSaveWords(String prefKey, Map<Integer, String> words) {
		StringBuilder builder = new StringBuilder();
		for (Integer key : words.keySet()) {
			builder.append(key);
			builder.append(":");
			builder.append(words.get(key));
			builder.append(";");
		}
		preferences.edit().putString(prefKey, builder.toString()).apply();
	}

	@Override
	public void openWordEvent(int id) {
		addEvent(STAT_OPEN_WORD_KEY, id);
	}

	@Override
	public void searchWordEvent(String begin) {
		addEvent(STAT_SEARCH_KEY, begin);
	}

	@Override
	public void fullSearchWordEvent(String query) {
		addEvent(STAT_FULL_SEARCH_KEY, query);
	}

	@Override
	public void openWordWidgetEvent(int id) {
		addEvent(STAT_OPEN_WORD_WIDGET_KEY, id);
	}

	@Override
	public void bookmarkWordEvent(int id) {
		addEvent(STAT_BOOKMARK_KEY, id);
	}
	
	private void addEvent(String prefKey, Object id) {
		String str = preferences.getString(prefKey, "");
		if (str.length() != 0) {
			str += ";";
		}
		str += id;
		preferences.edit().putString(prefKey, str).apply();
	}
	
	@Override
	public String[] getEventSearch() {
		String str = preferences.getString(STAT_SEARCH_KEY, null);
		return parseSearches(str);
	}
	
	@Override
	public String[] getEventFullSearch() {
		String str = preferences.getString(STAT_FULL_SEARCH_KEY, null);
		return parseSearches(str);
	}
	
	@Override
	public Integer[] getEventOpenWord() {
		String str = preferences.getString(STAT_OPEN_WORD_KEY, null);
		return parseIds(str);
	}
	
	@Override
	public Integer[] getEventOpenWordWidget() {
		String str = preferences.getString(STAT_OPEN_WORD_WIDGET_KEY, null);
		return parseIds(str);
	}
	
	@Override
	public Integer[] getEventBookmark() {
		String str = preferences.getString(STAT_BOOKMARK_KEY, null);
		return parseIds(str);
	}
	
	private String[] parseSearches(String str) {
		if (str == null) return null;
		return str.split(";");
	}

	private Integer[] parseIds(String str) {
		if (str == null) return null;
		String[] idStrs = str.split(";");
		Integer[] ids = new Integer[idStrs.length];
		for (int q = 0; q < ids.length; q ++) {
			try {
				ids[q] = Integer.parseInt(idStrs[q]);
			}
			catch (Exception e) {
				Log.e("daldic", "upload event error", e);
			}
		}
		return ids;
	}
	
	@Override
	public void clearEventSearch() {
		preferences.edit().remove(STAT_SEARCH_KEY).apply();
	}
	
	@Override
	public void clearEventFullSearch() {
		preferences.edit().remove(STAT_FULL_SEARCH_KEY).apply();
	}
	
	@Override
	public void clearEventOpenWord() {
		preferences.edit().remove(STAT_OPEN_WORD_KEY).apply();
	}
	
	@Override
	public void clearEventOpenWordWidget() {
		preferences.edit().remove(STAT_OPEN_WORD_WIDGET_KEY).apply();
	}
	
	@Override
	public void clearEventBookmark() {
		preferences.edit().remove(STAT_BOOKMARK_KEY).apply();
	}

	@Override
	public void clearHistory() {
		preferences.edit().remove(HISTORY_KEY).apply();
	}

	@Override
	public void clearBookmarks() {
		preferences.edit().remove(BOOKMARK_KEY).apply();
	}

}
