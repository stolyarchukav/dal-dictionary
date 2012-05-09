package forzaverita.daldic.core.event;

import java.util.Collection;
import java.util.Date;

public class WordEvents extends BaseEvents {

	private Collection<Integer> wordIds;
	
	public WordEvents() {
	}
	
	public WordEvents(Date eventDate, String deviceModel, String country, Collection<Integer> wordIds) {
		super(eventDate, deviceModel, country);
		this.wordIds = wordIds;
	}

	@Override
	public String toString() {
		return "OpenWordEvent [wordIds=" + wordIds + ", super: " + super.toString() + "]";
	}

	public Collection<Integer> getWordIds() {
		return wordIds;
	}
	
	public void setWordIds(Collection<Integer> wordIds) {
		this.wordIds = wordIds;
	}
	
}
