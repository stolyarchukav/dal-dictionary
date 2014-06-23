package org.forzaverita.brefdic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.forzaverita.brefdic.data.Constants;
import org.forzaverita.brefdic.data.SearchType;
import org.forzaverita.brefdic.menu.MenuUtils;
import org.forzaverita.brefdic.service.AppService;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordListActivity extends ListActivity {
	
private static final int MARGIN = 5;
    
	private AppService service;
	private LayoutInflater inflater;
	private LinearLayout parent;
	private Date lastPreferencesCheck = new Date();
	
	private class SearchTask extends AsyncTask<Void, Void, Map<Integer, String>> {
    	
    	ProgressDialog dialog;
    	String queryString;
    	SearchType searchType;
    	
    	@Override
    	protected void onPreExecute() {
    		dialog = ProgressDialog.show(WordListActivity.this, 
    				getString(R.string.progress_title), getString(R.string.progress_text));
    	}
    	
    	@Override
    	protected Map<Integer, String> doInBackground(Void... paramArrayOfParams) {
    		Map<Integer, String> words = null;
    		Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            	String query = intent.getStringExtra(SearchManager.QUERY);
            	words = service.getWordsBeginWith(query);
            	queryString = query;
            	searchType = SearchType.BEGIN;
            }
            else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            	String idStr = intent.getData().getLastPathSegment();
            	if (idStr != null) {
            		Integer id = Integer.parseInt(idStr);
            		words = new HashMap<Integer, String>();
            		words.put(id, intent.getExtras().getString(SearchManager.EXTRA_DATA_KEY));
            		startWordActivity(id);
            	}
            }
            else {
            	Character letter = (Character) intent.getExtras().get(Constants.SEARCH_LETTER);
                if (letter != null) {
                	words = service.getWordsBeginWith(letter);
                	queryString = String.valueOf(letter);
                	searchType = SearchType.FIRST_LETTER;
                }
                else {
                	String query = (String) intent.getExtras().get(Constants.SEARCH_QUERY_FULL);
                	words = service.getWordsFullSearch(query);
                	queryString = query;
                	searchType = SearchType.FULL;
                }
            }
            return words;
    	}
    	
    	@Override
    	protected void onPostExecute(Map<Integer, String> words) {
    		dialog.dismiss();
    		TextView textView = (TextView) parent.findViewById(R.id.word_not_found);
    		if (words != null && ! words.isEmpty()) {
    			ArrayList<Entry<Integer, String>> wordList = new ArrayList<Entry<Integer, String>>(
    					words.entrySet());
    			Collections.sort(wordList, new Comparator<Entry<Integer, String>>() {
    				@Override
    				public int compare(Entry<Integer, String> object1,
    						Entry<Integer, String> object2) {
    					return object1.getValue().compareTo(object2.getValue());
    				}
				});
    			setListAdapter(new ArrayAdapter<Entry<Integer, String>>(WordListActivity.this, R.layout.wordlist_item, wordList) {
                	@Override
                	public View getView(int position, View convertView, ViewGroup parent) {
                		View row;
                        if (convertView == null) {
                        	row =  inflater.inflate(R.layout.wordlist_item, null);
                        }
                        else {
                        	row = convertView;
                        }
                        
                        TextView tv = (TextView) row.findViewById(android.R.id.text1);
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
            	textView.setText(getString(R.string.word_not_found) + ": " + queryString);
            	textView.setTypeface(service.getFont());
            	textView.setVisibility(View.VISIBLE);
            }
    		configureSearchFullButton();
    	}

		private void configureSearchFullButton() {
			Button searchFull = (Button) parent.findViewById(R.id.search_full);
			if (searchType == SearchType.BEGIN && queryString.length() > 2) {
				searchFull.setTypeface(service.getFont());
	    		searchFull.setVisibility(View.VISIBLE);
	    		searchFull.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View paramView) {
						Intent intent = new Intent(WordListActivity.this, WordListActivity.class);
						intent.putExtra(Constants.SEARCH_QUERY_FULL, queryString);
						startActivity(intent);
					}
				});
			}
			else {
				searchFull.setVisibility(View.GONE);
			}
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        service = (AppService) getApplicationContext();
        
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parent = (LinearLayout) findViewById(R.id.wordlist);
        
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
	
}