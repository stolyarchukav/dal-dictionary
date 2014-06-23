package org.forzaverita.brefdic.service;

public interface DatabaseDeployer {

	String getDatabasePath();

	void reinstallDatabase();

}
