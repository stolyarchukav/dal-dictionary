package org.forzaverita.trackoftrek.service.impl;

import android.app.Application;

import org.forzaverita.trackoftrek.data.Punto;
import org.forzaverita.trackoftrek.database.impl.SqliteDatabase;
import org.forzaverita.trackoftrek.service.AppService;

public class AppServiceImpl extends Application implements AppService {

    private SqliteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new SqliteDatabase(this);
        database.open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        database.close();
    }

    @Override
    public void storeLocation(Punto location) {
        database.storeLocation(location);
    }

}
