package org.forzadroid.attentiontest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Application;

public class AttentionTestApplication extends Application {

	private AtomicInteger next = new AtomicInteger(1);
	private Long startTime;
	private List<Integer> values;
	
	@Override
	public void onCreate() {
		super.onCreate();
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

	public Long getStartTime() {
		if (startTime == null) {
			startTime = System.currentTimeMillis();
		}
		return startTime;
	}
	
}
