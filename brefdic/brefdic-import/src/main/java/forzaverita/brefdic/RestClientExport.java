package forzaverita.brefdic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import forzaverita.brefdic.model.Word;

public class RestClientExport {

	private static final String URL_CLOUD = "http://brefdic.cloudfoundry.com/api/word";
	private static final String URL_LOCAL = "http://localhost:9090/brefdic-server/api/word";

	public static void main(String[] args) throws URISyntaxException {
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		URI url = new URI(URL_CLOUD);
		List<Integer> ids = Database.getInstance().getIds();
		int q = 0;
		for (Integer id : ids) {
			Word word = Database.getInstance().getWord(id);
			Word response = restTemplate.postForObject(url, word, Word.class);
			if (++q % 100 == 0) {
				System.out.println("Stored " + q + " words");
			}
		}
	}
	
}
