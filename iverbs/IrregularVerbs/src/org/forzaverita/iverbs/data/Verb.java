package org.forzaverita.iverbs.data;

public class Verb {

	private int id;
	
	private String form1;
	
	private String form1Transcription;
	
	private String form2;
	
	private String form2Transcription;
	
	private String form3;
	
	private String form3Transcription;
	
	private String translation;

	/* Getters and Setters */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getForm1() {
		return form1;
	}

	public void setForm1(String form1) {
		this.form1 = form1;
	}

	public String getForm2() {
		return form2;
	}

	public void setForm2(String form2) {
		this.form2 = form2;
	}

	public String getForm3() {
		return form3;
	}

	public void setForm3(String form3) {
		this.form3 = form3;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getForm1Transcription() {
		return form1Transcription;
	}

	public void setForm1Transcription(String form1Transcription) {
		this.form1Transcription = form1Transcription;
	}

	public String getForm2Transcription() {
		return form2Transcription;
	}

	public void setForm2Transcription(String form2Transcription) {
		this.form2Transcription = form2Transcription;
	}

	public String getForm3Transcription() {
		return form3Transcription;
	}

	public void setForm3Transcription(String form3Transcription) {
		this.form3Transcription = form3Transcription;
	}
	
}
