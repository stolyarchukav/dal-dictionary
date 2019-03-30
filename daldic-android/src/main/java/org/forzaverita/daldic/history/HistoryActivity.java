package org.forzaverita.daldic.history;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Map;

import org.forzaverita.daldic.R;

public class HistoryActivity extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.history);
		Button clearAll = findViewById(R.id.clear_all);
		clearAll.setVisibility(View.VISIBLE);
		clearAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				getService().clearHistory();
			}
		});
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
