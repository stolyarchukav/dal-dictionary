package org.forzaverita.iverbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.forzaverita.iverbs.data.Verb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrainActivity extends BaseActivity {

	private Random random = new Random();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train);
        setActivityTitle();
        
        createQueston();
    }
    
    private void createQueston() {
    	Verb verb = service.getRandomVerb();
    	TextView questFormText = (TextView) findViewById(R.id.train_question_form);
    	TextView questVerbText = (TextView) findViewById(R.id.train_question_verb);
    	int formQuest = random.nextInt(3);
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
    	String verbQuest = formQuest != 0 ? verb.getForm1() : verb.getTranslation();
    	questVerbText.setText(verbQuest);
    	
    	Map<String, Boolean> options = new HashMap<String, Boolean>();
    	if (formQuest < 3) {
    		options.put(verb.getForm1(), false);
        	options.put(verb.getForm2(), false);
        	options.put(verb.getForm3(), false);
        	options.put(answer, true);
        	addRandomOption(false, options);
    	}
    	else {
    		options.put(answer, true);
    		addRandomOption(true, options);
    	}
    	List<String> keys = new ArrayList<String>(options.keySet());
    	Collections.shuffle(keys);
    	Iterator<String> iterator = keys.iterator();
    	Button answer1 = (Button) findViewById(R.id.train_answer_1);
    	String text = iterator.next();
    	answer1.setText(text);
    	answer1.setOnClickListener(new AnswerListener(options.get(text)));
    	Button answer2 = (Button) findViewById(R.id.train_answer_2);
    	text = iterator.next();
    	answer2.setText(text);
    	answer2.setOnClickListener(new AnswerListener(options.get(text)));
    	Button answer3 = (Button) findViewById(R.id.train_answer_3);
    	text = iterator.next();
    	answer3.setText(text);
    	answer3.setOnClickListener(new AnswerListener(options.get(text)));
    	Button answer4 = (Button) findViewById(R.id.train_answer_4);
    	text = iterator.next();
    	answer4.setText(text);
    	answer4.setOnClickListener(new AnswerListener(options.get(text)));
    }
    
    private void addRandomOption(boolean translate, Map<String, Boolean> options) {
    	if (options.size() != 4) {
    		Verb verb = service.getRandomVerb();
    		if (translate) {
    			options.put(verb.getTranslation(), false);
    		}
    		else {
    			switch (random.nextInt(2)) {
    				case 0 : options.put(verb.getForm1(), false);
    					break;
    				case 1 : options.put(verb.getForm2(), false);
						break;
    				case 2 : options.put(verb.getForm3(), false);
						break;
    			}
    		}
    		addRandomOption(translate, options);
    	}
    }
    
    private static class AnswerListener implements View.OnClickListener {
    	
    	private boolean correct;
    	
    	public AnswerListener(boolean correct) {
			this.correct = correct;
		}
    	
		@Override
		public void onClick(View v) {
			if (correct) {
				v.setBackgroundColor(v.getResources().getColor(R.color.train_correct));
			}
			else {
				v.setBackgroundColor(v.getResources().getColor(R.color.train_wrong));
			}
		}
	}
    
}
