package org.forzaverita.daldic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DalDicActivity extends Activity {
	
	private EditText searchText;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface font = ((DalDicService) getApplicationContext()).getFont();
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(font);
        title.setTextColor(Color.BLACK);
        TextView author = (TextView) findViewById(R.id.author);
        author.setTypeface(font);
        author.setTextColor(Color.BLACK);
 
        Button browseBtn = (Button) findViewById(R.id.browse);
        browseBtn.setTypeface(font);
        browseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBrowseActivity();
			}
		});
        
        searchText = (EditText) findViewById(R.id.text_search);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					startSearchActivity();
				}
				return true;
			}
		});
        
        Button searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setTypeface(font);
        searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSearchActivity();
			}
		});
        
        Button rateBtn = (Button) findViewById(R.id.rate_app);
        rateBtn.setTypeface(font);
        rateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + 
							getApplicationInfo().packageName)));
				}
				catch (ActivityNotFoundException e) {
				}
			}
		});
    }

	private void startBrowseActivity() {
		Intent intent = new Intent(this, AlphabetActivity.class);
		startActivity(intent);
	}
	
	private void startSearchActivity() {
		Intent intent = new Intent(this, WordListActivity.class);
		String text = searchText.getText().toString();
		if (text.length() > 0) {
			intent.putExtra(Constants.BEGIN, text);
			startActivity(intent);
		}
		else {
			Toast toast = Toast.makeText(this, getString(R.string.search_string_empty), 0);
			toast.show();
		}
	}
}