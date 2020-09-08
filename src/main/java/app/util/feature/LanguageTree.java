package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class LanguageTree {
	private static final String GRAMMAR_FILE = "english_grammar.txt";
	private Map<String, String> grammar;
	private RegularFunction regularFunction;
	
	public LanguageTree() {
		grammar = this.getGrammar();
	}
	
	public void setRegularFunction(RegularFunction regularFunction) {
		this.regularFunction = regularFunction;
	}
	
	public String parseClause(Section section) {
		Integer index=0;
		String sentenceBlock = "", sentencePart = "";
		String[] sentencePhrases = null;
		sentenceBlock = grammar.get("<sentence>");
		sentencePhrases = this.getSyntaxParts(sentenceBlock);
		while (index<sentencePhrases.length) {
		     sentencePart = sentencePhrases[index];
			 // matchExpression(section, sentencePart);
			 index++;
		}
	   return "";
	}
	
	public String doGrammar(Section section) {
		Boolean subjectfound=false,verbfound=false,objectfound=false,finish=false;
		Integer index=0;
		List<PhraseMatch> matchList=null, subjectlist=null, verblist=null, objectlist=null;
		Map<String, List<PhraseMatch>> matchesTable = new HashMap<>();
		String sentenceBlock = "", sentencePart = "", previousPart="", subjectlink="", verblink="", result="";
		PhraseMatch phraseMatch = null, subjectMatch = null, verbMatch = null, objectMatch = null;
		String[] sentencePhrases = null;
		sentenceBlock = grammar.get("<sentence>");
		sentencePhrases = this.getSyntaxParts(sentenceBlock);
		while (index<sentencePhrases.length) {
		     sentencePart = sentencePhrases[index];
		     matchesTable.put(sentencePart, new ArrayList<>());
			 matchExpression(section, sentencePart, matchesTable, previousPart);
			 previousPart = sentencePart;
			 index++;
		}
		for (String str:matchesTable.keySet()) {
			matchList = matchesTable.get(str);
			System.out.println("Phrase:"+str);
			for (PhraseMatch match:matchList) {
				System.out.println("  "+match.getStartWordIndex()+":"+match.getEndWordIndex()+":"+match.getPhrase()+":"+match.getId()+":Link:"+match.getLinkedId());
			}
		}
		subjectfound=false; index=0;
		subjectlist=matchesTable.get("<subjectphrase>");
		verblist=matchesTable.get("<verbphrase>");
		objectlist=matchesTable.get("<objectphrase>");
		if (subjectlist!=null) {
			subjectMatch = null;index=0;
		    while ((!subjectfound) && (index<subjectlist.size())) {
		        phraseMatch = subjectlist.get(index);	
		        if (phraseMatch.getStartWordIndex()==0) {
		        	subjectfound = true;
		        	subjectMatch = phraseMatch;
		        	result="{" + subjectMatch.getStartWordIndex()+":"+subjectMatch.getEndWordIndex() + "}"+subjectMatch.getPhrase();
		        }
		        index++;
	   	    }
		    if (subjectfound) {
		    	verbMatch = null; phraseMatch = null; verbfound = false; index=0;
		    	subjectlink = subjectMatch.getId();
		    	while ((!verbfound) && (index<verblist.size())) {
			        phraseMatch = verblist.get(index);	
			        if (phraseMatch.getLinkedId()==subjectlink) {
			        	verbfound = true;
			        	verbMatch = phraseMatch;
			        	result= result + "{" + verbMatch.getStartWordIndex()+":"+verbMatch.getEndWordIndex() + "}"+verbMatch.getPhrase();
			        }
			        index++;
		   	    }
		    }
		    if ((verbfound) && (objectlist!=null)) {
		    	objectMatch = null; phraseMatch = null; objectfound = false;index=0;
		    	verblink = verbMatch.getId();
		    	while ((!objectfound) && (index<objectlist.size())) {
			        phraseMatch = objectlist.get(index);	
			        if (phraseMatch.getLinkedId()==verblink) {
			        	objectfound = true;
			        	objectMatch = phraseMatch;
			        	result= result + "{" + objectMatch.getStartWordIndex()+":"+objectMatch.getEndWordIndex()+"}"+objectMatch.getPhrase();
			        }
			        index++;
		   	    }	
		    }
		} else {
			verbMatch = null; verbfound = false; index = 0;
		    while ((!verbfound) && (index<verblist.size())) {
		        phraseMatch = verblist.get(index);	
		        if (phraseMatch.getStartWordIndex()==0) {
		        	verbfound = true;
		        	verbMatch = phraseMatch;
		        	result="{" + verbMatch.getStartWordIndex()+":"+verbMatch.getEndWordIndex()+"}"+verbMatch.getPhrase();
		        }
		        index++;
	   	    }
		    if ((verbfound) && (objectlist!=null)) {
		    	objectMatch = null; phraseMatch = null; objectfound = false;index=0;
		    	verblink = verbMatch.getId();
		    	while ((!objectfound) && (index<objectlist.size())) {
			        phraseMatch = objectlist.get(index);	
			        if (phraseMatch.getLinkedId()==verblink) {
			        	objectfound = true;
			        	objectMatch = phraseMatch;
			        	result= result + "{" + objectMatch.getStartWordIndex()+":"+objectMatch.getEndWordIndex()+"}"+objectMatch.getPhrase();
			        }
			        index++;
		   	    }
		    }
		}
		return result;
	}
	
	public void matchExpression(Section section, String phrase, Map<String, List<PhraseMatch>> matchesTable, String previousPart) {
		Integer index = 0, phraseIndex = 0, wordIndex = 0, startWordIndex;
		List<PhraseMatch> phraseMatchList = new ArrayList<>();
		List<PhraseMatch> currentPhraseMatchList = new ArrayList<>();
		List<PhraseMatch> matchList;
		PhraseMatch phraseMatch = null, itemMatch = null;
		String[] itemList = null;
		String itemPhrase = "", partPhrase="", match="";
		WordToken wordToken=null;
		if (phrase != null) {
			itemPhrase = grammar.get(phrase);
			itemList = this.getSyntaxItems(itemPhrase);
			index = 0;
			if (previousPart.length()>0) {
				phraseMatchList = matchesTable.get(previousPart);
			} else {
				phraseMatch = new PhraseMatch(0,-1,"");
				phraseMatch.setId("-1");
				phraseMatch.setLinkedId("-1");
				phraseMatchList.add(phraseMatch);
			}
            while (phraseIndex<phraseMatchList.size()) {
            	phraseMatch = phraseMatchList.get(phraseIndex);
            	wordIndex = phraseMatch.getEndWordIndex()+1;
            	startWordIndex = phraseMatch.getStartWordIndex();
   			    while ((index<itemList.length) && (wordIndex<section.getWordLimit())) {
				    wordToken = section.getCurrentSentence().get(wordIndex);
				    partPhrase = itemList[index];
				    if (partPhrase.contains("phrase")) {
				            matchList = doMultiPhrase(section, wordToken, partPhrase);	
				            if (matchList.size()>0) {
				            	currentPhraseMatchList=matchesTable.get(phrase);
				            	for (PhraseMatch matchItem:matchList) {
				            		matchItem.setLinkedId(phraseMatch.getId());
				            		matchItem.setId(UUID.randomUUID().toString());
				            		matchItem.setStartWordIndex(wordIndex);
							    	currentPhraseMatchList.add(matchItem);
							    	System.out.println("**Match:"+partPhrase+"  "+matchItem.getStartWordIndex()+"  "+matchItem.getId()+"  "+matchItem.getLinkedId());
				            	}
				            	matchesTable.replace(phrase, currentPhraseMatchList);
				            }
				    } else {
				            wordToken = doPhrase(section, wordToken, partPhrase);
   				            if (wordToken!=null) {
				    	       itemMatch = new PhraseMatch(wordIndex,wordToken.getIndex()-1,partPhrase);
				    	       itemMatch.setId(UUID.randomUUID().toString());
				    	       itemMatch.setLinkedId(phraseMatch.getId());
				    	       currentPhraseMatchList=matchesTable.get(phrase);
				    	       currentPhraseMatchList.add(itemMatch);
				    	       matchesTable.replace(phrase, currentPhraseMatchList);
					           System.out.println("**Match:"+partPhrase+"  "+itemMatch.getStartWordIndex()+"   "+itemMatch.getEndWordIndex()+"  "+itemMatch.getId()+"  "+itemMatch.getLinkedId()+"  "+wordToken.getToken());
				            }
				    }
				    index++;
			    }
   			 phraseIndex++;
            }	 
		}	
	}
	
	public WordToken doPhrase(Section section, WordToken wordToken, String phrase) {
		Boolean valid=false;
		String item="", match="";
		String[] items=null;
		Integer index=0, wordIndex=0;
		if (wordToken!=null) {
			wordIndex = wordToken.getIndex();
		}
		if ((phrase!=null) && (phrase.length()>0) && (wordIndex<=section.getWordLimit())) {
			items = getSyntaxParts(phrase);
			index = 0;
			valid = true;
			while ((items!=null) && (wordToken!=null) && (valid) && (index<items.length) && (wordIndex<=section.getWordLimit())) {
				item = items[index];
				if (item.contains("phrase")) {
					item = grammar.get(item);
					wordToken = doPhrase(section, wordToken, item);
					if (wordToken!=null) {
					   wordIndex = wordToken.getIndex();
					}
				} else {
					valid = doPart(section, wordToken, item);
					if ((valid) && ((index+1)==items.length)) {
						wordIndex++;
						if (wordIndex<section.getWordLimit()) {
							wordToken = section.getCurrentSentence().get(wordIndex);
						} 
					} else if (valid) {
						       wordIndex++;
						       if (wordIndex<section.getWordLimit()) {
						           wordToken = section.getCurrentSentence().get(wordIndex);
						       }
					}
					if ((!valid) && (optional(item))) {
						valid = true;
					} else if (!valid) {
						wordToken = null;
					}
				}
			  index++;	
			}
		} else {
			wordToken=null;
		}
	  return wordToken;
	}
	
	public List<PhraseMatch> doMultiPhrase(Section section, WordToken wordToken, String phrase) {
		Boolean valid=true, finish=false;
		String item="", match="",phraseline="", phraseitem="";
		String[] items=null;
		String[] phrases=null;
		Integer index=0, wordIndex=0, phraseindex=0;
		List<PhraseMatch> matchList = new ArrayList<>();
		PhraseMatch phraseMatch;
		WordToken localWordToken=null;
		if ((phrase!=null) && (phrase.length()>0)) {
			phraseline = grammar.get(phrase);
			phrases = this.getSyntaxItems(phraseline);
			phraseindex = 0;
			while ((phraseindex<phrases.length) && (!finish)) {
				phraseitem=phrases[phraseindex];
 			    items = getSyntaxParts(phraseitem);
			    index = 0;
			    valid = true;
			    localWordToken = wordToken;
			    wordIndex = wordToken.getIndex();
			    while ((items!=null) && (valid) && (index<items.length) && (wordIndex<section.getWordLimit())) {
				    item = items[index];
				    if (item.contains("phrase")) {
					    item = grammar.get(item);
					    localWordToken = doPhrase(section, wordToken, item);
					    if (localWordToken!=null) {
					       wordIndex = localWordToken.getIndex();
					    }
				    } else {
					    valid = doPart(section, localWordToken, item);
					    if ((valid) && ((index+1)==items.length)) {
						    wordIndex++;
						    phraseMatch = new PhraseMatch(0,wordIndex-1,phraseitem);
						    matchList.add(phraseMatch);
						    if (wordIndex<section.getWordLimit()) {
							    localWordToken = section.getCurrentSentence().get(wordIndex);
						    } 
					    } else if (valid) {
						           wordIndex++;
						           if (wordIndex<section.getWordLimit()) {
						              localWordToken = section.getCurrentSentence().get(wordIndex);
					                }
					    }          
					    if ((!valid) && (optional(item))) {
						    valid = true;
					    } 
				    }
			      index++;	
			    }
			  phraseindex++;
			}
		}
	  return matchList;
	}
	
	public boolean doPart(Section section, WordToken wordToken, String item) {
		Boolean check = false;
		String part = "", postag="";
		if (optional(item)) {
			item = item.substring(1, item.length());
			item = item.substring(0, item.length() - 1);
		}
		part = grammar.get(item);
		System.out.println("**Syntax Part:" + part);
		if (part.startsWith("$")) {
			part = part.substring(1, part.length());
			postag = wordToken.getPostag();
			System.out.println("**Word Postag:" + postag + "  Word:" + wordToken);
			if (regularFunction.doFunction(postag, part, "", wordToken, section)) {
				    check = true;
			} 
			System.out.println("**DoPart Check:" + check);
		}
		return check;
	}
	
	private String[] getSyntaxItems(String items) {
		String[] parts = items.split("\\|");
		return parts;
	}
	
	private String[] getSyntaxParts(String items) {
		char ch;
		Boolean optional = false;
		List<String> parts = new ArrayList<>();
		String[] arrParts = null;
		String phrase = "";
		Integer size = items.length(), count = 0;
		while (count < size) {
			ch = items.charAt(count);
			if (ch == '[') {
				optional = true;
			}
			if (((!optional) && (ch == '>')) || ((optional) && (ch == ']'))) {
				phrase = phrase + ch;
				parts.add(phrase);
				optional = false;
				phrase = "";
			} else {
				phrase = phrase + ch;
			}
			count++;
		}
		arrParts = new String[parts.size()];
		arrParts = parts.toArray(arrParts);
		return arrParts;
	}
	
	private Map<String, String> getGrammar() {
		Map<String, String> grammar = null;
		grammar = this.readResource(GRAMMAR_FILE);
		return grammar;
	}
	
	public Map<String, String> readResource(String filename) {
		BufferedReader bufferedReader = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		Map<String, String> grammar = new HashMap<>();
		String parts[] = null;
		String line = "";
		try {
			inputStream = new ClassPathResource(filename).getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			line = bufferedReader.readLine();
			while (line != null) {
				if ((line != null) && (line.length() > 0)) {
					parts = line.split("=");
					grammar.put(parts[0], parts[1]);
				}
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return grammar;
	}
	
	private boolean optional(String item) {
        Boolean isOptional = false;
		if ((item.startsWith("[")) && (item.endsWith("]"))) {
			isOptional = true;
		}
		return isOptional;
	}
	
	public static void main(String[] args) {
		LanguageTree languageTree = new LanguageTree();
		RegularFunction regularFunction = new RegularFunction();
		WordStorage wordStorage = new WordStorage();
		EnglishParser englishParser = new EnglishParser();
		regularFunction.setLanguageTree(languageTree);
		regularFunction.setWordStorage(wordStorage);
		languageTree.setRegularFunction(regularFunction);
		Section section = null;
		section = englishParser.parseTextToSentence("Daniel was watching the birds");
		languageTree.doGrammar(section);
	}
}
