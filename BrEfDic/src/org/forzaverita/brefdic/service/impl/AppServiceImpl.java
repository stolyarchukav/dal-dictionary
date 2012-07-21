package org.forzaverita.brefdic.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.forzaverita.brefdic.data.Constants;
import org.forzaverita.brefdic.data.WordsCache;
import org.forzaverita.brefdic.preferences.TextAlignment;
import org.forzaverita.brefdic.preferences.TextFont;
import org.forzaverita.brefdic.service.AppService;
import org.forzaverita.brefdic.service.DatabaseService;
import org.forzaverita.brefdic.service.PreferencesService;
import org.forzaverita.brefdic.widget.WidgetRefreshTask;

import android.app.Application;
import android.database.Cursor;
import android.graphics.Typeface;
import forzaverita.brefdic.model.Word;

public class AppServiceImpl extends Application implements AppService {

	private Typeface fontPhilosopher;
	private Typeface fontCuprum;
	private Typeface fontFlow;
	private DatabaseService dataBaseService;
	private Random random = new Random();
	private WidgetRefreshTask widgetRefreshTask;
	private WordsCache wordsCache = new WordsCache();
	private PreferencesService preferencesService;
	private Date preferenceChangeDate;
	private boolean bookmarksChanged;
	
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
	public Word getWord(Integer id) {
		String[] word = dataBaseService.getWordAndDescriptionById(id);
		return word != null ? new Word(id, word[0], word[1]) : null;
	}
	
	@Override
	public String getNextWord() {
		Word word = wordsCache.next();
		if (word == null) {
			word = generateRandomWord();
			wordsCache.addToEnd(word);
		}
		return word.getDescription();
	}
	
	@Override
	public String getPreviuosWord() {
		Word word = wordsCache.previuos();
		if (word == null) {
			word = generateRandomWord();
			wordsCache.addToBegin(word);
		}
		return word.getDescription();
	}

	private Word generateRandomWord() {
		Word word = null;
		while (word == null) {
			int id = random.nextInt(Constants.MAX_WORD_ID) + 1;
			String[] wordAndDesc = dataBaseService.getWordAndDescriptionById(id);
			if (wordAndDesc != null) {
				word = new Word(id, wordAndDesc[0], wordAndDesc[1]);
			}
		}
		return word;
	}
	
	@Override
	public Word getCurrentWord() {
		Word word = wordsCache.current();
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
	
	@Override
	public Map<Integer, String> getBookmarks() {
		return preferencesService.getBookmarks();
	}
	
	@Override
	public void addBookmark(Integer id, String word) {
		preferencesService.addBookmark(id, word);
		bookmarksChanged = true;
	}
	
	@Override
	public void removeBookmark(Integer id) {
		preferencesService.removeBookmark(id);
		bookmarksChanged = true;
	}
	
	@Override
	public boolean isBookmarked(Integer wordId) {
		return preferencesService.isBookmarked(wordId);
	}
	
	@Override
	public boolean isBookmarksChanged() {
		boolean result = bookmarksChanged;
		bookmarksChanged = false;
		return result;
	}
	
}
