package org.forzaverita.daldic.util.initial;

import org.forzaverita.daldic.util.db.Database;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class DataLoaderVidahl {
	
	private static final String URL = "http://vidahl.agava.ru/P%03d.HTM";
	private static final String TITLE_BEGIN = "<P><B>";
	private static final String TITLE_END = "</B>";
	private static final String WORD_END = "<A name=";
	
	public static void main(String[] args) throws Exception {
		for (int q = 238; q <= 259; q ++) {
			String urlString = new Formatter().format(URL, q).toString();
			Word[] words = processURL(urlString);
			checkWords(words);
		}
	}

	private static Word[] processURL(String urlString) {
		try {
			URL url = new URL(urlString);
			BufferedInputStream is = new BufferedInputStream(url.openStream());
			return processPage(is);
		}
		catch (Exception e) {
			System.out.println("err url = " + urlString);
			e.printStackTrace();
			return processURL(urlString);
		}
	}
	
	public static Word[] processPage(InputStream is) throws Exception {
		StringBuilder builder = new StringBuilder();
		while (is.available() > 0) {
			byte[] data = new byte[10240];
			is.read(data);
			builder.append(new String(data, "cp1251"));
			Thread.sleep(100);
		}
		String string = builder.toString();
		List<Word> words = new ArrayList<Word>();
		processString(string, words);
		return words.toArray(new Word[0]);
	}
	
	private static void processString(String string, List<Word> words) {
		int index = string.indexOf(TITLE_BEGIN);
		if (index != -1) {
			string = string.substring(index + TITLE_BEGIN.length());
			index = string.indexOf(TITLE_END);
			if (index != -1) {
				String title = string.substring(0, index);
				string = string.substring(index + TITLE_END.length());
				index = string.indexOf(WORD_END);
				if (index != -1) {
					String desc = string.substring(0, index);
					Word word = new Word();
					word.setWord(title);
					word.setDescription(desc);
					fixTwoWordsTitle(word);
					fixIncorrectness(word);
					words.add(word);
					string = string.substring(index + WORD_END.length());
					processString(string, words);
				}
			}
		}
	}

	private static void fixTwoWordsTitle(Word word) {
		if (word.getDescription().trim().length() >=2 &&
				Character.isUpperCase(word.getDescription().trim().charAt(0)) &&
				Character.isUpperCase(word.getDescription().trim().charAt(1))) {
			int index = word.getDescription().indexOf(',');
			if (index != -1) {
				word.setWord(word.getWord().trim() + " " + word.getDescription().substring(0, index).trim());
				word.setDescription(word.getDescription().substring(index + 1).trim());
			}
		}		
	}
	
	private static void fixIncorrectness(Word word) {
		word.setDescription(word.getDescription().replaceAll("\n", " ").trim());
		String title = word.getWord().trim();
		word.setDescription(title + " " + word.getDescription());
		for (int q = 0; q < title.length(); q++) {
			if (! (Character.isUpperCase(title.charAt(q)) || 
					Character.isWhitespace(title.charAt(q)) || 
					title.charAt(q) == '-' || title.charAt(q) == 'ё')) {
				title = title.substring(0, q);
				break;
			}
		}
		word.setWord(title);
	}

	private static void checkWords(Word[] words) throws IOException {
		int id = Database.getInstance().getMaxWordId();
		for (Word word : words) {
			Word existing = Database.getInstance().getWord(word.getWord());
			if (existing == null) {
				word.setId(++id);
				word.setFirstLetter(String.valueOf(word.getWord().charAt(0)));
				if (! Database.getInstance().createWord(word)) {
					System.out.println("Not saved " + word);
				}
				System.out.println("Saved " + word);
			}
		}
	}
	
}
