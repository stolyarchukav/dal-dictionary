package org.forzaverita.iverbs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.table.OrderingHandler;
import org.forzaverita.iverbs.table.SortableTable;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableActivity extends BaseActivity implements SortableTable<Verb> {

    private volatile Comparator<Verb> comparator;

    private OrderingHandler<Verb> orderingForm1;
    private OrderingHandler<Verb> orderingForm2;
    private OrderingHandler<Verb> orderingForm3;
    private OrderingHandler<Verb> orderingTranslate;

    private static boolean transcriptionVisible;

    private LoadTableTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        configureOrdering();
        orderingForm1.setOrder(true);
    }

    @Override
    protected void onDestroy() {
        if (task != null) {
            task.dialog.cancel();
        }
        super.onDestroy();
    }

    private void configureOrdering() {
        orderingForm1 = new OrderingHandler<Verb>((TextView) findViewById(R.id.table_header_form1),
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return lhs.getForm1().compareTo(rhs.getForm1());
                    }
                },
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return rhs.getForm1().compareTo(lhs.getForm1());
                    }
                }, this
        );
        orderingForm2 = new OrderingHandler<Verb>((TextView) findViewById(R.id.table_header_form2),
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return lhs.getForm2().compareTo(rhs.getForm2());
                    }
                },
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return rhs.getForm2().compareTo(lhs.getForm2());
                    }
                }, this
        );
        orderingForm3 = new OrderingHandler<Verb>((TextView) findViewById(R.id.table_header_form3),
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return lhs.getForm3().compareTo(rhs.getForm3());
                    }
                },
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return rhs.getForm3().compareTo(lhs.getForm3());
                    }
                }, this
        );
        TextView headerTranslation = (TextView) findViewById(R.id.table_header_translation);
        headerTranslation.setText(getString(R.string.table_translation) + "\n" +
                getString(service.getLanguage().getIdString()));
        orderingTranslate = new OrderingHandler<Verb>(headerTranslation,
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return lhs.getTranslation().compareTo(rhs.getTranslation());
                    }
                },
                new Comparator<Verb>() {
                    @Override
                    public int compare(Verb lhs, Verb rhs) {
                        return rhs.getTranslation().compareTo(lhs.getTranslation());
                    }
                }, this
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_table).setVisible(false);
        return true;
    }
    
    private void configureClickListener(View view, final String text) {
    	view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speak(text);
			}
		});
    }

    public void onClickHeaderForm1(View view) {
        orderingForm1.changeOrder();
    }

    public void onClickHeaderForm2(View view) {
        orderingForm2.changeOrder();
    }

    public void onClickHeaderForm3(View view) {
        orderingForm3.changeOrder();
    }

    public void onClickHeaderTranslation(View view) {
        orderingTranslate.changeOrder();
    }

    public void onCLickShowTranscription(View view) {
        final TextView textView = (TextView) view;
        transcriptionVisible ^= true;
        if (transcriptionVisible) {
            textView.setText(getString(R.string.table_hide_transcription));
        }
        else {
            textView.setText(getString(R.string.table_show_transcription));
        }
        loadTable();
    }

    @Override
    public void clearHeaders() {
        orderingForm1.clear();
        orderingForm2.clear();
        orderingForm3.clear();
        orderingTranslate.clear();
    }

    @Override
    public void setComparator(Comparator<Verb> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void loadTable(final Runnable... preLoadTasks) {
        task = new LoadTableTask(this, preLoadTasks);
        task.execute();
    }

    private static class LoadTableTask extends AsyncTask<Void, Void, List<Verb>> {

        private final WeakReference<TableActivity> activityReference;
        private final Runnable[] preLoadTasks;

        private ProgressDialog dialog;

        public LoadTableTask(TableActivity activity, final Runnable... preLoadTasks) {
            this.activityReference = new WeakReference<TableActivity>(activity);
            this.preLoadTasks = preLoadTasks;
        }

        @Override
        protected void onPreExecute() {
            TableActivity activity = activityReference.get();
            if (activity != null) {
                dialog = ProgressDialog.show(activity,
                        activity.getString(R.string.progress_title), activity.getString(R.string.progress_text));
            }
        }

        @Override
        protected List<Verb> doInBackground(Void... params) {
            for (Runnable preLoadTask : preLoadTasks) {
                preLoadTask.run();
            }
            TableActivity activity = activityReference.get();
            if (activity != null) {
                return activity.service.getVerbs();
            }
            return Collections.emptyList();
        }
        
        @Override
        protected void onPostExecute(List<Verb> verbs) {
            TableActivity activity = activityReference.get();
            if (activity != null) {
                float fontSize = activity.service.getFontSize();
                TableLayout layout = (TableLayout) activity.findViewById(R.id.table_table);
                layout.removeAllViews();
                Collections.sort(verbs, activity.comparator);
                boolean odd = false;
                for (Verb verb : verbs) {
                    TableRow row = (TableRow) activity.getLayoutInflater().inflate(R.layout.table_row, null);
                    TextView form1 = (TextView) row.findViewById(R.id.table_form_1);
                    form1.setText(separateLines(verb.getForm1(), verb.getForm1Transcription()));
                    form1.setTextSize(fontSize);
                    activity.configureClickListener(form1, verb.getForm1());
                    TextView form2 = (TextView) row.findViewById(R.id.table_form_2);
                    form2.setText(separateLines(verb.getForm2(), verb.getForm2Transcription()));
                    form2.setTextSize(fontSize);
                    activity.configureClickListener(form2, verb.getForm2());
                    TextView form3 = (TextView) row.findViewById(R.id.table_form_3);
                    form3.setText(separateLines(verb.getForm3(), verb.getForm3Transcription()));
                    form3.setTextSize(fontSize);
                    activity.configureClickListener(form3, verb.getForm3());
                    TextView translation = (TextView) row.findViewById(R.id.table_translation);
                    translation.setText(verb.getTranslation());
                    translation.setTextSize(fontSize);
                    if (odd ^= true) {
                        int color = activity.getResources().getColor(R.color.cell_background_odd);
                        form1.setBackgroundColor(color);
                        form2.setBackgroundColor(color);
                        form3.setBackgroundColor(color);
                        translation.setBackgroundColor(color);
                    }
                    layout.addView(row);
                }
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    private String separateLines(String verb, String transcription) {
        if (transcriptionVisible) {
            return verb + System.getProperty("line.separator") + transcription;
        } else {
            return verb;
        }
    }

}

}
