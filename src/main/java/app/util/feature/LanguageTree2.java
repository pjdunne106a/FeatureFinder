package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class LanguageTree2 {
	private static final String GRAMMAR_FILE = "english_grammar.txt";
	private Map<String, String> grammar;
	private RegularFunction regularFunction;

	public LanguageTree2() {
		grammar = this.getGrammar();
	}

	public void setRegularFunction(RegularFunction regularFunction) {
		this.regularFunction = regularFunction;
	}
	
	public String parseSentence(Section section) {
		Integer sentenceIndex = 0, sentenceLimit = 0, clauseIndex = 0;
		List<WordToken> sentence = null;
		Section clausesSection = null;
		String resultString = "", resultClause = "";
		sentenceLimit = section.getSentenceCount();
		while (sentenceIndex<sentenceLimit) {
			section.setCurrentSentence(sentenceIndex);
			clausesSection = regularFunction.getClauses(section);
			clauseIndex = 0;
			while (clauseIndex<clausesSection.getSentenceCount()) {
				clausesSection.setCurrentSentence(clauseIndex);
				sentence = clausesSection.getCurrentSentence();
			    resultClause = parseClause(clausesSection);
			    System.out.println("** ResultClause:"+resultClause);
			    resultString = resultString + resultClause;
  			    clauseIndex++; 
			}  
			if (resultString.length()==0) {
				    resultString = "[]";
			} else {
				    resultString = resultString.substring(0, resultString.length() - 1);
				    resultString = "[" + resultString + "]";
		    }
			sentenceIndex++;
		}
	
	  return resultString;
	}
	
	public List<String> getClosestMatches(Map<String, List<PhraseMatch>> matchesTable, Integer wordLimit) {
		Integer subjectbegin=0, subjectend=0, largestEnd=-1, verbBegin, verbEnd, objectBegin, objectEnd, listindex=0, index=0;
		String subjectindexes="", verbindexes, objectindexes, part;
		String[] parts=null;
		List<String> indexeslist=new ArrayList<>();
		List<String> closestMatchList=new ArrayList<>();
		List<PhraseMatch> subject=null, verb=null, object=null,verbLinkedMatches=null,objectLinkedMatches=null;
		subject = matchesTable.get("<subjectphrase>");
		verb = matchesTable.get("<verbphrase>");
		object = matchesTable.get("<objectphrase>");
		if ((subject!=null) && (subject.size()>largestEnd)) {
			largestEnd = subject.size();
		}
		if ((verb!=null) && (verb.size()>largestEnd)) {
			largestEnd = verb.size();
		}
		if ((object!=null) && (object.size()>largestEnd)) {
			largestEnd = object.size();
		}
		for (int i=0;i<largestEnd;i++) {
			indexeslist.add("");
		}
		index=0;
		for (PhraseMatch subjectlinkmatch:subject) {
		    subjectbegin = subjectlinkmatch.getStartWordIndex();
			subjectend = subjectlinkmatch.getEndWordIndex();
			subjectindexes = String.valueOf(subjectbegin) + ":" + String.valueOf(subjectend);
			indexeslist.set(index,subjectindexes);
			index++;
		}
		if ((verb!=null) && (verb.size()>0)) {
			index = 0;
			listindex = 0;
			while (index<indexeslist.size()) {
				part = indexeslist.get(listindex);
				System.out.println("**Part:"+part+"**");
				if ((part!=null) && (part.length()>0)) {
					parts = part.split(":");
				    subjectbegin = Integer.valueOf(parts[0]);
				    subjectend = Integer.valueOf(parts[1]);
				} else {
					subjectbegin = -1;
					subjectend = -1;
				}
			    verbLinkedMatches=getLinkedMatches(verb,subjectbegin, subjectend);
			    for (PhraseMatch verblinkmatch:verbLinkedMatches) {
						   verbBegin = verblinkmatch.getStartWordIndex();
						   verbEnd = verblinkmatch.getEndWordIndex();
						   verbindexes = String.valueOf(verbBegin) + ":" + String.valueOf(verbEnd);
						   if (subjectend==-1) {
						        indexeslist.set(listindex,verbindexes);
						        listindex++;
						   } else {
							    indexeslist.set(listindex,part + "," + verbindexes);
						   }
						   if ((subject!=null) && (subject.size()>0)) {
						       objectLinkedMatches=getLinkedMatches(subject,verbBegin, verbEnd);
						       for (PhraseMatch objectlinkmatch:objectLinkedMatches) {
							       objectBegin = objectlinkmatch.getStartWordIndex();
							       objectEnd = objectlinkmatch.getEndWordIndex();
							       objectindexes = String.valueOf(objectBegin) + ":" + String.valueOf(objectEnd);
							       if (subjectend==-1) {
							           indexeslist.set(listindex, verbindexes + "," + objectindexes);
							           listindex++;
							       } else {
							    	   indexeslist.set(listindex,part + "," + verbindexes + "," + objectindexes);
							       }
							       
						       }
						   } else {
							   if (subjectend==-1) {
							       indexeslist.set(listindex,verbindexes);
							       listindex++;
							   } else {
								   indexeslist.set(listindex,part + "," + verbindexes);
							   }
						   }
				}
			   if (subjectend==-1) {
				   index=indexeslist.size();
			   } else {
   			       index++;
			    }
			}
		}
		for (String str:indexeslist) {
			System.out.println("IndexesList:"+str);
		}
		return closestMatchList;
	}
	
	public List<PhraseMatch> getLinkedMatches(List<PhraseMatch> matches,Integer begin, Integer end) {
		List<PhraseMatch> phraseMatches = new ArrayList<>();
		if ((begin==-1) && (end==-1)) {
			for (PhraseMatch match:matches) {
				phraseMatches.add(match);
			}
		} else {
			for (PhraseMatch match:matches) {
				if (match.getEndWordIndex()==end) {
				   phraseMatches.add(match);
				}
			}
		}
		return phraseMatches;
	}

	public String parseClause(Section section) {
		Boolean check = false;
		Integer matchIndex = 0, matchesSize = 0;
		Map<String, List<PhraseMatch>> matchesTable = null;
		List<PhraseMatch> subjectMatches = null;
		List<PhraseMatch> verbMatches = null;
		List<PhraseMatch> objectMatches = null;
		PhraseMatch phraseMatch = null;
		String resultString = "", phrase = "";
		matchesTable = doPhrase(section, "<sentence>");
		if (matchesTable != null) {
			System.out.println("**** PHRASE:" + phrase);
			subjectMatches = matchesTable.get("<subjectphrase>");
			verbMatches = matchesTable.get("<verbphrase>");
			if ((verbMatches!=null) && (verbMatches.size()>0)) {
			    for (PhraseMatch str:verbMatches) {
				   System.out.println("**Verb Matches:"+str.getPhrase());
			    }
			}
			objectMatches = matchesTable.get("<objectphrase>");
			if ((subjectMatches!=null) && (subjectMatches.size()>0)) {
				matchesSize = subjectMatches.size();
			} else if ((verbMatches!=null) && (verbMatches.size()>0)) {
				matchesSize = verbMatches.size();
			}
			resultString = "";
			matchIndex = 0;
			while (matchIndex < matchesSize) {
				    if ((subjectMatches!=null) && (subjectMatches.size()>0)) {
					    phraseMatch = subjectMatches.get(matchIndex);
					    resultString = resultString + phraseMatch.getPhrase() + ":" + phraseMatch.getStartWordIndex() + ":"
							+ phraseMatch.getEndWordIndex() + ",";
				    }
					if ((verbMatches != null) && (matchIndex < verbMatches.size())) {
						phraseMatch = verbMatches.get(matchIndex);
						resultString = resultString + phraseMatch.getPhrase() + ":" + phraseMatch.getStartWordIndex()
								+ ":" + phraseMatch.getEndWordIndex() + ",";
					}
					if ((objectMatches != null) && (matchIndex < objectMatches.size())) {
						phraseMatch = objectMatches.get(matchIndex);
						resultString = resultString + phraseMatch.getPhrase() + ":" + phraseMatch.getStartWordIndex()
								+ ":" + phraseMatch.getEndWordIndex();
					}
					if (resultString.endsWith(",")) {
						resultString = resultString.substring(0, resultString.length() - 1);
					}
					resultString = "{" + resultString + "},";
					matchIndex++;
			}
		}
		// getClosestMatches(matchesTable, section.getWordLimit());
		return resultString;
	}

	public Map<String, List<PhraseMatch>> doPhrase(Section section, String item) {
		Boolean found = true;
		List<PhraseMatch> phraseMatchList = null;
		Map<String, List<PhraseMatch>> matchesTable = new HashMap<>();
		String phrases = "", currentPhrase = "", previousPhrase = "";
		String[] phraseList = null;
		Integer phraseListCount = 0;
		phrases = grammar.get(item);
		phraseList = this.getSyntaxParts(phrases);
		while ((found) && (phraseListCount < phraseList.length)) {
			currentPhrase = phraseList[phraseListCount];
			System.out.println("** DoPhrase:  SentenceIndex:"+section.getCurrentSentenceIndex());
			found = matchesExpressions(matchesTable, section, currentPhrase, previousPhrase);
			if ((!found) && ((currentPhrase.contains("subject")) || (currentPhrase.contains("verb")))) {
				 found = true;
			}
			System.out.println("**After MatchesExpression:  SentenceIndex:"+section.getCurrentSentenceIndex());
			previousPhrase = currentPhrase;
			phraseListCount++;
		}

		return matchesTable;
	}

	public boolean matchesExpressions(Map<String, List<PhraseMatch>> matchesTable, Section section, String phrase,
			String previousPhase) {
		Boolean found = false, match = false;
		Integer previousMatchesTableCount = -1, previousPhraseMatchesLimit = 0, wordIndex = 0, index = 0, wordLimit = 0, 
				expressionsIndex = 0, currentWordIndex = 0;
		List<PhraseMatch> phraseMatchList = null, previousPhraseMatchList = null;
		PhraseMatch phraseMatch, previousPhraseMatch;
		String[] expressions = null, expressionParts = null;
		String expressionPart = "", expressionPhrase = "", matchPart = "";
		currentWordIndex = section.getCurrentSentence().get(0).getIndex();
		System.out.println("**********PHRASE:" + phrase + "********************");
		if (previousPhase.length() > 0) {
			previousPhraseMatchList = matchesTable.get(previousPhase);
			if (previousPhraseMatchList!=null) {
			   previousPhraseMatchesLimit = previousPhraseMatchList.size();
			   previousMatchesTableCount=0;
			}
		}
		while (previousMatchesTableCount < previousPhraseMatchesLimit) {
			System.out.println("**********PreviousMatchesLimit:" + previousPhraseMatchesLimit); 
			System.out.println("**********PreviousMatchesTableCount:" + previousMatchesTableCount);
			if (previousPhraseMatchesLimit > 0) {
				previousPhraseMatch = previousPhraseMatchList.get(previousMatchesTableCount);
				wordIndex = previousPhraseMatch.getEndWordIndex();
			} else {
				wordIndex = currentWordIndex;
			}
			expressionPhrase = grammar.get(phrase);
			expressions = this.getSyntaxItems(expressionPhrase);
			expressionsIndex = 0;
			match = false;
			for (String str:expressions) {
				System.out.println("** Matches Expression:"+str);
			}
			while (expressionsIndex < expressions.length) {
				expressionPart = expressions[expressionsIndex];
				if (expressionPart.contains("phrase")) {
					expressionPart = grammar.get(expressionPart);
				}
				expressionParts = this.getSyntaxItems(expressionPart);
				wordIndex = currentWordIndex;
				System.out.println("**WordIndex:"+currentWordIndex+"  SentenceIndex:"+section.getCurrentSentenceIndex());
				System.out.println("** Expression Loop:" + expressionPart + "***ExpressionsIndex:" + expressionsIndex);
				matchPart = doOnePhrase(wordIndex, section, expressionParts, phrase);
				System.out.println("**WordIndex:"+section.getCurrentWordIndex());
				System.out.println("**Phrase:"+phrase);				
				if ((matchPart.length()>0) && (phrase.contains("object"))) {
				    	index = section.getCurrentWordIndex();
				    	wordLimit = section.getWordLimit();
				    	if (index == wordLimit) {
				    		match = true;
				    	}
	  		    } else if (matchPart.length() > 0) {
					match = true;
				}
				System.out.println("**WordIndex index:"+index+"  "+wordLimit);
				if (match) {
					phraseMatch = new PhraseMatch(wordIndex, wordIndex, matchPart);
					phraseMatchList = matchesTable.get(phrase);
					if (phraseMatchList == null) {
						phraseMatchList = new ArrayList<>();
					}
					phraseMatchList.add(phraseMatch);
					matchesTable.put(phrase, phraseMatchList);
					System.out.println("**Match Adding To:"+phrase+"  Match:"+matchPart);
					System.out.println("Phrases:");
					for (PhraseMatch phrasematch:phraseMatchList) {
						System.out.println("**Phrase:"+phrasematch.getPhrase());
					}
					found = true;
					match = false;
				}
				expressionsIndex++;
			}
			System.out.println("**While Loop:"+section.getCurrentSentenceIndex());
			previousMatchesTableCount++;
		}
		return found;
	}

	public String doOnePhrase(Integer currentWordIndex, Section section, String[] syntaxItems, String phrase) {
		Boolean validPart = true, optional = false, found = false;
		String syntaxPart = "", itemPart = "", part = "", matchPart = "";
		String[] syntaxParts = null;
		Integer itemIndex = 0, partIndex = 0, wordIndex = 0, wordLimit = 0, tokenIndex = 0, actualWordIndex=0;
		/* One alternative expression from a phrase */
		System.out.println("** SentenceIndex:"+section.getCurrentSentenceIndex());
		wordIndex = currentWordIndex;
		wordLimit = section.getWordLimit();
		System.out.println("**DoOnePhrase Sentence:" + wordIndex + "***WordLimit:" + wordLimit);
		while ((!found) && (itemIndex < syntaxItems.length) && (wordIndex < wordLimit)) {
			syntaxPart = syntaxItems[itemIndex];
			syntaxParts = this.getSyntaxParts(syntaxPart);
			System.out.println("** Phrase Loop Index:" + itemIndex + "***Part:" + syntaxPart + "**Syntax Parts:"
					+ syntaxParts.length);
			partIndex = 0;
			validPart = true;
			tokenIndex = currentWordIndex;
			System.out.println("** WordIndex:"+tokenIndex);
			while ((validPart) && (partIndex < syntaxParts.length)) {
				itemPart = syntaxParts[partIndex];
				optional = false;
				System.out.println("**Do Phrase, item part:" + itemPart);
				part = itemPart;
				if ((part.startsWith("[")) && (part.endsWith("]"))) {
					part = part.substring(1, part.length() - 1);
					optional = true;
				}
				part = grammar.get(part);
				if ((part != null) && (part.startsWith("$"))) {
					validPart = doPart(tokenIndex, section, itemPart);
				} else {
					validPart = doSubPart(tokenIndex, section, part, phrase);
				}
				if ((!validPart) && (optional)) {
					validPart = true;
				}
				partIndex++;
			}
			itemIndex++;
			if (phrase.contains("object")) {
			       actualWordIndex = itemIndex;
			       if (!(actualWordIndex>=wordLimit)) {
			    	   validPart = false;
			       }
			}
			System.out.println("** Outside of the While loop WordIndex:"+itemIndex+"  Actual:"+section.getCurrentWordIndex());
			if ((validPart) && (syntaxParts.length>0) && (partIndex == syntaxParts.length)) {
				matchPart = syntaxPart;
				System.out.println("** Match:"+syntaxPart);
			} else {
				section.setCurrentWordIndex(itemIndex);
			}
		}
		System.out.println("** Outside of the outer While loop WordIndex:"+section.getCurrentWordIndex());
		return matchPart;
	}

	public boolean doSubPart(Integer tokenIndex, Section section, String part, String phrase) {
		Boolean matching = false, validPart = true;
		Integer itemIndex = 0, partIndex = 0, wordIndex = 0, wordLimit = 0;
		String syntaxSubPart = "", itemPart = "";
		String[] syntaxItems = this.getSyntaxItems(part);
		String[] syntaxSubParts = null;
		while ((matching) && (itemIndex < syntaxItems.length)) {
			syntaxSubPart = syntaxItems[itemIndex];
			syntaxSubParts = this.getSyntaxParts(syntaxSubPart);
			System.out.println("** Phrase Loop Subpart:" + syntaxSubPart + "**Syntax Parts:" + syntaxSubParts.length);
			partIndex = 0;
			validPart = true;
			while ((validPart) && (itemIndex < syntaxSubParts.length)) {
				itemPart = syntaxSubParts[partIndex];
				System.out.println("**Do Phrase, item part:" + itemPart);
				validPart = doPart(tokenIndex, section, itemPart);
				if (validPart) {
					tokenIndex++;
				}
				partIndex++;
			}
			itemIndex++;
		}
		if ((validPart) && (itemIndex == syntaxItems.length)) {
			if (phrase.startsWith("object")) {
				wordLimit = section.getWordLimit();
				System.out.println("***DoSubPart:"+wordIndex+"  "+wordLimit);
				if (tokenIndex==wordLimit) {
					matching = true;
				}
			} else {
			        matching = true;
			}        
		}
		return matching;
	}

	public boolean doPart(Integer tokenIndex, Section section, String item) {
		Boolean check = false;
		Integer wordLimit = 0;
		String part = "", postag="";
		String type = findType(item);
		List<WordToken> sentence = null;
		WordToken wordToken = null;
		if (type.equalsIgnoreCase("optional")) {
			item = item.substring(1, item.length());
			item = item.substring(0, item.length() - 1);
		}
		part = grammar.get(item);
		System.out.println("**Syntax Type:" + type);
		System.out.println("**Syntax Part:" + part);
		if (part.startsWith("$")) {
			part = part.substring(1, part.length());
			sentence = section.getCurrentSentence();
			wordLimit = section.getWordLimit();
			if (tokenIndex < wordLimit) {
				wordToken = sentence.get(tokenIndex);
				postag = wordToken.getPostag();
				System.out.println("**Word Postag:" + postag + "  Word:" + wordToken);
				if (regularFunction.doFunction(postag, part, "", wordToken, section)) {
				    check = true;
				} 
				System.out.println("**DoPart Check:" + check);
			}
		}
		return check;
	}

	private String findType(String item) {
		String type = "mandatory";
		if ((item.startsWith("[")) && (item.endsWith("]"))) {
			type = "optional";
		}
		return type;
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

	private String[] getSyntaxItems(String items) {
		String[] parts = items.split("\\|");
		return parts;
	}

	private String getItemName(String item) {
		String name = item;
		if (name.startsWith("<")) {
			name = name.substring(1, name.length());
		}
		if (name.endsWith(">")) {
			name = name.substring(0, name.length() - 1);
		}
		return name;
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

}