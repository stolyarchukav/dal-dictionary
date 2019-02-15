package org.forzaverita.iverbs.database.impl;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.database.Database;
import org.forzaverita.iverbs.service.AppService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class SqliteDatabase extends SQLiteOpenHelper implements Database {

	private static int DATA_VERSION = 5;
	
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
    public List<Integer> getVerbIds() {
        Cursor cursor = database.query(VERB, new String[] {ID},
                null, null, null, null, null);
        return extractIds(cursor);
    }

    private List<Integer> extractIds(Cursor cursor) {
        List<Integer> ids = new ArrayList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    @Override
    public List<Verb> getVerbs() {
        List<String> columns = new ArrayList<String>(asList(ID, FORM_1, FORM_2, FORM_3, getLang()));
        columns.addAll(asList(FORM_1_TRANSCRIPTION, FORM_2_TRANSCRIPTION, FORM_3_TRANSCRIPTION));
        Cursor cursor = database.query(VERB, columns.toArray(new String[0]), null, null, null, null, null);
        return extractVerbs(cursor);
    }

    private String getLang() {
		return ((AppService) context).getLanguage().name();
	}

	private List<Verb> extractVerbs(Cursor cursor) {
		List<Verb> verbs = new ArrayList<Verb>();
		if (cursor.moveToFirst()) {
            do {
            	Verb verb = new Verb();
            	verb.setId(cursor.getInt(0));
            	verb.setForm1(cursor.getString(1));
            	verb.setForm2(cursor.getString(2));
            	verb.setForm3(cursor.getString(3));
            	verb.setTranslation(cursor.getString(4));
                verb.setForm1Transcription(cursor.getString(5));
                verb.setForm2Transcription(cursor.getString(6));
                verb.setForm3Transcription(cursor.getString(7));
            	verbs.add(verb);
            } while (cursor.moveToNext());
        }
        cursor.close();
		return verbs;
	}
	
	@Override
	public Verb getVerb(int id) {
		Verb verb = null;
		Cursor cursor = database.query(VERB, new String[] {FORM_1, FORM_1_TRANSCRIPTION, 
				FORM_2, FORM_2_TRANSCRIPTION, FORM_3, FORM_3_TRANSCRIPTION, getLang()}, 
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

	@Override
	public int getMaxId() {
		Cursor cursor = database.query(VERB, new String[] {ID}, 
				null, null, null, null, ID + " desc", "1");
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return 0;
	}
	
	@Override
	public Cursor getCursorVerbsContains(String search) {
		final String SELECT_ID_FIELD_VERB = "select " + ID + " as " + BaseColumns._ID +
				", " + ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID +
				", %1$s as " + SearchManager.SUGGEST_COLUMN_TEXT_1 +
				", %1$s as " + SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA +
				" from " + VERB + 
				" where upper(%1$s) like '%%%2$s%%' ";
		StringBuilder query = new StringBuilder(String.format(SELECT_ID_FIELD_VERB, FORM_1, search));
		query.append(" union all ");
		query.append(String.format(SELECT_ID_FIELD_VERB, FORM_2, search));
		query.append(" union all ");
		query.append(String.format(SELECT_ID_FIELD_VERB, FORM_3, search));
		query.append(" union all ");
		query.append(String.format(SELECT_ID_FIELD_VERB, getLang(), search));
		return database.rawQuery(query.toString(), null);
	}

	@Override
	public List<Verb> searchVerbs(String query) {
		String lang = getLang();
		Cursor cursor = database.query(VERB, new String[] {ID, FORM_1, FORM_2, FORM_3, lang}, 
				restrictionLike(FORM_1, query) + " or " +
				restrictionLike(FORM_2, query) + " or " +
				restrictionLike(FORM_3, query) + " or " +
				restrictionLike(lang, query), 
				null, null, null, null);
		return extractVerbs(cursor);
	}
	
	private String restrictionLike(String field, String query) {
		return "upper(" + field + ") like '%" + query + "%'";
	}
	
}
