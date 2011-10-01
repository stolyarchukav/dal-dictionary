package org.forzaverita.daldic.service;

import java.util.Map;

import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.widget.WidgetRefreshTask;

import android.graphics.Typeface;

public interface DalDicService {

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
	
}
