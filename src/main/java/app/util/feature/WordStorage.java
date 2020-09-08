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
	public static final String COMMON_WORDS = "commonwords";
	public static final String CONJUNCTION_WORDS = "conjunction";
	public static final String IRREGULAR_VERB = "irregularverb";
	public static final String AUXILLIARY = "auxiliary";
	public static final String REGULAR_WORDS = "regularwords";
	private static final String IRREGULARVERB_WORDS_FILE = "irregularverbs.txt";
	private static final String CONJUNCTION_WORDS_FILE = "clause_conjunction.txt";
	private static final String COMMON_WORDS_FILE = "bealeassortedwordlist.txt";
	private static final String REGULAR_WORDS_FILE = "commonwords.txt";
	private static final String AUXILLIARY_WORDS_FILE = "auxiliaryverbs.txt";
	private static final String PENN_POSTAG_FILE = "postaglist.jsonl";
	private static final String GROUP_REGEX_FILE = "featuregroups.jsonl";
    private BigInteger[] commonHashWords;
    private List<String> commonWords;
    private List<String> auxilliaryverbWords;
    private List<String> irregularverbWords;
    private List<String> conjunctionWords;
    private List<String> regularWords;
    private WordSearch wordSearch;
    private PostagSearch postagSearch;
    
	public WordStorage() {
		this.wordSearch = new WordSearch();
		this.postagSearch = new PostagSearch();
		this.readWordLists();
	}
	
	public void readWordLists() {
		commonWords = new ArrayList<>();
		auxilliaryverbWords = new ArrayList<>();
		irregularverbWords = new ArrayList<>();
		conjunctionWords = new ArrayList<>();
		regularWords = new ArrayList<>();
		this.readResource(COMMON_WORDS_FILE, commonWords);
		this.readResource(AUXILLIARY_WORDS_FILE, auxilliaryverbWords);
		this.readResource(IRREGULARVERB_WORDS_FILE, irregularverbWords);
		this.readResource(REGULAR_WORDS_FILE, regularWords);
		this.readResource(CONJUNCTION_WORDS_FILE, conjunctionWords);
		postagSearch.setPostags(this.readJsonResource(PENN_POSTAG_FILE));
	}
	
	public PostagSearch getPostagSearch() {
		return this.postagSearch;
	}
	
	public List<String> getWordList(String listname) {
		List<String> list = new ArrayList<>();
		if (listname.equalsIgnoreCase(CONJUNCTION_WORDS)) {
			list = conjunctionWords;
		}
	  return list;
	}
	
	public boolean wordExists(String listname, String word) {
		Boolean exists = false;
		Integer index = 0;
		String[] wordArray = null;
		System.out.println("**Word Exists:"+word);
		if ((listname.equalsIgnoreCase(COMMON_WORDS_FILE)) || (listname.equalsIgnoreCase(COMMON_WORDS))) {
			wordArray = commonWords.toArray(new String[0]);
			index = wordSearch.interpolationSearch(wordArray, word);
		}
		if ((listname.equalsIgnoreCase(AUXILLIARY_WORDS_FILE)) || (listname.equalsIgnoreCase(AUXILLIARY))) {
			if (auxilliaryverbWords.contains(word)) {
				index = 1;
			} else {
				index = -1;
			}
		}
		if ((listname.equalsIgnoreCase(REGULAR_WORDS))) {
			if (regularWords.contains(word)) {
				index = 1;
			} else {
				index = -1;
			}
			System.out.println("**Word:"+word);
		}
		if ((listname.equalsIgnoreCase(CONJUNCTION_WORDS))) {
			if (conjunctionWords.contains(word)) {
				index = 1;
			} else {
				index = -1;
			}
			System.out.println("**Word:"+word);
		}
		if (index>-1) {
			exists = true;
		}
		System.out.println("**WordIndex:"+index);
		return exists;
	}
	
	public boolean wordExists(String listname, String word, Section section) {
		Boolean exists = false;
		Integer wordIndex = 0;
		WordToken wordToken = null;
		String postag = null;
		System.out.println("**Word Exists:"+word);
		if (listname.equalsIgnoreCase(IRREGULAR_VERB)) {
			if (irregularverbWords.contains(word)) {
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
	
	public void readResource(String filename, List<String> wordsList) {
		BufferedReader bufferedReader=null;
		InputStream inputStream=null;
		InputStreamReader inputStreamReader = null;
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
