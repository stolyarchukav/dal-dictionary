
package org.forzaverita.iverbs.service;

import android.database.Cursor;

import org.forzaverita.iverbs.data.Lang;
import org.forzaverita.iverbs.data.StatItem;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.train.TrainMode;

import java.util.Date;
import java.util.List;

public interface AppService {

	List<Verb> getVerbs();

    List<Integer> getVerbIds();

    Verb getVerb(int id);

	Verb getPreviousVerb();

	Verb getNextVerb();

	Cursor getCursorVerbsContains(String search);

	List<Verb> searchVerbs(String query);

	Verb getRandomVerb(Verb... excludes);

	void addCorrect(int formQuest, Verb verb, TrainMode select);

	void addWrong(int formQuest, Verb verb, TrainMode select);
	
	Lang getLanguage();

	void setLanguage(Lang lang);

	boolean isLanguagePrefered();
	
	boolean isPreferencesChanged(Date lastPreferencesCheck);

	void preferencesChanged();

	float getSpeechRate();

	float getPitch();

	List<StatItem> getStats();

	void resetStats();

	void setInTraining(Integer verbId, boolean inTraining);

    void setInTrainingAll(boolean inTraining);

	int getInTrainingCount();

	float getFontSize();

    int getVerbsCount();

}
