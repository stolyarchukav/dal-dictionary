package org.forzadroid.attentiontest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AttentionTestApplication extends Application {

	private static final long DEFAULT_RECORD = -1L;
	public static final String DIGIT_KEY_PREFIX = "digit";
	public static final String VAR_FONT_SIZE_KEY = "varFontSize";
	private static final String PREFERENCES_FILE = "AttentionTest";
	private AtomicInteger next = new AtomicInteger(1);
	private Long startTime;
	private List<Integer> values;
	private Map<String, Long> records;
	private SharedPreferences preferences;
	private boolean varFontSize;
	private Random random = new Random();
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
		records = new HashMap<String, Long>();
		for (int q = 3; q <= 6; q++) {
			String key = DIGIT_KEY_PREFIX + q;
			records.put(key, preferences.getLong(key, DEFAULT_RECORD));
			key += VAR_FONT_SIZE_KEY;
			records.put(key, preferences.getLong(key, DEFAULT_RECORD));
		}
		varFontSize = preferences.getBoolean(VAR_FONT_SIZE_KEY, false);
	}
	
	public void clearDigSequence() {
		next = new AtomicInteger(1);
		startTime = null;
		values = null;
	}

	public List<Integer> getValues(int size) {
		if (values == null) {
			values = new ArrayList<Integer>();
		    for (int q = 1; q <= size * size; q++) {
		    	values.add(q);
		    }
		    Collections.shuffle(values);
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
		String key = DIGIT_KEY_PREFIX + size + (varFontSize ? VAR_FONT_SIZE_KEY : "");
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
			records.put(key, DEFAULT_RECORD);
			editor.putLong(key, DEFAULT_RECORD);
			key += VAR_FONT_SIZE_KEY;
			records.put(key, DEFAULT_RECORD);
			editor.putLong(key, DEFAULT_RECORD);
		}
		editor.commit();
	}
	
	public void setVarFontSize(boolean varFontSize) {
		this.varFontSize = varFontSize;
		preferences.edit().putBoolean(VAR_FONT_SIZE_KEY, varFontSize);
	}
	
	public boolean isVarFontSize() {
		return varFontSize;
	}
	
	public float getFontSize(int size) {
		float dig = 2;
		if (varFontSize) {
			dig = random.nextFloat() * 3 + 3;
		}
		return 250 / (size * dig);
	}
	
}
