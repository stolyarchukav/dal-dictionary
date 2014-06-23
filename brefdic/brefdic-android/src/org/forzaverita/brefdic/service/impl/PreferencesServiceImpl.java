package org.forzaverita.brefdic.service.impl;

import static org.forzaverita.brefdic.data.Constants.NAME_PREF_TEXT_ALIGN;
import static org.forzaverita.brefdic.data.Constants.NAME_PREF_TEXT_CAPITAL_LETTERS;
import static org.forzaverita.brefdic.data.Constants.NAME_PREF_TEXT_FONT;
import static org.forzaverita.brefdic.data.Constants.NAME_PREF_WIDGET_REFRESH_AUTO;
import static org.forzaverita.brefdic.data.Constants.NAME_PREF_WIDGET_REFRESH_INTERVAL;
import static org.forzaverita.brefdic.data.Constants.PREF_REFRESH_AUTO;
import static org.forzaverita.brefdic.data.Constants.PREF_REFRESH_INTERVAL;
import static org.forzaverita.brefdic.data.Constants.PREF_TEXT_CAPITAL_LETTERS;

import java.util.LinkedHashMap;
import java.util.Map;

import org.forzaverita.brefdic.preferences.TextAlignment;
import org.forzaverita.brefdic.preferences.TextFont;
import org.forzaverita.brefdic.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String INTERNAL_STORAGE = "internalStorage";
	private static final String DATABASE_PATH = "databasePath";
	private static final String HISTORY_KEY = "searchHistory";
	private static final String BOOKMARK_KEY = "bookmark";
	
	private SharedPreferences preferences;
	
	public PreferencesServiceImpl(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@Override
	public boolean isInteranalStorage() {
		return preferences.getBoolean(INTERNAL_STORAGE, false);
	}
	
	@Override
	public void switchPreferencedStorage() {
		preferences.edit().putBoolean(INTERNAL_STORAGE, ! isInteranalStorage()).commit();
	}
	
	@Override
	public String getDatabasePath() {
		return preferences.getString(DATABASE_PATH, null);
	}
	
	@Override
	public void setDatabasePath(String databasePath) {
		preferences.edit().putString(DATABASE_PATH, databasePath).commit();
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
		return TextFont.valueOf(preferences.getString(NAME_PREF_TEXT_FONT, TextFont.PHILOSOPHER.name()));
	}
	
	@Override
	public Map<Integer, String> getHistory() {
		return loadWords(HISTORY_KEY);
	}
	
	@Override
	public void addToHistory(Integer id, String word) {
		addToWords(id, word, HISTORY_KEY, 30);
	}
	
	@Override
	public Map<Integer, String> getBookmarks() {
		return loadWords(BOOKMARK_KEY);
	}
	
	@Override
	public void addBookmark(Integer id, String word) {
		addToWords(id, word, BOOKMARK_KEY, 30);
	}
	
	@Override
	public void removeBookmark(Integer id) {
		removeFromWords(id, BOOKMARK_KEY);
	}
	
	@Override
	public boolean isBookmarked(Integer id) {
		return loadWords(BOOKMARK_KEY).containsKey(id);
	}

	private Map<Integer, String> loadWords(String prefKey) {
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
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
	
	private void addToWords(Integer id, String word, String prefKey, int maxSize) {
		Map<Integer, String> words = loadWords(prefKey);
		if (words.size() >= maxSize) {
			if  (words.keySet().iterator().hasNext()) {
				words.remove(words.keySet().iterator().next());
			}
		}
		words.remove(id);
		words.put(id, word);
		buildAndSaveWords(prefKey, words);
	}
	
	private void removeFromWords(Integer id, String prefKey) {
		Map<Integer, String> words = loadWords(prefKey);
		words.remove(id);
		buildAndSaveWords(prefKey, words);
	}

	private void buildAndSaveWords(String prefKey, Map<Integer, String> words) {
		StringBuilder builder = new StringBuilder();
		for (Integer key : words.keySet()) {
			builder.append(key);
			builder.append(":");
			builder.append(words.get(key));
			builder.append(";");
		}
		preferences.edit().putString(prefKey, builder.toString()).commit();
	}
	
	
}
