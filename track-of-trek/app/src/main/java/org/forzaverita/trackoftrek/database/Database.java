package org.forzaverita.trackoftrek.database;

import org.forzaverita.trackoftrek.data.Punto;

public interface Database {

	void open();

    void storeLocation(Punto location);
	
}
