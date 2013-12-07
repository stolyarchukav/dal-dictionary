package org.forzaverita.iverbs.pay.service;

import org.forzaverita.iverbs.pay.data.StatItem;
import org.forzaverita.iverbs.pay.train.TrainMode;
import org.forzaverita.iverbs.pay.data.Verb;

public interface PreferencesService {

	void addCorrect(int formQuest, Verb verb, TrainMode select);

	void addWrong(int formQuest, Verb verb, TrainMode select);

	String getLanguage();

	void setLanguage(String lang);

	float getSpeechRate();

	float getPitch();

	StatItem getStat(Verb verb);
	
	void resetStat(Verb verb);

	void setInTraining(Verb verb, boolean inTraining);

	boolean isInTraining(Verb verb);

	float getFontSize();

}
