package forzaverita.brefdic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import forzaverita.brefdic.model.Image;
import forzaverita.brefdic.model.Word;

public class DataLoaderWikiLaser {

	private static final String URL_BASE = "http://wiki.laser.ru/be/1/001/007/";
	private static final String URL_PAGE = URL_BASE + "%03d/%03d.htm";
	private static final String WORD_SUFFIX = "</p>";
	private static final String WORD_PREFIX = "<p align=\"justify\">";
	private static final String TITLE_SUFFIX = "</p>";
	private static final String TITLE_PREFIX = "<p>";
	private static final String IMAGE_PREFIX = "<a target=\"_blank\" href=\"";
	private static final String IMAGE_SUFFIX = "</a>";
	private static final String IMAGE_SRC_PREFIX = "<img src=\"../";
	private static final String IMAGE_SRC_SUFFIX = "\" border=\"0\">";
	private static final String BASE_PATH = "src/main/resources/";
	private static final String IMAGES_PATH = BASE_PATH + "images/image-";
	
	private static int wordId;
	private static int imageCounter;
	private static Map<Integer, String> imageUrls = new HashMap<Integer, String>();
	
	public static void main(String[] args) throws Exception {
		wordId = Database.getInstance().getMaxWordId();
		for (int q = 1; q < 120795; q ++) {
			String urlString = new Formatter().format(URL_PAGE, q / 1000, q).toString();
			Word word = processURL(urlString);
			if (word != null) {
				saveWord(word);
			}
			if (q % 1000 == 0) {
				System.out.println(q);
			}
		}
	}

	private static Word processURL(String urlString) {
		try {
			URL url = new URL(urlString);
			BufferedInputStream is = new BufferedInputStream(url.openStream());
			return processPage(is);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found: err url = " + urlString);
			return null;
		}
		catch (Exception e) {
			//System.out.println("err url = " + urlString);
			//e.printStackTrace();
			return processURL(urlString);
		}
	}
	
	public static Word processPage(InputStream is) throws Exception {
		StringBuilder builder = new StringBuilder();
		Thread.sleep(100);
		while (is.available() > 0) {
			byte[] data = new byte[1024];
			is.read(data);
			builder.append(new String(data, "cp1251"));
		}
		String string = builder.toString();
		return processString(string);
	}

	private static Word processString(String string) throws Exception {
		int titleBegin = string.indexOf(TITLE_PREFIX);
		int titleEnd = string.indexOf(TITLE_SUFFIX, titleBegin);
		String title = string.substring(titleBegin + TITLE_PREFIX.length(), titleEnd);
		int descBegin = string.indexOf(WORD_PREFIX, titleEnd);
		int descEnd = string.lastIndexOf(WORD_SUFFIX);
		String desc = string.substring(descBegin + WORD_PREFIX.length(), descEnd);
		desc = trim(desc);
		return buildWord(title, desc);
	}

	private static Word buildWord(String title, String desc) throws Exception {
		Set<Image> images = new HashSet<Image>();
		desc = extractImage(desc, images);
		
		Word word = new Word();
		word.setWord(title);
		word.setDescription(desc);
		word.setFirstLetter(title.substring(0, 1));
		word.setImages(images);
		
		return word;
	}

	private static String extractImage(String desc, Set<Image> images) throws Exception {
		int start = desc.indexOf(IMAGE_PREFIX);
		if (start != -1) {
			int end = desc.indexOf(IMAGE_SUFFIX);
			String full = desc.substring(start, end + IMAGE_SUFFIX.length());
			int startImg = full.indexOf(IMAGE_SRC_PREFIX);
			int endImg = full.indexOf(IMAGE_SRC_SUFFIX);
			String img = full.substring(startImg + IMAGE_SRC_PREFIX.length(), endImg);
			Image image = new Image();
			image.setPosition(start);
			int imageId = saveImage(img);
			image.setImageId(imageId);
			desc = desc.replace(full, "");
			images.add(image);
			return extractImage(desc, images);
		}
		return desc;
	}

	private static String trim(String word) {
		word = word.replaceAll("&quot;", "\"");
		word = word.replaceAll("\n", " ");
		word = word.replaceAll("\r", "");
		word = word.replace(WORD_PREFIX, "\r");
		word = word.replaceAll("<i>", "");
		word = word.replaceAll("</i>", "");
		word = word.replaceAll("</p>", "");
		word = word.trim();
		if (word.startsWith("—")) {
			word = word.substring(1);
		}
		word = word.trim();
		return word;
	}
	
	private static void saveWord(Word word) {
		word.setId(++wordId);
		if (Database.getInstance().createWord(word)) {;
			//System.out.println("Saved " + word);
		}
		else {
			System.out.println("Not saved " + word);
		}
	}
	
	private static int saveImage(String img) throws Exception {
		for (Integer imgId : imageUrls.keySet()) {
			if (img.equals(imageUrls.get(imgId))) {
				return imgId;
			}
		}
		int imgId = imageCounter++;
		imageUrls.put(imgId, img);
		URL url = new URL(URL_BASE + img);
		File file = new File(IMAGES_PATH + imgId + ".jpg");
		FileOutputStream os = new FileOutputStream(file);
		InputStream is = new BufferedInputStream(url.openConnection().getInputStream());
		Thread.sleep(100);
		while (is.available() > 0) {
			byte[] data = new byte[1024];
			is.read(data);
			os.write(data);
		}
		is.close();
		os.close();
		return imgId;
	}
	
}
