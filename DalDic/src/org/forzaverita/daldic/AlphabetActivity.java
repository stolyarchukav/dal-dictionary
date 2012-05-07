package org.forzaverita.daldic;

import java.util.Date;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.history.HistoryActivity;
import org.forzaverita.daldic.preferences.AppPreferenceActivity;
import org.forzaverita.daldic.service.DalDicService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AlphabetActivity extends Activity {
    
	private static final int MARGIN = 5;
	private DalDicService service;
	private Date lastPreferencesCheck = new Date();

	@Override
	protected void onResume() {
		super.onResume();
		if (service.isPreferencesChanged(lastPreferencesCheck)) {
			lastPreferencesCheck = new Date();
			onCreate(null);
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet);
        
        service = (DalDicService) getApplicationContext();
        
        ScrollView parent = (ScrollView) findViewById(R.id.alphabet);
        
        final TableLayout layout = new TableLayout(this) {
        	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				
        		// Check child count to avoid duplicate buttons
				if  (getChildCount() > 0) {
					return;
				}
				
				int width = MeasureSpec.getSize(widthMeasureSpec);
				int colums = width / 100;
			    TableRow row = null;
			    int w = 0;
			    for (char q = 'А'; q <= 'Я'; q++) {
                	if (w++ % colums == 0) {
                		row = new TableRow(AlphabetActivity.this);
                        addView(row);
                	}
                	final Button button = new Button(AlphabetActivity.this);
                	button.setText("" + q);
                	button.setTag(q);
                	button.setWidth(width / colums - MARGIN * 2); 
                	TableRow.LayoutParams params = new TableRow.LayoutParams(
                			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
                	params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                	button.setLayoutParams(params);
                	button.setTypeface(service.getFont(), Typeface.ITALIC);
                	button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startWordListActivity((Character) view.getTag());
						}
					});
                	row.addView(button);
                }
			    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        	}
        };
        parent.addView(layout);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, AppPreferenceActivity.class));
			return true;
		case R.id.menu_seacrh:
			onSearchRequested();
			return true;
		case R.id.menu_history:
			startActivity(new Intent(this, HistoryActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void startWordListActivity(char letter) {
		Intent intent = new Intent(this, WordListActivity.class);
		intent.putExtra(Constants.SEARCH_LETTER, letter);
		startActivity(intent);
	}
	
}