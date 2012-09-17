package org.forzaverita.iverbs.service.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.database.impl.SqliteDatabase;
import org.forzaverita.iverbs.service.AppService;

import android.app.Application;

public class AppServiceImpl extends Application implements AppService {
	
	private Database database;
	
	private Reference<List<Verb>> verbsCache = new WeakReference<List<Verb>>(null);
	
	@Override
	public void onCreate() {
		database = new SqliteDatabase(this);
		database.open();
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
	
}
