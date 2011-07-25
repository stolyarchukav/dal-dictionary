package org.forzaverita.daldic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DataBaseService {
	
	private static int DATA_VERSION = 2;
	private static int WORDS_COUNT = 44652;
	private static String ASSET_FILE = "daldic.jet";
	private static String SD_PATH = "Android/data/org.forzaverita.daldic/";
	private static String DB_NAME = "daldic.sqllite";
	private static String WORD_ID = "word_id";
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
	        	clearOldAppFiles();
	        	copyDataBase();
	        	tryOpenDataBase();
	        }
		}
    }

    private boolean tryOpenDataBase() {
    	try {
    		String path = getDatabaseFile().getPath();
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
    		FileOutputStream os = new FileOutputStream(getDatabaseFile());
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
    
    private void clearOldAppFiles() {
    	try {
    		context.deleteFile(DB_NAME);
    	}
    	catch (Exception e) {
			// Nothing to do
		}
    }
    
    private File getDatabaseFile() throws IOException {
    	File extDir = Environment.getExternalStorageDirectory();
    	File sdPath = new File(extDir.getPath(), SD_PATH);
    	if (! sdPath.exists()) {
    		boolean s = sdPath.mkdirs();
    		System.out.println(s);
    	}
    	File dbFile = new File(sdPath, DB_NAME);
    	if (! dbFile.exists()) {
    		dbFile.createNewFile();
    	}
    	return dbFile;
    }
 
    public void close() {
        if (database != null) {
        	database.close();
        }
    }
    
    public Set<String> getWordsBeginWith(String begin) {
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD}, WORD + " like '" + begin + "%'",
        		null, null, null, WORD + " asc");
        return getSetFromCursor(cursor);
    }
    
    public Set<String> getWordsBeginWith(char letter) {
    	openDataBase();
    	Cursor cursor = database.query(WORD, new String[] {WORD}, FIRST_LETTER + " = '" + letter + "'",
        		null, null, null, WORD + " asc");
        return getSetFromCursor(cursor);
    }

	public Set<String> getDescriptions(String word) {
		openDataBase();
		Cursor cursor = database.query(WORD, new String[] {DESCRIPTION}, WORD + " = '" + word + "'",
        		null, null, null, null);
        return getSetFromCursor(cursor);
	}
	
	public String[] getWordAndDescriptionById(long id) {
		openDataBase();
		Cursor cursor = database.query(WORD, new String[] {WORD, DESCRIPTION}, WORD_ID + " = " + id + "",
        		null, null, null, null);
		String[] result = null;
		if (cursor.moveToFirst()) {
			result = new String[] {cursor.getString(0), cursor.getString(1)};
		}
		cursor.close();
		return result;
	}
	
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
	
}
