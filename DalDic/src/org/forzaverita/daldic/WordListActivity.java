package org.forzaverita.daldic;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class WordListActivity extends ListActivity {
	
	private static final int MARGIN = 5;
    
	private DalDicService service;
	private LayoutInflater inflater;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
        
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout parent = (LinearLayout) findViewById(R.id.wordlist);
        
        new AsyncTask<Void, Void, Map<Integer, String>>() {
        	
        	ProgressDialog dialog;
        	
        	@Override
        	protected void onPreExecute() {
        		dialog = ProgressDialog.show(WordListActivity.this, 
        				getString(R.string.progress_title), getString(R.string.progress_text));
        	}
        	
        	@Override
        	protected Map<Integer, String> doInBackground(Void... paramArrayOfParams) {
        		service = (DalDicService) getApplicationContext();
        		Map<Integer, String> words = null;
                Character letter = (Character) getIntent().getExtras().get(Constants.LETTER);
                if (letter != null) {
                	words = service.getWordsBeginWith(letter);
                }
                else {
                	String begin = (String) getIntent().getExtras().get(Constants.BEGIN);
                	words = service.getWordsBeginWith(begin);
                }
                if (words.size() == 1) {
            		startWordActivity(words.entrySet().iterator().next().getKey());
            	}
                return words;
        	}
        	
        	@Override
        	protected void onPostExecute(Map<Integer, String> words) {
        		dialog.dismiss();
        		if (words != null && ! words.isEmpty()) {
        			ArrayList<Entry<Integer, String>> wordList = new ArrayList<Entry<Integer, String>>(
        					words.entrySet());
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
                            tv.setText(getItem(position).getValue());
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
                }
                else {
                	TextView textView = new TextView(WordListActivity.this);
                	textView.setText(getString(R.string.word_not_found));
                	textView.setTypeface(service.getFont());
                	textView.setTextColor(Color.BLACK);
                	textView.setTextSize(30);
                	LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
                	params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                	textView.setLayoutParams(params);
                	parent.addView(textView);
                }
        	}
		}.execute();
    }
	
	private void startWordActivity(Integer wordId) {
		Intent intent = new Intent(this, WordActivity.class);
		intent.putExtra(Constants.WORD_ID, wordId);
		startActivity(intent);
	}
	
}