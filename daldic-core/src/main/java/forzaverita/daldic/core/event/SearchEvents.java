package forzaverita.daldic.core.event;

import java.util.Collection;
import java.util.Date;

public class SearchEvents extends BaseEvents {

	private Collection<String> searchStrings;
	
	public SearchEvents() {
	}
	
	public SearchEvents(Date eventDate, String deviceModel, String country, Collection<String> searchStrings) {
		super(eventDate, deviceModel, country);
		this.searchStrings = searchStrings;
	}
	
	@Override
	public String toString() {
		return "SearchEvent [searchStrings=" + searchStrings + ", super: " + super.toString() + "]";
	}

	public Collection<String> getSearchStrings() {
		return searchStrings;
	}
	
	public void setSearchStrings(Collection<String> searchStrings) {
		this.searchStrings = searchStrings;
	}
	
}
