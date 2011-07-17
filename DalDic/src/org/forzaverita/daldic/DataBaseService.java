package org.forzaverita.daldic;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseService {
	
	private static String DB_NAME = "daldic.sqlite";
	private static String WORD = "word";
	private static String DESCRIPTION = "description";
	private static String FIRST_LETTER = "first_letter";
    private SQLiteDatabase database;
    private Context context;
	
	public DataBaseService(Context context) {
        this.context = context;
    }
	
	public void openDataBase() {
        boolean dbExist = tryOpenDataBase();
        if (! dbExist) {
        	copyDataBase();
        	tryOpenDataBase();
        }
    }
 
    private boolean tryOpenDataBase() {
    	try {
    		String path = context.getFilesDir() + "/" + DB_NAME;
    		database = SQLiteDatabase.openDatabase(path, 
    				null, SQLiteDatabase.OPEN_READONLY);
    		return database != null;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return false;
		}
    }

    private void copyDataBase() {
    	try {
    		InputStream is = context.getAssets().open(DB_NAME);
            FileOutputStream os = context.openFileOutput(DB_NAME, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0){
            	os.write(buffer, 0, length);
            }
            os.close();
            is.close();
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
 
    public void close() {
        if (database != null) {
        	database.close();
        }
    }
    
    public Set<String> getWordsBeginWith(String begin) {
    	Cursor cursor = database.query(WORD, new String[] {WORD}, WORD + " like '" + begin + "%'",
        		null, null, null, WORD + " ASC");
        return getSetFromCursor(cursor);
    }
    
    public Set<String> getWordsBeginWith(char letter) {
    	Cursor cursor = database.query(WORD, new String[] {WORD}, FIRST_LETTER + " = '" + letter + "'",
        		null, null, null, WORD + " ASC");
        return getSetFromCursor(cursor);
    }

	public Set<String> getDescriptions(String word) {
		Cursor cursor = database.query(WORD, new String[] {DESCRIPTION}, WORD + " = '" + word + "'",
        		null, null, null, null);
        return getSetFromCursor(cursor);
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

}
