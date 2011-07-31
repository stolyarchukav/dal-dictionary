package org.forzaverita.daldic;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Application;
import android.graphics.Typeface;

public class DalDicServiceImpl extends Application implements DalDicService {
	
	private Typeface font;
	private DataBaseService dataBaseService;
	private Random random = new Random();
	private String[] randomWord;
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
	public String getNewRandomWord() {
		generateRandomWord();
		return randomWord[1];
	}

	private void generateRandomWord() {
		int count = dataBaseService.getWordsCount();
		String result = null;
		while (result == null) {
			int id = random.nextInt(count) + 1;
			String[] wordAndDesc = dataBaseService.getWordAndDescriptionById(id);
			if (wordAndDesc != null) {
				randomWord = wordAndDesc;
				result = wordAndDesc[1];
			}
		}
	}
	
	@Override
	public String[] getCurrentWord() {
		if (randomWord == null) {
			generateRandomWord();
		}
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
