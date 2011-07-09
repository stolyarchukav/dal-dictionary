package org.forzadroid.attentiontest;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class RecordsActivity extends Activity {

	private AttentionTestApplication appState;
	private Map<Integer, TextView> textMap = new HashMap<Integer, TextView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records);
		
		appState = (AttentionTestApplication) getApplicationContext();

		createRecordText((TableRow) findViewById(R.id.rec3), 3);
		createRecordText((TableRow) findViewById(R.id.rec4), 4);
		createRecordText((TableRow) findViewById(R.id.rec5), 5);
		createRecordText((TableRow) findViewById(R.id.rec6), 6);
		
		Button recordsClear = (Button) findViewById(R.id.recordsClear);
		recordsClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				appState.clearRecords();
				updateRecords();
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateRecords();
	}

	private void createRecordText(TableRow row, int size) {
		TextView text = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.topMargin = 10;
		params.leftMargin = 20;
		text.setLayoutParams(params);
		text.setTextSize(20);
		row.addView(text);
		textMap.put(size, text);
	}

	@SuppressWarnings("static-access")
	private void updateRecords() {
		Map<String, Long> records = appState.getRecords();
		for (Integer size : textMap.keySet()) {
			Long time = records.get(appState.DIGIT_KEY_PREFIX + size);
			String timeStr = String.valueOf(time / 1000.0) + " "+ getString(R.string.records_sec);
			if (time == -1) {
				timeStr = "-";
			}
			textMap.get(size).setText(timeStr);
		}
	}
	
}
