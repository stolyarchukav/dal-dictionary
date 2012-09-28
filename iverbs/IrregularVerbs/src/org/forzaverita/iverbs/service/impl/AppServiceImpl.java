package org.forzaverita.iverbs.service.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.database.impl.SqliteDatabase;
import org.forzaverita.iverbs.service.AppService;

import android.app.Application;
import android.database.Cursor;

public class AppServiceImpl extends Application implements AppService {
	
	private Database database;
	
	private Reference<List<Verb>> verbsCache = new WeakReference<List<Verb>>(null);
	
	private int currentId = 1;
	
	private int maxId;
	
	@Override
	public void onCreate() {
		database = new SqliteDatabase(this);
		database.open();
		maxId = database.getMaxId();
		super.onCreate();
	}
	
	@Override
	public List<Verb> getVerbs() {
		List<Verb> verbs = verbsCache.get();
		if (verbs == null || verbs.isEmpty()) {
			verbs = database.getVerbs();
			verbsCache = new WeakReference<List<Verb>>(verbs);
		}
		return verbs;
	}
	
	@Override
	public Verb getVerb(int id) {
		if (id != 0) {
			currentId = id;
		}
		return database.getVerb(currentId);
	}

	@Override
	public Verb getPreviousVerb() {
		if (--currentId < 1) {
			currentId = maxId;
		}
		return database.getVerb(currentId);
	}

	@Override
	public Verb getNextVerb() {
		if (++currentId > maxId) {
			currentId = 1;
		}
		return database.getVerb(currentId);
	}
	
	@Override
	public Cursor getCursorVerbsContains(String search) {
		return database.getCursorVerbsContains(search);
	}

	@Override
	public List<Verb> searchVerbs(String query) {
		return database.searchVerbs(query);
	}
	
}
