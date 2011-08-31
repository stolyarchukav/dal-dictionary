package org.forzaverita.daldic.service;

public interface PreferencesService {

	boolean isInteranalStorage();

	void switchPreferencedStorage();

	String getDatabasePath();
	
	void setDatabasePath(String databasePath);

}
