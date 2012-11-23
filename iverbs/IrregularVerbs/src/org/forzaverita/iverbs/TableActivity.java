package org.forzaverita.iverbs;

import java.util.List;

import org.forzaverita.iverbs.data.Verb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableActivity extends BaseActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);
        setActivityTitle();
        
        new AsyncTask<Void, Void, List<Verb>>() {

        	private ProgressDialog dialog;
        	
        	@Override
        	protected void onPreExecute() {
        		dialog = ProgressDialog.show(TableActivity.this, 
        				getString(R.string.progress_title), getString(R.string.progress_text));
        	}
        	
        	@Override
			protected List<Verb> doInBackground(Void... params) {
				List<Verb> verbs = service.getVerbs();
				return verbs;
			}
			
        	@Override
        	protected void onPostExecute(List<Verb> verbs) {
        		dialog.dismiss();
        		TableLayout layout = (TableLayout) findViewById(R.id.table_table);
        		TextView headerTranslation = (TextView) findViewById(R.id.table_header_translation);
        		headerTranslation.setText(getString(R.string.table_translation) + "\n" + 
        				getString(service.getLanguage().getIdString()));
        		boolean odd = false;
        		for (Verb verb : verbs) {
		        	TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
		        	TextView form1 = (TextView) row.findViewById(R.id.table_form_1);
		            form1.setText(verb.getForm1());
		            configureClickListener(form1);
		            TextView form2 = (TextView) row.findViewById(R.id.table_form_2);
		            form2.setText(verb.getForm2());
		            configureClickListener(form2);
		            TextView form3 = (TextView) row.findViewById(R.id.table_form_3);
		            form3.setText(verb.getForm3());
		            configureClickListener(form3);
		            TextView translation = (TextView) row.findViewById(R.id.table_translation);
		            translation.setText(verb.getTranslation());
		            if (odd ^= true) {
		        		int color = getResources().getColor(R.color.cell_background_odd);
						form1.setBackgroundColor(color);
		        		form2.setBackgroundColor(color);
		        		form3.setBackgroundColor(color);
		        		translation.setBackgroundColor(color);
		        	}
		            layout.addView(row);
		        }
        	}
        	
        }.execute();
    }
    
    private void configureClickListener(final TextView text) {
    	text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speak((String) text.getText());
			}
		});
    }
    
}
