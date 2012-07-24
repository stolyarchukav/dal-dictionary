package forzaverita.brefdic.model.wrapper;

import java.util.Collection;
import forzaverita.brefdic.model.Word;

public class WordWrapper {

	private Collection<Word> words;
	
	public WordWrapper() {}
	
	public WordWrapper(Collection<Word> words) {
		this.words = words;
	}
	
	public Collection<Word> getWords() {
		return words;
	}

	public void setWords(Collection<Word> words) {
		this.words = words;
	}
	
}
