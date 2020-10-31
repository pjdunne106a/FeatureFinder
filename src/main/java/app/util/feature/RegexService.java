package app.util.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import io.swagger.annotations.ApiOperation;
 
@RestController
public class RegexService { 

	@Autowired
	private EnglishParser englishParser;
	@Autowired
	private RegularFunction regularFunction;
	@Autowired
	WordStorage  wordStorage;
	@Autowired
	LanguageTree  languageTree;
	@Autowired
	RegexLibrary  regexLibrary;
	@Autowired
	Documentation documentation;
	@Autowired
	private WebApplicationContext applicationContext;
	
	@RequestMapping(value = "/singleregexrun", method = RequestMethod.GET)
    public String regexcount( @RequestParam String granularity, @RequestParam String text, @RequestParam String firstregex, @RequestParam(required = false) String secondregex ) { 
	  Matcher matcher1 = null, matcher2 = null;
	  Double matches1 = new Double(0);
	  Double matches2 = new Double(0);
	  Integer wordCount = 0;
	  Double average = new Double(0);
	  Double divisor;
	  Integer count1=0, count2=0;
	  Section section = englishParser.parseTextToSentence(text);
	  String matches = "";
	  if ((firstregex!=null) && (firstregex.length()>0)) {
		  regularFunction.initialise();
		  matcher1 = new Matcher(firstregex, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
		  count1 = matcher1.matchcount(section, granularity);
		  matches = matches + "firstregex:"+String.valueOf(count1) +",\n";
	  }
	  if ((secondregex!=null) && (secondregex.length()>0)) {
		  regularFunction.initialise();
		  matcher2 = new Matcher(secondregex, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
		  count2 = matcher2.matchcount(section, granularity);
		  matches = matches + "secondregex:"+String.valueOf(count2) +",\n";
	  }
	  matches = matches + "sentences:"+section.getSentenceCount() + ",\n";
	  wordCount = section.getWordCount();
	  matches = matches + "words:"+section.getWordCount() + ",\n";
	  if (wordCount>1) {
		    matches1 = new Double(count1);
		    matches2 = new Double(count2);
		    divisor = new Double(wordCount);
	        average = (matches1+matches2)/divisor;
	        average = average * 100;
	  } else {
		  average = new Double(0);
	  }
	  matches = matches + "average matches/words:"+String.format("%5.2f",average) + "%";
	  matches = "{ \n" + matches + "\n }";
      return matches;
    }
	
	
	@RequestMapping(value = "/groupregexrun", method = RequestMethod.GET)
    public String groupregexrun( @RequestParam String granularity, @RequestParam String text, @RequestParam String regexlist ) { 
	  Matcher matcher = null;
	  Map<String, Object> groupList;
	  Double matches1 = new Double(0);
	  Double matches2 = new Double(0);
	  Integer wordCount = 0;
	  JSONArray jsonArray = null;
	  Double average = new Double(0);
	  Double divisor;
	  Integer count1=0, count2=0;
	  Section section = englishParser.parseTextToSentence(text);
	  String matches = "", regex="", results="", itemStr="", jsonStr = "";
	  String[] regexArray = null;
	  if ((regexlist!=null) && (regexlist.length()>0)) {
		  results = "[";
		  regexArray = regexlist.split(",");
		  groupList = regexLibrary.getGroupNames();
		  for (String key: regexArray) {
		      jsonArray = (JSONArray) groupList.get(key);
		      System.out.println("**********JSON Array:"+jsonArray.toString());
		      if (jsonArray!=null) {
                 results = results + key + ":[";				 
			     for (int i=0; i<jsonArray.size(); i++) {
			             jsonStr = (String)jsonArray.get(i);
			             System.out.println("**********JSON Item:"+i+"  JsonStr:"+jsonStr);
 			             itemStr = regexLibrary.getFeatureRegex(key, jsonStr);
 			             System.out.println("**********JSON Item:"+i+"  ItemStr:"+itemStr);
			             if (itemStr != null) {
			            	 regularFunction.initialise();
			            	 matcher = new Matcher(itemStr, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
			            	 wordCount = matcher.matchcount(section, granularity);
			    	         results = results + jsonStr + ":" + wordCount + ",";
			    	     }
			     }
   		       results = results + "]";  
	        }
		  }		 
	   results = results + "]";
	  }	  
      return results;
    }
	
	
	@RequestMapping(value = "/runregex", method = RequestMethod.GET)
    public String runregex(@RequestParam String text, @RequestParam String granularity, @RequestParam String regex ) { 
	  regularFunction.initialise();
	  Section section = englishParser.parseTextToSentence(text);
	  Matcher matcher = new Matcher(regex, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
      Integer count = matcher.matchcount(section, granularity);
      String matches = "Regex matches:"+String.valueOf(count);
      return matches;
    }
	
	@RequestMapping(value = "/runfeature", method = RequestMethod.GET)
    public String runfeature(@RequestParam String featurename, @RequestParam String text, @RequestParam String granularity) { 
	  regularFunction.initialise();
	  String response="";
	  String featureRegex = regexLibrary.getFeatureRegex(featurename);
	  Section section = englishParser.parseTextToSentence(text);
	  Matcher matcher = new Matcher(featureRegex, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
      Integer matches = matcher.matchcount(section, granularity);
      response = "Feature matches:"+String.valueOf(matches);
      return response;
    }
	
	@RequestMapping(value = "/postagtext", method = RequestMethod.GET)
    public String postagtext(@RequestParam String text ) { 
	  Section section = englishParser.parseTextToText(text);
	  List<WordToken> wordTokenList = section.getSentenceAtIndex(0);
	  String reply = "[";
	  for (WordToken wordToken:wordTokenList) {
		  reply = reply + "{";
		  reply = reply + "\"token\":";
		  reply = reply + "\""+wordToken.getToken()+"\",";
		  reply = reply + "\"lemma\":";
		  reply = reply + "\""+wordToken.getLemma()+"\",";
		  reply = reply + "\"postag\":";
		  reply = reply + "\""+wordToken.getPostag()+"\",";
		  reply = reply + "\"dependency\":";
		  reply = reply + "\""+wordToken.getDependency()+"\"";
		  reply = reply + "},";
	  }
	  reply = reply.substring(0,reply.length()-1);
	  reply = reply + "]";
      return reply;
    }
	
	@RequestMapping(value = "/texttodepgraph", method = RequestMethod.GET)
    public String texttodepgraph(@RequestParam String text ) { 
	  String graphString = englishParser.textToGraph(text);
      return graphString;
    }
	
	@RequestMapping(value = "/parsetext", method = RequestMethod.GET)
    public String parsetext(@RequestParam String text ) { 
	  Section section = englishParser.parseTextToText(text);
	  String results="";
	  regularFunction.setWordStorage(wordStorage);
	  languageTree.setRegularFunction(regularFunction);
	  results = languageTree.doGrammar(section);
      return results;
    }
	
	@RequestMapping(value = "/grouplist", method = RequestMethod.GET)
    public String grouplist() {
	  String regexes = regexLibrary.getGroupList();	
      return regexes;
    }
	
	@RequestMapping(value = "/featurelist", method = RequestMethod.GET)
    public String featurelist() {
	  String regexes = regexLibrary.getFeatureList();	
      return regexes;
    }
	
	@RequestMapping(value = "/documentation", method = RequestMethod.GET)
    public String documentation() { 
	  String docs = documentation.getDocumentation(applicationContext);
      return docs;
    }
	
	@ApiOperation(value = "/passivetest", hidden = true)
    public String passivetest(@RequestParam String text ) { 
	  String[] texts= {"The card was made by Fred.",
	  "The crisp packet was thrown away.",
	  "The egg was laid by the bird.",
	  "Susan found her car keys.",
	  "The policeman chased after Fred.",
	  "The pencil had been lost."};
	  String regex="<token=passive()>";
	  String granularity="text";
	  Section section = englishParser.parseTextToSentence(text);
	  Matcher matcher = new Matcher(regex, regularFunction, wordStorage,languageTree, regexLibrary, applicationContext);
	  String matches = null;
	  Map<String, String> results = new HashMap<>();
      for (String str: texts) {
    	  section = englishParser.parseTextToText(text);
	      matches = matcher.matchtext(section, granularity);
	      results.put(str,matches);
      }
       return results.toString();
    }
	
	@ApiOperation(value = "/activetest", hidden = true)
    public String activetest(@RequestParam String text ) { 
	  Section section = englishParser.parseTextToSentence(text);
	  String[] texts= {"The key was used to open the door.",
	  "Mark was eating an apple.",
	  "The picture was painted by Bob.",
	  "Tina opened the present.",
	  "The phone was being used by Mr Thomas.",
	  "James hit the tree with his stick.",
	  "The man jumped off the step.",
	  "The man threw himself off the step",
	  "Daniel was watching the birds.",
	  "James couldn’t see the man.",
	  "Susan found her car keys.",
	  "The boy picked up the coin.",
	  "The policeman chased after Fred.",
	  "The car was fixed.",
      "Mark was given a warning.",
	  "I was delighted to see you",
	  "I love",
	  "I am loving",
	  "I do love",
	  "I will love",
	  "I will be loving",
	  "I loved",
	  "I have loved",
	  "I did love",
	  "I had loved",
	  "I was loving",
	  "I will have loved"};
	  String regex="<token=active()>";
	  String granularity="text";
	  Matcher matcher = new Matcher(regex, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
	  String matches = null;
	  Map<String, String> results = new HashMap<>();
      for (String str: texts) {
    	  section = englishParser.parseTextToText(text);
	      matches = matcher.matchtext(section, granularity);
	      results.put(str,matches);
      }
       return results.toString();
    }
	
	
	
	
	
	
	
   
}