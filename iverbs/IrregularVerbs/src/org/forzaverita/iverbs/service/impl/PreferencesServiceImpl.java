package org.forzaverita.iverbs.service.impl;

import static org.forzaverita.iverbs.data.Constants.LOG_TAG;
import static org.forzaverita.iverbs.data.Constants.RATE_DEFAULT;
import static org.forzaverita.iverbs.data.Constants.RATE_SCALE;

import org.forzaverita.iverbs.data.TrainMode;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.service.PreferencesService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String LANG = "lang";
	private static final String VERB_STAT_PREFIX = "vs";
	private static final String CORRECT = "c";
	private static final String WRONG = "w";
	private static final String KEY_SPLITTER = "-";
	private static final String SPEECH_RATE = "pref_speak_speech_rate";
	private static final String PITCH = "pref_speak_pitch";
	
	private SharedPreferences preferences;
	
	public PreferencesServiceImpl(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public void addCorrect(int formQuest, Verb verb, TrainMode select) {
		addStat(formQuest, verb, select, CORRECT);
	}

	@Override
	public void addWrong(int formQuest, Verb verb, TrainMode select) {
		addStat(formQuest, verb, select, WRONG);
	}
	
	@Override
	public String getLanguage() {
		return preferences.getString(LANG, null);
	}
	
	@Override
	public void setLanguage(String lang) {
		preferences.edit().putString(LANG, lang).commit();
	}
	
	@Override
	public float getSpeechRate() {
		return getRate(SPEECH_RATE);
	}
	
	@Override
	public float getPitch() {
		return getRate(PITCH);
	}
	
	private void addStat(int formQuest, Verb verb, TrainMode select, String postfix) {
		String key = VERB_STAT_PREFIX + KEY_SPLITTER + verb.getId() + KEY_SPLITTER + 
				select.ordinal() + KEY_SPLITTER + postfix;
		int value = preferences.getInt(key, 0);
		preferences.edit().putInt(key, ++value).commit();
		Log.d(LOG_TAG, formQuest + "_" + verb + "_" + select + "_" + postfix + "_" + value);
	}

	private float getRate(String key) {
		return preferences.getInt(key, (int) (RATE_DEFAULT * RATE_SCALE)) 
				* 1f / RATE_SCALE;
	}
	
}
