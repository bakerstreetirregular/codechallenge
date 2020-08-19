package com.dictionary;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dictionary.model.DictionaryEntityModel;
import com.dictionary.model.RequestResponseModel;
import com.dictionary.model.Words;
import com.dictionary.service.DictionaryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/dictionary")
@Api(value="Dictionary Services")
public class DictionaryController {
	
	@Autowired
	DictionaryService service;
	
	//Find all to obtain all records
	@ApiOperation(value="List All words from dictionary",response=List.class)
	@ApiResponses(value= {
			
			 @ApiResponse(code = 200, message = "Succesfully obtained search result"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 404, message = "No record found"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@GetMapping
	
	public ResponseEntity<List<RequestResponseModel>> findAll()
	{
		try {
		List<RequestResponseModel>response= service.findAll();
		
		if(response.isEmpty()) {
			
			return new ResponseEntity<List<RequestResponseModel>>(HttpStatus.NOT_FOUND);
		}
		
		else {
			
			return new ResponseEntity<List<RequestResponseModel>>(response,HttpStatus.OK);
		}}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<List<RequestResponseModel>>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	//Search Dictionary by word
	
	@ApiOperation(value="Search Dictionary By Word",response=RequestResponseModel.class)
	@ApiResponses(value= {
			
			 @ApiResponse(code = 200, message = "Succesfully obtained search result"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 404, message = "No record found"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@GetMapping(value = "/word/{word}")
	
	public ResponseEntity<RequestResponseModel> findByWord(@PathVariable String word,@RequestParam(value="type",required=false,defaultValue="") String type )
	{
		
		if(word==null || word.isEmpty()) {
			
			return new ResponseEntity<RequestResponseModel>(HttpStatus.BAD_REQUEST);
			
		}
		try {
		RequestResponseModel response= service.findByWord(word.toUpperCase(),type.toUpperCase());
		if(!response.getWords().isEmpty()) {
			
			return new ResponseEntity<RequestResponseModel>(response,HttpStatus.OK);
		}
		
		else {
			
			return new ResponseEntity<RequestResponseModel>(HttpStatus.NOT_FOUND);
		}}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<RequestResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	
		
	}
	
	//Get all words starting with a letter
	
	@ApiOperation(value="Get all words for a  Letter",response=List.class)
	@ApiResponses(value= {
			
			 @ApiResponse(code = 200, message = "Succesfully obtained search result"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 404, message = "No record found"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@GetMapping(value = "/letter/{letter}")
	public ResponseEntity<RequestResponseModel> findByLetter(@PathVariable String letter){
		
		try {
		
		if(letter==null || letter.isEmpty() || letter.length()!=1 || !(letter.chars().allMatch(Character::isLetter))) {
			
			return new ResponseEntity<RequestResponseModel>(HttpStatus.BAD_REQUEST);
		}
		
		RequestResponseModel wordList=service.findByLetter(letter.toUpperCase());
		
		if(!wordList.getWords().isEmpty()) {
			
			return new ResponseEntity<RequestResponseModel>(wordList,HttpStatus.OK);
		}
		
		else{
			
			return new ResponseEntity<RequestResponseModel>(HttpStatus.NOT_FOUND);
		}}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new ResponseEntity<RequestResponseModel>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	
		
	}
	
	//Save records to dictionary
	
	@ApiOperation(value="Save words to Dictionary")
	@ApiResponses(value= {
			
			 @ApiResponse(code = 201, message = "Succesfully Inserted"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@PostMapping
    public ResponseEntity<String> insertWord(@RequestBody List<Words> request)
    {
		
	   
		
	   String result=service.saveWord(request);
	   
	   if(result.equals("Insertion Failure")){
		   
		   return new ResponseEntity<String>("Internal Processing Error",HttpStatus.INTERNAL_SERVER_ERROR);
	   }
	   
	   else {
		   
		   return new ResponseEntity<String>(result,HttpStatus.CREATED);
	   }
		
    }
	
	//Upsert records
	
	@ApiOperation(value="Upsert existing records to Dictionary")
	@ApiResponses(value= {
			
			 @ApiResponse(code = 201, message = "Succesfully Inserted/Updated"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})	
   @PutMapping
   public ResponseEntity<String> updateWord(@RequestBody Words request)
   
   {
	  if(request.getWord().isEmpty() || request.getType().isEmpty()) {
		  
		  return new ResponseEntity<String>("Word/Type missing",HttpStatus.BAD_REQUEST);
	  }
		
	  String result=service.updateWord(request);
	   
	   if(result.contains("Failure")) {
		   
		   return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		   
		}
	   
	   else {
		   
		   return new ResponseEntity<String>(result,HttpStatus.CREATED);
	   }
   }
	
	//Delete record from Words
	@ApiOperation(value="Delete word(s) from Dictionary")
	@ApiResponses(value= {
			
			 @ApiResponse(code = 200, message = "Deleted Succesfully"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 404, message = "No records found"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@DeleteMapping(value = "/word/{word}")
	public ResponseEntity<String> deleteWord(@PathVariable String word,@RequestParam(value="type",required=false,defaultValue="") String type ) {
		
		 String result=service.DeleteWord(word.toUpperCase(), type.toUpperCase());
		 if(result.equals("No records found")) {
			 
			 return new ResponseEntity<String>(result,HttpStatus.NOT_FOUND);
		 }
		 
		 else if(result.contains("Failure")) {
			 
			 return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 
		 else {
			 
			 return new ResponseEntity<String>(result,HttpStatus.OK);
		 }
		
	}
	
	//Delete by Starting letter
	@ApiOperation(value="Delete word(s) by letter from Dictionary")
	@ApiResponses(value= {
			
			 @ApiResponse(code = 200, message = "Deleted Succesfully"),
			 @ApiResponse(code = 400, message = "Bad Request"),
			 @ApiResponse(code = 404, message = "No records found"),
			 @ApiResponse(code = 500, message = "Internal Processing Error")
			 
	})
	@DeleteMapping(value = "/letter/{letter}")
	public ResponseEntity<String> deleteByLetter(@PathVariable String letter) {
		
		if(letter==null || letter.isEmpty() || letter.length()==1 || (letter.chars().allMatch(Character::isLetter)))
		{
		
		 String result=service.DeleteByLetter(letter.toUpperCase());
		 
         if(result.equals("No records found")) {
			 
			 return new ResponseEntity<String>(result,HttpStatus.NOT_FOUND);
		 }
         else if(result.contains("Failure")) {
			 
			 return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 else {
			 
			 return new ResponseEntity<String>(result,HttpStatus.OK);
		 }}
		else {
			
			 return new ResponseEntity<String>("Invalid Input",HttpStatus.BAD_REQUEST);
		}
		
	}
}
