package org.forzaverita.iverbs.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.forzaverita.iverbs.data.Lang;
import org.forzaverita.iverbs.data.TrainMode;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.database.impl.SqliteDatabase;
import org.forzaverita.iverbs.service.AppService;
import org.forzaverita.iverbs.service.PreferencesService;

import android.app.Application;
import android.database.Cursor;

public class AppServiceImpl extends Application implements AppService {
	
	private Database database;
	
	private PreferencesService preferences;
	
	private Random random = new Random();
	
	private int currentId = 1;
	
	private int maxId;
	
	private Date preferenceChangeDate;
	
	@Override
	public void onCreate() {
		database = new SqliteDatabase(this);
		database.open();
		maxId = database.getMaxId();
		preferences = new PreferencesServiceImpl(this);
		super.onCreate();
	}
	
	@Override
	public List<Verb> getVerbs() {
		return database.getVerbs();
	}
	
	@Override
	public Verb getVerb(int id) {
		if (id != 0) {
			currentId = id;
		}
		return database.getVerb(currentId);
	}

	@Override
	public Verb getPreviousVerb() {
		if (--currentId < 1) {
			currentId = maxId;
		}
		return database.getVerb(currentId);
	}

	@Override
	public Verb getNextVerb() {
		if (++currentId > maxId) {
			currentId = 1;
		}
		return database.getVerb(currentId);
	}
	
	@Override
	public Cursor getCursorVerbsContains(String search) {
		return database.getCursorVerbsContains(search);
	}

	@Override
	public List<Verb> searchVerbs(String query) {
		return database.searchVerbs(query);
	}
	
	@Override
	public Verb getRandomVerb(Verb... excludes) {
		Verb verb = database.getVerb(random.nextInt(maxId + 1));
		if (verb != null && 
				! (excludes != null && Arrays.asList(excludes).contains(verb))) {
			return verb;
		}	
		return getRandomVerb(excludes);	
	}
	
	@Override
	public void addCorrect(int formQuest, Verb verb, TrainMode select) {
		preferences.addCorrect(formQuest, verb, select);
	}
	
	@Override
	public void addWrong(int formQuest, Verb verb, TrainMode select) {
		preferences.addWrong(formQuest, verb, select);
	}
	
	@Override
	public Lang getLanguage() {
		String lang = preferences.getLanguage();
		Lang result = null;
		if (lang != null) {
			result = Lang.valueOf(lang);			
		}
		if (result == null) {
			String locale = Locale.getDefault().getLanguage();
			result = Lang.tryValueOf(locale);
		}
		if (result == null) {
			result = Lang.RU;
		}
		return result;
	}
	

	@Override
	public void setLanguage(Lang lang) {
		preferences.setLanguage(lang.name());
	}
	
	@Override
	public boolean isLanguagePrefered() {
		return preferences.getLanguage() != null;
	}
	
	@Override
	public boolean isPreferencesChanged(Date lastPreferencesCheck) {
		return preferenceChangeDate != null && preferenceChangeDate.after(lastPreferencesCheck);
	}
	
	@Override
	public void preferencesChanged() {
		preferenceChangeDate = new Date();
	}
	
	@Override
	public float getSpeechRate() {
		return preferences.getSpeechRate();
	}
	
	@Override
	public float getPitch() {
		return preferences.getPitch();
	}
	
}
