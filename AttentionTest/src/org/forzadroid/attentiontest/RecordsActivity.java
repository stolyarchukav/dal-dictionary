package org.forzadroid.attentiontest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.forzadroid.attentiontest.advert.AdUtils;

import java.util.HashMap;
import java.util.Map;

import static org.forzadroid.attentiontest.Constants.DIGIT_KEY_PREFIX;
import static org.forzadroid.attentiontest.Constants.REVERSE_KEY;
import static org.forzadroid.attentiontest.Constants.VAR_FONT_COLOR_KEY;
import static org.forzadroid.attentiontest.Constants.VAR_FONT_SIZE_KEY;

public class RecordsActivity extends Activity {

	private AttentionTestApplication appState;
	private Map<Integer, TextView> textMap = new HashMap<Integer, TextView>();
	private boolean varSize;
	private boolean varColor;
    private boolean reverse;
	private ToggleButton varSizeBtn;
	private ToggleButton varColorBtn;
	private ToggleButton reverseBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records);
		
		appState = (AttentionTestApplication) getApplicationContext();

		varSizeBtn = (ToggleButton) findViewById(R.id.rec_var_size);
		varSizeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                varSize = isChecked;
                updateRecords();
            }
        });
		
		varColorBtn = (ToggleButton) findViewById(R.id.rec_var_color);
        varColorBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                varColor = isChecked;
                updateRecords();
            }
        });

        reverseBtn = (ToggleButton) findViewById(R.id.rec_reverse);
        reverseBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                reverse = isChecked;
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

        AdUtils.loadAd(this);
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
			String key = DIGIT_KEY_PREFIX + size;
			if (varColor) {
				key += VAR_FONT_COLOR_KEY;
			}
			if (varSize) {
				key += VAR_FONT_SIZE_KEY;
			}
            if (reverse) {
                key += REVERSE_KEY;
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
