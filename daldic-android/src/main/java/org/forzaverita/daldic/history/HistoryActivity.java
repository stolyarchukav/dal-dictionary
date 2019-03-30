package org.forzaverita.daldic.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.forzaverita.daldic.R;

import java.util.Map;

public class HistoryActivity extends AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.history);
	}

	@Override
	protected Map<Integer, String> getResultList() {
		Map<Integer, String> history = getService().getHistory();
		configureClearButton(history.size());
		return history;
	}

	private void configureClearButton(int size) {
    	if (size > 0) {
			Button clearAll = findViewById(R.id.clear_all);
			clearAll.setVisibility(View.VISIBLE);
			clearAll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					new AlertDialog.Builder(HistoryActivity.this)
							.setMessage(R.string.are_you_sure)
							.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									getService().clearHistory();
									finish();
									startActivity(getIntent());
								}
							})
							.setNegativeButton(R.string.no, null)
							.show();

				}
			});
		}
	}
	
	@Override
	protected String getEmptyText() {
		return getString(R.string.history_empty);
	}
	
}
