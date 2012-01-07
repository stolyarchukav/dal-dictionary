package org.forzaverita.brefdic.data;


public class WordsCache {

	private static final int SIZE = 20;
	
	private String[][] cache = new String[SIZE][];
	
	private int index = 0;
	
	public String[] current() {
		return cache[index];
	}
	
	public String[] next() {
		String[] result = cache[getNextIndex()];
		if (result != null) {
			index = getNextIndex();
		}
		return result;
	}
	
	public String[] previuos() {
		String[] result = cache[getPreviousIndex()];
		if (result != null) {
			index = getPreviousIndex();
		}
		return result;
	}
	
	public void addToBegin(String[] word) {
		index = getPreviousIndex();
		cache[index] = word;
		cache[getPreviousIndex()] = null;
	}
	
	public void addToEnd(String[] word) {
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
