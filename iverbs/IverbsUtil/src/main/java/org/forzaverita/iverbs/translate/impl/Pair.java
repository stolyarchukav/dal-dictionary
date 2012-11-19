package org.forzaverita.iverbs.translate.impl;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class Pair {
	
	private String text;
	
	private String translation;

	/* Methods */
	
	@Override
	public String toString() {
		return "Pair [text=" + text + ", translation=" + translation + "]";
	}
	
	/* Getters and Setters */
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
}
