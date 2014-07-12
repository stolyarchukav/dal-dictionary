package org.forzaverita.trackoftrek.database.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.forzaverita.trackoftrek.data.Constants;
import org.forzaverita.trackoftrek.data.Punto;
import org.forzaverita.trackoftrek.database.Database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

public class SqliteDatabase extends SQLiteOpenHelper implements Database {

    private static int DATA_VERSION = 1;

	private static String DB_NAME = "tot.sqlite";
	private static String DB_PATH = "/data/data/org.forzaverita.trackoftrek/databases/" + DB_NAME;

	private static String TOT_METADATA = "tot_metadata";
	private static String DATA_VERSION_FIELD = "data_version";

    private static final String PUNTO = "punto";
	private static final String ID = "id";
    private static final String TIME = "time";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

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
			getWritableDatabase();
			try {
				copyDatabase();
			}
			catch (IOException e) {
				throw new Error("Error on setup application. Can't install database. " +
						"Please, try to start it again", e);
			}
			database = checkDatabase();
		}
	}

    @Override
    public void storeLocation(Punto location) {
        ContentValues values = new ContentValues();
        values.put(ID, UUID.randomUUID().getLeastSignificantBits());
        values.put(TIME, new Date().getTime());
        values.put(LATITUDE, location.getLatitude());
        values.put(LONGITUDE, location.getLongitude());
        database.insert(PUNTO, null, values);
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
    		Cursor cursor = db.query(TOT_METADATA, new String[]{DATA_VERSION_FIELD},
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
		return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
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



}
