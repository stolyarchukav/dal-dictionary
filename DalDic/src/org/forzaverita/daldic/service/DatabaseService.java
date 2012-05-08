package org.forzaverita.daldic.service;

import java.util.Map;

import android.database.Cursor;

public interface DatabaseService {

	Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters);

	Map<Integer, String> getWordsBeginWith(char letter, boolean capitalLetters);

	Map<Integer, String> getWordsFullSearch(String query, boolean capitalLetters);
	
	String[] getWordAndDescriptionById(long id);

	String getWordById(Integer wordId);
	
	int getWordsCount();

	void close();

	boolean isDatabaseReady();

	void open();

	Cursor getCursorOfWordsBeginWith(String begin);

}
