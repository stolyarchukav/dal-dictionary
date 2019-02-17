package org.forzaverita.daldic.service;

public interface DatabaseDeployer {

	String getDatabasePath();

	void reinstallDatabase();

}
