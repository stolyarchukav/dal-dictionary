package org.forzaverita.daldic.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.service.DatabaseDeployer;
import org.forzaverita.daldic.service.DatabaseService;
import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseServiceImpl implements DatabaseService {
	
	private static int DATA_VERSION = 7;
	private static int WORDS_COUNT = 44652;
	private static String WORD_ID = "word_id";
	private static String WORD = "word";
	private static String WORD_REF = "word_ref";
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
    public Map<Integer, String> getWordsBeginWith(String begin, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, WORD + " like '" + begin.trim().toUpperCase() + "%'",
            		null, null, null, null);
            return getIdWordMapFromCursor(cursor, capitalLetters);
    	}
    	catch (Exception e) {
			throw searchError();
		}
    }
    
    @Override
    public Map<Integer, String> getWordsBeginWith(char letter, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD}, FIRST_LETTER + " = '" + letter + "'",
            		null, null, null, null);
            return getIdWordMapFromCursor(cursor, capitalLetters);
    	}
    	catch (Exception e) {
			throw searchError();
		}
    }
    
    @Override
    public Map<Integer, String> getWordsFullSearch(String query, boolean capitalLetters) {
    	openDataBase();
    	try {
    		Cursor cursor = database.query(WORD, new String[] {WORD_ID, WORD, DESCRIPTION}, "upper(" + DESCRIPTION + ") like '%" + query.trim().toUpperCase() + "%'",
            		null, null, null, null);
            return getIdWordDescMapFromCursor(cursor, capitalLetters, query);
    	}
    	catch (Exception e) {
			throw searchError();
		}
    }

    @Override
	public String[] getDescription(Integer id) {
		openDataBase();
		try {
			Cursor cursor = database.query(WORD, new String[] {DESCRIPTION, WORD_REF}, WORD_ID + " = " + id,
	        		null, null, null, null);
	        return getDescriptionFromCursor(cursor);
		}
		catch (Exception e) {
			throw searchError();
		}
	}
	
    @Override
	public String[] getWordAndDescriptionById(long id) {
		openDataBase();
		try {
			Cursor cursor = database.query(WORD, new String[] {WORD, DESCRIPTION, WORD_REF}, WORD_ID + " = " + id,
	        		null, null, null, null);
			String[] result = null;
			if (cursor.moveToFirst()) {
				Integer ref = cursor.getInt(2);
				String refDesc = getReferenceDesc(ref);
				result = new String[] {cursor.getString(0), cursor.getString(1), refDesc};
			}
			cursor.close();
			return result;
		}
		catch (Exception e) {
			throw searchError();
		}
	}
	
    @Override
	public int getWordsCount() {
		return WORDS_COUNT;
	}
	
    private String[] getDescriptionFromCursor(Cursor cursor) {
		String[] result = new String[2];
		if (cursor.moveToFirst()) {
			 result[0] = cursor.getString(0);
			 Integer ref = cursor.getInt(1);
			 result[1] = getReferenceDesc(ref);
        }
        cursor.close();
        return result;
	}
    
   	private String getReferenceDesc(Integer referenceId){
   		String result = null;
   		if (referenceId != null && referenceId != 0) {
   			Cursor cursor = database.query(WORD, new String[] {DESCRIPTION}, WORD_ID + " = " + referenceId,
   					null, null, null, null);
   			if (cursor.moveToFirst()) {
   				result = cursor.getString(0);
   			}
   			cursor.close(); 
   		}
   		return result;
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
	
	private DatabaseException searchError() {
		database = null;
		throw new DatabaseException(context.getString(R.string.error_search_try_again));
	}
	
}
