package com.dictionary.model;

import java.util.List;

public class Words {
	
    private String word;
	
	private List<String> meaning;
	
	private String type;
	
	private List<String> derivatives;
	
	private List<String> relatedWords;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<String> getMeaning() {
		return meaning;
	}

	public void setMeaning(List<String> meaning) {
		this.meaning = meaning;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getDerivatives() {
		return derivatives;
	}

	public void setDerivatives(List<String> derivatives) {
		this.derivatives = derivatives;
	}

	public List<String> getRelatedWords() {
		return relatedWords;
	}

	public void setRelatedWords(List<String> relatedWords) {
		this.relatedWords = relatedWords;
	}

	public Words(String word, List<String> meaning, String type, List<String> derivatives, List<String> relatedWords) {
		super();
		this.word = word;
		this.meaning = meaning;
		this.type = type;
		this.derivatives = derivatives;
		this.relatedWords = relatedWords;
	}
	
	public Words() {}

}
