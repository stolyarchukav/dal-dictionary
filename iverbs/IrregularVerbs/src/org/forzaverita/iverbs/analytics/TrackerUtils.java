package org.forzaverita.iverbs.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import org.forzaverita.iverbs.data.Constants;

public class TrackerUtils {

    public static void track(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setDryRun(true);
        analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        Tracker tracker = analytics.newTracker(Constants.ANALYTICS_ID);
        tracker.enableAutoActivityTracking(true);
        tracker.enableExceptionReporting(true);
    }

}
