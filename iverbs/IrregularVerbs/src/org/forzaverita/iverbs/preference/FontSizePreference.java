package org.forzaverita.iverbs.preference;

import static org.forzaverita.iverbs.data.Constants.FONT_SIZE_DEFAULT;
import android.content.Context;
import android.util.AttributeSet;

public class FontSizePreference extends AbstractSliderPreference {
	
	public FontSizePreference(Context context) {
		super(context);
	}

	public FontSizePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FontSizePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getMinimum() {
		return 8;
	}

	@Override
	protected int getMaximum() {
		return 30;
	}

	@Override
	protected int getDefault() {
		return (int) FONT_SIZE_DEFAULT;
	}
	
}
