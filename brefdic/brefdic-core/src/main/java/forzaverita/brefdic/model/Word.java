package forzaverita.brefdic.model;

import java.util.Set;

public class Word {

	private Integer id;
	
	private String word;
	
	private String description;
	
	private String firstLetter;
	
	private Set<Reference> references;
	
	private Set<Image> images;
	
	/* Constructors */
	
	public Word() {}
	
	public Word(int id, String word, String description) {
		this.id = id;
		this.word = word;
		this.description = description;
	}
	
	/* Methods */
	
	@Override
	public String toString() {
		return "Word [id=" + id + ", word=" + word + ", description="
				+ description + ", firstLetter=" + firstLetter
				+ ", references=" + references + ", images=" + images + "]";
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

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
}
