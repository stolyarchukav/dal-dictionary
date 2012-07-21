package org.forzaverita.brefdic.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.forzaverita.brefdic.R;
import org.forzaverita.brefdic.exception.DatabaseException;
import org.forzaverita.brefdic.service.DatabaseDeployer;
import org.forzaverita.brefdic.service.DatabaseService;
import org.forzaverita.brefdic.service.PreferencesService;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class DataBaseServiceImpl implements DatabaseService {
	
	private static int DATA_VERSION = 1;
	private static String WORD_ID = "word_id";
	private static String WORD = "word";
	private static String DESCRIPTION = "description";
	private static String FIRST_LETTER = "first_letter";
	private static String BREFDIC_METADATA = "brefdic_metadata";
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
    		Cursor cursor = database.query(BREFDIC_METADATA, new String[]{DATA_VERSION_FIELD},
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
    public Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, 
    				WORD + " like '" + begin.trim().toUpperCase() + "%'",
            		null, null, null, null);
            return getIdWordMapFromCursor(cursor, capitalLetters);
    	}
    	catch (Exception e) {
			throw searchError(e);
		}
    }
    
    @Override
    public Map<Integer, String> getWordsBeginWith(char letter, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, 
    				FIRST_LETTER + " = '" + letter + "'",
            		null, null, null, null);
            return getIdWordMapFromCursor(cursor, capitalLetters);
    	}
    	catch (Exception e) {
			throw searchError(e);
		}
    }
    
    @Override
    public Map<Integer, String> getWordsFullSearch(String query, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD, DESCRIPTION}, 
    				"upper(" + DESCRIPTION + ") like '%" + query.trim().toUpperCase() + "%'",
            		null, null, null, null);
            return getIdWordDescMapFromCursor(cursor, capitalLetters, query);
    	}
    	catch (Exception e) {
			throw searchError(e);
		}
    }
	
    @Override
	public String[] getWordAndDescriptionById(long id) {
		openDataBase();
		try {
			Cursor cursor = database.query(WORD, new String[] {WORD, DESCRIPTION}, 
					WORD_ID + " = " + id,
	        		null, null, null, null);
			String[] result = null;
			if (cursor.moveToFirst()) {
				result = new String[] {cursor.getString(0), 
						getDescPrecededByWord(cursor.getString(0), cursor.getString(1), false), null};
			}
			cursor.close();
			return result;
		}
		catch (Exception e) {
			throw searchError(e);
		}
	}

	private Map<Integer, String> getIdWordMapFromCursor(Cursor cursor, boolean capitalLetters) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		if (cursor.moveToFirst()) {
            do {
            	String word = cursor.getString(1);
            	if (! capitalLetters) {
            		word = word.charAt(0) + word.substring(1).toLowerCase();
            	}
            	result.put(cursor.getInt(0), word);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
	}
	
	private Map<Integer, String> getIdWordDescMapFromCursor(Cursor cursor,	boolean capitalLetters, String query) {
		 Map<Integer, String> result = new HashMap<Integer, String>();
		 if (cursor.moveToFirst()) {
			 do {
				 String word = cursor.getString(1);
				 String desc = getDescPrecededByWord(word,  cursor.getString(2), true);
				 StringBuilder wordDesc = new StringBuilder();
				 if (capitalLetters) {
					 wordDesc.append(word);
				 }
				 else {
					 wordDesc.append(word.charAt(0));
					 wordDesc.append(word.substring(1).toLowerCase());
				 }
				 int index = desc.toUpperCase().indexOf(query.toUpperCase());
				 int begin = index >= 10 ? index - 10 : 0;
				 String prefix = desc.substring(begin, index);
				 int end = index < desc.length() - 25 ? index + 25 : desc.length() - 1;
				 String body = desc.substring(index, index + query.length());
				 String postfix = desc.substring(index + query.length(), end);
				 wordDesc.append("</br><small><i> ...");
				 wordDesc.append(prefix);
				 wordDesc.append("<font color='green'>");
				 wordDesc.append(body);
				 wordDesc.append("</font>");
				 wordDesc.append(postfix);
				 wordDesc.append("...</i></small>");
				 result.put(cursor.getInt(0), wordDesc.toString());
			 }
			 while (cursor.moveToNext());
		 }
	     cursor.close();
	     return result;
	}
	
	private DatabaseException searchError(Exception e) {
		database = null;
		Log.d("brefdic", e.getMessage(), e);
		throw new DatabaseException(context.getString(R.string.error_search_try_again));
	}
	
	@Override
	public String getWordById(Integer wordId) {
		openDataBase();
		try {
			Cursor cursor = database.query(WORD, new String[] {WORD}, 
					WORD_ID + " = " + wordId,
	        		null, null, null, null);
			String result = null;
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
			}
			cursor.close();
			return result;
		}
		catch (Exception e) {
			throw searchError(e);
		}
	}
	
	private String getDescPrecededByWord(String word, String desc, boolean markWord) {
		if (markWord) {
			return  "<p>" + word + "</p>" + desc;
		}
		return word + " - " + desc;
	}
	
	@Override
	public Cursor getCursorOfWordsBeginWith(String begin) {
		return database.query(WORD, new String[] {
				WORD_ID + " as " + BaseColumns._ID,
				WORD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
				WORD + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1,
				WORD + " as " + SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA},
				WORD + " like '" + begin.trim().toUpperCase() + "%'", 
				null, null, null, null, "10");
	}
	
}
