package org.forzaverita.verbita.preference;

import static org.forzaverita.verbita.data.Constants.RATE_DEFAULT;
import android.content.Context;
import android.util.AttributeSet;

public class PitchPreference extends AbstractSliderPreference {
	
	public PitchPreference(Context context) {
		super(context);
	}

	public PitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PitchPreference(Context context, AttributeSet attrs, int defStyle) {
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
