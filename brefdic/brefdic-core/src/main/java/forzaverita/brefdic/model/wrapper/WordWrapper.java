package forzaverita.brefdic.model.wrapper;

import java.util.ArrayList;
import java.util.Collection;

import forzaverita.brefdic.model.Word;

public class WordWrapper {

	private Collection<Word> words = new ArrayList<Word>();
	
	public WordWrapper append(Collection<Word> words) {
		this.words.addAll(words);
		return this;
	}
	
	public Collection<Word> getWords() {
		return words;
	}

	public void setWords(Collection<Word> words) {
		this.words = words;
	}
	
}
