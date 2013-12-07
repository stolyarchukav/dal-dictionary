package org.forzaverita.iverbs.pay.database;

import java.util.List;

import org.forzaverita.iverbs.pay.data.Verb;

import android.database.Cursor;

public interface Database {

	void open();

	List<Verb> getVerbs();

	Verb getVerb(int id);

	int getMaxId();

	Cursor getCursorVerbsContains(String search);

	List<Verb> searchVerbs(String query);
	
}
