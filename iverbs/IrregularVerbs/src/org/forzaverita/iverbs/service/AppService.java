package org.forzaverita.iverbs.service;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;

import android.database.Cursor;

public interface AppService {

	List<Verb> getVerbs();

	Verb getVerb(int id);

	Verb getPreviousVerb();

	Verb getNextVerb();

	Cursor getCursorVerbsContains(String search);

	List<Verb> searchVerbs(String query);

	Verb getRandomVerb(Verb... excludes);

}
