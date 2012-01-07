package org.forzaverita.brefdic.service;

import java.util.Map;

import org.forzaverita.brefdic.preferences.TextAlignment;
import org.forzaverita.brefdic.widget.WidgetRefreshTask;

import android.graphics.Typeface;

public interface AppService {

	Typeface getFont();

	Map<Integer, String> getWordsBeginWith(char letter);
	
	Map<Integer, String> getWordsBeginWith(String begin);
	
	Map<Integer, String> getWordsFullSearch(String query);

	String[] getDescription(Integer id);

	String getNextWord();

	String getPreviuosWord();
	
	String[] getCurrentWord();
	
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
	
}
