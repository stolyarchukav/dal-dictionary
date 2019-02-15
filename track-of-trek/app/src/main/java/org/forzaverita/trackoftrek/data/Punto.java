package org.forzaverita.trackoftrek.data;

import java.util.Date;

public class Punto {

    private final Date date;

    private final double latitude;

    private final double longitude;

    public Punto(Date date, double latitude, double longitude) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* Getter and Setter */

    public Date getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
