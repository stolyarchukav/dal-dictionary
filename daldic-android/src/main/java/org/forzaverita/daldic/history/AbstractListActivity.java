package org.forzaverita.daldic.history;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.WordActivity;
import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractListActivity extends ListActivity {

	private static final int MARGIN = 5;
	
	private DalDicService service;
	private LayoutInflater inflater;
	private LinearLayout parent;
	private Date lastPreferencesCheck = new Date();

	private class SearchTask extends AsyncTask<Void, Void, Map<Integer, String>> {
    	
    	ProgressDialog dialog;
    	
    	@Override
    	protected void onPreExecute() {
    		dialog = ProgressDialog.show(AbstractListActivity.this, 
    				getString(R.string.progress_title), getString(R.string.progress_text));
    	}
    	
    	@Override
    	protected Map<Integer, String> doInBackground(Void... paramArrayOfParams) {
    		return getResultList();
    	}
    	
    	@Override
    	protected void onPostExecute(Map<Integer, String> words) {
    		dialog.dismiss();
    		TextView textView = parent.findViewById(R.id.word_not_found);
    		if (words != null && ! words.isEmpty()) {
    			ArrayList<Entry<Integer, String>> wordList = new ArrayList<>(
						words.entrySet());
    			Collections.reverse(wordList);
    			configureClearButton(wordList.size());
    			setListAdapter(new ArrayAdapter<Entry<Integer, String>>(AbstractListActivity.this, R.layout.wordlist_item, wordList) {
                	@Override
                	public View getView(int position, View convertView, ViewGroup parent) {
                		View row;
                        if (convertView == null) {
                        	row =  inflater.inflate(R.layout.wordlist_item, null);
                        }
                        else {
                        	row = convertView;
                        }
                        
                        TextView tv = row.findViewById(android.R.id.text1);
                        tv.setText(Html.fromHtml(getItem(position).getValue()));
                        tv.setTypeface(service.getFont());
                        tv.setTextColor(Color.BLACK);
                        
                        row.setTag(getItem(position).getKey());
                        row.setOnClickListener(new View.OnClickListener() {
    						@Override
    						public void onClick(View paramView) {
    							startWordActivity((Integer) paramView.getTag());
    						}
    					});
                        row.setPadding(MARGIN, MARGIN, MARGIN, MARGIN);
                        return row;
                	}
                });
    			textView.setVisibility(View.GONE);
            }
            else {
            	textView.setText(getEmptyText());
            	textView.setTypeface(service.getFont());
            	textView.setVisibility(View.VISIBLE);
            }
    		configureSearchFullButton();
    	}

		private void configureSearchFullButton() {
			Button searchFull = parent.findViewById(R.id.search_full);
			searchFull.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (service.isPreferencesChanged(lastPreferencesCheck)) {
			lastPreferencesCheck = new Date();
			onCreate(null);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlist);
		service = (DalDicService) getApplicationContext();
	
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = findViewById(R.id.wordlist);
        
        new SearchTask().execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return MenuUtils.createOptionsMenu(menu, this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtils.optionsItemSelected(item, this);
	}
	
	private void startWordActivity(Integer wordId) {
		Intent intent = new Intent(this, WordActivity.class);
		intent.putExtra(Constants.WORD_ID, wordId);
		startActivity(intent);
	}

	private void configureClearButton(int size) {
		if (size > 0) {
			Button clearAll = findViewById(R.id.clear_all);
			clearAll.setVisibility(View.VISIBLE);
			clearAll.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showClearConfirmation();
				}
			});
		}
	}

	private void showClearConfirmation() {
		new AlertDialog.Builder(this)
				.setMessage(R.string.are_you_sure)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doClear();
						finish();
						startActivity(getIntent());
					}
				})
				.setNegativeButton(R.string.no, null)
				.show();
	}

	protected abstract Map<Integer, String> getResultList();

	protected abstract void doClear();

	protected abstract String getEmptyText();
	
	protected final DalDicService getService() {
		return service;
	}
	
}
