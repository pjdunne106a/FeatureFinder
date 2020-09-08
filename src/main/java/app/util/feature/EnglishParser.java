package app.util.feature;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@Component
public class EnglishParser {
    private StanfordCoreNLP stanfordParser;
    
	public EnglishParser() {
		 Properties props = new Properties();
		// props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
		 props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse");
		 props.setProperty("coref.algorithm", "neural");
		 stanfordParser = new StanfordCoreNLP(props);
	}
	
	 @PostConstruct
	 private void init() {
		 Properties props = new Properties();
		 InputStream stream = null;
		// props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
		 props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse");
		 props.setProperty("coref.algorithm", "neural");
		 stanfordParser = new StanfordCoreNLP(props);
	    }
	
	public Section parseTextToSentence(String text) {
		CoreDocument document=null;
		Integer wordIndex=0;
		Integer sentenceIndex=0;
		List<CoreLabel> coreLabelList=null;
		Section section = new Section();
		String lemma, tag, token, sentence, endofline;
		List<WordToken> wordTokenList = null;
		List<Map<String,String>> sentences = this.getSentences(text);
		WordToken wordToken=null;
		for (Map<String, String> sentenceMap:sentences) {
			sentence = sentenceMap.keySet().iterator().next();
			endofline = sentenceMap.get(sentence);
			document = new CoreDocument(sentence);
			stanfordParser.annotate(document);
			coreLabelList = document.tokens();
			wordTokenList = new ArrayList<>();
			wordIndex = 0;
			for (CoreLabel label:coreLabelList) {
				tag = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				token = label.get(CoreAnnotations.TextAnnotation.class);
				lemma = label.get(CoreAnnotations.LemmaAnnotation.class);
				// System.out.println("Lemma:"+lemma+"  tag:"+tag+"  token:"+token);
				wordToken = new WordToken(token, lemma, tag, wordIndex, sentenceIndex);
				wordIndex = wordIndex +1;
				wordTokenList.add(wordToken);
			}
			endofline=endofline.trim();
			if (endofline.length()>0) {
			   wordToken = new WordToken(endofline, endofline, endofline, wordIndex, sentenceIndex);
			   wordTokenList.add(wordToken);
			}
			sentenceIndex = sentenceIndex+1;
			section.addSentence(wordTokenList);
		}
		return section;
	}
	
	public Section parseTextToText(String text) {
		CoreDocument document=null;
		Integer wordIndex=0;
		Integer sentenceIndex=0;
		List<CoreLabel> coreLabelList=null;
		Section section = new Section();
		String lemma, tag, token, sentence, endofline;
		List<WordToken> wordTokenList = null;
		List<Map<String,String>> sentences = this.getSentences(text);
		WordToken wordToken=null;
		wordTokenList = new ArrayList<>();
		for (Map<String, String> sentenceMap:sentences) {
			sentence = sentenceMap.keySet().iterator().next();
			endofline = sentenceMap.get(sentence);
			document = new CoreDocument(sentence);
			stanfordParser.annotate(document);
			coreLabelList = document.tokens();
			wordIndex = 0;
			for (CoreLabel label:coreLabelList) {
				tag = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				token = label.get(CoreAnnotations.TextAnnotation.class);
				lemma = label.get(CoreAnnotations.LemmaAnnotation.class);
				// System.out.println("Lemma:"+lemma+"  tag:"+tag+"  token:"+token);
				wordToken = new WordToken(token, lemma, tag, wordIndex, sentenceIndex);
				wordIndex = wordIndex +1;
				wordTokenList.add(wordToken);
			}
			endofline=endofline.trim();
			if (endofline.length()>0) {
			   wordToken = new WordToken(endofline, endofline, endofline, wordIndex, sentenceIndex);
			   wordTokenList.add(wordToken);
			}
		}
		section.addSentence(wordTokenList);
		return section;
	}
	
	public List<Map<String,String>> getSentences(String text) {
		Boolean finished = false;
		String[] dividers = {".","...","?","??","???","????","!","!!","!!!","!!!!",":-"};
		String currentSentence = "", currentSplitter = "";
		List<String> splitterList = Arrays.asList(dividers);
		List<Map<String, String>> sentenceList = new ArrayList<>();
		Map<String, String> sentenceMap = null;
		Integer currentIndex=0;
		while ((!finished) && (currentIndex<text.length())) {
			sentenceMap = this.getSentence(splitterList, text, currentIndex);
			sentenceList.add(sentenceMap);
			currentSentence = sentenceMap.keySet().iterator().next();
			currentSplitter = sentenceMap.get(currentSentence);
			currentIndex = currentIndex + currentSentence.length() + currentSplitter.length();
		}
		return sentenceList;
	}
	
	private Map<String, String> getSentence(List<String> splitterList, String text, Integer currentIndex) {
		Map<String, String> sentenceMap = new HashMap<String,String>();
		String sentence="";
		String part="";
		String endofsentence="";
		Boolean endofline=false;
		Integer longSplitter = this.getLargest(splitterList);
		while ((!endofline) && (currentIndex<text.length())) {
			endofsentence="";
			part = text.substring(currentIndex, currentIndex+1);
			endofsentence = getEndOfSentence(splitterList, text, part, longSplitter, currentIndex);
			if (endofsentence.length()>0) {
				sentenceMap.put(sentence, endofsentence);
				endofline = true;
			} else {
				    sentence = sentence + part; 
				    currentIndex = currentIndex + 1;
			}
		}
		if (!endofline) {
			endofsentence = " ";
			sentenceMap.put(sentence, endofsentence);
		}
		return sentenceMap;
	}
	
	private Integer getLargest(List<String> splitterList) {
		Integer digits = 0;
		for (String str:splitterList) {
			if (str.length()>digits) {
				digits = str.length();
			}
		}
	  return digits;	
	}
	
	private String getEndOfSentence(List<String> splitterList, String text, String part, Integer largest, Integer currentIndex) {
		String endofsentence="";
		Integer index=0;
		currentIndex = currentIndex +1;
		while ((index<=largest) && (currentIndex<text.length())) {
			if (splitterList.contains(part)) {
				endofsentence = part;
			} 
			if (currentIndex<text.length()){
				part = part + text.substring(currentIndex,currentIndex+1);
			}
			currentIndex = currentIndex +1;
			index = index + 1;
		}
        if (splitterList.contains(part)) {
        	endofsentence=part;
        }
		return endofsentence;
	}
	
}
