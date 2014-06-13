package org.forzaverita.brefdic.service;

import java.util.Collection;
import java.util.Map;

import forzaverita.brefdic.model.Word;

import android.database.Cursor;

public interface DatabaseService {

	Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters);

	Map<Integer, String> getWordsBeginWith(char letter, boolean capitalLetters);

	Map<Integer, String> getWordsFullSearch(String query, boolean capitalLetters);
	
	String[] getWordAndDescriptionById(long id);

	String getWordById(Integer wordId);
	
	void close();

	boolean isDatabaseReady();

	void open();
	
	Cursor getCursorOfWordsBeginWith(String begin);

	Map<Integer, String> storeWords(Collection<Word> words,	boolean capitalLetters);
	
	Map<Integer, String> storeWords(Collection<Word> words, String query, boolean capitalLetters);	

}
