package org.forzadroid.attentiontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DigitalSequenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dig_seq);
		
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
	}
	
	private void startDigSquare(int size) {
		Intent intent = new Intent(DigitalSequenceActivity.this, DigitalSquare.class);
		intent.putExtra(Constants.DIG_SQUARE_SIZE, size);
		startActivity(intent);
	}

}
