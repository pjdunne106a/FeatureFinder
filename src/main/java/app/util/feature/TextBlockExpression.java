package app.util.feature;

public class TextBlockExpression {
	private Integer locationIndex;

	public TextBlockExpression() {
		this.locationIndex = 1;
	}
	
	public boolean apply(String part, String value, String valueType, WordToken wordToken, Section section) {
		boolean found = false;
		if (valueType.equalsIgnoreCase("definedregex")) {
		    System.out.println("**TextBlockExpression:"+part+"  "+value+ locationIndex);
		    locationIndex++;
		}
		return found;
	}
	
	 private Boolean checkPreDefinedList(String part, String valueType, String value, WordToken wordToken, TextBlock textBlock, RegexLibrary regexLibrary) {
   	  boolean found = false;
   	  CustomRegularExpressionParser customRegularExpressionParser;
   	  CustomRegularExpression logicExpression;
   	  //String regex = regexLibrary.getRegex(value);
		//  customRegularExpressionParser = new CustomRegularExpressionParser(regularFunction, textBlock, regexLibrary);
		 // logicExpression = customRegularExpressionParser.process(regex);
   	  //found = logicExpression.findAll(wordTokenList);
   	  
   	  return found;
     }
}
