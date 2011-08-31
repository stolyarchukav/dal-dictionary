package org.forzaverita.daldic.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.service.DatabaseDeployer;
import org.forzaverita.daldic.service.DatabaseService;
import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseServiceImpl implements DatabaseService {
	
	private static int DATA_VERSION = 3;
	private static int WORDS_COUNT = 44652;
	private static String WORD_ID = "word_id";
	private static String WORD = "word";
	private static String DESCRIPTION = "description";
	private static String FIRST_LETTER = "first_letter";
	private static String DALDIC_METADATA = "daldic_metadata";
	private static String DATA_VERSION_FIELD = "data_version";
    
	private SQLiteDatabase database;
    private DatabaseDeployer databaseDeployer;
	private Context context;
	
	public DataBaseServiceImpl(Context context, PreferencesService preferencesService) {
		this.context = context;
		databaseDeployer = new DatabaseDeployerImpl(context, preferencesService);
    }
	
	private void openDataBase() {
		if (database == null) {
			boolean dbExist = tryOpenDataBase();
	        if (! dbExist || ! isCorrectVersion()) {
	        	close();
	        	databaseDeployer.reinstallDatabase();
	        	if (! tryOpenDataBase()) {
	        		throw new DatabaseException(context.getString(R.string.error_db_open));
	        	}
	        }
		}
    }

    private boolean tryOpenDataBase() {
    	try {
    		String path = databaseDeployer.getDatabasePath();
    		database = SQLiteDatabase.openDatabase(path, 
    				null, SQLiteDatabase.OPEN_READONLY);
    		return database != null;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return false;
		}
    }

    private boolean isCorrectVersion() {
    	boolean result = false;
    	try {
    		Cursor cursor = database.query(DALDIC_METADATA, new String[]{DATA_VERSION_FIELD},
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
    
    @Override
	public boolean isDatabaseReady() {
		return database != null;
	}
	
	@Override
	public void open() {
		openDataBase();
	}
    
    @Override
    public void close() {
        if (database != null) {
        	database.close();
        }
    }
    
    @Override
    public Map<Integer, String> getWordsBeginWith(String begin) {
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, WORD + " like '" + begin.trim().toUpperCase() + "%'",
        		null, null, null, WORD + " asc");
        return getMapFromCursor(cursor);
    }
    
    @Override
    public Map<Integer, String> getWordsBeginWith(char letter) {
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, FIRST_LETTER + " = '" + letter + "'",
        		null, null, null, WORD + " asc");
        return getMapFromCursor(cursor);
    }

    @Override
	public Set<String> getDescriptions(Integer id) {
		openDataBase();
		Cursor cursor = database.query(WORD, new String[] {DESCRIPTION}, WORD_ID + " = " + id,
        		null, null, null, null);
        return getSetFromCursor(cursor);
	}
	
    @Override
	public String[] getWordAndDescriptionById(long id) {
		openDataBase();
		Cursor cursor = database.query(WORD, new String[] {WORD, DESCRIPTION}, WORD_ID + " = " + id,
        		null, null, null, null);
		String[] result = null;
		if (cursor.moveToFirst()) {
			result = new String[] {cursor.getString(0), cursor.getString(1)};
		}
		cursor.close();
		return result;
	}
	
    @Override
	public int getWordsCount() {
		return WORDS_COUNT;
	}
	
	private Set<String> getSetFromCursor(Cursor cursor) {
		Set<String> result = new TreeSet<String>();
		if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
	}
	
	private Map<Integer, String> getMapFromCursor(Cursor cursor) {
		Map<Integer, String> result = new TreeMap<Integer, String>();
		if (cursor.moveToFirst()) {
            do {
                result.put(cursor.getInt(0), cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
	}
	
}
