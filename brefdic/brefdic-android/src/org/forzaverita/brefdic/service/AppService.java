package org.forzaverita.brefdic.service;

import java.util.Date;
import java.util.Map;

import org.forzaverita.brefdic.preferences.TextAlignment;
import org.forzaverita.brefdic.preferences.TextFont;
import org.forzaverita.brefdic.widget.WidgetRefreshTask;

import android.database.Cursor;
import android.graphics.Typeface;
import forzaverita.brefdic.model.Word;

public interface AppService {

	Typeface getFont();

	Map<Integer, String> getWordsBeginWith(char letter);
	
	Map<Integer, String> getWordsBeginWith(String begin);
	
	Map<Integer, String> getWordsFullSearch(String query);

	Word getWord(Integer id);

	String getNextWord();

	String getPreviuosWord();
	
	Word getCurrentWord();
	
	void setWidgetRefreshTask(WidgetRefreshTask task);
	
	WidgetRefreshTask getWidgetRefreshTask();

	boolean isAutoRefresh();
	
	int getRefreshInterval();

	boolean isDatabaseReady();

	void openDatabase();

	TextAlignment getWordTextAlign();

	Map<Integer, String> getHistory();

	void addToHistory(Integer id, String word);

	String getWordById(Integer wordId);
	
	Cursor getCursorOfWordsBeginWith(String string);

	TextFont resolveTypeface(Typeface typeface);

	boolean isPreferencesChanged(Date lastPreferencesCheck);

	void preferencesChanged();

	Map<Integer, String> getBookmarks();
	
	void addBookmark(Integer id, String word);

	void removeBookmark(Integer id);

	boolean isBookmarked(Integer wordId);

	boolean isBookmarksChanged();
	
}
