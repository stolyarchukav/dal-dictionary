package org.forzaverita.daldic;

import java.util.Map;
import java.util.Set;

import android.graphics.Typeface;

public interface DalDicService {

	Typeface getFont();

	Map<Integer, String> getWordsBeginWith(char letter);
	
	Map<Integer, String> getWordsBeginWith(String begin);

	Set<String> getDescriptions(Integer id);

	String getNewRandomWord();

	String[] getCurrentWord();
	
	void setWidgetRefreshTask(WidgetRefreshTask task);
	
	WidgetRefreshTask getWidgetRefreshTask();
	
}
