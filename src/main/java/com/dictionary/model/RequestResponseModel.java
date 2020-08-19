package com.dictionary.model;

import java.util.List;

public class RequestResponseModel {
	
	private String letter;
	
	private List<Words> words;

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public List<Words> getWords() {
		return words;
	}

	public void setWords(List<Words> words) {
		this.words = words;
	}

	public RequestResponseModel(String letter, List<Words> words) {
		super();
		this.letter = letter;
		this.words = words;
	}
	
	public RequestResponseModel() {}
}
