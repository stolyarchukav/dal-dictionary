package org.forzaverita.verbita.database;

import java.util.List;

import org.forzaverita.verbita.data.Verb;

import android.database.Cursor;

public interface Database {

	void open();

    List<Integer> getVerbIds();

    List<Verb> getVerbs(boolean withTranscription);

	Verb getVerb(int id);

	int getMaxId();

	Cursor getCursorVerbsContains(String search);

	List<Verb> searchVerbs(String query);
	
}
