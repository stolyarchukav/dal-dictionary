package org.forzaverita.donate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForzaVeritaDonateActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button appsBtn = (Button) findViewById(R.id.appsButton);
        appsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse("market://search?q=pub:ForzaVerita")));
				}
				catch (ActivityNotFoundException e) {
				}
			}
		});
        
    }
}