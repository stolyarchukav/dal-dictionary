package org.forzadroid.attentiontest;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class DigitalSquare extends Activity {

	private static final int MARGIN = 2;
	private long startTime;
	private AttentionTestApplication appState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dig_square);
		FrameLayout parent = (FrameLayout) findViewById(R.id.digSquare);
	
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		final int size = (Integer) getIntent().getExtras().get(Constants.DIG_SQUARE_SIZE);
	    final int count = size * size;
	    
	    appState = (AttentionTestApplication) getApplicationContext();
	    final AtomicInteger next = appState.getNext();
	    final List<Integer> values = appState.getValues(size);
		
		TableLayout layout = new TableLayout(this) {
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				int width = MeasureSpec.getSize(widthMeasureSpec);
			    int height = MeasureSpec.getSize(heightMeasureSpec);

			    Iterator<Integer> iterator = values.iterator();
			    
			    for (int q = 0; q < size; q++) {
		        	TableRow tableRow = new TableRow(DigitalSquare.this);
		        	tableRow.setId(q);
		        	tableRow.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));

		        	for (int w = 0; w < size; w++) {
		        		final Button button = new Button(DigitalSquare.this);
		        		button.setWidth(width / size - MARGIN * 2);
		        		button.setHeight(height / size - MARGIN * 2);
		        		int number = iterator.next();
		                button.setText(String.valueOf(number));
		                button.setTag(number);
		                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
		                        LayoutParams.FILL_PARENT,
		                        LayoutParams.FILL_PARENT);
		                buttonParams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
		                button.setLayoutParams(buttonParams);
		                button.setTextSize(120 / size);
		                updateButtonStatus(button, number < next.get());
		                button.setOnClickListener(new View.OnClickListener() {
		                	public void onClick(View view) {
		                		if (button.getTag().equals(next.get())) {
		                			updateButtonStatus(button, true);
		                			if (next.incrementAndGet() > count) {
		                				finishGame();
		                			}
		                		}
		                		else {
		                			vibrator.vibrate(200);
		                		}
		                	}
		                });
		                tableRow.addView(button);
		        	}
		        	
		            addView(tableRow, new TableLayout.LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		        }
			}
			
		};
		parent.addView(layout);
		startTime = appState.getStartTime();
	}
	
	private void updateButtonStatus(Button button, boolean passed) {
		button.setEnabled(! passed);
		button.setBackgroundColor(passed ? Color.GRAY : Color.CYAN);
	}
	
	private void finishGame() {
		appState.clearDigSequence();
		
		float time = System.currentTimeMillis() - startTime;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Congratulations! It takes " + time / 1000 + " sec")
		       .setCancelable(false)
		       .setPositiveButton("Start new", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                Intent intent = getIntent();
		                finish();
		                startActivity(intent);
		           }
		       })
		       .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                finish();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
