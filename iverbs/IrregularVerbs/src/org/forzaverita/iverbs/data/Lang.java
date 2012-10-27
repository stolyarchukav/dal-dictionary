package org.forzaverita.iverbs.data;

import org.forzaverita.iverbs.R;

public enum Lang {
	
	RU (R.id.selectLangRu), 
	IT (R.id.selectLangIt), 
	FR (R.id.selectLangFr), 
	DE (R.id.selectLangDe), 
	ES (R.id.selectLangEs), 
	CZ (-1), JA (-1), PT (-1), ZH (-1), KO (-1), UK (-1);
	
	private int id;
	
	private Lang(int id) {
		this.id = id;
	}
	
	public static Lang valueOf(int id) {
		for (Lang lang : values()) {
			if (lang.id == id) {
				return lang;
			}
		}
		throw new IllegalArgumentException("Unknown lang id");
	}
	
	public static Lang tryValueOf(String name) {
		for (Lang lang : values()) {
			if (lang.name().equalsIgnoreCase(name)) {
				return lang;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

}
