package org.forzaverita.daldic;

import java.util.Set;

import android.graphics.Typeface;

public interface DalDicService {

	Typeface getFont();

	Set<String> getWordsBeginWith(char letter);
	
	Set<String> getWordsBeginWith(String begin);

	Set<String> getDescriptions(String word);

}
