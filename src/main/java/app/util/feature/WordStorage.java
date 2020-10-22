package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class WordStorage {
	public static final String[] FILES_OF_TEXT= {"subject.txt","object.txt","irregularverbs.txt","conjunction.txt","regularwords.txt",
			"commonwords.txt","auxiliaryverbs.txt"};
	private static final String PENN_POSTAG_FILE = "postaglist.jsonl";
	private static final String GROUP_REGEX_FILE = "featuregroups.jsonl";
    private BigInteger[] commonHashWords;
    private Map<String, List<String>> wordsMap;
    private WordSearch wordSearch;
    private PostagSearch postagSearch;
    
	public WordStorage() {
		this.wordSearch = new WordSearch();
		this.postagSearch = new PostagSearch();
		wordsMap = new HashMap<>();
		this.readWordLists();
	}
	
	public void readWordLists() {
		List<String> wordsList=null;
		String listname="";
		for (String str: FILES_OF_TEXT) {
			wordsList = new ArrayList<>();
			wordsList = this.readResource(str);
			listname = str.replace(".txt","");
			wordsMap.put(listname, wordsList);
		}
		postagSearch.setPostags(this.readJsonResource(PENN_POSTAG_FILE));
	}
	
	public PostagSearch getPostagSearch() {
		return this.postagSearch;
	}
	
	public List<String> getWordList(String listname) {
		List<String> list = new ArrayList<>();
		listname = listname.toLowerCase();
		if (wordsMap.containsKey(listname)) {
			list = wordsMap.get(listname);
		}
	  return list;
	}
	
	public boolean wordExists(String listname, String word) {
		Boolean exists = false;
		List<String> wordList;
		wordList = this.getWordList(listname);
		if (wordList.contains(word)) {
			exists = true;
		} 
		return exists;
	}
	
	public boolean listExists(String listname) {
		Boolean exists = false;
		if (wordsMap.containsKey(listname)) {
			exists = true;
		} 
		return exists;
	}
	
	public boolean wordExists(String listname, String word, Section section) {
		Boolean exists = false;
		Integer wordIndex = 0;
		List<String> wordList=null;
		WordToken wordToken = null;
		String postag = null;
		System.out.println("**Word Exists:"+word);
		if (listname!=null) {
			wordList = this.getWordList(listname);
			if (wordList.contains(word)) {
		       System.out.println("**WordStorage Word Exists:"+word);
			   wordIndex = section.getCurrentWordIndex();
			   if ((wordIndex+1)<section.getWordLimit()) {
				  wordToken = section.getSentenceAtIndex(section.getCurrentSentenceIndex()).get(wordIndex+1);
				  if (wordToken!=null) {
					  postag = wordToken.getPostag();
					  System.out.println("**WordStorage Next Word:"+wordToken.getToken()+"  "+wordToken.getPostag());
					  if (postag.startsWith("VB")) {
						 exists = true;
					  }
				  }
			  }
		   }
		}		
		System.out.println("**WordIndex:"+wordIndex);
		return exists;
	}
	
	public List<String> readResource(String filename) {
		BufferedReader bufferedReader=null;
		InputStream inputStream=null;
		InputStreamReader inputStreamReader = null;
		List<String> wordsList = new ArrayList<>();
		String line="";
		try {
			 inputStream = new ClassPathResource(filename).getInputStream();
			 inputStreamReader = new InputStreamReader(inputStream);
			 bufferedReader = new BufferedReader(inputStreamReader);
			 line = bufferedReader.readLine();
			 while (line!=null) {
				 if ((line!=null) && (line.length()>0)) {
					 wordsList.add(line);
				 }
				 line = bufferedReader.readLine();
			 }
			 bufferedReader.close();
			 inputStreamReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return wordsList;
	}
	
	public List<Object> readJsonResource(String filename) {
		BufferedReader bufferedReader=null;
		JSONParser parser = new JSONParser();
		InputStream inputStream=null;
		InputStreamReader inputStreamReader = null;
		List<Object> lines = new ArrayList<>();
		Object jsonObject = null;
		String line="";
		try {
			 inputStream = new ClassPathResource(filename).getInputStream();
			 inputStreamReader = new InputStreamReader(inputStream);
			 bufferedReader = new BufferedReader(inputStreamReader);
			 line = bufferedReader.readLine();
			 while (line!=null) {
				 jsonObject = parser.parse(line);
				 lines.add(jsonObject);
				 line = bufferedReader.readLine();
			 }
			 bufferedReader.close();
			 inputStreamReader.close();
             System.out.println("Lines"+lines.size());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return lines;
	}
}
