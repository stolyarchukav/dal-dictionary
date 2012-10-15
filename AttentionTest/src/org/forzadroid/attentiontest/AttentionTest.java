package org.forzadroid.attentiontest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AttentionTest extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button digSeqButton = (Button) findViewById(R.id.digSeqButton);
        digSeqButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AttentionTest.this, DigitalSequenceActivity.class);
				startActivity(intent);
			}
		});
        
        Button recordsButton = (Button) findViewById(R.id.recordsButton);
        recordsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AttentionTest.this, RecordsActivity.class);
				startActivity(intent);
			}
		});
        
        Button moreAppsBtn = (Button) findViewById(R.id.moreAppsButton);
        moreAppsBtn.setTextColor(Color.GREEN);
        moreAppsBtn.setOnClickListener(new View.OnClickListener() {
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