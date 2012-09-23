package org.forzaverita.iverbs.service;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;

public interface AppService {

	List<Verb> getVerbs();

	Verb getVerb(int id);

	Verb getPreviousVerb();

	Verb getNextVerb();

}
