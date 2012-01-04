package org.forzaverita.daldic.service;

import java.util.Map;

import org.forzaverita.daldic.preferences.TextAlignment;

public interface PreferencesService {

	boolean isInteranalStorage();

	void switchPreferencedStorage();

	String getDatabasePath();
	
	void setDatabasePath(String databasePath);

	boolean isCapitalLetters();

	boolean isAutoRefresh();

	int getRefreshInterval();

	TextAlignment getWordTextAlign();

	Map<Integer, String> getHistory();

	void addToHistory(Integer id, String word);

}
