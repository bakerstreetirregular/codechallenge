package com.dictionary.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.dictionary.model.DictionaryEntityModel;
import com.dictionary.model.DictionaryEntityModelIdClass;

public interface DictionaryRepository extends CrudRepository<DictionaryEntityModel,DictionaryEntityModelIdClass> {
	
	 List<DictionaryEntityModel> findAllByLetter(String letter);
	
	 List<DictionaryEntityModel> findByWord(String word);
     
	 Long deleteByLetter(String letter);
	 
	 Long deleteByWord(String word);
}
