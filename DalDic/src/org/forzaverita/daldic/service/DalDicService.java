package org.forzaverita.daldic.service;

import java.util.Map;
import java.util.Set;

import org.forzaverita.daldic.widget.WidgetRefreshTask;

import android.graphics.Typeface;

public interface DalDicService {

	Typeface getFont();

	Map<Integer, String> getWordsBeginWith(char letter);
	
	Map<Integer, String> getWordsBeginWith(String begin);

	Set<String> getDescriptions(Integer id);

	String getNextWord();

	String getPreviuosWord();
	
	String[] getCurrentWord();
	
	void setWidgetRefreshTask(WidgetRefreshTask task);
	
	WidgetRefreshTask getWidgetRefreshTask();

	long getRefreshInterval();
	
}
