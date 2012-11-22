package org.forzaverita.iverbs.data;

import org.forzaverita.iverbs.R;

public enum Lang {
	
	RU (R.id.selectLangRu, R.string.lang_ru), 
	IT (R.id.selectLangIt, R.string.lang_it), 
	FR (R.id.selectLangFr, R.string.lang_fr), 
	DE (R.id.selectLangDe, R.string.lang_de), 
	ES (R.id.selectLangEs, R.string.lang_es),
	PT (R.id.selectLangPt, R.string.lang_pt),
	UA (R.id.selectLangUa, R.string.lang_ua),
	RO (R.id.selectLangRo, R.string.lang_ro),
	CZ (-1, -1), JA (-1, -1), ZH (-1, -1), KO (-1, -1);
	
	private int id;
	private int idString;
	
	private Lang(int id, int idString) {
		this.id = id;
		this.idString = idString;
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

	public int getIdString() {
		return idString;
	}

}
