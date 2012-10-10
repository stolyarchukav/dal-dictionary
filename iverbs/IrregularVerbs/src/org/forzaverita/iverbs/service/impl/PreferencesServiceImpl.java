package org.forzaverita.iverbs.service.impl;

import org.forzaverita.iverbs.data.TrainMode;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesServiceImpl implements PreferencesService {

	private SharedPreferences preferences;
	
	public PreferencesServiceImpl(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public void addCorrect(int formQuest, Verb verb, TrainMode select) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWrong(int formQuest, Verb verb, TrainMode select) {
		// TODO Auto-generated method stub
		
	}
	
}
