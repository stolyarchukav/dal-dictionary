package org.forzaverita.daldic.service;

import java.util.Map;

import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.preferences.TextFont;

public interface PreferencesService {

	boolean isInternalStorage();

	void switchPreferredStorage();

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

	void openWordEvent(int id);

	void searchWordEvent(String begin);

	void fullSearchWordEvent(String query);

	void openWordWidgetEvent(int id);

	void bookmarkWordEvent(int id);

	String[] getEventSearch();

	String[] getEventFullSearch();

	Integer[] getEventOpenWord();

	Integer[] getEventOpenWordWidget();

	Integer[] getEventBookmark();

	void clearEventSearch();
	
	void clearEventFullSearch();
	
	void clearEventOpenWord();

	void clearEventOpenWordWidget();

	void clearEventBookmark();

	void clearHistory();

	void clearBookmarks();

}
