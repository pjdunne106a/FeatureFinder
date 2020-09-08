package app.util.feature;

import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.logic.LogicExpressionParser;

public class TextLogicExpressionParser extends LogicExpressionParser<WordToken> {
	private RegularFunction regularFunction; 
	private TextBlock textBlock;
	private RegexLibrary regexLibrary;
	private WordStorage wordStorage;
	
	public TextLogicExpressionParser(RegularFunction regularFunction, WordStorage wordStorage, TextBlock textBlock, RegexLibrary regexLibrary) {
		 this.regularFunction = regularFunction;
		 this.textBlock = textBlock;
		 this.regexLibrary = regexLibrary;
		 this.wordStorage = wordStorage;
	 }
	 
	 public Arg<WordToken> factory(String str) {
		 return new TextLogicBaseExpression(str, regularFunction, wordStorage, textBlock, regexLibrary);
	 }
	
}
