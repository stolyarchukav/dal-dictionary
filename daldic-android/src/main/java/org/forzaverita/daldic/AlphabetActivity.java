package org.forzaverita.daldic;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlphabetActivity extends ListActivity {
    
	private static final int MARGIN = 5;
	private DalDicService service;
	private LayoutInflater inflater;
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
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        service = (DalDicService) getApplicationContext();
		List<Character> letters = new ArrayList<>();
		for (char letter = 'А'; letter <= 'Я'; letter++) {
			letters.add(letter);
		}
		setListAdapter(new ArrayAdapter<Character>(AlphabetActivity.this, R.layout.wordlist_item, letters) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row;
				if (convertView == null) {
					row = inflater.inflate(R.layout.wordlist_item, null);
				}
				else {
					row = convertView;
				}

				TextView tv = row.findViewById(android.R.id.text1);
				tv.setText(Html.fromHtml(getItem(position).toString()));
				tv.setTypeface(service.getFont());
				tv.setTextColor(Color.BLACK);

				row.setTag(getItem(position));
				row.setOnClickListener(paramView -> startWordListActivity((Character) paramView.getTag()));
				row.setPadding(MARGIN, MARGIN, MARGIN, MARGIN);
				return row;
			}
		});
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
