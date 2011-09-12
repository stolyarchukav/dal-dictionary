package org.forzaverita.daldic.service;

import java.util.Map;

public interface DatabaseService {

	Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters);

	Map<Integer, String> getWordsBeginWith(char letter, boolean capitalLetters);

	String getDescription(Integer id);

	String[] getWordAndDescriptionById(long id);

	int getWordsCount();

	void close();

	boolean isDatabaseReady();

	void open();

}
