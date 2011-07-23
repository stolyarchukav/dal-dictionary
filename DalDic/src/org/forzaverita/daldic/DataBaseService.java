package org.forzaverita.daldic;

import java.io.FileOutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseService {
	
	private static int DATA_VERSION = 2;
	private static String ASSET_FILE = "daldic.jet";
	private static String DB_NAME = "daldic.sqllite";
	private static String WORD = "word";
	private static String DESCRIPTION = "description";
	private static String FIRST_LETTER = "first_letter";
	private static String DALDIC_METADATA = "daldic_metadata";
	private static String DATA_VERSION_FIELD = "data_version";
    private SQLiteDatabase database;
    private Context context;
	
	public DataBaseService(Context context) {
        this.context = context;
    }
	
	private void openDataBase() {
		if (database == null) {
			boolean dbExist = tryOpenDataBase();
	        if (! dbExist || ! isCorrectVersion()) {
	        	close();
	        	copyDataBase();
	        	tryOpenDataBase();
	        }
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
    
    private void copyDataBase() {
    	try {
    		ZipInputStream zis = new ZipInputStream(context.getAssets().open(ASSET_FILE));
    		FileOutputStream os = context.openFileOutput(DB_NAME, Context.MODE_PRIVATE);
    		if (zis.getNextEntry() != null) {
    			byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) > 0){
                	os.write(buffer, 0, length);
                }
                os.close();
    		}
            zis.close();
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
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD}, WORD + " like '" + begin + "%'",
        		null, null, null, WORD + " ASC");
        return getSetFromCursor(cursor);
    }
    
    public Set<String> getWordsBeginWith(char letter) {
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD}, FIRST_LETTER + " = '" + letter + "'",
        		null, null, null, WORD + " ASC");
        return getSetFromCursor(cursor);
    }

	public Set<String> getDescriptions(String word) {
		openDataBase();
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
