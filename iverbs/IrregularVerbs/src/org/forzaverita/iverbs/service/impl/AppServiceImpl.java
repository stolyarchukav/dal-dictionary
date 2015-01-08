package org.forzaverita.iverbs.service.impl;

import android.app.Application;
import android.database.Cursor;

import org.forzaverita.iverbs.analytics.TrackerUtils;
import org.forzaverita.iverbs.data.Lang;
import org.forzaverita.iverbs.data.StatItem;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.database.impl.SqliteDatabase;
import org.forzaverita.iverbs.service.AppService;
import org.forzaverita.iverbs.service.PreferencesService;
import org.forzaverita.iverbs.train.TrainMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AppServiceImpl extends Application implements AppService {
	
	private Database database;
	
	private PreferencesService preferences;
	
	private Random random = new Random();
	
	private int currentId = 1;
	
	private int maxId;
	
	private Date preferenceChangeDate;
	
	private List<Integer> verbsInTraining = new ArrayList<Integer>();

    private List<Verb> verbs;
	
	@Override
	public void onCreate() {
		database = new SqliteDatabase(this);
		database.open();
		maxId = database.getMaxId();
		preferences = new PreferencesServiceImpl(this);
		List<Integer> verbIds = database.getVerbIds();
		for (Integer verbId : verbIds) {
			if (preferences.isInTraining(verbId)) {
				verbsInTraining.add(verbId);
			}			
		}
        TrackerUtils.track(this.getApplicationContext());
		super.onCreate();
	}
	
	@Override
	public List<Verb> getVerbs() {
        if (verbs == null) {
            verbs = database.getVerbs();
        }
		return verbs;
	}

    @Override
    public List<Integer> getVerbIds() {
        return database.getVerbIds();
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
		int index = random.nextInt(verbsInTraining.size());
		Verb verb = database.getVerb(verbsInTraining.get(index));
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
	
	@Override
	public List<StatItem> getStats() {
		List<StatItem> stats = new ArrayList<StatItem>();
		for (Verb verb : getVerbs()) {
			stats.add(preferences.getStat(verb));			
		}
		return stats;
	}
	
	@Override
	public void resetStats() {
		for (Integer verbId : getVerbIds()) {
			preferences.resetStat(verbId);
		}
	}
	
	@Override
	public void setInTraining(Integer verbId, boolean inTraining) {
		preferences.setInTraining(verbId, inTraining);
		if (inTraining) {
			if (! verbsInTraining.contains(verbId)) {
				verbsInTraining.add(verbId);
			}	
		}
		else {
			verbsInTraining.remove(verbId);
		}
        preferencesChanged();
	}

    @Override
    public void setInTrainingAll(boolean inTraining) {
        List<Integer> ids = getVerbIds();
        for (Integer id : ids) {
            setInTraining(id, inTraining);
        }
        preferencesChanged();
    }

    @Override
	public int getInTrainingCount() {
		return verbsInTraining.size();
	}
	
	@Override
	public float getFontSize() {
		return preferences.getFontSize();
	}

    @Override
    public int getVerbsCount() {
        return maxId;
    }

}
