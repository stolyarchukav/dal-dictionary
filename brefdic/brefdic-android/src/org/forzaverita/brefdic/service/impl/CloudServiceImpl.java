package org.forzaverita.brefdic.service.impl;

import android.util.Log;

import org.forzaverita.brefdic.service.CloudService;

import java.util.Collection;
import java.util.Collections;

import forzaverita.brefdic.model.Word;

public class CloudServiceImpl implements CloudService {

	private static final String URL = "http://brefdic.cloudfoundry.com/api/";
	private static final String URL_SEARCH = URL + "search/";
	private static final String URL_OPEN = URL + "word/";
	//private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public Collection<Word> getWordsBeginWith(String begin) {
		String url = URL_SEARCH + "begin/" + begin;
		return getWords(url);
	}

	@Override
	public Collection<Word> getWordsFullSearch(String query) {
		String url = URL_SEARCH + "fullsearch/" + query;
		return getWords(url);
	}
	
	@Override
	public Word getWord(Integer id) {
		String url = URL_OPEN + id;
		return getWord(url);
	}
	
	private Collection<Word> getWords(String url) {
		try {
			//return restTemplate.getForObject(url, WordWrapper.class).getWords();
		}
		catch (Exception e) {
			Log.w("brefdic", "error on cloud search", e);
		}
		return Collections.<Word>emptyList();
	}
	
	private Word getWord(String url) {
		try {
			//return restTemplate.getForObject(url, Word.class);
		}
		catch (Exception e) {
			Log.w("brefdic", "error on cloud open", e);
		}
		return null;
	}

}
