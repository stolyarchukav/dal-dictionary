package forzaverita.brefdic.model;

public class Image {

	private Integer wordId;
	
	private Integer imageId;
	
	private Integer position;

	/* Methods */
	
	@Override
	public String toString() {
		return "Image [wordId=" + wordId + ", imageId=" + imageId
				+ ", position=" + position + "]";
	}
	
	/* Getters and Setters */
	
	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
