package org.forzaverita.brefdic.history;

import android.os.Bundle;

import java.util.Map;

import org.forzaverita.brefdic.R;

public class BookmarksActivity extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.bookmarks);
    }

	@Override
	protected void onResume() {
		super.onResume();
		if (getService().isBookmarksChanged()) {
			onCreate(null);
		}
	}
	
	@Override
	protected Map<Integer, String> getResultList() {
		return getService().getBookmarks();
	}
	
	@Override
	protected String getEmptyText() {
		return getString(R.string.bookmarks_empty);
	}
	
}
