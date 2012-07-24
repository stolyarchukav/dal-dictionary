package org.forzaverita.brefdic.service.impl;

import java.util.Collection;
import java.util.Collections;

import org.forzaverita.brefdic.service.CloudService;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import forzaverita.brefdic.model.Word;
import forzaverita.brefdic.model.wrapper.WordWrapper;

public class CloudServiceImpl implements CloudService {

	private static final String URL = "http://brefdic.cloudfoundry.com/api/search/";
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public Collection<Word> getWordsBeginWith(String begin) {
		String url = URL + "begin/" + begin;
		return getWords(url);
	}

	@Override
	public Collection<Word> getWordsFullSearch(String query) {
		String url = URL + "fullsearch/" + query;
		return getWords(url);
	}
	
	private Collection<Word> getWords(String url) {
		try {
			return restTemplate.getForObject(url, WordWrapper.class).getWords();
		}
		catch (Exception e) {
			Log.w("brefdic", "error on cloud search", e);
		}
		return Collections.<Word>emptyList();
	}

}
