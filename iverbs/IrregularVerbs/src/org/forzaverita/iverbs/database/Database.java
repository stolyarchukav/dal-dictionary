package org.forzaverita.iverbs.database;

import android.database.Cursor;

import org.forzaverita.iverbs.data.Verb;

import java.util.List;

public interface Database {

	void open();

    List<Integer> getVerbIds();

    List<Verb> getVerbs();

	Verb getVerb(int id);

	int getMaxId();

	Cursor getCursorVerbsContains(String search);

	List<Verb> searchVerbs(String query);
	
}
