package org.forzaverita.iverbs.preference;

import org.forzaverita.iverbs.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public abstract class AbstractSliderPreference extends Preference {
	
	private int position = getDefault();
	
	private TextView positionText;
	private SeekBar seekBar;
	
	public AbstractSliderPreference(Context context) {
		super(context);
	}

	public AbstractSliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AbstractSliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected abstract int getMinimum();
	
	protected abstract int getMaximum();
	
	protected abstract int getDefault();
	
	@Override
	public View onCreateView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) 
			getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.preference_slider, null);
		
		TextView title = (TextView) view.findViewById(R.id.pref_slider_title);
		title.setText(getTitle());
		
		TextView titleSummary = (TextView) view.findViewById(R.id.pref_slider_title_summary);
		titleSummary.setText(getSummary());
		
		seekBar = (SeekBar) view.findViewById(R.id.pref_slider_seek);
		seekBar.setMax(getProgress(getMaximum()));
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
				positionText.setText(String.valueOf(position));
				persistInt(position);
				notifyChanged();
			}
		});
		
		positionText = (TextView) view.findViewById(R.id.pref_slider_position);
		positionText.setText(String.valueOf(position));
		
		return view;
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		position = getPersistedInt(position);
	}

	private int getPosition(int progress) {
		return progress + getMinimum();
	}
	
	private int getProgress(int position) {
		return position - getMinimum();
	}
	
}
