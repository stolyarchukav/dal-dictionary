package org.forzaverita.daldic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.Date;

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
        setTitle(R.string.browse);
        setContentView(R.layout.alphabet);
        
        service = (DalDicService) getApplicationContext();

        ScrollView parent = findViewById(R.id.alphabet);

        final TableLayout layout = new TableLayout(this) {
        	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        		// Check child count to avoid duplicate buttons
				if  (getChildCount() > 0) {
					return;
				}
				int width = MeasureSpec.getSize(widthMeasureSpec);
				int columns = width / 250;
			    TableRow row = null;
			    int w = 0;
			    for (char letter = 'А'; letter <= 'Я'; letter++) {
                	if (w++ % columns == 0) {
                		row = raw();
                        addView(row);
                	}
					if (row != null) {
						row.addView(letterButton(letter));
					}
                }
			    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        	}

			private TableRow raw() {
				return new TableRow(AlphabetActivity.this);
			}

			private Button letterButton(char letter) {
				final Button button = new Button(AlphabetActivity.this);
				button.setBackgroundResource(R.drawable.selector_dashboard_button);
				button.setText(String.valueOf(letter));
				button.setTag(letter);
				TableRow.LayoutParams params = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
				button.setLayoutParams(params);
				button.setTypeface(service.getFont(), Typeface.ITALIC);
				button.setTextSize(20);
				button.setTextColor(getResources().getColor(R.color.black));
				button.setOnClickListener(view -> startWordListActivity((Character) view.getTag()));
				return button;
			}
		};
        parent.addView(layout);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return MenuUtils.createOptionsMenu(menu, this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtils.optionsItemSelected(item, this);
	}
	
	private void startWordListActivity(char letter) {
		Intent intent = new Intent(this, WordListActivity.class);
		intent.putExtra(Constants.SEARCH_LETTER, letter);
		startActivity(intent);
	}
	
}
