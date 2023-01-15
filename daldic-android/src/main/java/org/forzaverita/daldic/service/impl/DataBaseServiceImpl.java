package org.forzaverita.daldic.service.impl;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.service.DatabaseDeployer;
import org.forzaverita.daldic.service.DatabaseService;
import org.forzaverita.daldic.service.PreferencesService;

import java.util.HashMap;
import java.util.Map;

public class DataBaseServiceImpl implements DatabaseService {

	private static final String ACCENT = "́";
	private static final int DATA_VERSION = 10;
	private static final String WORD_ID = "word_id";
	private static final String WORD = "word";
	private static final String WORD_REF = "word_ref";
	private static final String DESCRIPTION = "description";
	private static final String FIRST_LETTER = "first_letter";
	private static final String ACCENT_POSITION = "accent_position";
	private static final String DALDIC_METADATA = "daldic_metadata";
	private static final String DATA_VERSION_FIELD = "data_version";
    
    private final DatabaseDeployer databaseDeployer;
	private final Context context;

	private SQLiteDatabase database;

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
    public Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD, ACCENT_POSITION},
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
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD, ACCENT_POSITION},
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
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD, DESCRIPTION, ACCENT_POSITION},
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
			Cursor cursor = database.query(WORD, new String[] {WORD, DESCRIPTION, WORD_REF, ACCENT_POSITION},
					WORD_ID + " = " + id,
	        		null, null, null, null);
			String[] result = null;
			if (cursor.moveToFirst()) {
				String word = cursor.getString(0);
				String description = cursor.getString(1);
				Integer ref = cursor.getInt(2);
				String refDesc = getReferenceDesc(ref);
				int accentPosition = cursor.getInt(3);
				result = new String[] {formattedWord(word, accentPosition), description, refDesc};
			}
			cursor.close();
			return result;
		}
		catch (Exception e) {
			throw searchError(e);
		}
	}
	
    @Override
	public int getWordsCount() {
		return Constants.WORDS_COUNT;
	}
    
   	private String getReferenceDesc(Integer referenceId){
   		String result = null;
   		if (referenceId != null && referenceId != 0) {
   			Cursor cursor = database.query(WORD, new String[] {DESCRIPTION}, 
   					WORD_ID + " = " + referenceId,
   					null, null, null, null);
   			if (cursor.moveToFirst()) {
   				result = cursor.getString(0);
   			}
   			cursor.close(); 
   		}
   		return result;
   	}

	private Map<Integer, String> getIdWordMapFromCursor(Cursor cursor, boolean capitalLetters) {
		Map<Integer, String> result = new HashMap<>();
		if (cursor.moveToFirst()) {
            do {
            	String word = cursor.getString(1);
				int accentPosition = cursor.getInt(2);
            	if (! capitalLetters) {
            		word = word.charAt(0) + word.substring(1).toLowerCase();
            	}
            	result.put(cursor.getInt(0), formattedWord(word, accentPosition));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return result;
	}
	
	private Map<Integer, String> getIdWordDescMapFromCursor(Cursor cursor,	boolean capitalLetters, String query) {
		 Map<Integer, String> result = new HashMap<>();
		 if (cursor.moveToFirst()) {
			 do {
				 String word = formattedWord(cursor.getString(1), cursor.getInt(3));
				 String desc = cursor.getString(2);
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
		Log.d("daldic", e.getMessage(), e);
		throw new DatabaseException(context.getString(R.string.error_search_try_again));
	}
	
	@Override
	public String getWordById(Integer wordId) {
		openDataBase();
		try {
			Cursor cursor = database.query(WORD, new String[] {WORD, ACCENT_POSITION},
					WORD_ID + " = " + wordId,
	        		null, null, null, null);
			String result = null;
			if (cursor.moveToFirst()) {
				result = formattedWord(cursor.getString(0), cursor.getInt(1));
			}
			cursor.close();
			return result;
		}
		catch (Exception e) {
			throw searchError(e);
		}
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

	private static String formattedWord(String word, int accentPosition) {
		if (accentPosition >= 0) {
			return new StringBuilder().append(word).insert(accentPosition, ACCENT).toString();
		}
		return word;
	}
	
}
