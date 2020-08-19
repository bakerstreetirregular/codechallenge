package com.dictionary.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DictionaryEntityModelIdClass implements Serializable {
	
	private String word;
	
	private String type;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
