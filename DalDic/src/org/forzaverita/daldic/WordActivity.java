package org.forzaverita.daldic;

import java.io.IOException;
import java.io.InputStream;

import org.forzaverita.daldic.service.Constants;
import org.forzaverita.daldic.service.DalDicService;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;

public class WordActivity extends Activity {
	
	private static final String UTF_8 = "UTF-8";
	private static final String FILE_ANDROID_ASSET = "file:///android_asset/";
	private static final String WORD_TEMPLATE_HTML = "word_template.html";
	
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
        
        configureWordView(description);
        
        configureGotoMain(fromWidget);
    }

	private void configureWordView(String description) {
		if (description != null) {
        	WebView text = (WebView) findViewById(R.id.word_text);
        	text.setBackgroundColor(0x00000000);
        	text.getSettings().setDefaultFontSize(25);
        	text.getSettings().setSupportZoom(true);
        	text.getSettings().setBuiltInZoomControls(true);
        	text.getSettings().setDefaultTextEncodingName(UTF_8);
        	text.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        	String data = "";
        	AssetManager assetManager = getApplicationContext().getResources().getAssets();
        	try {
        	    InputStream inputStream = assetManager.open(WORD_TEMPLATE_HTML);
        	    byte[] b = new byte[inputStream.available()];
        	    inputStream.read(b);
        	    data = String.format(new String(b), service.getWordTextAlign(), description);
        	    inputStream.close();
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        	}
        	text.loadDataWithBaseURL(FILE_ANDROID_ASSET + WORD_TEMPLATE_HTML, data,
        			"text/html", UTF_8, "about:blank");
        }
	}

	private void configureGotoMain(boolean fromWidget) {
		Button button = (Button) findViewById(R.id.word_goto_main);
        if (fromWidget) {
        	button.setVisibility(View.VISIBLE);
        	button.setTypeface(service.getFont());
        	button.setTextColor(Color.BLACK);
        	button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WordActivity.this, DalDicActivity.class);
					startActivity(intent);
				}
			});
        }
        else {
        	button.setVisibility(View.GONE);
        }
	}
	
}