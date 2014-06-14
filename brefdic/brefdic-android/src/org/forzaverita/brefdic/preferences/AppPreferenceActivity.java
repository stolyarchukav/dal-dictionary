package org.forzaverita.brefdic.preferences;

import org.forzaverita.brefdic.R;
import org.forzaverita.brefdic.service.AppService;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private AppService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
        setTitle(R.string.pref);
		service =  (AppService) getApplicationContext();
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
