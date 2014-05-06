package org.forzaverita.daldic.history;

import android.os.Bundle;

import java.util.Map;

import org.forzaverita.daldic.R;

public class HistoryActivity extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.history);
    }

	@Override
	protected Map<Integer, String> getResultList() {
		return getService().getHistory();
	}
	
	@Override
	protected String getEmptyText() {
		return getString(R.string.history_empty);
	}
	
}
