package org.forzaverita.daldic;

import java.util.Set;

import android.app.Application;
import android.graphics.Typeface;

public class DalDicServiceImpl extends Application implements DalDicService {
	
	private Typeface font;
	private DataBaseService dataBaseService;
	
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
	
}
