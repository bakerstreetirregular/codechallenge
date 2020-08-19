package com.dictionary.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dictionary.model.DictionaryEntityModel;
import com.dictionary.model.DictionaryEntityModelIdClass;
import com.dictionary.model.RequestResponseModel;
import com.dictionary.model.Words;
import com.dictionary.repository.DictionaryRepository;

@Service
public class DictionaryService {

@Autowired	
DictionaryRepository repository;


public List<RequestResponseModel> findAll() {
	
	List<RequestResponseModel> response=new ArrayList<RequestResponseModel>();
	List<DictionaryEntityModel> model=new ArrayList<DictionaryEntityModel>();
	
   
    repository.findAll().forEach(model::add);
    
    //Flattening result to Map with Letter as Key and word list as value
    Map<String,List<DictionaryEntityModel>>map=model.stream().collect(Collectors.groupingBy(DictionaryEntityModel::getLetter));
    
    List<String> keyList=map.keySet().stream().sorted().collect(Collectors.toList());
    
    //Iterating through map
    for(String key:keyList) {
    	
    	RequestResponseModel responseElement=new RequestResponseModel();
    	responseElement.setLetter(key);
    	List<Words> wordList=new ArrayList<Words>();
    	for(DictionaryEntityModel iterator:map.get(key)) {
            		
    		Words word=new Words();
    		word.setDerivatives(Stream.of(iterator.getDerivatives()).collect(Collectors.toList()));
    		word.setMeaning(Stream.of(iterator.getMeaning()).collect(Collectors.toList()));
    		word.setRelatedWords(Stream.of(iterator.getRelatedWords()).collect(Collectors.toList()));
    		word.setType(iterator.getType());
    		word.setWord(iterator.getWord());
    		wordList.add(word);
    	}
    	
    	responseElement.setWords(wordList);
    	response.add(responseElement);
    }
    
    return response;
    	
    }  



public RequestResponseModel findByWord(String word,String type)
{
	List<DictionaryEntityModel> responseList=new ArrayList<DictionaryEntityModel>();
	//If type is empty
	if("".equals(type))
	{
	responseList=repository.findByWord(word);
	}
	else {
		
		DictionaryEntityModelIdClass id=new DictionaryEntityModelIdClass();
		id.setType(type);
		id.setWord(word);
		responseList.add(repository.findById(id).get());
		
	}
	return ResponseMapper(responseList);
}


public RequestResponseModel findByLetter(String letter)
{
	List<DictionaryEntityModel> responseList=repository.findAllByLetter(letter);
	return ResponseMapper(responseList);
	
} 

@Transactional
public String saveWord(List<Words> request)

{
 

	
	
	List<DictionaryEntityModel> insertList=new ArrayList<DictionaryEntityModel>();
	
	List<String> uninsertedWords=new ArrayList<String>();
	
	
	for(Words word:request)
	
	{
		DictionaryEntityModel dictionaryModel=new DictionaryEntityModel();
		if(!(word.getWord().isEmpty() || word.getMeaning().size()==0 || word.getType().isEmpty())) {
		dictionaryModel.setWord(word.getWord().toUpperCase());
		dictionaryModel.setLetter(word.getWord().substring(0,1).toUpperCase());
		dictionaryModel.setType(word.getType().toUpperCase());
		
		Optional<String> meaning=word.getMeaning().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
		dictionaryModel.setMeaning(meaning.orElse(""));
		
		Optional<String> relatedWords=word.getRelatedWords().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
		dictionaryModel.setRelatedWords(relatedWords.orElse(""));
		
		Optional<String> derivatives=word.getDerivatives().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
		dictionaryModel.setDerivatives(derivatives.orElse(""));
		
		insertList.add(dictionaryModel);
		
	    }
		else {
			
			uninsertedWords.add(((Integer)request.indexOf(word)).toString());
			continue;
		}
		
		
	}
	try {
	//Batch insert to enable multiple inserts in a single statement	
	Iterable<DictionaryEntityModel> model= repository.saveAll(insertList);
	Long count=StreamSupport.stream(model.spliterator(), false).count();
	if(count!=0) {
		
		
		if(uninsertedWords.isEmpty()) {
		return count+" word(s) inserted to dictionary";}
		
		else {
			
			Optional<String> uninsertedNumbers=uninsertedWords.stream().reduce((s1,s2)->s1+','+s2);
			return "Word numbers "+uninsertedNumbers.get()+" not inserted due to Word/Type/Meaning not present"; 
			
		}
	}
	
	else {
		
		return "Insertion Failure";
	}}
	
	catch(Exception ex) {
		
		ex.printStackTrace();
		return "Insertion Failure";
	}
	
}
@Transactional
public String updateWord(Words Request) {
	
	DictionaryEntityModelIdClass id=new DictionaryEntityModelIdClass();
	DictionaryEntityModel model=new DictionaryEntityModel();
	
	id.setType(Request.getType().toUpperCase());
	id.setWord(Request.getWord().toUpperCase());
	
	if(repository.existsById(id)) {
	model=repository.findById(id).get();
	    
	Optional<String> derivatives=Request.getDerivatives().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
	model.setDerivatives(derivatives.orElse(model.getDerivatives()));
	
	Optional<String> meaning=Request.getMeaning().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
	model.setMeaning(meaning.orElse(model.getMeaning()));
	
	Optional<String> relatedWords=Request.getRelatedWords().stream().reduce((s1,s2)->s1.toUpperCase()+','+s2.toUpperCase());
	model.setDerivatives(relatedWords.orElse(model.getRelatedWords()));
	
	try {
		//Update existing data
		repository.save(model);
		
		return "Word "+Request.getWord()+" of type "+Request.getType()+" updated succesfully";
		
		}
		
		catch(Exception ex) {
			
			ex.printStackTrace();
			return "Updatinon Failure";
		
		}
	}
	
	else {
		//Insert if data doesnt exist
	    List<Words> words=new ArrayList<Words>();
	    words.add(Request);
	    String result=saveWord(words);
	    return result;
	}
	
}
@Transactional
public String DeleteWord(String word,String type)

{
   
	DictionaryEntityModelIdClass id=new DictionaryEntityModelIdClass();
	
	try {
	
	if(type.equals("")) {
		
		Long numofRecords=repository.deleteByWord(word);
		if(numofRecords>0) {
		return numofRecords+" records for word "+word+ " deleted";}
		else {
			
			return "No record found";
		}
	}
	
	else {
		
		id.setType(type);
		id.setWord(word);
		if(repository.existsById(id)) {
		
		repository.deleteById(id);
		return word+" word of type "+type+" deleted";}
		else {
			
			return "No record found";
		}
		
	}}
	
	catch(Exception ex) {
		
		ex.printStackTrace();
		return "Deletion Failure";
	}
}
@Transactional
public String DeleteByLetter(String letter) {
	try {
	Long numofRecords=repository.deleteByLetter(letter);
	 if(numofRecords>0) {
		 
		 return numofRecords+" words starting with letter "+letter+" deleted";
	 }
	
	 else {
		 
		 return "No records found";
	 }}
	
	catch(Exception ex) {
		
		ex.printStackTrace();
		return "Deletion Failure";
	}
}

public RequestResponseModel ResponseMapper(List<DictionaryEntityModel> responseList) {
	
	RequestResponseModel response=new RequestResponseModel();
	List<Words> wordList=new ArrayList<Words>();
	if(responseList!=null) {
	for(DictionaryEntityModel record:responseList) {
		
		Words word=new Words();
		
		word.setWord(record.getWord());
		word.setType(record.getType());
		word.setDerivatives(Stream.of(record.getDerivatives()).collect(Collectors.toList()));
	    word.setMeaning(Stream.of(record.getMeaning()).collect(Collectors.toList()));
	    word.setRelatedWords(Stream.of(record.getRelatedWords()).collect(Collectors.toList()));
	    wordList.add(word);
	}
	if(wordList!=null && !wordList.isEmpty()) {
	System.out.println("List"+wordList);
	response.setLetter(wordList.get(0).getWord().substring(0,1));}
	response.setWords(wordList);
	}
	
	return response;
}

}
