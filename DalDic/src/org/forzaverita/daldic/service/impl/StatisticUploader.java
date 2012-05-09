package org.forzaverita.daldic.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.forzaverita.daldic.service.PreferencesService;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import forzaverita.daldic.core.event.SearchEvents;
import forzaverita.daldic.core.event.WordEvents;

public class StatisticUploader {
	
	private static final int PERIOD = 1000 * 60 * 60 * 3;
	private static final int START_DELAY = 1000 * 30;
	private Context context;
	private PreferencesService preferencesService;
	
	public StatisticUploader(Context context, PreferencesService preferencesService) {
		this.context = context;
		this.preferencesService = preferencesService;
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					sendSearchStrings();
					sendFullSearchStrings();
					sendOpenWords();
					sendOpenWidgetWords();
					sendBookmarks();
				}
				catch (Exception e) {
					Log.w("daldic", "error on send stat", e);
				}
			}
		}, START_DELAY, PERIOD);
    }

	private void sendSearchStrings() {
		String[] searchStrings = preferencesService.getEventSearch();
		if (uploadSearches(searchStrings, "search")) {
			preferencesService.clearEventSearch();
		}
	}
	
	private void sendFullSearchStrings() {
		String[] searchStrings = preferencesService.getEventFullSearch();
		if (uploadSearches(searchStrings, "full_search")) {
			preferencesService.clearEventFullSearch();
		}
	}
	
	private void sendOpenWords() {
		Integer[] ids = preferencesService.getEventOpenWord();
		if (uploadWords(ids, "open_word")) {
			preferencesService.clearEventOpenWord();
		}
	}
	
	private void sendOpenWidgetWords() {
		Integer[] ids = preferencesService.getEventOpenWordWidget();
		if (uploadWords(ids, "open_word_widget")) {
			preferencesService.clearEventOpenWordWidget();
		}
	}
	
	private void sendBookmarks() {
		Integer[] ids = preferencesService.getEventBookmark();
		if (uploadWords(ids, "bookmark_word")) {
			preferencesService.clearEventBookmark();
		}
	}

	private boolean uploadSearches(String[] searchStrings, String urlPath) {
		if (searchStrings != null && searchStrings.length != 0) {
			SearchEvents event = new SearchEvents(new Date(), Build.MODEL, 
					 context.getResources().getConfiguration().locale.getCountry(), Arrays.asList(searchStrings));
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://daldic.cloudfoundry.com/api/statistic/" + urlPath;
			String result = restTemplate.postForObject(url, event, String.class);
			Log.i("daldic", "Uploaded searches " + urlPath + " : " + result);
			if (result == null) {
				return true;
			}
		}
		return false;
	}
	
	private boolean uploadWords(Integer[] ids, String urlPath) {
		if (ids != null && ids.length != 0) {
			WordEvents event = new WordEvents(new Date(), Build.MODEL, 
					 context.getResources().getConfiguration().locale.getCountry(), Arrays.asList(ids));
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://daldic.cloudfoundry.com/api/statistic/" + urlPath;
			String result = restTemplate.postForObject(url, event, String.class);
			Log.i("daldic", "Uploaded words " + urlPath + " : " + result);
			if (result == null) {
				return true;
			}
		}
		return false;
	}
	
}
