package org.forzaverita.daldic.preferences;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.service.DalDicService;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private DalDicService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTitle(R.string.pref);
		addPreferencesFromResource(R.xml.preferences);
		service =  (DalDicService) getApplicationContext();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
		service.preferencesChanged();
	}

}
