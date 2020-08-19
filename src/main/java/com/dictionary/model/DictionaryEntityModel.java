package com.dictionary.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@IdClass(DictionaryEntityModelIdClass.class)
@Table(name="Dictionary")
public class DictionaryEntityModel {


private String letter;
	
@Id
private String word;

private String meaning;

@Id
private String type;

private String derivatives;


private String relatedWords;

public String getLetter() {
	return letter;
}

public void setLetter(String letter) {
	this.letter = letter;
}

public String getWord() {
	return word;
}

public void setWord(String word) {
	this.word = word;
}

public String getMeaning() {
	return meaning;
}

public void setMeaning(String meaning) {
	this.meaning = meaning;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getDerivatives() {
	return derivatives;
}

public void setDerivatives(String derivatives) {
	this.derivatives = derivatives;
}

public String getRelatedWords() {
	return relatedWords;
}

public void setRelatedWords(String relatedWords) {
	this.relatedWords = relatedWords;
}




	
	

}
