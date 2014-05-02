package org.forzaverita.iverbs.database;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;

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
