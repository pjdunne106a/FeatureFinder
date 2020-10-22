package app.util.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RegularFunction {
	public static String[] FUNCTION_FEATURES= {"active:identifies whether a word is an active verb:function:none:function",
			                                   "actionverb:identifies whether a word is an action verb:function:none:function",
			                                   "auxiliary:identifies whether a word is an auxiliary verb:function:none:function",
			                                   "auxiliaryverb:identifies whether a word is an auxiliary verb:function:none:function",
			                                   "anyword:represents any generic word:function:none:function",
			                                   "conjunction:identifies whether a phrase is a conjunction:function:none:function",
			                                   "commonword:identifies whether a word is present in the list of common words:list:none:list",
			                                   "causative:identifies whether a phrase is a causative:function:none:function",
			                                   "coordinatingconjunction:identifies whether a phrase is a coordinating conjunction:function:none:function",
			                                   "startswith:identifies whether a word starts with a known sequence of letters:function:none:function",
			                                   "endswith:identifies whether a word ends with a known sequence of letters:function:none:function",
			                                   "futuretense:identifies whether a phrase is in the future tense:function:none:function",
			                                   "gerund:identifies whether a word is a gerund:function:none:function",
			                                   "intransitiveverb:identifies whether a word is an intransitive verb:function:none:function",
			                                   "irregularverb:identifies whether a word is an irregular word:function:none:function",
			                                   "notcommonword:identifies whether a word is not int he list of common words:function:none:function",
			                                   "notregularword:identifies whether a word is not in the list of regular words:function:none:function",
			                                   "object:identifies whether a word is classed as an object:function:none:function",
			                                   "perfectpasttense:identifies whether a phrase is in the perfect past tense:function:none:function",
			                                   "pastparticiple:identifies whether a word is a past participle:function:none:function",
			                                   "preposition:identifies whether a phrase is a preposition:function:none:function",
			                                   "passive:identifies whether a phrase is a passive phrase:function:none:function",
			                                   "notprevious:identifies whether a word is nor preceded by another word:function:none:function",
			                                   "next:identifies whether a word is followed bby another word:function:none:function",
			                                   "notnext:identifies whether a word is not followd by another word:function:none:function",
			                                   "regularword:identifies whether a word is in the rgular list of words:list:none:list",
			                                   "subject:identifies whether a word can be classed as a subject:function:none:function",
			                                   "transitiveverb:identifies whether a word is a transitive verb:function:none:function",
			                                   "validwithoutprefix:identifies whether a word is still a known word with any prefix removed:function:none:function",
			                                   "verblinked:identifies whether a word islinked to a verb:function:none:function",
			                                   "verb:identifies whether a word is a verb:function:none:function"};
	
	private WordStorage wordStorage;
	private LanguageTree languageTree;

	public RegularFunction() {
		String[] parts=null;
		String item="";
		Integer count=0;
	}
	
	public void initialise() {
		String[] parts=null;
		String item="";
		Integer count=0;
	}

	public Boolean doFunction(String part, String name, String value, WordToken wordToken, Section section) {
		Boolean found = false;
		String content = "", description="";
		String[] parts = null;
		List<String> parameters = null;
		parameters = this.getParameters(name, value);
		System.out.println("  DoFunction, Name:"+name+"  Postag:"+wordToken.getPostag()+"   Reply:"+found);
		switch (name) {
		case "active": {
			found = this.active(part, wordToken, section, parameters);
		}
			;
			break;
		case "actionverb": {
			found = this.actionverb(part, wordToken, section, parameters);
		}
			;
			break;
		case "auxiliary": {
			found = this.auxiliary(wordToken);
		}
			;
			break;
		case "anyword": {
			found = this.anyword(part, wordToken, parameters);
		}
			;
			break;
		case "commonword": {
			found = this.commonword(part, wordToken, parameters);
		}
			;
			break;
		case "causative": {
			found = this.causative(part, wordToken, section, parameters);
		}
			;
			break;
		case "coordinatingconjunction": {
			found = this.coordinatingconjunction(part, wordToken, section, parameters);
		}
			;
			break;
		case "startswith": {
			found = this.startsWith(part, wordToken, parameters);
		}
			;
			break;
		case "endswith": {
			found = this.endsWith(part, wordToken, parameters);
		}
			;
			break;
		case "futuretense": {
			found = this.futureTense(part, wordToken, section, parameters);
		}
			;
			break;
		case "gerund": {
			found = gerund(part, wordToken, section, parameters);
		}
			;
			break;
		case "intransitiveverb": {
			found = this.intransitiveverb(part, wordToken, section, parameters);
		}
			;
			break;	
		case "notcommonword": {
			found = notcommonword(part, wordToken, parameters);
		}
			;
			break;
		case "notregularword": {
			found = notregularword(part, wordToken, parameters);
		}
			;
			break;
		case "perfectpasttense": {
			found = this.perfectpasttense(part, wordToken, section, parameters);
		}
			;
			break;
		case "presentparticiple": {
			found = this.presentParticiple(part, wordToken, section, parameters);
		 }
			;
			break;
		case "preposition": {
			found = this.preposition(part, wordToken, section, parameters);
		 }
			;
			break;
		case "passive": {
			found = this.passive(part, wordToken, section, parameters);
		}
			;
			break;
		case "notprevious": {
			found = this.notPrevious(part, wordToken, section, parameters);
		}
			;
			break;
		case "next": {
			found = this.next(part, wordToken, section, parameters);
		}
			;
			break;
		case "notnext": {
			found = this.notNext(part, wordToken, section, parameters);
		}
			;
			break;
		case "regularword": {
			found = this.regularword(part, wordToken, parameters);
		}
			;
			break;
		case "transitiveverb": {
			found = this.transitiveverb(part, wordToken, section, parameters);
		}
			;
			break;		
		case "validwithoutprefix": {
			found = this.validwithoutprefix(part, wordToken, section, parameters);
		}
			;
			break;
		case "verblinked": {
			found = this.verblinked(part, wordToken, section, parameters);
		}
			;
			break;	
	
		default: {
			System.out.println("  Name:"+name+"  Postag:"+wordToken.getPostag()+"  Token:"+wordToken.getToken()+"   Reply:"+found);
			if (wordStorage.listExists(name)) {
				description = this.getValue(part, wordToken);
				if (wordStorage.wordExists(name, description)) {
					found = true;
				}
			} else if (name.contains(":")) {
				parts = name.split(":");
				name = parts[0];
				description = parts[1];
				found = wordStorage.getPostagSearch().isPostag(wordToken.getPostag(), name, description);
			} else {
				found = wordStorage.getPostagSearch().isPostag(wordToken.getPostag(), name);
				System.out.println("  Name:"+name+"  Postag:"+wordToken.getPostag()+"   Reply:"+found);
			}
		}
	  }
	 return found;
	}

	public void setWordStorage(WordStorage wordStorage) {
		this.wordStorage = wordStorage;
	}
	
	public void setLanguageTree(LanguageTree languageTree) {
		this.languageTree = languageTree;
		this.languageTree.setRegularFunction(this);
	}
	
	protected boolean actionverb(String part, WordToken wordToken, Section section, List<String> parameters) {
		Boolean found = false, isAuxiliary = false, isPastParticiple = false,isVerb = false;
		isAuxiliary = this.auxiliary(wordToken);
		if (!isAuxiliary) {
			isPastParticiple = wordStorage.getPostagSearch().isPostag(part, "verb", "pastparticiple");
			if (!isPastParticiple) {
			    isVerb = verb("postag", wordToken, section, parameters);
			    if (isVerb) {
				   found = true;
			    }
			}    
		}
      return found;		
	}
	protected boolean adjective(String part, WordToken wordToken, Section section, List<String> parameters) {
		Boolean found = false;
		String content = this.getValue(part, wordToken);
		if (wordStorage.getPostagSearch().isPostag(content,"adjective")) {
            found = true;
		}
      return found;		
	}
		
	protected boolean verb(String part, WordToken wordToken, Section section, List<String> parameters) {
			String content = this.getValue(part, wordToken);
			Boolean found = false;
			if (wordStorage.getPostagSearch().isPostag(content,"verb")) {
                found = true;
			}
      return found; 			
	}
	
	protected boolean transitiveverb(String part, WordToken wordToken, Section section, List<String> parameters) {
		String content = this.getValue("postag", wordToken);
		WordToken nextToken=null;
		Integer wordIndex = wordToken.getIndex();
		Boolean found = false;
		Boolean isVerb = false;
		if (wordStorage.getPostagSearch().isPostag(content,"verb")) {
            isVerb = true;
		}
		if ((isVerb) && ((wordIndex+1)<section.getWordLimit())) {
 			nextToken = section.getCurrentSentence().get(wordIndex +1);
 			content = this.getValue("postag", nextToken);
 			if (wordStorage.getPostagSearch().isDeterminer(content)) {
 				if ((wordIndex+2)<section.getWordLimit()) {
 					nextToken = section.getCurrentSentence().get(wordIndex + 2);
 					content = this.getValue("postag", nextToken);
 					if (wordStorage.getPostagSearch().isPostag(content,"noun")) {
 		                found = true;
 				}
			} else if (wordStorage.getPostagSearch().isPostag(content,"noun")) {
                found = true;
			}
 		}
	   }	
	   return found;	
	}
	
	protected boolean intransitiveverb(String part, WordToken wordToken, Section section, List<String> parameters) {
		String content = this.getValue("postag", wordToken);
		WordToken nextToken=null;
		Integer wordIndex = wordToken.getIndex();
		Boolean found = false;
		Boolean isVerb = false;
		if (wordStorage.getPostagSearch().isPostag(content,"verb")) {
            isVerb = true;
		}
		if ((isVerb) && ((wordIndex+1)<section.getWordLimit())) {
 			nextToken = section.getCurrentSentence().get(wordIndex +1);
 			content = this.getValue("postag", nextToken);
 			if (wordStorage.getPostagSearch().isDeterminer(content)) {
 				if ((wordIndex+2)<section.getWordLimit()) {
 					nextToken = section.getCurrentSentence().get(wordIndex + 2);
 					content = this.getValue("postag", nextToken);
 					if (!wordStorage.getPostagSearch().isPostag(content,"noun")) {
 		                found = true;
 				}
			} else if (!wordStorage.getPostagSearch().isPostag(content,"noun")) {
                found = true;
			}
 		}
	   }	
	   return found;	
	}

	protected boolean startsWith(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		String content = "";
		if (part.equalsIgnoreCase("postag")) {
			content = wordToken.getPostag();
		} else if (part.equalsIgnoreCase("lemma")) {
			content = wordToken.getLemma();
		} else if (part.equalsIgnoreCase("token")) {
			content = wordToken.getToken();
		}
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			for (String param : params) {
				param = General.removeQuotes(param);
				if (content.startsWith(param)) {
					found = true;
				}
			}
		}
		return found;
	}
	
	protected boolean commonword(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		String content = "";
		if (part.equalsIgnoreCase("postag")) {
			content = wordToken.getPostag();
		} else if (part.equalsIgnoreCase("lemma")) {
			content = wordToken.getLemma();
		} else if (part.equalsIgnoreCase("token")) {
			content = wordToken.getToken();
		}
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			content = content.toLowerCase();
			if (wordStorage.wordExists("commonwords", content)) {
				found = true;
			}
		}
		return found;
	}
	
	protected boolean notcommonword(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		found = this.commonword(part, wordToken, params);
		return !found;
	}
	
	protected boolean regularword(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		String content = "";
		if (part.equalsIgnoreCase("token")) {
			content = wordToken.getPostag();
		} else {
			content = wordToken.getLemma();
		}
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			content = content.toLowerCase();
			if (wordStorage.wordExists("regularwords", content)) {
				found = true;
			}
		}
		return found;
	}
	
	protected boolean notregularword(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		found = this.regularword(part, wordToken, params);
		return !found;
	}

	protected boolean endsWith(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		String content = "";
		if (part.equalsIgnoreCase("postag")) {
			content = wordToken.getPostag();
		} else if (part.equalsIgnoreCase("lemma")) {
			content = wordToken.getLemma();
		} else if (part.equalsIgnoreCase("token")) {
			content = wordToken.getToken();
		}
		content = General.removeQuotes(content);
		if ((content != null) && (content.length() > 0)) {
			for (String param : params) {
				param = General.removeQuotes(param);
				if (content.endsWith(param)) {
					found = true;
				}
			}
		}
		return found;
	}

	protected boolean previous(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found = false;
		String previousItem = "", param = "";
		Integer wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		Integer paramsIndex = 0;
		List<WordToken> sentence = null;
		WordToken previousToken = null;
		if ((params.size() > 0) && (wordIndex > 1)) {
			sentence = section.getSentenceAtIndex(sentenceIndex - 1);
			previousToken = sentence.get(wordIndex - 2);
			if (part.equalsIgnoreCase("postag")) {
				previousItem = previousToken.getPostag();
			} else if (part.equalsIgnoreCase("lemma")) {
				previousItem = previousToken.getLemma();
			} else if (part.equalsIgnoreCase("token")) {
				previousItem = previousToken.getToken();
			}
			previousItem = General.removeQuotes(previousItem);
			paramsIndex = 0;
			while ((!found) && (paramsIndex < params.size())) {
				param = params.get(paramsIndex);
				param = General.removeQuotes(param);
				if (previousItem.equalsIgnoreCase(param)) {
					found = true;
				}
				paramsIndex++;
			}
		}
		return found;
	}

	protected boolean auxiliary(WordToken wordToken) {
		boolean found = false;
		String lemma = wordToken.getLemma();
		if (this.wordStorage.wordExists("auxiliaryverbs.txt", lemma)) {
			found = true;
		}
		return found;
	}
	
	protected boolean coordinatingconjunction(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found = false;
		String lemma = wordToken.getLemma();
		if (this.wordStorage.wordExists("conjunction", lemma)) {
			found = true;
		}
		return found;
	}

	protected boolean active(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found=false, isVerb=false, pastParticiple=false, isAuxiliary=false, isGerund = false, isLinked=false;
		Integer wordIndex = wordToken.getIndex();
		String postagCurrent = wordToken.getPostag(), nextPostag="";
		String lemmaCurrent = "";
		String tokenCurrent = "";
		lemmaCurrent = wordToken.getLemma();
		postagCurrent = wordToken.getPostag();
		tokenCurrent = wordToken.getToken();
 		isVerb = this.verb("postag", wordToken, section, params);
 		pastParticiple = wordStorage.getPostagSearch().isPostag(postagCurrent, "verb", "pastparticiple");
 		isGerund = wordStorage.getPostagSearch().isGerund(tokenCurrent, postagCurrent);
 		isAuxiliary = wordStorage.wordExists("auxiliaryverbs", lemmaCurrent);
 		isLinked = this.verblinked("postag", wordToken, section, params);
        if (this.perfectpasttense(part,wordToken,section,params)) {
        	found = true;
        } else if ((isVerb || isGerund) && (!isLinked) && (!isAuxiliary) && (!pastParticiple) && (!passive(part, wordToken, section, params))) {
			     found = true;
		}
     	return found;
	}
	
	
	protected boolean verblinked(String part, WordToken wordToken, Section section, List<String> params) {
		Boolean isLinked = false, isVerb = false, isNextVerb = false;
		Integer wordIndex = 0;
		String currentPostag = wordToken.getPostag(), nextPostag="", nextWord="";
		WordToken nextToken=null;
		wordIndex = wordToken.getIndex();
		isVerb = this.verb("postag", wordToken, section, params);
		if ((isVerb) && ((wordIndex+1)<section.getWordLimit())) {
 			nextToken = section.getCurrentSentence().get(wordIndex +1);
 			isNextVerb = this.verb("postag", nextToken, section, params);
 			nextWord = nextToken.getToken();
 			if ((isNextVerb) && (nextWord.endsWith("ing"))) {
 				isLinked = true;
 			}
 		}
	  return isLinked;	
	}
	
	protected Boolean syntaxCheck(String syntaxItem, WordToken wordToken, String type, String form) {
		Boolean check = false;
		String postag = wordToken.getPostag(), lemma = wordToken.getLemma(), token = wordToken.getToken();
		if (syntaxItem.equalsIgnoreCase("complex")) {
			check = false;
			if (type.equalsIgnoreCase("lemma")) {
				 if (lemma.equalsIgnoreCase(form)) {
					 check = true;
				 }
			} else if (type.equalsIgnoreCase("token")) {
				 if (token.equalsIgnoreCase(form)) {
					 check = true;
				 }
			} else if ((type.equalsIgnoreCase("verb")) && (form.equalsIgnoreCase("presnoaux"))) {
				     check = wordStorage.getPostagSearch().isPostag(postag, type, "present");
				     if (check) {
				    	 check = wordStorage.wordExists("auxiliaryverbs.txt", lemma);
				    	 check = !check;
				     }
				     
			} else if ((type.equalsIgnoreCase("verb")) && (form.equalsIgnoreCase("pastnoaux"))) {
			     check = wordStorage.getPostagSearch().isPostag(postag, type, "past");
			     if (check) {
			    	 check = wordStorage.wordExists("auxiliaryverbs.txt", lemma);
			    	 check = !check;
			     }	 
			     
			} else if ((type.equalsIgnoreCase("verb")) && (form.equalsIgnoreCase("pastpartnoaux"))) {
				     check = wordStorage.getPostagSearch().isPostag(postag, type, "pastparticiple");
				     if (check) {
				    	 check = wordStorage.wordExists("auxiliaryverbs.txt", lemma);
				    	 check = !check;
				     }	
			} else {	
			    check = wordStorage.getPostagSearch().isPostag(postag, type, form);
			}
		} else if (syntaxItem.equalsIgnoreCase("auxiliary")) {
			  check = wordStorage.wordExists("auxiliaryverbs.txt", lemma);
		} else {
			check = wordStorage.getPostagSearch().isPostag(postag, syntaxItem, syntaxItem);
		}	
	  return check;
	}

	protected boolean passive(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found=false, auxExist=false, pastParticiple=false, adverb=false;
		Integer wordIndex = wordToken.getIndex(), beforeIndex = 0;
		String postagCurrent = wordToken.getPostag();
		String lemmaBefore = "", lemmaCurrent = "", postagBefore="";
		WordToken previousToken = null;
		
		lemmaCurrent = wordToken.getLemma();
 		pastParticiple = wordStorage.getPostagSearch().isPostag(postagCurrent, "verb", "pastparticiple");
 		if (pastParticiple) {
    		if ((wordIndex-1)>0) {
	    		beforeIndex = wordIndex-1;
		    	previousToken = section.getCurrentSentence().get(beforeIndex);
			    lemmaBefore = previousToken.getLemma();
			    postagBefore = previousToken.getPostag();
                adverb = wordStorage.getPostagSearch().isPostag(postagBefore, "adverb");
                while ((adverb) && ((beforeIndex-1)>0)) {
            	    beforeIndex = beforeIndex-1;
            	    previousToken = section.getCurrentSentence().get(beforeIndex);
    			    lemmaBefore = previousToken.getLemma();
    			    postagBefore = previousToken.getPostag();
                    adverb = wordStorage.getPostagSearch().isPostag(postagBefore, "adverb");
                }
                if (beforeIndex>=0) {
                    previousToken = section.getCurrentSentence().get(beforeIndex);
			        lemmaBefore = previousToken.getLemma();
			        auxExist = wordStorage.wordExists("auxiliaryverbs", lemmaBefore);
                }
		    }
		    if (auxExist) {
     			auxExist = wordStorage.wordExists("auxiliaryverbs", lemmaCurrent);
     			if ((!auxExist) && (!perfectpasttense(part, wordToken, section, params))) {
     				found = true;
     			}
     		}
		}
     	return found;
	}
	
	protected boolean causative(String part, WordToken wordToken, Section section, List<String> params) {
		/* The causative is formed with 'have + object + past participle' */
		/* The causative structure: <subject> + <causative_verb> + <agent> + <verb> + <subject> */
		/* causative verbs: have, make, let, get, ask */ 
		boolean found=false, auxExist=false, foundVerb=false, adverb=false;
		Integer wordIndex = wordToken.getIndex(), afterIndex = 0;
		Integer wordLimit = section.getWordLimit();
		String[] causative_verbs= {"let","permit","allow","make","force","require","have","get","help"};
		String postagCurrent = wordToken.getPostag();
		String lemmaBefore = "", lemmaCurrent = "", postagBefore="";
		List<String> causativeList = Arrays.asList(causative_verbs);
		WordToken previousToken = null;
		
		
     	return found;
	}
	
	
	
	protected boolean futureTense(String part, WordToken wordToken, Section section, List<String >parameters) {
		WordToken nextWordToken = null;
		String[] options= {"will [not] have been <presentparticiple>", 
				           "will [you] have <pastparticiple>|won't have <pastparticiple", 
				           "will [I] be <>|'ll be <presentparticiple>|won't be <presentparticiple>",
				           "will [NOT] <baseverb>|won't <baseverb>"};
		return true;
		
	}
	
	protected boolean preposition(String part, WordToken wordToken, Section section, List<String >parameters) {
		Boolean found = false, verb=false;
		String[] preposition_words= {"through", "since","to", "of", "at", "by", "for", "from", "into", "in", "under", "on", "off", "out", "over", "down", "up", "with"};
		String[] preposition_nouns = {"PRP", "NN", "NNS"};
		List<String> words = Arrays.asList(preposition_words);
		List<String> nouns = Arrays.asList(preposition_nouns);
		Integer wordIndex = wordToken.getIndex(), nextIndex = 0;
		WordToken nextToken = null, previousToken = null;
		String word = wordToken.getToken(), nextWord="", nextPostag="", lemmaBefore="";
		word = word.toLowerCase();
		if ((words.contains(word)) && ((wordIndex+1)<section.getWordLimit())) {
				nextIndex = wordIndex+1;
			    nextToken = this.getWordToken(section, nextIndex);
			    nextPostag = nextToken.getPostag();
			    if (nextToken!=null) {
				    nextWord = nextToken.getToken();
				    if (nextPostag.equalsIgnoreCase("DT")) {
				    	if ((nextIndex+1)<section.getWordLimit()) {
							nextIndex = nextIndex+1;
						    nextToken = this.getWordToken(section, nextIndex);
						    if (nextToken!=null) {
							    nextPostag = nextToken.getPostag();
							    nextWord = nextToken.getToken();
						    }
				         }
				    } else if (words.contains(nextWord)) {
				    	if ((nextIndex+1)<section.getWordLimit()) {
							nextIndex = nextIndex+1;
						    nextToken = this.getWordToken(section, nextIndex);
						    if (nextToken!=null) {
							    nextPostag = nextToken.getPostag();
							    nextWord = nextToken.getToken();
						    }
				         }
				    } 
				    if (nouns.contains(nextPostag)) {
				    	found = true;
				    }
			    } 
		}
	   return found;
	}
	
	protected boolean presentParticiple(String part, WordToken wordToken, Section section, List<String >parameters) {
		Boolean found = false, verb=false;
		Integer wordIndex = wordToken.getIndex(), nextIndex = 0;
		WordToken nextWordToken = null, previousToken = null;
		String word = wordToken.getToken(), postagBefore="", lemmaBefore="";
		if (word.endsWith("ing")) {
				nextIndex = wordIndex-1;
			    previousToken = this.getWordToken(section, nextIndex);
			    if (previousToken!=null) {
				    lemmaBefore = previousToken.getLemma();
				    postagBefore = previousToken.getPostag();
	                verb = wordStorage.getPostagSearch().isPostag(postagBefore, "verb");
	                if (verb) {
	                	found = true;
	                }
			    } 
		}
	   return found;
	}
	
	
	protected boolean gerund(String part, WordToken wordToken, Section section, List<String >parameters) {
		Boolean found = false;
		found = wordStorage.getPostagSearch().isGerund(wordToken.getToken(), wordToken.getPostag());
	    return found;
	}
	
	
	protected boolean perfectpasttense(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found=false, pastExist=false, pastParticiple=false, auxExist=false;
		Integer wordIndex = wordToken.getIndex();
		String postagCurrent = wordToken.getPostag();
		String lemmaBefore = "", lemmaCurrent = "";
		WordToken previousToken = null;
		if (((wordIndex-1)>0) && ((wordIndex-1)<section.getWordLimit())) {
			previousToken = section.getCurrentSentence().get(wordIndex-1);
			lemmaBefore = previousToken.getLemma();
			if (lemmaBefore.equalsIgnoreCase("have")) {
				pastExist=true;
			}
		}
		if (pastExist) {
			lemmaCurrent = wordToken.getLemma();
			auxExist = wordStorage.wordExists("auxiliaryverbs", lemmaCurrent);
     		pastParticiple = wordStorage.getPostagSearch().isPostag(postagCurrent, "verb", "pastparticiple");
     		if ((!auxExist) && (pastParticiple)) {
     			found = true;
     		}
		}
     	return found;
	}

	protected boolean validwithoutprefix(String part, WordToken wordToken, Section section, List<String> parameters) {
		Boolean found = false;
		Integer index =0, wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		String content = this.getValue(part, wordToken), word="", param="";
		index = 0;
		while ((!found) && (index<parameters.size())) {
			param = parameters.get(index);
			param = General.removeQuotes(param);
			param = param.toLowerCase();
			content = General.removeQuotes(content);
			content = content.toLowerCase();
			if (content.startsWith(param)) {
				word = content.substring(param.length(),content.length());
				if (wordStorage.wordExists("commonwords", word)) {
					found = true;
				}
			}
			index++;
		}
		if (found) {
			   if (section.getMatches()!=null) {
			     section.getMatches().add(String.valueOf(sentenceIndex)+":"+String.valueOf(wordIndex)+":"+String.valueOf(wordIndex+1));
		       }
		 }
		return found;
	}
	
	protected boolean anyword(String part, WordToken wordToken, List<String> params) {
		boolean found = false;
		String currentItem = "", param = "";
		Integer paramsIndex = 0;
		if (params.size() > 0) {
			if (part.equalsIgnoreCase("postag")) {
				currentItem = wordToken.getPostag();
			} else if (part.equalsIgnoreCase("lemma")) {
				currentItem = wordToken.getLemma();
			} else if (part.equalsIgnoreCase("token")) {
				currentItem = wordToken.getToken();
			}
			currentItem = General.removeQuotes(currentItem);
			paramsIndex = 0;
			while ((!found) && (paramsIndex < params.size())) {
				param = params.get(paramsIndex);
				param = General.removeQuotes(param);
				if (currentItem.equalsIgnoreCase(param)) {
					found = true;
				}
				paramsIndex++;
			}
		}
		return !found;
	}

	protected boolean notPrevious(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found = false;
		found = !(this.previous(part, wordToken, section, params));
		return found;
	}

	protected boolean next(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found = false;
		String nextItem = "", param = "";
		Integer wordIndex = wordToken.getIndex();
		Integer sentenceIndex = wordToken.getSentence();
		Integer paramsIndex = 0;
		List<WordToken> sentence = null;
		WordToken nextToken = null;
		sentence = section.getSentenceAtIndex(sentenceIndex - 1);
		if ((params.size() > 0) && (wordIndex < sentence.size() - 1)) {
			nextToken = sentence.get(wordIndex);
			if (part.equalsIgnoreCase("postag")) {
				nextItem = nextToken.getPostag();
			} else if (part.equalsIgnoreCase("lemma")) {
				nextItem = nextToken.getLemma();
			} else if (part.equalsIgnoreCase("token")) {
				nextItem = nextToken.getToken();
			}
			paramsIndex = 0;
			nextItem = General.removeQuotes(nextItem);
			while ((!found) && (paramsIndex < params.size())) {
				param = params.get(paramsIndex);
				param = General.removeQuotes(param);
				if (nextItem.equalsIgnoreCase(param)) {
					found = true;
				}
				paramsIndex++;
			}
		}
		return found;
	}

	protected boolean notNext(String part, WordToken wordToken, Section section, List<String> params) {
		boolean found = false;
		found = !(this.next(part, wordToken, section, params));
		return found;
	}

	protected String getValue(String part, WordToken wordToken) {
		String value="";
		if (part.equalsIgnoreCase("token")) {
			value = wordToken.getToken();
		} else if (part.equalsIgnoreCase("lemma")) {
			value = wordToken.getLemma();
		} else if (part.equalsIgnoreCase("postag")) {
			value = wordToken.getPostag();
		} else if (part.equalsIgnoreCase("type")) {
			value = wordToken.getDependency();
		}
		return value;
	}
	
	protected List<String> getParameters(String name, String value) {
		List<String> parameters = new ArrayList<>();
		String functionParameters = "";
		if (value.length()>0) {
			functionParameters = value.substring(name.length() + 1, value.length() - 1);
		    if (functionParameters.length() > 0) {
			   parameters = General.getListParameters(functionParameters);
		    }
		}
		return parameters;
	}
	
	public Section getClauses(Section section) {
	     Integer wordIndex = 0, wordLimit = 0;
		 Section clausesSection = new Section();
		 String word = "";
	     List<String> conjunctions = wordStorage.getWordList("conjuntion");
	     List<WordToken> sentence = section.getCurrentSentence();
	     List<WordToken> clause = new ArrayList<>();
	     WordToken wordToken = null;
         clausesSection.setCurrentWordIndex(0);
         wordLimit = sentence.size();
         System.out.println("**GetClauses**");
         for (String str:conjunctions) {
        	 System.out.println("**Conjunction:"+str+"**");
         }
	     while (wordIndex<wordLimit) {
	    	 wordToken = sentence.get(wordIndex);
	    	 System.out.println("**GetClauses:"+wordToken.getToken());
	    	 word = wordToken.getToken().toLowerCase();
	    	 System.out.println("**Word:"+word+"**");
	    	 if ((conjunctions.contains(word) && (clause.size()>0))) {
	    		 clausesSection.addSentence(clause);
	    		 clause = new ArrayList<>();
	    		 System.out.println("**GetClauses; Adding Sentence");
	    	 } else {
	    		 System.out.println("**GetClauses: Adding new Word");
	    		 clause.add(wordToken);
	    	 }
	    	 wordIndex++;
	     }
	     if (clause.size()>0) {
	    	 clausesSection.addSentence(clause);
	     }
	     return clausesSection;
  	}
	
	private WordToken getWordToken(Section section, Integer index) {
		WordToken wordToken=null;
		if ((index>0) && (index<section.getWordLimit())) {
    	    wordToken = section.getCurrentSentence().get(index);
		}
	    return wordToken;	
	}
}
