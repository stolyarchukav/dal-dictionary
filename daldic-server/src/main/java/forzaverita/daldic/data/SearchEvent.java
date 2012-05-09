package forzaverita.daldic.data;

public class SearchEvent extends BaseEvent {

	private String searchString;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [searchString=" + searchString + ", super: " + super.toString() + "]";
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
}
