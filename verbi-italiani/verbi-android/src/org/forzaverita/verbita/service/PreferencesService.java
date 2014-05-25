package org.forzaverita.verbita.service;

import org.forzaverita.verbita.data.StatItem;
import org.forzaverita.verbita.train.TrainMode;
import org.forzaverita.verbita.data.Verb;

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
