package org.forzaverita.iverbs;

import static java.lang.Math.abs;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Verb;

import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class LearnActivity extends BaseActivity {

    private float x;
    private float y;
    
    private String form1;
    private String form2;
    private String form3;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn);
        Bundle extras = getIntent().getExtras();
        int id = 0;
        if (extras != null) {
        	id = extras.getInt(Constants.VERB_ID);
        }
        Verb verb = service.getVerb(id);
        showVerb(verb);
        
        ScrollView view = (ScrollView) findViewById(R.id.learn_scroll_view);
        view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
		    	case MotionEvent.ACTION_DOWN :
		    		takeCoords(event);
					break;
				case MotionEvent.ACTION_MOVE :
					float dx = event.getX() - x;
					float dyAbs = abs(event.getY() - y);
					View decorView = getWindow().getDecorView();
					int moveTarget = decorView.getRight() / 2;
					System.out.println(dx + "," + dyAbs + "," + moveTarget);
					if (abs(dx) > dyAbs && abs(dx) >= moveTarget) {
						if (dx > 0) {
							showNext();
						}
						else {
							showPrevious();
						}
						System.out.println("123");
						takeCoords(event);
						return true;
					}
					break;
				default:
					break;
				}
		    	return false;
			}
		});
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
    		float fontSize = service.getFontSize();
    		
        	TextView text = (TextView) findViewById(R.id.learn_form1);
        	form1 = verb.getForm1();
        	text.setText(form1);
        	text.setTextSize(fontSize);
        	text = (TextView) findViewById(R.id.learn_form1_transcription);
        	text.setText(verb.getForm1Transcription());
        	text.setTextSize(fontSize);
        	
        	text = (TextView) findViewById(R.id.learn_form2);
        	form2 = verb.getForm2();
        	text.setText(form2);
        	text.setTextSize(fontSize);
        	text = (TextView) findViewById(R.id.learn_form2_transcription);
        	text.setText(verb.getForm2Transcription());
        	text.setTextSize(fontSize);
        	
        	text = (TextView) findViewById(R.id.learn_form3);
        	form3 = verb.getForm3();
        	text.setText(form3);
        	text.setTextSize(fontSize);
        	text = (TextView) findViewById(R.id.learn_form3_transcription);
        	text.setText(verb.getForm3Transcription());
        	text.setTextSize(fontSize);
        	
        	text = (TextView) findViewById(R.id.learn_translation);
        	text.setText(verb.getTranslation());
        	text.setTextSize(fontSize);
        }
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
