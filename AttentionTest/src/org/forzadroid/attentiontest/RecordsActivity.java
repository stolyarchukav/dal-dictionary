package org.forzadroid.attentiontest;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class RecordsActivity extends Activity {

	private AttentionTestApplication appState;
	private Map<Integer, TextView> textMap = new HashMap<Integer, TextView>();
	private boolean varSize;
	private Button varSizeBtn;
	private Button stableSizeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records);
		
		appState = (AttentionTestApplication) getApplicationContext();
		
		varSizeBtn = (Button) findViewById(R.id.rec_var_size);
		varSizeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = true;
				varSizeBtn.setEnabled(false);
				stableSizeBtn.setEnabled(true);
				updateRecords();
			}
		});
		
		stableSizeBtn = (Button) findViewById(R.id.rec_stable_size);
		stableSizeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = false;
				varSizeBtn.setEnabled(true);
				stableSizeBtn.setEnabled(false);
				updateRecords();
			}
		});

		createRecordText((TableRow) findViewById(R.id.rec3), 3, true);
		createRecordText((TableRow) findViewById(R.id.rec4), 4, false);
		createRecordText((TableRow) findViewById(R.id.rec5), 5, true);
		createRecordText((TableRow) findViewById(R.id.rec6), 6, false);
		
		Button recordsClear = (Button) findViewById(R.id.recordsClear);
		recordsClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				appState.clearRecords();
				updateRecords();
			}
		});
		
		//Advertising
        AdView adView = new AdView(this, AdSize.BANNER, Constants.AD_MOB_ID);
        LinearLayout layout = (LinearLayout)findViewById(R.id.records);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        adView.loadAd(adRequest);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateRecords();
	}

	private void createRecordText(TableRow row, int size, boolean odd) {
		TextView text = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.topMargin = 10;
		params.leftMargin = 20;
		text.setLayoutParams(params);
		text.setTextSize(20);
		if (odd) {
			text.setTextColor(getResources().getColor(R.color.table_row_odd));
		}
		row.addView(text);
		textMap.put(size, text);
	}

	@SuppressWarnings("static-access")
	private void updateRecords() {
		Map<String, Long> records = appState.getRecords();
		for (Integer size : textMap.keySet()) {
			Long time = records.get(appState.DIGIT_KEY_PREFIX + size + (varSize ? appState.VAR_FONT_SIZE_KEY : ""));
			String timeStr = String.valueOf(time / 1000.0) + " "+ getString(R.string.records_sec);
			if (time == -1) {
				timeStr = "-";
			}
			textMap.get(size).setText(timeStr);
		}
	}
	
}
