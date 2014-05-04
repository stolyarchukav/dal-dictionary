package org.forzaverita.daldic.data;

public class Word {
	
	private int id;
	
	private String word;
	
	private String description;
	
	private String descriptionRef;
	
	public Word(int id, String word, String description, String descriptionRef) {
		this.id = id;
		this.word = word;
		this.description = description;
		this.descriptionRef = descriptionRef;
	}

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

	public String getDescriptionRef() {
		return descriptionRef;
	}

	public void setDescriptionRef(String descriptionRef) {
		this.descriptionRef = descriptionRef;
	}

}
