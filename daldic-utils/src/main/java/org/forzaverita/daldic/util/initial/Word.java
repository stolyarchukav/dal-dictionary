package org.forzaverita.daldic.util.initial;

public class Word {

	private int id;
	
	private String word;
	
	private String description;
	
	private String firstLetter;
	
	private Integer wordReference;
	
	/* Methods */
	
	@Override
	public String toString() {
		return "Word [id=" + id + ", word=" + word + ", description="
				+ description + ", firstLetter=" + firstLetter
				+ ", wordReference=" + wordReference + "]";
	}

	/* Getters and Setters */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public void setWordReference(Integer wordReference) {
		this.wordReference = wordReference;
	}

	public Integer getWordReference() {
		return wordReference;
	}
	
}
