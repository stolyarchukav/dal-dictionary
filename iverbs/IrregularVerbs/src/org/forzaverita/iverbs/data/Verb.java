package org.forzaverita.iverbs.data;

import java.io.Serializable;

public class Verb implements Serializable {

	private int id;
	
	private String form1;
	
	private String form1Transcription;
	
	private String form2;
	
	private String form2Transcription;
	
	private String form3;
	
	private String form3Transcription;
	
	private String translation;

	/* Methods */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Verb other = (Verb) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Verb [id=" + id + ", form1=" + form1 + ", form2=" + form2
				+ ", form3=" + form3 + ", translation=" + translation + "]";
	}
	
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
