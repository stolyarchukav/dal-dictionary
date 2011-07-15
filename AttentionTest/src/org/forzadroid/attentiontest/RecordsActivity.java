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
	private boolean varColor;
	private Button stableBtn;
	private Button varSizeBtn;
	private Button varColorBtn;
	private Button varColorSizeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records);
		
		appState = (AttentionTestApplication) getApplicationContext();
		
		stableBtn = (Button) findViewById(R.id.rec_stable);
		stableBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = false;
				varColor = false;
				stableBtn.setEnabled(false);
				varSizeBtn.setEnabled(true);
				varColorBtn.setEnabled(true);
				varColorSizeBtn.setEnabled(true);
				updateRecords();
			}
		});
		
		varSizeBtn = (Button) findViewById(R.id.rec_var_size);
		varSizeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = true;
				varColor = false;
				stableBtn.setEnabled(true);
				varSizeBtn.setEnabled(false);
				varColorBtn.setEnabled(true);
				varColorSizeBtn.setEnabled(true);
				updateRecords();
			}
		});
		
		varColorBtn = (Button) findViewById(R.id.rec_var_color);
		varColorBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = false;
				varColor = true;
				stableBtn.setEnabled(true);
				varSizeBtn.setEnabled(true);
				varColorBtn.setEnabled(false);
				varColorSizeBtn.setEnabled(true);
				updateRecords();
			}
		});
		
		varColorSizeBtn = (Button) findViewById(R.id.rec_var_color_size);
		varColorSizeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				varSize = true;
				varColor = true;
				stableBtn.setEnabled(true);
				varSizeBtn.setEnabled(true);
				varColorBtn.setEnabled(true);
				varColorSizeBtn.setEnabled(false);
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
			String key = appState.DIGIT_KEY_PREFIX + size;
			if (varColor) {
				key += appState.VAR_FONT_COLOR_KEY;
			}
			if (varSize) {
				key += appState.VAR_FONT_SIZE_KEY;
			}
			Long time = records.get(key);
			String timeStr = String.valueOf(time / 1000.0) + " " + getString(R.string.records_sec);
			if (time == -1) {
				timeStr = "-";
			}
			textMap.get(size).setText(timeStr);
		}
	}
	
}
