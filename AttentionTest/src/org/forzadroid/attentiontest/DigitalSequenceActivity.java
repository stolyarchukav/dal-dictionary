package org.forzadroid.attentiontest;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class DigitalSequenceActivity extends Activity {

	private AttentionTestApplication appState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dig_seq);
		
		appState = (AttentionTestApplication) getApplicationContext();
		
		CheckBox var_font_size = (CheckBox) findViewById(R.id.varFontSize);
		var_font_size.setChecked(appState.isVarFontSize());
		var_font_size.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appState.setVarFontSize(isChecked);
			}
		});
		
		CheckBox var_font_color = (CheckBox) findViewById(R.id.varFontColor);
		var_font_color.setChecked(appState.isVarFontColor());
		var_font_color.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appState.setVarFontColor(isChecked);
			}
		});
		
		Button digSeq3 = (Button) findViewById(R.id.digSeq3);
		digSeq3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startDigSquare(3);
			}
		});
		
		Button digSeq4 = (Button) findViewById(R.id.digSeq4);
		digSeq4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startDigSquare(4);
			}
		});
		
		Button digSeq5 = (Button) findViewById(R.id.digSeq5);
		digSeq5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startDigSquare(5);
			}
		});
		
		Button digSeq6 = (Button) findViewById(R.id.digSeq6);
		digSeq6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startDigSquare(6);
			}
		});
		
		//Advertising
        AdView adView = new AdView(this, AdSize.BANNER, Constants.AD_MOB_ID);
        LinearLayout layout = (LinearLayout)findViewById(R.id.dig_seq);
        layout.addView(adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        adView.loadAd(adRequest);
	}
	
	private void startDigSquare(int size) {
		appState.clearDigSequence();
		Intent intent = new Intent(DigitalSequenceActivity.this, DigitalSquareActivity.class);
		intent.putExtra(Constants.DIG_SQUARE_SIZE, size);
		startActivity(intent);
	}

}
