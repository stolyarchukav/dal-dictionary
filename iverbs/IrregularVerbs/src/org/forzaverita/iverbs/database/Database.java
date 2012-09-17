package org.forzaverita.iverbs.database;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;

public interface Database {

	void open();

	List<Verb> getVerbs();

	Verb getVerb(int id);
	
}
