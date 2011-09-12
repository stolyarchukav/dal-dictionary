package org.forzaverita.daldic;

import org.forzaverita.daldic.service.Constants;
import org.forzaverita.daldic.service.DalDicService;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class WordActivity extends AbstractActivity {
	
	private static final int MARGIN = 5;
    
	private DalDicService service;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        
        service = (DalDicService) getApplicationContext();
        
        boolean fromWidget = false;
        String description = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	Integer wordId = (Integer) extras.get(Constants.WORD_ID);
        	description = service.getDescription(wordId);
        }
        else {
        	String[] wordAndDesc = service.getCurrentWord();
        	description = wordAndDesc[1];
        	fromWidget = true;
        }
        
        LinearLayout parent = (LinearLayout) findViewById(R.id.word);
        
        if (description != null) {
        	TextView text = new TextView(this);
        	text.setText(description);
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