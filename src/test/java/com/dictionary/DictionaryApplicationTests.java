package com.dictionary;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dictionary.model.Words;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class DictionaryApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
    
	@Test
	public void contextLoads() {
	}
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
    
	@Test
	public void testDictionaryPost() throws Exception {
		
		List<Words> wordList=new ArrayList<Words>();
		
		List<String> meaning=Stream.of( "1.compelling attractiveness or charm that can inspire devotion in others").collect(Collectors.toList());
		List<String> derivatives=Stream.of( "Charismatic").collect(Collectors.toList());
		List<String> relatedWords=Stream.of( "aura","charm").collect(Collectors.toList());
		
		Words word=new Words("Charisma",meaning,"noun",derivatives,relatedWords);
		wordList.add(word);
		
		
		mockMvc.perform(
				post("/dictionary")
				.content(asJsonString(word))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				;
				
	}
	
	@Test
	public void testDictionaryGetAll() throws Exception{
		
		mockMvc.perform(get("/dictionary")).andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.words").isArray())
		.andExpect(jsonPath("$.words",hasItem("CAUSE")))
		.andExpect(jsonPath("$.words",hasItem("COALITION")))
		.andExpect(jsonPath("$.words",hasItem("AJAR")))
		.andExpect(jsonPath("$.words",hasItem("ABSOLVE")))
		;

		
	}
	
	
	@Test
	public void testDictionaryGetByLetter() throws Exception{
		
		mockMvc.perform(get("/dictionary/letter/C")).andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.words").isArray())
		.andExpect(jsonPath("$.words",hasItem("CAUSE")))
		.andExpect(jsonPath("$.words",hasItem("COALITION")))
		;

		
	}
	
	@Test
	public void testDictionaryGetByWord() throws Exception{
		
		mockMvc.perform(get("/dictionary/word/CAUSE")).andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.words").isArray())
		.andExpect(jsonPath("$.words",hasItem("CAUSE")))
		;
     }

	@Test
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
}
	
	
}
