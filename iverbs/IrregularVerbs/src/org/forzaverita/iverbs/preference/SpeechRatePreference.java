package org.forzaverita.iverbs.preference;

import static org.forzaverita.iverbs.data.Constants.RATE_DEFAULT;
import android.content.Context;
import android.util.AttributeSet;

public class SpeechRatePreference extends AbstractSliderPreference {
	
	public SpeechRatePreference(Context context) {
		super(context);
	}

	public SpeechRatePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpeechRatePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getMinimum() {
		return 1;
	}

	@Override
	protected int getMaximum() {
		return 30;
	}

	@Override
	protected int getDefault() {
		return (int) RATE_DEFAULT;
	}
	
}
