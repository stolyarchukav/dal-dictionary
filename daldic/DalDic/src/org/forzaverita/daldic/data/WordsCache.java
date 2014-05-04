package org.forzaverita.daldic.data;

public class WordsCache {

	private static final int SIZE = 20;
	
	private Word[] cache = new Word[SIZE];
	
	private int index = 0;
	
	public Word current() {
		return cache[index];
	}
	
	public Word next() {
		Word word = cache[getNextIndex()];
		if (word != null) {
			index = getNextIndex();
		}
		return word;
	}
	
	public Word previuos() {
		Word word = cache[getPreviousIndex()];
		if (word != null) {
			index = getPreviousIndex();
		}
		return word;
	}
	
	public void addToBegin(Word word) {
		index = getPreviousIndex();
		cache[index] = word;
		cache[getPreviousIndex()] = null;
	}
	
	public void addToEnd(Word word) {
		index = getNextIndex();
		cache[index] = word;
		cache[getNextIndex()] = null;
	}
	
	private int getNextIndex() {
		return index == SIZE - 1 ? 0 : index + 1;
	}
	
	private int getPreviousIndex() {
		return index == 0 ? SIZE - 1 : index - 1;
	}
	
}
