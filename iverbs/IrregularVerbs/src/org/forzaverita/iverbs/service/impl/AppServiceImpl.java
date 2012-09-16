package org.forzaverita.iverbs.service.impl;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.database.impl.SqliteDatabase;
import org.forzaverita.iverbs.service.AppService;

import android.app.Application;

public class AppServiceImpl extends Application implements AppService {
	
	private Database database;
	
	@Override
	public void onCreate() {
		database = new SqliteDatabase(this);
		database.open();
		super.onCreate();
	}
	
	public List<Verb> getVerbs(int page, int count) {
		return database.getVerbs();
	}
	
	@Override
	public List<Verb> getVerbs() {
		return database.getVerbs();
	}
	
}
