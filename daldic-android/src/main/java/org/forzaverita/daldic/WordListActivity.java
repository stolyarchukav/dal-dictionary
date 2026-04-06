package org.forzaverita.daldic;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.data.SearchType;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WordListActivity extends AppCompatActivity {

	private DalDicService service;
	private Date lastPreferencesCheck = new Date();
	private RecyclerView recyclerView;
	private TextView wordNotFound;
	private Button searchFull;
	private Button clearAll;

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
            		words = new HashMap<>();
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
    		if (words != null && ! words.isEmpty()) {
    			List<Entry<Integer, String>> wordList = new ArrayList<>(words.entrySet());
    			Collections.sort(wordList, (object1, object2) -> object1.getValue().compareTo(object2.getValue()));
    			recyclerView.setAdapter(new WordAdapter(wordList));
    			wordNotFound.setVisibility(View.GONE);
            }
            else {
            	wordNotFound.setText(getString(R.string.word_not_found) + ": " + queryString);
            	wordNotFound.setTypeface(service.getFont());
            	wordNotFound.setVisibility(View.VISIBLE);
            }
    		configureSearchFullButton(searchType, queryString);
    	}
	}

	private void configureSearchFullButton(SearchType searchType, String queryString) {
		if (searchType == SearchType.BEGIN) {
			searchFull.setTypeface(service.getFont());
    		searchFull.setVisibility(View.VISIBLE);
    		searchFull.setOnClickListener(paramView -> {
				Intent intent = new Intent(WordListActivity.this, WordListActivity.class);
				intent.putExtra(Constants.SEARCH_QUERY_FULL, queryString);
				startActivity(intent);
			});
		}
		else {
			searchFull.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (service.isPreferencesChanged(lastPreferencesCheck)) {
			lastPreferencesCheck = new Date();
			recreate();
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordlist);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setNavigationOnClickListener(v -> finish());
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setNavigationOnClickListener(v -> finish());

        service = (DalDicService) getApplicationContext();
		recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		wordNotFound = findViewById(R.id.word_not_found);
		searchFull = findViewById(R.id.search_full);
		clearAll = findViewById(R.id.clear_all);

        new SearchTask().execute();
    }

	private class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
		private final List<Entry<Integer, String>> words;

		WordAdapter(List<Entry<Integer, String>> words) {
			this.words = words;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			Entry<Integer, String> entry = words.get(position);
			holder.textView.setText(Html.fromHtml(entry.getValue()));
			holder.textView.setTypeface(service.getFont());
			holder.itemView.setOnClickListener(v -> startWordActivity(entry.getKey()));
		}

		@Override
		public int getItemCount() {
			return words.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			TextView textView;
			ViewHolder(View view) {
				super(view);
				textView = view.findViewById(android.R.id.text1);
			}
		}
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
