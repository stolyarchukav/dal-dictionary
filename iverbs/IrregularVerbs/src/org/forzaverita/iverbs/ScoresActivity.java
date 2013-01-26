package org.forzaverita.iverbs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.forzaverita.iverbs.data.StatItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoresActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        setActivityTitle();
        
        new AsyncTask<Void, Void, List<StatItem>>() {

        	private ProgressDialog dialog;
        	
        	@Override
        	protected void onPreExecute() {
        		dialog = ProgressDialog.show(ScoresActivity.this, 
        				getString(R.string.progress_title), getString(R.string.progress_text));
        	}
        	
        	@Override
			protected List<StatItem> doInBackground(Void... params) {
				List<StatItem> stats = service.getStats();
				return stats;
			}
			
        	@Override
        	protected void onPostExecute(List<StatItem> stats) {
        		dialog.dismiss();
        		float fontSize = service.getFontSize();
        		TableLayout layout = (TableLayout) findViewById(R.id.scores_table);
        		Collections.sort(stats, new Comparator<StatItem>() {
        			@Override
        			public int compare(StatItem lhs, StatItem rhs) {
        				int result = rhs.getCorrect() - lhs.getCorrect();
        				if (result == 0) {
        					result = lhs.getWrong() - rhs.getWrong();
        				}
        				return result;
        			}
				});
        		for (final StatItem stat : stats) {
		        	TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.scores_row, null);
		        	TextView verb = (TextView) row.findViewById(R.id.scores_verb);
		            verb.setText(stat.getVerb().getForm1() + " / " + stat.getVerb().getTranslation());
		            verb.setTextSize(fontSize);
		            TextView correct = (TextView) row.findViewById(R.id.scores_correct_count);
		            correct.setText(String.valueOf(stat.getCorrect()));
		            correct.setTextSize(fontSize);
		            TextView correctPercent = (TextView) row.findViewById(R.id.scores_correct_percent);
		            correctPercent.setTextSize(fontSize);
		            float percent = stat.getCorrectPercent();
		            correctPercent.setText(String.format("%.2f", percent));
		            if (percent > 50) {
		            	correctPercent.setTextColor(ScoresActivity.this.getResources().
		            			getColor(R.color.train_correct));
		            }
		            else {
		            	correctPercent.setTextColor(ScoresActivity.this.getResources().
		            			getColor(R.color.train_wrong));
		            }
		            CheckBox inTraining = (CheckBox) row.findViewById(R.id.scores_in_training);
		            inTraining.setChecked(stat.isInTraining());
		            inTraining.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							service.setInTraining(stat.getVerb(), isChecked);
						}
					});
		            layout.addView(row);
		        }
        	}
        	
        }.execute();
    }
    
    public void onClickReset(View view) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(getString(R.string.question_submit)).
    		setPositiveButton(getString(R.string.answer_yes), dialogClickListener).
    		setNegativeButton(getString(R.string.answer_no), dialogClickListener).
    		show();
    }
    
    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                service.resetStats();
                Intent intent = new Intent(ScoresActivity.this, ScoresActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            	break;

            case DialogInterface.BUTTON_NEGATIVE:
                break;
            }
        }
    };
    
}
