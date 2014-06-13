package org.forzaverita.brefdic.advert;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.forzaverita.brefdic.data.Constants;

import static org.forzaverita.brefdic.data.Constants.LOG_TAG;

public class AdUtils {

    private static final long AD_SHOW_INTERVAL_MS = 1000 * 60 * 30;
    private static long lastShow = 0;

    public static void loadAd(Activity activity) {
        if (System.currentTimeMillis() - lastShow > AD_SHOW_INTERVAL_MS) {
            final InterstitialAd interstitialAd = new InterstitialAd(activity);
            interstitialAd.setAdUnitId(Constants.AD_UNIT_ID);

            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d(LOG_TAG, "main ad loaded");
                    interstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.d(LOG_TAG, String.format("ad loading failed (%s)", errorCode));
                }
            });

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("58BCBE6FCFEC155514A263200C0E81B0")
                    .build();

            interstitialAd.loadAd(adRequest);
            lastShow = System.currentTimeMillis();
        }
    }

}
