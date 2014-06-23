package org.forzaverita.brefdic.service;

import java.util.Map;

import org.forzaverita.brefdic.preferences.TextAlignment;
import org.forzaverita.brefdic.preferences.TextFont;

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

	TextFont getTextFont();

	Map<Integer, String> getBookmarks();

	void addBookmark(Integer id, String word);

	void removeBookmark(Integer id);

	boolean isBookmarked(Integer id);

}
