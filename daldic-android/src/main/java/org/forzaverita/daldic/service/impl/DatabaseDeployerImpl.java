package org.forzaverita.daldic.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.service.DatabaseDeployer;
import org.forzaverita.daldic.service.PreferencesService;

import android.content.Context;
import android.os.Environment;

public class DatabaseDeployerImpl implements DatabaseDeployer {
	
	private static String ASSET_FILE = "daldic.jet";
	private static String SD_DIR = "Android/data/";
	private static String DB_DIR = "org.forzaverita.daldic/";
	private static String DB_NAME = "daldic.sqllite";
	
	private Context context;
	private PreferencesService preferencesService;
	private String databasePath;
	
	public DatabaseDeployerImpl(Context context, PreferencesService preferencesService) {
		this.context = context;
		this.preferencesService = preferencesService;
	}

	@Override
	public String getDatabasePath() {
		if (databasePath == null) {
			databasePath = preferencesService.getDatabasePath();
		}
		return databasePath;
	}
	
	@Override
	public void reinstallDatabase() {
		clearOldAppFiles();
		File file = null;
		try {
			file = getDatabaseFile(true);
			copyDataBase(file);
		}
		catch (Exception e) {
			try {
				file = getDatabaseFile(false);
				copyDataBase(file);
				preferencesService.switchPreferencedStorage();
			}
			catch (Exception e2) {
				throw new DatabaseException(context.getString(R.string.error_db_install));
			}
		}
		databasePath = file.getAbsolutePath();
		preferencesService.setDatabasePath(databasePath);
	}
	
	private void copyDataBase(File file) throws Exception {
		ZipInputStream zis = new ZipInputStream(context.getAssets().open(ASSET_FILE));
		FileOutputStream os = new FileOutputStream(file);
		if (zis.getNextEntry() != null) {
			byte[] buffer = new byte[1024 * 100];
            int length;
            while ((length = zis.read(buffer)) > 0){
            	os.write(buffer, 0, length);
            }
            os.close();
		}
        zis.close();
    }
    
    private void clearOldAppFiles() {
    	try {
    		context.deleteFile(DB_NAME);
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private File getDatabaseFile(boolean preference) throws Exception {
    	boolean internal = preferencesService.isInteranalStorage();
    	if (internal == preference) {
    		return openInternalFile();
    	}
    	else {
    		return openExtrnalFile();
    	}
    }

	private File openInternalFile() throws FileNotFoundException {
		context.openFileOutput(DB_NAME, Context.MODE_PRIVATE);
		return context.getFileStreamPath(DB_NAME);
	}
	
	private File openExtrnalFile() throws Exception {
		File file = null;
		String state = Environment.getExternalStorageState();
    	if (state.equals(Environment.MEDIA_MOUNTED)) {
    		File extDir = Environment.getExternalStorageDirectory();
        	File sdPath = new File(extDir.getPath(), SD_DIR + DB_DIR);
        	if (! sdPath.exists()) {
        		sdPath.mkdirs();
        	}
        	file = new File(sdPath, DB_NAME);
        	if (! file.exists()) {
        		file.createNewFile();
        	}
    	}
    	return file;
	}

}
