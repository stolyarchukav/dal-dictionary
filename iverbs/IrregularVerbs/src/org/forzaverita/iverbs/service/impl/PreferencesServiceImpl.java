package org.forzaverita.iverbs.service.impl;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Lang;
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
	public Lang getLanguage() {
		return Lang.valueOf(preferences.getString(LANG, Lang.RU.name()));
	}
	
	@Override
	public void setLanguage(Lang lang) {
		preferences.edit().putString(LANG, lang.name()).commit();
	}
	
	private void addStat(int formQuest, Verb verb, TrainMode select, String postfix) {
		String key = VERB_STAT_PREFIX + KEY_SPLITTER + verb.getId() + KEY_SPLITTER + 
				select.ordinal() + KEY_SPLITTER + postfix;
		int value = preferences.getInt(key, 0);
		preferences.edit().putInt(key, ++value).commit();
		Log.d(Constants.LOG_TAG, formQuest + "_" + verb + "_" + select + "_" + postfix + "_" + value);
	}
	
}
