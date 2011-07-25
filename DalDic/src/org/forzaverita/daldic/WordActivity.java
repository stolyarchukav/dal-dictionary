package org.forzaverita.daldic;

import java.util.Set;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class WordActivity extends Activity {
	
	private static final int MARGIN = 5;
    
	private DalDicService service;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        
        service = (DalDicService) getApplicationContext();
        
        String word = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	word = (String) extras.get(Constants.WORD);
        }
        else {
        	word = service.getCurrentWord();
        }
        
        LinearLayout parent = (LinearLayout) findViewById(R.id.word);
        Set<String> descriptions = service.getDescriptions(word);
        for (String desc : descriptions) {
        	TextView text = new TextView(this);
        	text.setText(desc);
        	text.setTypeface(service.getFont());
        	text.setTextColor(Color.BLACK);
        	text.setTextSize(25);
        	LayoutParams params = new LayoutParams();
        	params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        	text.setLayoutParams(params);
        	parent.addView(text);
        }
        
    }
}