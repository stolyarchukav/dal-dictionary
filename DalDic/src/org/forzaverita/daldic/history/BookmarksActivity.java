package org.forzaverita.daldic.history;

import java.util.Map;

import org.forzaverita.daldic.R;

public class BookmarksActivity extends AbstractListActivity {

	@Override
	protected Map<Integer, String> getResultList() {
		return getService().getBookmarks();
	}
	
	@Override
	protected String getEmptyText() {
		return getString(R.string.bookmarks_empty);
	}
	
}
