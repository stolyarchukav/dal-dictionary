package org.forzadroid.attentiontest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.forzadroid.attentiontest.advert.AdUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DigitalSquareActivity extends Activity {

	private static final int MARGIN = 2;
	private AttentionTestApplication appState;
	private int size;
    private AsyncTask<Void, String, Void> titleTimerTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dig_square);
		final FrameLayout parent = (FrameLayout) findViewById(R.id.digSquare);
	
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		size = (Integer) getIntent().getExtras().get(Constants.DIG_SQUARE_SIZE);
	    final int count = size * size;

        appState = (AttentionTestApplication) getApplicationContext();
	    final boolean reverse = appState.isReverse();
        final List<Integer> values = appState.getValues(size);
        final AtomicInteger next = appState.getNext();
	    final Set<Button> buttons = new HashSet<Button>(); 
	    
	    TableLayout layout = new TableLayout(this) {
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				int width = MeasureSpec.getSize(widthMeasureSpec);
			    int height = MeasureSpec.getSize(heightMeasureSpec);
			    for (Button button : buttons) {
			    	button.setWidth(width / size - MARGIN * 2);
	        		button.setHeight(height / size - MARGIN * 2);
			    }
			    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};
		
		Iterator<Integer> iterator = values.iterator();
	    
	    for (int q = 0; q < size; q++) {
        	TableRow tableRow = new TableRow(this);
        	tableRow.setId(q);
        	tableRow.setBaselineAligned(false);
        	tableRow.setLayoutParams(new ViewGroup.LayoutParams(
        			ViewGroup.LayoutParams.FILL_PARENT,
        			ViewGroup.LayoutParams.FILL_PARENT));

        	for (int w = 0; w < size; w++) {
        		final Button button = new Button(this);
        		buttons.add(button);
        		int number = iterator.next();
                button.setText(String.valueOf(number));
                button.setTag(number);
                button.setPadding(0, 0, 0, 0);
                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                		ViewGroup.LayoutParams.FILL_PARENT,
                		ViewGroup.LayoutParams.FILL_PARENT);
                buttonParams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                button.setLayoutParams(buttonParams);
                button.setTextSize(appState.getFontSize(size));
                button.setTextColor(appState.getFontColor());
                
                updateButtonStatus(button, number, next.get(), reverse);
                button.setOnClickListener(new View.OnClickListener() {
                	public void onClick(View view) {
                		if (button.getTag().equals(next.get())) {
                			updateButtonStatus(button, true);
                            if (reverse) {
                                if (next.decrementAndGet() == 0) {
                                    finishGame();
                                }
                            } else {
                                if (next.incrementAndGet() > count) {
                                    finishGame();
                                }
                            }
                		}
                		else {
                			vibrator.vibrate(200);
                		}
                	}
                });
                tableRow.addView(button);
        	}
        	layout.addView(tableRow, new TableLayout.LayoutParams(
        			ViewGroup.LayoutParams.FILL_PARENT,
        			ViewGroup.LayoutParams.FILL_PARENT));
        }
		
	    parent.addView(layout);
	    
	    titleTimerTask = new TitleTimerTask();
	    titleTimerTask.execute(new Void[0]);
	    
		appState.startDigitTest();
	}

    private void updateButtonStatus(Button button, boolean passed) {
        button.setEnabled(! passed);
        if (passed) {
            button.setTextColor(Color.BLACK);
        }
        button.setBackgroundColor(passed ? Color.GRAY : Color.rgb(105, 214, 241));
    }

    private void updateButtonStatus(Button button, int number, int next, boolean reverse) {
        boolean passed = reverse ? number > next : number < next;
		updateButtonStatus(button, passed);
	}
	
	private void finishGame() {
        AdUtils.loadAd(this);
		String resultString = appState.finishDigitTest(size);
		titleTimerTask.cancel(false);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(resultString)
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.dig_square_start_again), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               Intent intent = getIntent();
		               finish();
		               startActivity(intent);
		           }
		       })
		       .setNegativeButton(getString(R.string.dig_square_finish), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		
		appState.clearDigSequence();
	}
	
	@Override
	protected void onDestroy() {
		super.onStop();
		titleTimerTask.cancel(true);
	}
	
	private final class TitleTimerTask extends AsyncTask<Void, String, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			while (! isCancelled()) {
				publishProgress(appState.getDigitalSquareTitle());
				SystemClock.sleep(100);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... params) {
			setTitle(params[0]);
		}
	}
	
}
