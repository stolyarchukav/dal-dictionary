package org.forzaverita.brefdic.history;

import android.os.Bundle;

import org.forzaverita.brefdic.R;

import java.util.Map;

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
