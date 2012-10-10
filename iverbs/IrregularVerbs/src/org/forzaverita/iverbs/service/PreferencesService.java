package org.forzaverita.iverbs.service;

import org.forzaverita.iverbs.data.TrainMode;
import org.forzaverita.iverbs.data.Verb;

public interface PreferencesService {

	void addCorrect(int formQuest, Verb verb, TrainMode select);

	void addWrong(int formQuest, Verb verb, TrainMode select);

}
