package glib.app.textanalysis;

import edu.washington.cs.knowitall.regex.Expression.BaseExpression;
import edu.washington.cs.knowitall.regex.RegularExpressionParser;

public class CustomRegularExpressionParser extends RegularExpressionParser<WordToken> {
	private RegularFunction regularFunction; 
	private TextBlock textBlock;
	private RegexLibrary regexLibrary;
	
	 public CustomRegularExpressionParser(RegularFunction regularFunction, TextBlock textBlock, RegexLibrary regexLibrary) {
		 this.regularFunction = regularFunction;
		 this.textBlock = textBlock;
		 this.regexLibrary = regexLibrary;
	 }
	 
	 public CustomRegularExpressionParser(RegexHandler regexHandler) {
	 }
	 
	 public BaseExpression<WordToken> factory(String str) {
		 return new CustomRegularBaseExpression(str, regularFunction, textBlock, regexLibrary);
	 }
	
	 public CustomRegularExpression process(String string) {
	        CustomRegularExpression customLogicExpression =  new CustomRegularExpression(string);
	        customLogicExpression.setLogicExpression(this.apply(string));
	        return customLogicExpression;
	 }
	
}
