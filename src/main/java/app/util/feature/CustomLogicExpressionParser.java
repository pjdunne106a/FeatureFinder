package app.util.feature;

import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.logic.LogicExpressionParser;

public class CustomLogicExpressionParser extends LogicExpressionParser<WordToken> {
	private RegularFunction regularFunction; 
	private TextBlock textBlock;
	private RegexLibrary regexLibrary;
	
	public CustomLogicExpressionParser(RegularFunction regularFunction, TextBlock textBlock, RegexLibrary regexLibrary) {
		 this.regularFunction = regularFunction;
		 this.textBlock = textBlock;
		 this.regexLibrary = regexLibrary;
	 }
	 
	 public Arg<WordToken> factory(String str) {
		 return new CustomLogicBaseExpression(str, regularFunction, textBlock, regexLibrary);
	 }
	
}