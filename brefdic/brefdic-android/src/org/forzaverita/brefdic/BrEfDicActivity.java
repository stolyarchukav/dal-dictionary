package org.forzaverita.brefdic;

import java.util.Date;

import org.forzaverita.brefdic.exception.DatabaseException;
import org.forzaverita.brefdic.menu.MenuUtils;
import org.forzaverita.brefdic.service.AppService;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BrEfDicActivity extends Activity {
	
	private AppService service;
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
        setContentView(R.layout.main);
        service = (AppService) getApplicationContext();
        
        Typeface font = service.getFont();
        
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(font);
        
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
        
        Button moreAppsBtn = (Button) findViewById(R.id.moreAppsButton);
        moreAppsBtn.setTypeface(font);
        moreAppsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse("market://search?q=pub:ForzaVerita")));
				}
				catch (ActivityNotFoundException e) {
				}
			}
		});
        
        checkDatabase();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return MenuUtils.createOptionsMenu(menu, this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtils.optionsItemSelected(item, this);
	}

	private void checkDatabase() {
		// Check and prepare database
        if (! service.isDatabaseReady()) {
        	new AsyncTask<Void, Void, String>() {
            	ProgressDialog dialog;
            	
            	@Override
            	protected void onPreExecute() {
            		dialog = ProgressDialog.show(BrEfDicActivity.this, 
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
            			new AlertDialog.Builder(BrEfDicActivity.this).
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