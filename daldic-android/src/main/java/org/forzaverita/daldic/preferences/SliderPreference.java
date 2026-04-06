package org.forzaverita.daldic.preferences;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.data.Constants;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class SliderPreference extends Preference {

	public static final int MAXIMUM = 100;
	public static final int MINIMUM = 5;

	private int position = Constants.PREF_REFRESH_INTERVAL;
	
	public SliderPreference(Context context) {
		super(context);
		setLayoutResource(R.layout.preference_slider);
	}

	public SliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutResource(R.layout.preference_slider);
	}

	public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setLayoutResource(R.layout.preference_slider);
	}

	@Override
	public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
		super.onBindViewHolder(holder);

		TextView title = (TextView) holder.findViewById(R.id.pref_slider_title);
		if (title != null) title.setText(getTitle());
		
		TextView titleSummary = (TextView) holder.findViewById(R.id.pref_slider_title_summary);
		if (titleSummary != null) titleSummary.setText(getSummary());
		
		TextView positionText = (TextView) holder.findViewById(R.id.pref_slider_position);
		if (positionText != null) positionText.setText(String.valueOf(position));

		SeekBar seekBar = (SeekBar) holder.findViewById(R.id.pref_slider_seek);
		if (seekBar != null) {
			seekBar.setMax(getProgress(MAXIMUM));
			seekBar.setProgress(getProgress(position));
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					position = getPosition(progress);
					if (positionText != null) {
						positionText.setText(String.valueOf(position));
					}
					persistInt(position);
					notifyChanged();
				}
			});
		}
	}

	@Override
	protected void onSetInitialValue(Object defaultValue) {
		position = getPersistedInt(position);
	}

	private int getPosition(int progress) {
		return progress + MINIMUM;
	}
	
	private int getProgress(int position) {
		return position - MINIMUM;
	}
	
}
