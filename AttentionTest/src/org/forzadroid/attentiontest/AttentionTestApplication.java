package org.forzadroid.attentiontest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Application;
import android.content.SharedPreferences;

public class AttentionTestApplication extends Application {

	private static final String DIGIT_KEY_PREFIX = "digit";
	private static final String PREFERENCES_FILE = "AttentionTest";
	private AtomicInteger next = new AtomicInteger(1);
	private Long startTime;
	private List<Integer> values;
	private Map<String, Long> records;
	private SharedPreferences preferences;
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
		records = new HashMap<String, Long>();
		for (int q = 3; q <= 6; q++) {
			String key = DIGIT_KEY_PREFIX + q;
			records.put(key, preferences.getLong(key, -1));
		}
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
		String key = DIGIT_KEY_PREFIX + size;
		Long record = records.get(key);
		if (time < record || record == -1) {
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
		result.append(getString(R.string.dig_square_milliseconds));
		return result.toString();
	}
	
}
