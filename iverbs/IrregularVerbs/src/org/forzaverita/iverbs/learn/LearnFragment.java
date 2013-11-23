package org.forzaverita.iverbs.learn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.service.AppService;

public class LearnFragment extends Fragment {

    public static final String VERB = "verb";
    private AppService service;

    private View rootView;
    private String form1;
    private String form2;
    private String form3;
    private Verb verb;

    public LearnFragment() {
        super();
    }

    public LearnFragment(Verb verb) {
        super();
        this.verb = verb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.learn_fragment, container, false);
        service = (AppService) rootView.getContext().getApplicationContext();
        if (verb == null) {
            verb = (Verb) savedInstanceState.get(VERB);
        }
        showVerb(verb);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(VERB, verb);
        super.onSaveInstanceState(outState);
    }

    private void showVerb(Verb verb) {
        if (verb != null) {
            float fontSize = service.getFontSize();

            TextView text = (TextView) rootView.findViewById(R.id.learn_form1);
            form1 = verb.getForm1();
            text.setText(form1);
            text.setTextSize(fontSize);
            text = (TextView) rootView.findViewById(R.id.learn_form1_transcription);
            text.setText(verb.getForm1Transcription());
            text.setTextSize(fontSize);

            text = (TextView) rootView.findViewById(R.id.learn_form2);
            form2 = verb.getForm2();
            text.setText(form2);
            text.setTextSize(fontSize);
            text = (TextView) rootView.findViewById(R.id.learn_form2_transcription);
            text.setText(verb.getForm2Transcription());
            text.setTextSize(fontSize);

            text = (TextView) rootView.findViewById(R.id.learn_form3);
            form3 = verb.getForm3();
            text.setText(form3);
            text.setTextSize(fontSize);
            text = (TextView) rootView.findViewById(R.id.learn_form3_transcription);
            text.setText(verb.getForm3Transcription());
            text.setTextSize(fontSize);

            text = (TextView) rootView.findViewById(R.id.learn_translation);
            text.setText(verb.getTranslation());
            text.setTextSize(fontSize);
        }
    }

}
