package org.forzaverita.iverbs.data;

public class StatItem {

	private Verb verb;
	
	private int correct;
	
	private int wrong;
	
	private boolean inTraining;

	/* Methods */
	
	public float getCorrectPercent() {
		int all = correct + wrong;
		if (all != 0) {
			return 100f * correct / all;			
		}
		return 0;
	}
	
	/* Getters and Setters */
	
	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getWrong() {
		return wrong;
	}

	public void setWrong(int wrong) {
		this.wrong = wrong;
	}

	public boolean isInTraining() {
		return inTraining;
	}

	public void setInTraining(boolean inTraining) {
		this.inTraining = inTraining;
	}
	
}
