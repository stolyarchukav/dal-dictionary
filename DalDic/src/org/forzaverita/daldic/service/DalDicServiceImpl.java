package org.forzaverita.daldic.service;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.forzaverita.daldic.widget.WidgetRefreshTask;

import android.app.Application;
import android.graphics.Typeface;

public class DalDicServiceImpl extends Application implements DalDicService {
	
	private Typeface font;
	private DataBaseService dataBaseService;
	private Random random = new Random();
	private WidgetRefreshTask widgetRefreshTask;
	private long refreshInterval = 10000;
	private WordsCache wordsCache = new WordsCache();
	
	@Override
	public void onCreate() {
		super.onCreate();
		font = Typeface.createFromAsset(getAssets(), "philosopher.otf");
		dataBaseService = new DataBaseService(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		dataBaseService.close();
	}
	
	@Override
	public Typeface getFont() {
		return font;
	}
	
	@Override
	public Map<Integer, String> getWordsBeginWith(char letter) {
		return dataBaseService.getWordsBeginWith(letter);
	}
	
	@Override
	public Map<Integer, String> getWordsBeginWith(String begin) {
		return dataBaseService.getWordsBeginWith(begin);
	}
	
	@Override
	public Set<String> getDescriptions(Integer id) {
		return dataBaseService.getDescriptions(id);
	}
	
	@Override
	public String getNextWord() {
		String[] word = wordsCache.next();
		if (word == null) {
			word = generateRandomWord();
			wordsCache.addToEnd(word);
		}
		return word[1];
	}
	
	@Override
	public String getPreviuosWord() {
		String[] word = wordsCache.previuos();
		if (word == null) {
			word = generateRandomWord();
			wordsCache.addToBegin(word);
		}
		return word[1];
	}

	private String[] generateRandomWord() {
		int count = dataBaseService.getWordsCount();
		String[] wordAndDesc = null;
		while (wordAndDesc == null) {
			int id = random.nextInt(count) + 1;
			wordAndDesc = dataBaseService.getWordAndDescriptionById(id);
		}
		return wordAndDesc;
	}
	
	@Override
	public String[] getCurrentWord() {
		String[] word = wordsCache.current();
		if (word == null) {
			word = generateRandomWord();
			wordsCache.addToEnd(word);
		}
		return word;
	}
	
	@Override
	public WidgetRefreshTask getWidgetRefreshTask() {
		return widgetRefreshTask;
	}
	
	@Override
	public void setWidgetRefreshTask(WidgetRefreshTask task) {
		widgetRefreshTask = task;
	}
	
	@Override
	public long getRefreshInterval() {
		return refreshInterval;
	}
	
}
