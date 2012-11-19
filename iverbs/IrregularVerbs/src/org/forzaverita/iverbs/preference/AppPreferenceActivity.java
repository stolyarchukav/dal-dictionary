package org.forzaverita.iverbs.preference;

import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.service.AppService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

public class AppPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private AppService service;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
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
	
	public void selectLang(View view) {
		startActivity(new Intent(this, SelectLangDialog.class));
	}

}
