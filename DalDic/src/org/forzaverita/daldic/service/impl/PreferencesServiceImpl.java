package org.forzaverita.daldic.service.impl;

import static org.forzaverita.daldic.data.Constants.NAME_PREF_TEXT_ALIGN;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_TEXT_CAPITAL_LETTERS;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_WIDGET_REFRESH_AUTO;
import static org.forzaverita.daldic.data.Constants.NAME_PREF_WIDGET_REFRESH_INTERVAL;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String INTERNAL_STORAGE = "internalStorage";
	private static final String DATABASE_PATH = "databasePath";
	
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
		return preferences.getBoolean(NAME_PREF_TEXT_CAPITAL_LETTERS, Constants.PREF_TEXT_CAPITAL_LETTERS);
	}
	
	@Override
	public int getRefreshInterval() {
		return preferences.getInt(NAME_PREF_WIDGET_REFRESH_INTERVAL, Constants.PREF_REFRESH_INTERVAL);
	}
	
	@Override
	public boolean isAutoRefresh() {
		return preferences.getBoolean(NAME_PREF_WIDGET_REFRESH_AUTO, Constants.PREF_REFRESH_AUTO);
	}
	
	@Override
	public TextAlignment getWordTextAlign() {
		return TextAlignment.valueOf(preferences.getString(NAME_PREF_TEXT_ALIGN, TextAlignment.JUSTIFY.name()));
	}
	
}
