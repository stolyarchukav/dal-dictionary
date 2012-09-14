package forzaverita.brefdic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import forzaverita.brefdic.model.Word;

public class RestClientExport {

	private static final String URL_CLOUD = "http://brefdic-staging.cloudfoundry.com/api/word?user=app_editor";
	private static final String URL_LOCAL = "http://localhost:9090/brefdic-server-staging/api/word?user=app_editor";

	public static void main(String[] args) throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		URI url = new URI(URL_CLOUD);
		List<Integer> ids = Database.getInstance().getIds();
		int q = 0;
		for (Integer id : ids) {
			try {
				//if (id < 0) continue; //missed: 47822
				Word word = Database.getInstance().getWord(id);
				word.setWord(word.getWord().toUpperCase());
				Word response = restTemplate.postForObject(url, word, Word.class);
				if (response == null) throw new RuntimeException("Was not saved");
				if (++q % 1000 == 0) {
					System.out.println("Stored " + q + " words");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("error on storing id = " + id);
				break;
			}
		}
	}
	
}
