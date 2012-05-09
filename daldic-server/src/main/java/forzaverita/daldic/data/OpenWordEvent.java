package forzaverita.daldic.data;

import java.util.Collection;

public class OpenWordEvent extends BaseEvent {

	private Collection<Integer> wordIds;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [wordIds=" + wordIds + ", super: " + super.toString() + "]";
	}

	public Collection<Integer> getWordIds() {
		return wordIds;
	}
	
	public void setWordIds(Collection<Integer> wordIds) {
		this.wordIds = wordIds;
	}
	
}
