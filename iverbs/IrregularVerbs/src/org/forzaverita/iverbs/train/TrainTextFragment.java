package org.forzaverita.iverbs.train;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.forzaverita.iverbs.BaseActivity;
import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.data.Verb;
import org.forzaverita.iverbs.fragment.TitledFragment;
import org.forzaverita.iverbs.service.AppService;

import static org.forzaverita.iverbs.train.TrainUtils.checkSelectedVerbs;

public class TrainTextFragment extends TitledFragment {

    private View rootView;
    private AppService service;
    private float fontSize;
    private Verb verb;
    private TextView counterCorrect;
    private TextView counterWrong;
    private int correctCount = 0;
    private int wrongCount = 0;
    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private volatile boolean answered;
    private TextView original1;
    private TextView original2;
    private TextView original3;
    private Button buttonAnswer;

    public TrainTextFragment() {
        super();
    }

    public TrainTextFragment(String title) {
        super(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.train_text_fragment, container, false);
        service = (AppService) rootView.getContext().getApplicationContext();
        fontSize = service.getFontSize();

        counterCorrect = (TextView) rootView.findViewById(R.id.train_text_count_correct);
        counterCorrect.setText(String.valueOf(correctCount));
        counterWrong = (TextView) rootView.findViewById(R.id.train_text_count_wrong);
        counterWrong.setText(String.valueOf(wrongCount));

        buttonAnswer = (Button) rootView.findViewById(R.id.train_text_answer_check);
        configureButton(buttonAnswer, new CheckAnswerListener());
        Button buttonNext = (Button) rootView.findViewById(R.id.train_text_next);
        configureButton(buttonNext, new NextListener());

        answer1 = (EditText) rootView.findViewById(R.id.train_text_answer_1);
        answer2 = (EditText) rootView.findViewById(R.id.train_text_answer_2);
        answer3 = (EditText) rootView.findViewById(R.id.train_text_answer_3);
        configureAnswerFlow(answer1, answer2);
        configureAnswerFlow(answer2, answer3);
        configureAnswerFlow(answer3, buttonAnswer);

        original1 = (TextView) rootView.findViewById(R.id.train_text_form1_original);
        original2 = (TextView) rootView.findViewById(R.id.train_text_form2_original);
        original3 = (TextView) rootView.findViewById(R.id.train_text_form3_original);

        configureTitleSize(R.id.train_text_form1_title);
        configureTitleSize(R.id.train_text_form2_title);
        configureTitleSize(R.id.train_text_form3_title);

        if (checkSelectedVerbs(service, getActivity())) {
            createQuestion();
        }
        return rootView;
    }

    private void configureTitleSize(int id) {
        TextView text = (TextView) rootView.findViewById(id);
        text.setTextSize(fontSize);
    }

    private void configureAnswerFlow(EditText answer, final View nextView) {
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    nextView.requestFocus();
                }
                return true;
            }
        });
    }

    private void createQuestion() {
        verb = service.getRandomVerb();
        TextView questVerbText = (TextView) rootView.findViewById(R.id.train_text_verb);
        questVerbText.setTextSize(fontSize - 2);
        questVerbText.setText(verb.getTranslation());

        answered = false;
        buttonAnswer.setEnabled(true);
        clearAnswer(answer1);
        clearAnswer(answer2);
        clearAnswer(answer3);
        clearOriginal(original1);
        clearOriginal(original2);
        clearOriginal(original3);
    }

    private void clearOriginal(TextView original) {
        original.setText("");
        original.setTextSize(fontSize);
    }

    private void clearAnswer(EditText answer) {
        answer.getText().clear();
        answer.setTextSize(fontSize - 2);
        answer.setTextColor(getResources().getColor(R.color.train_key_text));
    }

    private void configureButton(Button button, View.OnClickListener listener) {
        button.setTextSize(fontSize);
        button.setOnClickListener(listener);
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_answer));
    }

    private class CheckAnswerListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            answered = true;
            buttonAnswer.setEnabled(false);
            speakCorrect();
            checkAnswer(verb.getForm1(), answer1, original1, 0);
            checkAnswer(verb.getForm2(), answer2, original2, 1);
            checkAnswer(verb.getForm3(), answer3, original3, 3);
        }

        private void checkAnswer(String original, EditText answer, TextView originalText, int formQuest) {
            if (original.equalsIgnoreCase(answer.getText().toString())) {
                answer.setTextColor(getResources().getColor(R.color.train_correct));
                counterCorrect.setText(String.valueOf(++ correctCount));
                service.addCorrect(formQuest, verb, TrainMode.TEXT);
            }
            else {
                answer.setTextColor(getResources().getColor(R.color.train_wrong));
                counterWrong.setText(String.valueOf(++ wrongCount));
                originalText.setText(original);
                service.addWrong(formQuest, verb, TrainMode.TEXT);
            }
        }
    }

    private class NextListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (! answered) {
                service.addWrong(0, verb, TrainMode.TEXT);
                service.addWrong(1, verb, TrainMode.TEXT);
                service.addWrong(2, verb, TrainMode.TEXT);
                counterWrong.setText(String.valueOf(++ wrongCount));
            }
            createQuestion();
        }
    }

    private void speakCorrect() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.speak(verb.getForm1());
        baseActivity.speak(verb.getForm2());
        baseActivity.speak(verb.getForm3());
    }

}
