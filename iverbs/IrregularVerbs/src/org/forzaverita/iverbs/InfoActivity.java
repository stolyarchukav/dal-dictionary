package org.forzaverita.iverbs;

import org.forzaverita.iverbs.data.Constants;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class InfoActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        setActivityTitle();
        
        WebView text = (WebView) findViewById(R.id.info_usage);
        text.loadData(getString(R.string.info_usage), "text/html", "utf-8");
    }
    
    public void onClickMoreApps(View view) {
    	try {
			startActivity(new Intent(Intent.ACTION_VIEW, 
					Uri.parse("market://search?q=pub:ForzaVerita")));
		}
		catch (ActivityNotFoundException e) {
			Log.w(Constants.LOG_TAG, "Can't open market forzaverita apps");
		}
	}
    
    public void onClickRateApp(View view) {
    	try {
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + 
					getApplicationInfo().packageName)));
		}
		catch (ActivityNotFoundException e) {
			Log.w(Constants.LOG_TAG, "Can't open market app page");
		}
	}

    
}
