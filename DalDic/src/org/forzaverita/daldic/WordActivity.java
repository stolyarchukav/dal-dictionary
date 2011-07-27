package org.forzaverita.daldic;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        
        boolean fromWidget = false;
        Set<String> descriptions = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Integer wordId = (Integer) extras.get(Constants.WORD_ID);
        	descriptions = service.getDescriptions(wordId);
        }
        else {
        	String[] wordAndDesc = service.getCurrentWord();
        	descriptions = new HashSet<String>();
        	descriptions.add(wordAndDesc[1]);
        	fromWidget = true;
        }
        
        LinearLayout parent = (LinearLayout) findViewById(R.id.word);
        
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
        
        if (fromWidget) {
        	Button button = new Button(this);
        	button.setText(getString(R.string.goto_menu));
        	button.setTypeface(service.getFont());
        	button.setTextColor(Color.BLACK);
        	button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WordActivity.this, DalDicActivity.class);
					startActivity(intent);
				}
			});
        	LayoutParams params = new LayoutParams();
        	params.setMargins(MARGIN, MARGIN * 3, MARGIN, MARGIN);
        	button.setLayoutParams(params);
        	parent.addView(button);
        }
        
    }
}