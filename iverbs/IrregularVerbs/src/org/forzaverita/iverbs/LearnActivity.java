package org.forzaverita.iverbs;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import org.forzaverita.iverbs.data.Verb;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class LearnActivity extends BaseActivity {

    private static final String VERB_ID = "verb_id";
    
    private float x;
    private float y;
    
    private String form1;
    private String form2;
    private String form3;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn);
        setActivityTitle();
        Bundle extras = getIntent().getExtras();
        int id = 0;
        if (extras != null) {
        	id = extras.getInt(VERB_ID);
        }
        Verb verb = service.getVerb(id);
        showVerb(verb);
    }
    
    public void onClickPrevious(View view) {
    	showPrevious();
	}
    
    public void onClickNext(View view) {
    	showNext();
	}

    private void showPrevious() {
		showVerb(service.getPreviousVerb());
	}
    
	private void showNext() {
		showVerb(service.getNextVerb());
	}

    private void showVerb(Verb verb) {
    	if (verb != null) {
        	TextView text = (TextView) findViewById(R.id.learn_form1);
        	form1 = verb.getForm1();
        	text.setText(form1);
        	text = (TextView) findViewById(R.id.learn_form1_transcription);
        	text.setText(verb.getForm1Transcription());
        	
        	text = (TextView) findViewById(R.id.learn_form2);
        	form2 = verb.getForm2();
        	text.setText(form2);
        	text = (TextView) findViewById(R.id.learn_form2_transcription);
        	text.setText(verb.getForm2Transcription());
        	
        	text = (TextView) findViewById(R.id.learn_form3);
        	form3 = verb.getForm3();
        	text.setText(form3);
        	text = (TextView) findViewById(R.id.learn_form3_transcription);
        	text.setText(verb.getForm3Transcription());
        	
        	text = (TextView) findViewById(R.id.learn_translation);
        	text.setText(verb.getTranslation());
        }
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
    	case MotionEvent.ACTION_DOWN :
    		takeCoords(event);
			break;
		case MotionEvent.ACTION_MOVE :
			float dx = event.getX() - x;
			float dy = event.getY() - y;
			float max = max(dx, dy);
			float min = abs(min(dx, dy));
			View decorView = getWindow().getDecorView();
			int moveTarget = min(decorView.getRight(), decorView.getBottom()) / 2;
			if (max >= min && max > moveTarget) {
				showNext();
				takeCoords(event);
			}
			else if (max < min && min > moveTarget) {
				showPrevious();
				takeCoords(event);
			}
			break;
		default:
			break;
		}
    	return true;
    }

	private void takeCoords(MotionEvent event) {
		x = event.getX();
		y = event.getY();
	}
	
	public void speakForm1(View view) {
		speak(form1);
	}
	
	public void speakForm2(View view) {
		speak(form2);
	}
	
	public void speakForm3(View view) {
		speak(form3);
	}
    
}
