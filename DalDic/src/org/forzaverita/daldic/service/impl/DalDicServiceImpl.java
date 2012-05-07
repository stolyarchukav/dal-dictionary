package org.forzaverita.daldic.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.forzaverita.daldic.data.WordsCache;
import org.forzaverita.daldic.preferences.TextAlignment;
import org.forzaverita.daldic.preferences.TextFont;
import org.forzaverita.daldic.service.DalDicService;
import org.forzaverita.daldic.service.DatabaseService;
import org.forzaverita.daldic.service.PreferencesService;
import org.forzaverita.daldic.widget.WidgetRefreshTask;

import android.app.Application;
import android.database.Cursor;
import android.graphics.Typeface;

public class DalDicServiceImpl extends Application implements DalDicService {
	
	private Typeface fontPhilosopher;
	private Typeface fontCuprum;
	private Typeface fontFlow;
	private DatabaseService dataBaseService;
	private Random random = new Random();
	private WidgetRefreshTask widgetRefreshTask;
	private WordsCache wordsCache = new WordsCache();
	private PreferencesService preferencesService;
	private Date preferenceChangeDate;
	
	@Override
	public void onCreate() {
		super.onCreate();
		fontPhilosopher = Typeface.createFromAsset(getAssets(), "PHILOSOPHER.otf");
		fontCuprum = Typeface.createFromAsset(getAssets(), "CUPRUM.otf");
		fontFlow = Typeface.createFromAsset(getAssets(), "FLOW.otf");
		preferencesService = new PreferencesServiceImpl(this);
		dataBaseService = new DataBaseServiceImpl(this, preferencesService);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		dataBaseService.close();
	}
	
	@Override
	public boolean isDatabaseReady() {
		return dataBaseService.isDatabaseReady();
	}
	
	@Override
	public void openDatabase() {
		dataBaseService.open();
	}
	
	@Override
	public Typeface getFont() {
		switch (preferencesService.getTextFont()) {
			case PHILOSOPHER : return fontPhilosopher;
			case MONOSPACE : return Typeface.MONOSPACE;
			case SERIF : return Typeface.SERIF;
			case SANS_SERIF : return Typeface.SANS_SERIF;
			case FLOW : return fontFlow;
			case CUPRUM : return fontCuprum;
			default : return Typeface.DEFAULT; 
		}
	}
	
	@Override
	public TextFont resolveTypeface(Typeface typeface) {
		if (typeface == fontPhilosopher) {
			return TextFont.PHILOSOPHER;
		}
		else if (typeface == fontCuprum) {
			return TextFont.CUPRUM;
		}
		else if (typeface == fontFlow) {
			return TextFont.FLOW;
		}
		else if (typeface == Typeface.MONOSPACE) {
			return TextFont.MONOSPACE;
		}
		else if (typeface == Typeface.SANS_SERIF) {
			return TextFont.SANS_SERIF;
		}
		else if (typeface == Typeface.SERIF) {
			return TextFont.SERIF;
		}
		else 	return TextFont.MONOSPACE;
	}
	
	@Override
	public Map<Integer, String> getWordsBeginWith(char letter) {
		return dataBaseService.getWordsBeginWith(letter, preferencesService.isCapitalLetters());
	}
	
	@Override
	public Map<Integer, String> getWordsBeginWith(String begin) {
		return dataBaseService.getWordsBeginWith(begin, preferencesService.isCapitalLetters());
	}
	
	@Override
	public Map<Integer, String> getWordsFullSearch(String query) {
		return dataBaseService.getWordsFullSearch(query, preferencesService.isCapitalLetters());
	}
	
	@Override
	public String[] getDescription(Integer id) {
		return dataBaseService.getDescription(id);
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
	public boolean isAutoRefresh() {
		return preferencesService.isAutoRefresh();
	}
	
	@Override
	public int getRefreshInterval() {
		return preferencesService.getRefreshInterval();
	}
	
	@Override
	public TextAlignment getWordTextAlign() {
		return preferencesService.getWordTextAlign();
	}

	@Override
	public Map<Integer, String> getHistory() {
		return preferencesService.getHistory();
	}
	
	@Override
	public void addToHistory(Integer id, String word) {
		preferencesService.addToHistory(id, word);
	}
	
	@Override
	public String getWordById(Integer wordId) {
		return dataBaseService.getWordById(wordId);
	}
	
	@Override
	public Cursor getCursorOfWordsBeginWith(String begin) {
		return dataBaseService.getCursorOfWordsBeginWith(begin);
	}
	
	@Override
	public boolean isPreferencesChanged(Date lastPreferencesCheck) {
		return preferenceChangeDate != null && preferenceChangeDate.after(lastPreferencesCheck);
	}
	
	@Override
	public void preferencesChanged() {
		preferenceChangeDate = new Date();
	}
	
}
