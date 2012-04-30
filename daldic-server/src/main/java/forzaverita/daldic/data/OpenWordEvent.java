package forzaverita.daldic.data;

public class OpenWordEvent extends BaseEvent {

	private Long wordId;
	
	@Override
	public String toString() {
		return "OpenWordEvent [wordId=" + wordId + ", super: " + super.toString() + "]";
	}

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}
	
}
