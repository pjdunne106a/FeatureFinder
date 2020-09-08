package app.util.feature;

import edu.washington.cs.knowitall.regex.Expression.BaseExpression;
import edu.washington.cs.knowitall.regex.RegularExpressionParser;

public class TextRegularExpressionParser extends RegularExpressionParser<WordToken> {
	private RegularFunction regularFunction; 
	private TextBlock textBlock;
	private RegexLibrary regexLibrary;
	private WordStorage wordStorage;
	
	 public TextRegularExpressionParser(RegularFunction regularFunction, WordStorage wordStorage, TextBlock textBlock, RegexLibrary regexLibrary) {
		 this.regularFunction = regularFunction;
		 this.textBlock = textBlock;
		 this.regexLibrary = regexLibrary;
		 this.wordStorage = wordStorage;
	 }
	 
	 public TextRegularExpressionParser(RegexHandler regexHandler) {
	 }
	 
	 public BaseExpression<WordToken> factory(String str) {
		 return new TextRegularBaseExpression(str, regularFunction, wordStorage, textBlock, regexLibrary);
	 }
	
	 public TextRegularExpression process(String string) {
	        TextRegularExpression textLogicExpression =  new TextRegularExpression(string);
	        textLogicExpression.setLogicExpression(this.apply(string));
	        return textLogicExpression;
	 }
	
}
