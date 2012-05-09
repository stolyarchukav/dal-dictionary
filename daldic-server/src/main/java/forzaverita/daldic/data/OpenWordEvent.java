package forzaverita.daldic.data;

public class OpenWordEvent extends BaseEvent {

	private Integer wordId;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [wordId=" + wordId + ", super: " + super.toString() + "]";
	}

	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}
	
}
