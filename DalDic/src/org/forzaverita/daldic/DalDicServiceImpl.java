package org.forzaverita.daldic;

import java.util.Random;
import java.util.Set;

import android.app.Application;
import android.graphics.Typeface;

public class DalDicServiceImpl extends Application implements DalDicService {
	
	private Typeface font;
	private DataBaseService dataBaseService;
	private Random random = new Random();
	private String randomWord;
	private WidgetRefreshTask widgetRefreshTask;
	
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
	public Set<String> getWordsBeginWith(char letter) {
		return dataBaseService.getWordsBeginWith(letter);
	}
	
	@Override
	public Set<String> getWordsBeginWith(String begin) {
		return dataBaseService.getWordsBeginWith(begin);
	}
	
	@Override
	public Set<String> getDescriptions(String word) {
		return dataBaseService.getDescriptions(word);
	}
	
	@Override
	public String getRandomWord() {
		int count = dataBaseService.getWordsCount();
		String result = null;
		while (result == null) {
			int id = random.nextInt(count) + 1;
			String[] wordAndDesc = dataBaseService.getWordAndDescriptionById(id);
			if (wordAndDesc != null) {
				randomWord = wordAndDesc[0];
				result = wordAndDesc[1];
			}
		}
		return result;
	}
	
	@Override
	public String getCurrentWord() {
		return randomWord;
	}
	
	@Override
	public WidgetRefreshTask getWidgetRefreshTask() {
		return widgetRefreshTask;
	}
	
	@Override
	public void setWidgetRefreshTask(WidgetRefreshTask task) {
		widgetRefreshTask = task;
	}
	
}
