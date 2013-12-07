
package org.forzaverita.iverbs.pay.service;

import java.util.Date;
import java.util.List;

import org.forzaverita.iverbs.pay.data.Lang;
import org.forzaverita.iverbs.pay.data.StatItem;
import org.forzaverita.iverbs.pay.train.TrainMode;
import org.forzaverita.iverbs.pay.data.Verb;

import android.database.Cursor;

public interface AppService {

	List<Verb> getVerbs();

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

	void setInTraining(Verb verb, boolean inTraining);

    void setInTrainingAll(boolean inTraining);

	int getInTrainingCount();

	float getFontSize();

    int getVerbsCount();

}
