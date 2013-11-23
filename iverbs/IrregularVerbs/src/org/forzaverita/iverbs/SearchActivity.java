package org.forzaverita.iverbs;

import java.util.ArrayList;
import java.util.List;

import org.forzaverita.iverbs.data.Constants;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.learn.LearnActivity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends BaseActivity {

	private static final int MARGIN = 5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
    	
        new AsyncTask<Void, Void, List<Verb>>() {

        	private ProgressDialog dialog;
        	private String query;
        	
        	@Override
        	protected void onPreExecute() {
        		dialog = ProgressDialog.show(SearchActivity.this, 
        				getString(R.string.progress_title), getString(R.string.progress_text));
        	}
        	
        	@Override
			protected List<Verb> doInBackground(Void... params) {
        		Intent intent = SearchActivity.this.getIntent();
        		List<Verb> verbs = new ArrayList<Verb>();
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                	String query = intent.getStringExtra(SearchManager.QUERY);
                	this.query = query;
                	verbs = service.searchVerbs(query);
                }
                else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                	String idStr = intent.getData().getLastPathSegment();
                	if (idStr != null) {
                		Integer id = Integer.parseInt(idStr);
                		verbs.add(service.getVerb(id));
                		startLearnActivity(id);
                	}
                }
				return verbs;
			}
			
        	@Override
        	protected void onPostExecute(List<Verb> verbs) {
        		dialog.dismiss();
        		TextView notFound = (TextView) SearchActivity.this.findViewById(R.id.verb_not_found);
        		ListView list = (ListView) findViewById(R.id.search_list);
        		if (verbs != null && ! verbs.isEmpty()) {
        			list.setAdapter(new ArrayAdapter<Verb>(SearchActivity.this, R.layout.search_item, verbs) {
        				@Override
        				public View getView(int position, View convertView, ViewGroup parent) {
        					View row = (View) getLayoutInflater().inflate(R.layout.search_item, null);
        		        	TextView text = (TextView) row.findViewById(R.id.search_item_text);
        		        	final Verb verb = getItem(position);
        		        	text.setText(verb.getForm1() + " / " + verb.getForm2() + " / " + 
        		        	verb.getForm3() + " - " + verb.getTranslation());
        		        	row.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									startLearnActivity(verb.getId());
								}
							});
        		        	row.setPadding(MARGIN, MARGIN, MARGIN, MARGIN);
        		            return row;
        				};
        			});
        			notFound.setVisibility(View.GONE);
        		}
		        else {
		        	notFound.setText(getString(R.string.search_not_found) +
		        			(query != null ? (": " + query) : ""));
		        	notFound.setVisibility(View.VISIBLE);
		        }
        	}
        	
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_search).setVisible(false);
        return true;
    }

	private void startLearnActivity(int id) {
		Intent intent = new Intent(this, LearnActivity.class);
		intent.putExtra(Constants.VERB_ID, id);
		startActivity(intent);
	}
    
}
