package org.forzaverita.daldic;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class WordListActivity extends Activity {
	
	private static final int MARGIN = 5;
    
	private DalDicService service;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        
        service = (DalDicService) getApplicationContext();
        
        Set<String> words = null;
        
        Character letter = (Character) getIntent().getExtras().get(Constants.LETTER);
        if (letter != null) {
        	words = service.getWordsBeginWith(letter);
        }
        else {
        	String begin = (String) getIntent().getExtras().get(Constants.BEGIN);
        	words = service.getWordsBeginWith(begin);
        	if (words != null && words.size() == 1) {
        		startWordActivity(words.iterator().next());
        	}
        }
        
        LinearLayout parent = (LinearLayout) findViewById(R.id.wordlist);
        if (words != null && ! words.isEmpty()) {
        	for (String word : words) {
            	Button button = new Button(this);
            	button.setText(word);
            	button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            	button.setTag(word);
            	button.setTypeface(service.getFont(), Typeface.ITALIC);
            	button.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View view) {
    					startWordActivity((String) view.getTag());
    				}
    			});
            	LayoutParams params = new LayoutParams();
            	params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
            	button.setLayoutParams(params);
            	parent.addView(button);
            }
        }
        else {
        	TextView textView = new TextView(this);
        	textView.setText(getString(R.string.word_not_found));
        	textView.setTypeface(service.getFont());
        	textView.setTextColor(Color.BLACK);
        	textView.setTextSize(30);
        	parent.addView(textView);
        }
    }
	
	private void startWordActivity(String word) {
		Intent intent = new Intent(this, WordActivity.class);
		intent.putExtra(Constants.WORD, word);
		startActivity(intent);
	}
	
}