package app.util.feature;

import edu.washington.cs.knowitall.logic.LogicExpression;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;

public class TextRegularBaseExpression extends BaseExpression<WordToken> {
       private TextLogicExpressionParser textLogicExpressionParser;
       private LogicExpression<WordToken> logicExpression;
       
	   public TextRegularBaseExpression(String regex, RegularFunction regularFunction, WordStorage wordStorage, TextBlock textBlock, RegexLibrary regexLibrary) {
           super(regex);
		   textLogicExpressionParser = new TextLogicExpressionParser(regularFunction, wordStorage, textBlock, regexLibrary);
		   if ((regex!=null) && regex.startsWith("<")) {
			   regex = regex.substring(1, regex.length());
		   }
		   if ((regex!=null) && regex.endsWith(">")) {
			   regex = regex.substring(0, regex.length()-1);
		   }
		   logicExpression = textLogicExpressionParser.parse(regex);
	   }
	   
	   public boolean apply(WordToken wordToken) {
		   Boolean found = false;
           found = logicExpression.apply(wordToken);
		   return found ;
	   }
	 
}
