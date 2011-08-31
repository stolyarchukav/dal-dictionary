package org.forzaverita.daldic.service.impl;

import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String PREFERENCES_FILE = "DalDicPref";
	private static final String INTERNAL_STORAGE = "internalStorage";
	private static final String DATABASE_PATH = "databasePath";
	
	private SharedPreferences preferences;
	
	public PreferencesServiceImpl(Context context) {
		preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
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
	
}
