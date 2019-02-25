package org.forzaverita.daldic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.forzaverita.daldic.exception.DatabaseException;
import org.forzaverita.daldic.menu.MenuUtils;
import org.forzaverita.daldic.service.DalDicService;

import java.util.Date;

public class DalDicActivity extends Activity {
	
	private DalDicService service;
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
        service = (DalDicService) getApplicationContext();
        
        Typeface font = ((DalDicService) getApplicationContext()).getFont();
        
        TextView title = findViewById(R.id.title);
        title.setTypeface(font);
        TextView author = findViewById(R.id.author);
        author.setTypeface(font);
        
        Button browseBtn = findViewById(R.id.browse);
        browseBtn.setTypeface(font);
        browseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBrowseActivity();
			}
		});
        
        Button searchBtn = findViewById(R.id.search);
        searchBtn.setTypeface(font);
        searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
		});
        
        Button rateBtn = findViewById(R.id.rate_app);
        rateBtn.setTypeface(font);
        rateBtn.setPaintFlags(rateBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
        
        Button moreAppsBtn = findViewById(R.id.moreAppsButton);
        moreAppsBtn.setTypeface(font);
        moreAppsBtn.setPaintFlags(moreAppsBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
		return MenuUtils.createOptionsMenuNoBar(menu, this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuUtils.optionsItemSelected(item, this);
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
