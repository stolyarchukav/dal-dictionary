package org.forzaverita.daldic.service.impl;

import static org.forzaverita.daldic.data.Constants.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.preferences.TextFont;
import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String INTERNAL_STORAGE = "internalStorage";
	private static final String DATABASE_PATH = "databasePath";
	private static final String HISTORY_KEY = "searchHistory";
	
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
		return loadHistory();
	}

	private Map<Integer, String> loadHistory() {
		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
		String historyStr = preferences.getString(HISTORY_KEY, "");
		String[] history = historyStr.split(";");
		for (String token : history) {
			String[] pair = token.split(":");
			if (pair.length == 2) {
				result.put(Integer.parseInt(pair[0]), pair[1]);
			}
		}
		return result;
	}
	
	@Override
	public void addToHistory(Integer id, String word) {
		Map<Integer, String> history = loadHistory();
		if (history.size() >= 30) {
			if  (history.keySet().iterator().hasNext()) {
				history.remove(history.keySet().iterator().next());
			}
		}
		history.remove(id);
		history.put(id, word);
		StringBuilder builder = new StringBuilder();
		for (Integer key : history.keySet()) {
			builder.append(key);
			builder.append(":");
			builder.append(history.get(key));
			builder.append(";");
		}
		preferences.edit().putString(HISTORY_KEY, builder.toString()).commit();
	}
	
}
