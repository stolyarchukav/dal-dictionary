package org.forzaverita.daldic.history;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.WordActivity;
import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractListActivity extends AppCompatActivity {

	private DalDicService service;
	private Date lastPreferencesCheck = new Date();
	private RecyclerView recyclerView;
	private TextView wordNotFound;
	private Button clearAll;

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
    		if (words != null && ! words.isEmpty()) {
    			List<Entry<Integer, String>> wordList = new ArrayList<>(words.entrySet());
    			Collections.reverse(wordList);
    			configureClearButton(wordList.size());
    			recyclerView.setAdapter(new WordAdapter(wordList));
    			wordNotFound.setVisibility(View.GONE);
            }
            else {
            	wordNotFound.setText(getEmptyText());
            	wordNotFound.setTypeface(service.getFont());
            	wordNotFound.setVisibility(View.VISIBLE);
            }
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlist);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setNavigationOnClickListener(v -> finish());

		service = (DalDicService) getApplicationContext();
		recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		wordNotFound = findViewById(R.id.word_not_found);
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

	private void configureClearButton(int size) {
		if (size > 0) {
			clearAll.setVisibility(View.VISIBLE);
			clearAll.setOnClickListener(view -> showClearConfirmation());
		}
	}

	private void showClearConfirmation() {
		new AlertDialog.Builder(this)
				.setMessage(R.string.are_you_sure)
				.setPositiveButton(R.string.yes, (dialog, which) -> {
					doClear();
					recreate();
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
