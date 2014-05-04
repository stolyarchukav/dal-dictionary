package org.forzaverita.daldic.history;

import java.util.Map;

import org.forzaverita.daldic.R;

public class HistoryActivity extends AbstractListActivity {

	@Override
	protected Map<Integer, String> getResultList() {
		return getService().getHistory();
	}
	
	@Override
	protected String getEmptyText() {
		return getString(R.string.history_empty);
	}
	
}
