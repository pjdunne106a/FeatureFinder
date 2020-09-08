package app.util.feature;

import edu.washington.cs.knowitall.logic.LogicExpression;
import edu.washington.cs.knowitall.regex.Expression.BaseExpression;

public class CustomRegularBaseExpression extends BaseExpression<WordToken> {
       private CustomLogicExpressionParser customLogicExpressionParser;
       private LogicExpression<WordToken> logicExpression;
       
	   public CustomRegularBaseExpression(String regex, RegularFunction regularFunction, TextBlock textBlock, RegexLibrary regexLibrary) {
           super(regex);
		   customLogicExpressionParser = new CustomLogicExpressionParser(regularFunction, textBlock, regexLibrary);
		   if ((regex!=null) && regex.startsWith("<")) {
			   regex = regex.substring(1, regex.length());
		   }
		   if ((regex!=null) && regex.endsWith(">")) {
			   regex = regex.substring(0, regex.length()-1);
		   }
		   logicExpression = customLogicExpressionParser.parse(regex);
	   }
	   
	   public boolean apply(WordToken wordToken) {
		   Boolean found = false;
           found = logicExpression.apply(wordToken);
		   return found ;
	   }
	 
}
