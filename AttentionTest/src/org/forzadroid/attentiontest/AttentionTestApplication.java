package org.forzadroid.attentiontest;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.forzadroid.attentiontest.Constants.DIGIT_KEY_PREFIX;
import static org.forzadroid.attentiontest.Constants.REVERSE_KEY;
import static org.forzadroid.attentiontest.Constants.VAR_FONT_COLOR_KEY;
import static org.forzadroid.attentiontest.Constants.VAR_FONT_SIZE_KEY;

public class AttentionTestApplication extends Application {

	private static final long DEFAULT_RECORD = -1L;
	private static final String PREFERENCES_FILE = "AttentionTest";
	private AtomicInteger next = new AtomicInteger(1);
	private Long startTime;
	private List<Integer> values;
	private Map<String, Long> records;
	private SharedPreferences preferences;
	private boolean varFontSize;
	private boolean varFontColor;
	private Random random = new Random();
    private boolean reverse;

    @Override
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
		records = new HashMap<String, Long>();
		for (int q = 3; q <= 6; q++) {
			String key = DIGIT_KEY_PREFIX + q;
			loadRecordItem(key);
            loadRecordItem(key + VAR_FONT_SIZE_KEY);
            loadRecordItem(key + VAR_FONT_COLOR_KEY);
            loadRecordItem(key + REVERSE_KEY);
            loadRecordItem(key + VAR_FONT_COLOR_KEY + VAR_FONT_SIZE_KEY);
            loadRecordItem(key + VAR_FONT_COLOR_KEY + REVERSE_KEY);
            loadRecordItem(key + VAR_FONT_SIZE_KEY + REVERSE_KEY);
            loadRecordItem(key + VAR_FONT_COLOR_KEY + VAR_FONT_SIZE_KEY + REVERSE_KEY);
		}
		varFontSize = preferences.getBoolean(VAR_FONT_SIZE_KEY, false);
		varFontColor = preferences.getBoolean(VAR_FONT_COLOR_KEY, false);
        reverse = preferences.getBoolean(REVERSE_KEY, false);
	}

    private void loadRecordItem(String key) {
        records.put(key, preferences.getLong(key, DEFAULT_RECORD));
    }

    public void clearDigSequence() {
		startTime = null;
		values = null;
        next = null;
	}

	public List<Integer> getValues(int size) {
		if (values == null) {
			values = new ArrayList<Integer>();
		    for (int q = 1; q <= size * size; q++) {
		    	values.add(q);
		    }
		    Collections.shuffle(values);
		}
        if (next == null) {
            next = new AtomicInteger(reverse ? size * size : 1);
        }
	    return values;
	}

	public AtomicInteger getNext() {
		return next;
	}

	public Map<String, Long> getRecords() {
		return records;
	}
	
	public void startDigitTest() {
		startTime = System.currentTimeMillis();
	}
	
	public String finishDigitTest(int size) {
		StringBuilder result = new StringBuilder();
		Long time = System.currentTimeMillis() - startTime;
		String key = DIGIT_KEY_PREFIX + size;
		if (varFontColor) {
			key += VAR_FONT_COLOR_KEY;
		}
		if (varFontSize) {
			 key += VAR_FONT_SIZE_KEY;
		}
        if (reverse) {
            key += REVERSE_KEY;
        }
		Long record = records.get(key);
		if (time < record || record == DEFAULT_RECORD) {
			records.put(key, time);
			preferences.edit().putLong(key, time).commit();
			result.append(getString(R.string.dig_square_new_record));
		}
		else {
			result.append(getString(R.string.dig_square_without_record));
		}
		result.append(" ");
		result.append(time / 1000.0);
		result.append(" ");
		result.append(getString(R.string.dig_square_seconds));
		return result.toString();
	}
	
	public void clearRecords() {
		Editor editor = preferences.edit();
		for (int q = 3; q <= 6; q++) {
            String key = DIGIT_KEY_PREFIX + q;
            clearRecordItem(editor, key);
            clearRecordItem(editor, key + VAR_FONT_SIZE_KEY);
            clearRecordItem(editor, key + VAR_FONT_COLOR_KEY);
            clearRecordItem(editor, key + REVERSE_KEY);
            clearRecordItem(editor, key + VAR_FONT_COLOR_KEY + VAR_FONT_SIZE_KEY);
            clearRecordItem(editor, key + VAR_FONT_COLOR_KEY + REVERSE_KEY);
            clearRecordItem(editor, key + VAR_FONT_SIZE_KEY + REVERSE_KEY);
            clearRecordItem(editor, key + VAR_FONT_COLOR_KEY + VAR_FONT_SIZE_KEY + REVERSE_KEY);
		}
		editor.commit();
	}

    private void clearRecordItem(Editor editor, String key) {
        records.put(key, DEFAULT_RECORD);
        editor.putLong(key, DEFAULT_RECORD);
    }

    public void setVarFontSize(boolean varFontSize) {
		this.varFontSize = varFontSize;
		preferences.edit().putBoolean(VAR_FONT_SIZE_KEY, varFontSize);
	}
	
	public void setVarFontColor(boolean varFontColor) {
		this.varFontColor = varFontColor;
		preferences.edit().putBoolean(VAR_FONT_COLOR_KEY, varFontColor);
	}

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        preferences.edit().putBoolean(REVERSE_KEY, reverse);
    }

	public boolean isVarFontSize() {
		return varFontSize;
	}
	
	public boolean isVarFontColor() {
		return varFontColor;
	}

    public boolean isReverse() {
        return reverse;
    }

	public float getFontSize(int size) {
		float dig = 2;
		if (varFontSize) {
			dig = random.nextFloat() * 1.5f + 1.7f;
		}
		return 250 / (size * dig);
	}

	public int getFontColor() {
		int color = Color.BLACK;
		if (varFontColor) {
			color = Color.rgb(random.nextInt(255), random.nextInt(200), random.nextInt(200));
		}
		return color; 		
	}
	
	public String getDigitalSquareTitle() {
        StringBuilder text = new StringBuilder(getString(reverse ?
                R.string.dig_square_title_reverse : R.string.dig_square_title));
		text.append(" [ ");
		text.append(next.get());
		text.append(" ]");
		if (startTime != null) {
			long time = System.currentTimeMillis() - startTime;
			text.append(". Time ");
			text.append(time / 100 / 10.0);
		}
		return text.toString();
	}

}
