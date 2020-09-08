package app.util.feature;

import java.util.ArrayList;
import java.util.List;

public class Section {
   private List<List<WordToken>> sentenceList;
   private List<String> matches;
   private Integer currentWordIndex;
   private Integer currentSentenceIndex;
   private Integer wordLimit;
   
   public Section() {
	   sentenceList = new ArrayList<>();
	   currentSentenceIndex=0;
	   currentWordIndex=0;
	   wordLimit=0;
   }
   
   public void addSentence(List<WordToken> sentence) {
	   sentenceList.add(sentence);
   }
   
   public void setMatches(List<String> matches) {
	   this.matches = matches;
   }
   
   public List<String> getMatches() {
	   return this.matches;
   }
   
   public Integer getCurrentSentenceIndex() {
	   return currentSentenceIndex;
   }
   
   public void setCurrentWordIndex(Integer wordIndex) {
	   this.currentWordIndex=wordIndex;
   }
   
   public Integer getWordCount() {
	   Integer numberOfWords = 0;
	   for (List<WordToken> someSentence: sentenceList) {
			   numberOfWords = numberOfWords + someSentence.size();
	   }
	   return numberOfWords;
   }
   
   public Integer getWordLimit() {
	   Integer numberOfWords = sentenceList.get(currentSentenceIndex).size();
	   return numberOfWords;
   }
   
   public Integer getStartWordIndex() {
	   return sentenceList.get(currentSentenceIndex).get(0).getIndex();
   }
   
   public Integer getCurrentWordIndex() {
	   return currentSentenceIndex;
   }
   
   public Integer getSentenceCount() {
	   return sentenceList.size();
   }
   
   public void setCurrentSentence(Integer sentenceIndex) {
	   this.currentWordIndex=sentenceIndex;
   }
   
   public void incrementCurrentWordIndex() {
	   this.currentWordIndex++;
   }
   
   public void toSingleSentence() {
	   List<List<WordToken>> temporaryList = new ArrayList<>();
	   List<WordToken> temporarySentence = new ArrayList<>();
	   for (List<WordToken> sentence:sentenceList) {
		   for (WordToken wordToken:sentence) {
		        temporarySentence.add(wordToken);
		   }     
	   }
	   temporaryList.add(temporarySentence);
	   sentenceList=temporaryList;
	   this.currentSentenceIndex=0;
   }
   
   public List<WordToken> getCurrentSentence() {
	   List<WordToken> sentence=null;
	   if (currentSentenceIndex<sentenceList.size()) {
		   sentence = sentenceList.get(currentSentenceIndex);
	   }
	   return sentence;
   }
   
   public List<WordToken> getSentenceAtIndex(Integer index) {
	   List<WordToken> sentence=null;
	   if ((index>=0) && (index<sentenceList.size())) {
		   sentence = sentenceList.get(index);
	   }
	   return sentence;
   }
}
