package org.forzaverita.daldic.service;

import java.util.Map;
import java.util.Set;

public interface DatabaseService {

	Map<Integer, String> getWordsBeginWith(String begin);

	Map<Integer, String> getWordsBeginWith(char letter);

	Set<String> getDescriptions(Integer id);

	String[] getWordAndDescriptionById(long id);

	int getWordsCount();

	void close();

	boolean isDatabaseReady();

	void open();

}
