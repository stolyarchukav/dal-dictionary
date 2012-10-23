package org.forzaverita.iverbs.preference;

import org.forzaverita.iverbs.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectLangPreference extends Preference {

	public SelectLangPreference(Context context) {
		super(context);
	}

	public SelectLangPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectLangPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public View onCreateView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) 
				getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.preference_select_lang, null);
		return view;
	}
	
}
