package org.forzaverita.daldic;

import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.history.HistoryActivity;
import org.forzaverita.daldic.preferences.AppPreferenceActivity;
import org.forzaverita.daldic.service.DalDicService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DalDicActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface font = ((DalDicService) getApplicationContext()).getFont();
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(font);
        TextView author = (TextView) findViewById(R.id.author);
        author.setTypeface(font);
        
        Button browseBtn = (Button) findViewById(R.id.browse);
        browseBtn.setTypeface(font);
        browseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBrowseActivity();
			}
		});
        
        Button searchBtn = (Button) findViewById(R.id.search);
        searchBtn.setTypeface(font);
        searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchRequested();
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
        
        Button donateBtn = (Button) findViewById(R.id.donate);
        donateBtn.setTypeface(font);
        donateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.forzaverita.donate")));
				}
				catch (ActivityNotFoundException e) {
					Log.e("daldic", e.getMessage(), e);
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
		case R.id.menu_history:
			startActivity(new Intent(this, HistoryActivity.class));
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
	
}