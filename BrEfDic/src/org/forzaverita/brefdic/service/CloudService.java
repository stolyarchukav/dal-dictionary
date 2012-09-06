package org.forzaverita.brefdic.service;

import java.util.Collection;

import forzaverita.brefdic.model.Word;

public interface CloudService {

	Collection<Word> getWordsBeginWith(String begin);

	Collection<Word> getWordsFullSearch(String query);

	Word getWord(Integer id);

}
