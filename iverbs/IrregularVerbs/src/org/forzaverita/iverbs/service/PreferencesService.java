package org.forzaverita.iverbs.service;

import org.forzaverita.iverbs.data.StatItem;
import org.forzaverita.iverbs.train.TrainMode;
import org.forzaverita.iverbs.data.Verb;

public interface PreferencesService {

	void addCorrect(int formQuest, Verb verb, TrainMode select);

	void addWrong(int formQuest, Verb verb, TrainMode select);

	String getLanguage();

	void setLanguage(String lang);

	float getSpeechRate();

	float getPitch();

	StatItem getStat(Verb verb);
	
	void resetStat(Integer verbId);

	void setInTraining(Integer verbId, boolean inTraining);

	boolean isInTraining(Integer verbId);

	float getFontSize();

}
