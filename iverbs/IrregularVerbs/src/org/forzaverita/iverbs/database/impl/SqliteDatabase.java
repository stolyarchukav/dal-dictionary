package org.forzaverita.iverbs.database.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteDatabase extends SQLiteOpenHelper implements Database {

	private static int DATA_VERSION = 1;
	
	private static String DB_NAME = "iverbs.sqlite";
	private static String DB_PATH = "/data/data/org.forzaverita.iverbs/databases/" + DB_NAME;

	private static String IVERBS_METADATA = "iverbs_metadata";
	private static String DATA_VERSION_FIELD = "data_version";
	
	private static final String VERB = "verb";
	private static final String ID = "id";
	private static final String FORM_1 = "form_1";
	private static final String FORM_1_TRANSCRIPTION = "form_1_transcription";
	private static final String FORM_2 = "form_2";
	private static final String FORM_2_TRANSCRIPTION = "form_2_transcription";
	private static final String FORM_3 = "form_3";
	private static final String FORM_3_TRANSCRIPTION = "form_3_transcription";
	private static final String TRANSLATION = "rus";
	
	private SQLiteDatabase database;
	private final Context context;

	public SqliteDatabase(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}
	
	@Override
	public void open() {
		database = checkDatabase();
		if (database == null) {
			getReadableDatabase();
			try {
				copyDatabase();
			}
			catch (IOException e) {
				throw new Error("Error on setup application. Can't install database. " +
						"Please, try to start it again");
			}
			database = checkDatabase();
		}
	}
	
	private SQLiteDatabase checkDatabase() {
		try {
			SQLiteDatabase db = openDatabase();
			if (db != null) {
				if (checkDataVersion(db)) {
					return db;
				}
				db.close();
			}
		}
		catch (SQLiteException e) {
			Log.e(Constants.LOG_TAG, "check database error", e);
		}
		return null;
	}
	
	private boolean checkDataVersion(SQLiteDatabase db) {
    	boolean result = false;
    	try {
    		Cursor cursor = db.query(IVERBS_METADATA, new String[]{DATA_VERSION_FIELD},
        			null, null, null, null, null);
        	if (cursor.moveToFirst()) {
                result = cursor.getInt(0) == DATA_VERSION;
            }
            cursor.close();
    	}
    	catch (Exception e) {
			result = false;
		}
        return result;
	}

	private void copyDatabase() throws IOException {
		InputStream is = context.getAssets().open(DB_NAME);
		OutputStream os = new FileOutputStream(DB_PATH);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		os.close();
		is.close();
	}

	private SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}	
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	@Override
	public List<Verb> getVerbs() {
		Cursor cursor = database.query(VERB, new String[] {FORM_1, FORM_2, FORM_3, TRANSLATION}, 
				null, null, null, null, null);
		List<Verb> verbs = new ArrayList<Verb>();
		if (cursor.moveToFirst()) {
            do {
            	Verb verb = new Verb();
            	verb.setForm1(cursor.getString(0));
            	verb.setForm2(cursor.getString(1));
            	verb.setForm3(cursor.getString(2));
            	verb.setTranslation(cursor.getString(3));
            	verbs.add(verb);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
		return verbs;
	}
	
	@Override
	public Verb getVerb(int id) {
		Verb verb = null;
		Cursor cursor = database.query(VERB, new String[] {FORM_1, FORM_1_TRANSCRIPTION, 
				FORM_2, FORM_2_TRANSCRIPTION, FORM_3, FORM_3_TRANSCRIPTION, TRANSLATION}, 
				ID + "=" + id, null, null, null, null);
		if (cursor.moveToFirst()) {
			verb = new Verb();
			verb.setId(id);
			verb.setForm1(cursor.getString(0));
			verb.setForm1Transcription(cursor.getString(1));
        	verb.setForm2(cursor.getString(2));
        	verb.setForm2Transcription(cursor.getString(3));
        	verb.setForm3(cursor.getString(4));
        	verb.setForm3Transcription(cursor.getString(5));
        	verb.setTranslation(cursor.getString(6));
		}
		return verb;
	}
	
}
