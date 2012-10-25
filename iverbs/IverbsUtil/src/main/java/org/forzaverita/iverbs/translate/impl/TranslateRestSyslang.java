package org.forzaverita.iverbs.translate.impl;

import java.util.HashMap;
import java.util.Map;

import org.forzaverita.iverbs.translate.Translator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TranslateRestSyslang implements Translator {
	
	private static final String URL = "http://www.syslang.com/frengly/controller?" +
			"action=translateREST&src={src}&dest={dest}&" +
			"text={text}&username=andrey&password=maldini";
	
	static {
		System.setProperty("http.proxyHost", "59.53.92.4");
		System.setProperty("http.proxyPort", "8090");
	}
	
	private RestTemplate restTemplate = new RestTemplate();
	private Map<String, String> params = new HashMap<String, String>();
	
	@Override
	public String translate(String text, String lang) throws Exception {
		Thread.sleep(3000);
		params.put("src", "en");
		params.put("dest", lang);
		params.put("text", text);
		ResponseEntity<Pair> response = restTemplate.getForEntity(URL, Pair.class, params);
		return response.getBody().getTranslation();
	}

}
