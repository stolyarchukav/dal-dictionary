package org.forzaverita.iverbs.train;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.forzaverita.iverbs.BaseActivity;
import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.fragment.TitledFragment;
import org.forzaverita.iverbs.service.AppService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.forzaverita.iverbs.train.TrainUtils.checkSelectedVerbs;

public class TrainQuizFragment extends TitledFragment {

    private final Random random = new Random();

    private AppService service;

    private volatile int formQuest;
    private volatile Verb verb;
    private TextView counterCorrect;
    private TextView counterWrong;
    private int correctCount = 0;
    private int wrongCount = 0;

    private float fontSize;
    private View rootView;

    public TrainQuizFragment() {
        super();
    }

    public TrainQuizFragment(String title) {
        super(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.train_quiz_fragment, container, false);
        service = (AppService) rootView.getContext().getApplicationContext();
        fontSize = service.getFontSize();
        counterCorrect = (TextView) rootView.findViewById(R.id.train_count_correct);
        counterCorrect.setText(String.valueOf(correctCount));
        counterWrong = (TextView) rootView.findViewById(R.id.train_count_wrong);
        counterWrong.setText(String.valueOf(wrongCount));
        if (checkSelectedVerbs(service, getActivity())) {
            createQuestion();
        }
        return rootView;
    }

    private void createQuestion() {
        verb = service.getRandomVerb();
        TextView questFormTitleText = (TextView) rootView.findViewById(R.id.train_question_form_title);
        questFormTitleText.setTextSize(fontSize - 2);
        TextView questFormText = (TextView) rootView.findViewById(R.id.train_question_form);
        questFormText.setTextSize(fontSize);
        TextView questVerbTitleText = (TextView) rootView.findViewById(R.id.train_question_verb_title);
        questVerbTitleText.setTextSize(fontSize - 2);
        TextView questVerbText = (TextView) rootView.findViewById(R.id.train_question_verb);
        questVerbText.setTextSize(fontSize);
        formQuest = random.nextInt(4);
        String answer = null;
        switch (formQuest) {
            case 0 : questFormText.setText(getString(R.string.table_form_1));
                answer = verb.getForm1();
                break;
            case 1 : questFormText.setText(getString(R.string.table_form_2));
                answer = verb.getForm2();
                break;
            case 2 : questFormText.setText(getString(R.string.table_form_3));
                answer = verb.getForm3();
                break;
            default : questFormText.setText(getString(R.string.table_translation));
                answer = verb.getTranslation();
        }
        String verbQuest = formQuest == 3 ? verb.getForm1() : verb.getTranslation();
        questVerbText.setText(verbQuest);

        Map<String, Boolean> options = new HashMap<String, Boolean>();
        options.put(answer, true);
        if (formQuest < 3) {
            putOption(options, verb.getForm1());
            putOption(options, verb.getForm2());
            putOption(options, verb.getForm3());
            addRandomOption(false, options);
        }
        else {
            addRandomOption(true, options);
        }
        List<String> keys = new ArrayList<String>(options.keySet());
        Collections.shuffle(keys);
        Iterator<String> iterator = keys.iterator();
        configureButton(R.id.train_answer_1, iterator.next(), options);
        configureButton(R.id.train_answer_2, iterator.next(), options);
        configureButton(R.id.train_answer_3, iterator.next(), options);
        configureButton(R.id.train_answer_4, iterator.next(), options);
    }

    private void configureButton(int id, String text, Map<String, Boolean> options) {
        Button answer = (Button) rootView.findViewById(id);
        answer.setText(text);
        answer.setTextSize(fontSize);
        answer.setOnClickListener(new AnswerListener(options.get(text)));
        answer.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_answer));
    }

    private void addRandomOption(boolean translate, Map<String, Boolean> options) {
        if (options.size() < 4) {
            Verb verb = service.getRandomVerb();
            if (translate) {
                putOption(options, verb.getTranslation());
            }
            else {
                putOption(options, verb.getForm1());
                if (options.size() < 4) {
                    putOption(options, verb.getForm2());
                }
            }
            addRandomOption(translate, options);
        }
    }

    private static void putOption(Map<String, Boolean> options, String key) {
        if (! options.containsKey(key)) {
            options.put(key, false);
        }
    }

    private class AnswerListener implements View.OnClickListener {

        private boolean correct;
        private AtomicBoolean selected = new AtomicBoolean();

        public AnswerListener(boolean correct) {
            this.correct = correct;
        }

        @Override
        public void onClick(final View v) {
            if (! selected.get()) {
                selected.set(true);
                if (correct) {
                    v.setBackgroundColor(v.getResources().getColor(R.color.train_correct));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createQuestion();
                            selected.set(false);
                        }
                    }, 1000);
                    speakCorrect();
                    service.addCorrect(formQuest, verb, TrainMode.SELECT);
                    counterCorrect.setText(String.valueOf(++ correctCount));
                }
                else {
                    v.setBackgroundColor(v.getResources().getColor(R.color.train_wrong));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_answer));
                            selected.set(false);
                        }
                    }, 500);
                    service.addWrong(formQuest, verb, TrainMode.SELECT);
                    counterWrong.setText(String.valueOf(++ wrongCount));
                }
            }
        }
    }

    private void speakCorrect() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.speak(verb.getForm1());
        baseActivity.speak(verb.getForm2());
        baseActivity.speak(verb.getForm3());
    }

}
