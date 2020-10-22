package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;

import edu.washington.cs.knowitall.regex.Match;

public class RegexHandler {
      private TextRegularExpressionParser textLogicExpressionParser;
      private TextRegularExpression logicExpression;
      private RegularFunction regularFunction;
      private TextBlock textBlock;
      private RegexLibrary regexLibrary;
      
	  public RegexHandler(String regex, RegularFunction regularFunction, WordStorage wordStorage, LanguageTree languageTree, RegexLibrary regexLibrary,  WebApplicationContext applicationContext) {
          String refinedRegex="";
		  this.regularFunction = regularFunction;
          this.regularFunction.setLanguageTree(languageTree);
          this.textBlock = new TextBlock();
          this.regexLibrary = regexLibrary;
		  textLogicExpressionParser = new TextRegularExpressionParser(regularFunction, wordStorage, textBlock, regexLibrary);
		  refinedRegex = preProcess(regex);
		  logicExpression = textLogicExpressionParser.process(refinedRegex);
	  }
	  
	  public Integer matchescount(List<WordToken> wordTokenList, Section section) {
		  Integer finds=0;
		  List<Match<WordToken>> matches=null;
		  this.textBlock.setSection(section);
		  this.textBlock.setTextBlockExpression(new TextBlockExpression());
		  matches = logicExpression.findAll(wordTokenList);
		  finds = matches.size();
		  return finds;
	  }
	  
	  public String matchestext(List<WordToken> wordTokenList, Section section) {
		  String match="[", innermatch="";
		  String[] parts = null;
		  Integer sentenceIndex=0, startIndex=0, endIndex=0, previousIndex=0;
		  List<Match<WordToken>> matches=null;
		  List<WordToken> tokens=null;
		  List<String> matchList = new ArrayList<>();
		  List<Match.Group<WordToken>> groupToken=null;
		  section.setMatches(matchList);
		  this.textBlock.setSection(section);
		  this.textBlock.setTextBlockExpression(new TextBlockExpression());
		  matches = logicExpression.findAll(wordTokenList);
		  innermatch = "";
		  /*
		  for (Match<WordToken> matchtoken:matches) {
			  groupToken = matchtoken.groups();
			  innermatch = "'";
			  for (Match.Group<WordToken> groups:groupToken) {
			       tokens = groups.tokens();
			       for (WordToken wordToken:tokens) {
				        innermatch = innermatch + wordToken.getToken() + " ";
			       }
			  }     
              match = match + innermatch + "',";
		  }
		  */
		  innermatch="";
		  match="[";
		  previousIndex = -1;
		  matchList = textBlock.getSection().getMatches();
		  for (String str:matchList) {
			  parts = str.split(":");
			  sentenceIndex = Integer.valueOf(parts[0]);
			  startIndex = Integer.valueOf(parts[1]);
			  endIndex = Integer.valueOf(parts[2]);
			  if (startIndex>previousIndex) {
			      if (previousIndex==startIndex) {
				      innermatch = innermatch.substring(0,innermatch.length()-2);
			      } else {
   			          innermatch = innermatch +"'";
			      }    
			      for (int i=startIndex; i<=endIndex; i++) {
				     innermatch = innermatch + wordTokenList.get(i).getToken() + " ";
			      }
			     previousIndex = endIndex;
			     innermatch = innermatch + "',";
			  }
		  }
		  if (innermatch.length()>1) {
			  innermatch = innermatch.substring(0,innermatch.length()-1);
		      match = match + innermatch;
		  }    
		  match = match + "]";
		  return match;
	  }
	  
	  
	  private String preProcess(String regex) {
		  String refinedRegex = regex;
		  if (regex.contains("<...>")) {
			  refinedRegex = regex.replace("<...>", "<token=anyword()>*");
		  }
		  return refinedRegex;
	  }
	  
}
