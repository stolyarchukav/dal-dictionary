package org.forzadroid.attentiontest;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
        
        //Advertising
        AdView adView = new AdView(this, AdSize.BANNER, Constants.AD_MOB_ID);
        LinearLayout layout = (LinearLayout)findViewById(R.id.main_layout);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest();
        adView.loadAd(adRequest);
    }
}