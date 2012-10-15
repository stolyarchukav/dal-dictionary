package forzaverita.daldic.data;

import java.util.Collection;

public class SearchEvent extends BaseEvent {

	private Collection<String> searchStrings;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [searchStrings=" + searchStrings + ", super: " + super.toString() + "]";
	}

	public Collection<String> getSearchStrings() {
		return searchStrings;
	}
	
	public void setSearchStrings(Collection<String> searchStrings) {
		this.searchStrings = searchStrings;
	}
	
}
