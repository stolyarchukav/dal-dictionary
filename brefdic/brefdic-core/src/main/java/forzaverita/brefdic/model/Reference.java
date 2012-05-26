package forzaverita.brefdic.model;

public class Reference {

	private Integer wordId;
	
	private Integer refWordId;
	
	private Integer position;

	/* Getters and Setters */
	
	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	public Integer getRefWordId() {
		return refWordId;
	}

	public void setRefWordId(Integer refWordId) {
		this.refWordId = refWordId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
