package forzaverita.brefdic;

import java.util.List;

import forzaverita.brefdic.model.Word;

public class DatabaseFiltering {

	public static void main(String[] args) {
		float counter = 0;
		List<Integer> ids = Database.getInstance().getIds();
		for (Integer id : ids) {
			Word word = Database.getInstance().getWord(id);
			/*boolean changed = false;
			String title = fixCleanTags(word.getWord());
			if (! title.equals(word.getWord())) {
				word.setWord(title.trim());
				changed = true;
			}
			String desc = fixCleanTags(word.getDescription());
			if (! desc.equals(word.getDescription())) {
				word.setDescription(desc.trim());
				changed = true;
			}*/
			/*if (word.getDescription().startsWith("см.") || word.getDescription().contains(" см.") || 
					word.getDescription().length() > 600) {
				Database.getInstance().deleteWord(id);
				counter++;
				System.out.println(id + " " + counter / id);
			}*/
			/*else if (changed) {
				Database.getInstance().saveWord(word);
				System.out.println(word);
			}*/
			word.setWord(word.getWord().toUpperCase());
			Database.getInstance().saveWord(word);
			if (id % 100 == 0) {
				System.out.println(100.0 * id / ids.size() + " %");
			}
		}
		//System.out.println(counter / ids.size());
	}
	
	private static String fixCleanTags(String str) {
		str = str.replaceAll("<b>", "");
		str = str.replaceAll("</b>", "");
		return str;
	}
	
}
