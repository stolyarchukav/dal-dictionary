package org.forzaverita.iverbs.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.forzaverita.iverbs.data.StatItem;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.service.PreferencesService;
import org.forzaverita.iverbs.train.TrainMode;

import static org.forzaverita.iverbs.data.Constants.FONT_SIZE_DEFAULT;
import static org.forzaverita.iverbs.data.Constants.LOG_TAG;
import static org.forzaverita.iverbs.data.Constants.RATE_DEFAULT;
import static org.forzaverita.iverbs.data.Constants.RATE_SCALE;

public class PreferencesServiceImpl implements PreferencesService {

	private static final String LANG = "lang";
	private static final String VERB_STAT_PREFIX = "vs";
	private static final String CORRECT = "c";
	private static final String WRONG = "w";
	private static final String KEY_SPLITTER = "-";
	private static final String SPEECH_RATE = "pref_speak_speech_rate";
	private static final String PITCH = "pref_speak_pitch";
	private static final String FONT_SIZE = "pref_font_size";
	private static final String IN_TRAINING = "in_training";
	
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
	
	@Override
	public StatItem getStat(Verb verb) {
		StatItem statItem = new StatItem();
		statItem.setVerb(verb);
		statItem.setInTraining(isInTraining(verb.getId()));
		int correct = 0;
		int wrong = 0;
		for (TrainMode mode : TrainMode.values()) {
			correct += getCount(getKey(verb.getId(), mode, CORRECT));
			wrong += getCount(getKey(verb.getId(), mode, WRONG));
		}
		statItem.setCorrect(correct);
		statItem.setWrong(wrong);
		return statItem;
	}
	
	@Override
	public void resetStat(Integer verbId) {
		for (TrainMode mode : TrainMode.values()) {
			preferences.edit().remove(getKey(verbId, mode, CORRECT)).commit();
			preferences.edit().remove(getKey(verbId, mode, WRONG)).commit();
		}
	}
	
	@Override
	public void setInTraining(Integer verbId, boolean inTraining) {
		preferences.edit().putBoolean(getInTrainingKey(verbId), inTraining).commit();
	}
	
	@Override
	public boolean isInTraining(Integer verbId) {
		return preferences.getBoolean(getInTrainingKey(verbId), true);
	}
	
	private int getCount(String key) {
		return preferences.getInt(key, 0);
	}

	private String getKey(Integer verbId, TrainMode select, String postfix) {
		String key = VERB_STAT_PREFIX + KEY_SPLITTER + verbId + KEY_SPLITTER +
				select.ordinal() + KEY_SPLITTER + postfix;
		return key;
	}
	
	private void addStat(int formQuest, Verb verb, TrainMode select, String postfix) {
		String key = getKey(verb.getId(), select, postfix);
		int value = getCount(key);
		preferences.edit().putInt(key, ++value).commit();
		Log.d(LOG_TAG, formQuest + "_" + verb + "_" + select + "_" + postfix + "_" + value);
	}

	private float getRate(String key) {
		return preferences.getInt(key, RATE_DEFAULT) * 1f / RATE_SCALE;
	}
	
	private String getInTrainingKey(Integer verbId) {
		return IN_TRAINING + verbId;
	}
	
	@Override
	public float getFontSize() {
		return preferences.getInt(FONT_SIZE, FONT_SIZE_DEFAULT);
	}
	
}
