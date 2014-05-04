package org.forzaverita.iverbs;

import org.forzaverita.iverbs.data.Constants;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;

public class InfoActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        
        WebView text = (WebView) findViewById(R.id.info_usage);
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        text.loadData(header + getString(R.string.info_usage), "text/html; charset=UTF-8", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_info).setVisible(false);
        return true;
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
        
}
