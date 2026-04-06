package org.forzaverita.daldic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.forzaverita.daldic.data.Constants;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlphabetActivity extends AppCompatActivity {

	private DalDicService service;
	private Date lastPreferencesCheck = new Date();

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
        setContentView(R.layout.alphabet);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setNavigationOnClickListener(v -> finish());

        service = (DalDicService) getApplicationContext();
		List<Character> letters = new ArrayList<>();
		for (char letter = 'А'; letter <= 'Я'; letter++) {
			letters.add(letter);
		}

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new AlphabetAdapter(letters));
    }

	private class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.ViewHolder> {
		private final List<Character> letters;

		AlphabetAdapter(List<Character> letters) {
			this.letters = letters;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			Character letter = letters.get(position);
			holder.textView.setText(Html.fromHtml(letter.toString()));
			holder.textView.setTypeface(service.getFont());
			holder.itemView.setOnClickListener(v -> startWordListActivity(letter));
		}

		@Override
		public int getItemCount() {
			return letters.size();
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

	private void startWordListActivity(char letter) {
		Intent intent = new Intent(this, WordListActivity.class);
		intent.putExtra(Constants.SEARCH_LETTER, letter);
		startActivity(intent);
	}

}
