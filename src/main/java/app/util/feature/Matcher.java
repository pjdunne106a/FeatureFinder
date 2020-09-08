package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

public class Matcher {
	private RegexHandler regexHandler;
	private WordStorage wordStorage;
	
	public Matcher(String regex, RegularFunction regularFunction, WordStorage wordStorage, LanguageTree languageTree, WebApplicationContext applicationContext) {
		 this.regexHandler = new RegexHandler(regex, regularFunction, wordStorage, languageTree, applicationContext);
		 this.wordStorage = wordStorage;
	}

	public Integer matchcount(Section section, String granularity) {
		Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0;
		List<WordToken> sentence = null;
		if (granularity.equalsIgnoreCase("sentence")) {
			sentenceCount = section.getSentenceCount();
			while (index<sentenceCount) {
			       partMatch = regexHandler.matchescount(section.getSentenceAtIndex(index), section);
			       match = match + partMatch;
			       index++;
			}
		} else if (granularity.equalsIgnoreCase("text")) {
			      section.toSingleSentence();
			      match = regexHandler.matchescount(section.getSentenceAtIndex(0), section);
		}
		return match;
	}
	
	public String matchtext(Section section, String granularity) {
		Integer sentenceCount = 0, index = 0, start = 0, end = 0, rule = 0;
		List<WordToken> sentence = null;
		List<String> phraseMatches = null;
		List<String> matches = null;
		String match="", partMatch="", word="";
		String[] parts = null;
		if (granularity.equalsIgnoreCase("sentence")) {
			sentenceCount = section.getSentenceCount();
			matches = new ArrayList<>();
			section.setMatches(matches);
			match="[";
			while (index<sentenceCount) {
			       partMatch = regexHandler.matchestext(section.getSentenceAtIndex(index), section);
			       match = match + partMatch;
			       index++;
			}
			match = match.substring(0,match.length()-1);  
			match = match +"]";
		} else if (granularity.equalsIgnoreCase("text")) {
			      section.toSingleSentence();
			      matches = new ArrayList<>();
				  section.setMatches(matches);
			      match = regexHandler.matchestext(section.getSentenceAtIndex(0), section);
		}
		return match;
	}
	
	public static void main(String argv[]) {
		 EnglishParser englishParser = new EnglishParser();
		 WebApplicationContext applicationContext = null;
		 WordStorage wordStorage =null;
		 RegularFunction regularFunction = new RegularFunction();
		 Section section = englishParser.parseTextToSentence("Try to see why this works");
		 Matcher matched = new Matcher("<token='Try'><token='to'><postag=['VB','VBD']>", regularFunction, wordStorage, null, applicationContext);
		 Integer number = matched.matchcount(section,"sentence");
		 System.out.println("**Matched:"+number);
	}
	
}
