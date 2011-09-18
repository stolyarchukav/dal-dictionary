package org.forzaverita.daldic;

import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.preferences.AppPreferenceActivity;
import org.forzaverita.daldic.service.Constants;
import org.forzaverita.daldic.service.DalDicService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DalDicActivity extends Activity {
	
	private EditText searchText;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface font = ((DalDicService) getApplicationContext()).getFont();
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(font);
        title.setTextColor(Color.BLACK);
        TextView author = (TextView) findViewById(R.id.author);
        author.setTypeface(font);
        author.setTextColor(Color.BLACK);
 
        Button browseBtn = (Button) findViewById(R.id.browse);
        browseBtn.setTypeface(font);
        browseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBrowseActivity();
			}
		});
        
        searchText = (EditText) findViewById(R.id.text_search);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL) {
					startSearchActivity();
				}
				return true;
			}
		});
        
        Button searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setTypeface(font);
        searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSearchActivity();
			}
		});
        
        Button rateBtn = (Button) findViewById(R.id.rate_app);
        rateBtn.setTypeface(font);
        rateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + 
							getApplicationInfo().packageName)));
				}
				catch (ActivityNotFoundException e) {
				}
			}
		});
        
        checkDatabase();
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void checkDatabase() {
		// Check and prepare database
        final DalDicService service = (DalDicService) getApplicationContext();
        if (! service.isDatabaseReady()) {
        	new AsyncTask<Void, Void, String>() {
            	ProgressDialog dialog;
            	
            	@Override
            	protected void onPreExecute() {
            		dialog = ProgressDialog.show(DalDicActivity.this, 
            				getString(R.string.progress_title), getString(R.string.database_init));
            	}
            	
            	@Override
            	protected String doInBackground(Void... params) {
            		String error = null;
            		try {
            			service.openDatabase();
            		}
            		catch (DatabaseException e) {
						error = e.getMessage();
					}
            		return error;
            	}
            	
            	@Override
            	protected void onPostExecute(String error) {
            		dialog.dismiss();
            		if (error != null) {
            			new AlertDialog.Builder(DalDicActivity.this).
            				setMessage(error).
            				setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface paramDialogInterface) {
									finish();
								}
							}).
            				create().show();
            		}
            	}
    		}.execute();
        }
	}

	private void startBrowseActivity() {
		Intent intent = new Intent(this, AlphabetActivity.class);
		startActivity(intent);
	}
	
	private void startSearchActivity() {
		Intent intent = new Intent(this, WordListActivity.class);
		String text = searchText.getText().toString();
		if (text.length() > 0) {
			intent.putExtra(Constants.BEGIN, text);
			startActivity(intent);
		}
		else {
			Toast toast = Toast.makeText(this, getString(R.string.search_string_empty), 0);
			toast.show();
		}
	}
	
}