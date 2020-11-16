package app.util.feature;

import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;

import edu.washington.cs.knowitall.logic.Expression.Arg;

public class TextLogicBaseExpression extends Arg<WordToken> {
	   private static String[] PART_LIST = {"postag","POSTAG","token","TOKEN","lemma","LEMMA","type","TYPE","text","TEXT","phrase","PHRASE"};
       private String part;
       private String value;
       private String valueType;
       private RegularFunction regularFunction;
       private TextBlock textBlock;
       private RegexLibrary regexLibrary;
       private WordStorage wordStorage;
       
	   public TextLogicBaseExpression(String regex, RegularFunction regularFunction, WordStorage wordStorage, TextBlock textBlock, RegexLibrary regexLibrary) {
		   this.regularFunction = regularFunction;
		   this.textBlock = textBlock;
		   this.regexLibrary = regexLibrary;
		   this.wordStorage = wordStorage;
		   regularFunction.setWordStorage(wordStorage);
		   if ((regex!=null) && regex.startsWith("<")) {
			   regex = regex.substring(1, regex.length());
		   }
		   if ((regex!=null) && regex.endsWith(">")) {
			   regex = regex.substring(0, regex.length()-1);
		   }
		   
		   this.part = this.getPart(regex);
		   if (part.length()>0) {
			   this.part = part.toLowerCase();
			   this.value = regex.substring(part.length()+1,regex.length());
			   this.valueType = this.getValueType(value);
			   System.out.println("   Part:"+part+"     ValueType:"+valueType+"     Value:"+value+"      Regex:"+regex);
		   }
	   }
	   
	   public boolean apply(WordToken wordToken) {
		   Boolean found = false;
		   System.out.println("**Apply, Part:"+part+"  "+valueType+"**  Value:"+value);
		   if ((valueType!=null) && (part!=null)) {
			   if (!part.equalsIgnoreCase("text")) {
		           switch (valueType) {
		             case "string": {found = checkString(part, value, wordToken);}; break;
		             case "name": {found = checkName(part, value, wordToken);}; break;
		             case "list": {found = checkList(part, value, wordToken);}; break;
		             case "function": {found= checkFunction(part, valueType, value, wordToken, textBlock);}; break;
		             case "predefinedlist": {found= checkPreDefinedList(part, valueType, value, wordToken, textBlock, regexLibrary);}; break;
		           } 
		        } else {
		             found = this.checkText(part, value, valueType, wordToken, textBlock);
		        }   
		    }
		   System.out.println("** TextLogicExpression:"+found);
		   return found;
	   }
	   
	   private Boolean checkText(String part, String value, String valueType, WordToken wordToken, TextBlock textBlock) {
           Boolean found = false;
		   Integer sentenceIndex=0;
           Integer wordIndex=0;
           TextBlockExpression textBlockExpression=null;
           wordIndex = wordToken.getIndex();
           sentenceIndex = wordToken.getSentence();
           if ((wordIndex==1) && (sentenceIndex==1)) {
        	   textBlockExpression = textBlock.getTextBlockExpression();
        	   found = textBlockExpression.apply(part,value,valueType,wordToken,textBlock.getSection());
           }
          return found;
	   }
	   
	   private String getPart(String regex) {
		   boolean found = false;
		   String part="", nextStr="";
		   for (String str:PART_LIST) {
			   if (regex.startsWith(str)) {
				   nextStr = regex.substring(str.length(), str.length()+1);
				   if (nextStr.equalsIgnoreCase("=")) {
					   found = true;
					   part = str.toLowerCase();
				   }	   
			   }
		   }
		   return part;
	   };
	   
	   private String getValueType(String value) {
		   String type = "";
		   type = isNumber(value);
		   if (type.length()==0) {
			   type = isString(value);
			   if (type.length()==0) {
				   type = isFunction(value);
				   if (type.length()==0) {
					   type = isList(value);
					   if (type.length()==0) {
						   type = isDefinedList(value);
						   if (type.length()==0) {
							   type = isName(value);
						   }
					   }
				   }
			   }
		   }
		   return type;
	   };
	
	   private String isNumber(String value) {
		 String type="";
		 Character digits[]= {'0','1','2','3','4','5','6','7','8','9'};
		 char chr;
		 List<Character> digitsList = Arrays.asList(digits);
		 boolean okay=true;
		 for (int i=0;i<value.length();i++) {
			 chr = value.charAt(i);
		     if (!digitsList.contains(chr)) {
		    	 okay = false;
		     }
	      }
		 if (okay) {
			 type="number";
		 }
		 return type;
	   }
	   
	   private String isString(String value) {
			 String type="";
			 Character singleQuotes[] = {'\'','\''};
			 Character doubleQuotes[]= {'"','"'};
			 List<Character> singles=Arrays.asList(singleQuotes);
			 List<Character> doubles=Arrays.asList(doubleQuotes);
			 Character start,end;
			 boolean okay=false;
			 start = value.charAt(0);
			 end = value.charAt(value.length()-1);
             if ((singles.contains(start)) && (singles.contains(end))) {
            	 okay=true;
             } else  if ((doubles.contains(start)) && (doubles.contains(end))) {
            	 okay=true;
             }
			 if (okay) {
				 type="string";
			 }
			 return type;
		   }
	   
	   private String isList(String value) {
			 String type="";
			 Character start,end;
			 boolean okay=false;
			 start = value.charAt(0);
			 end = value.charAt(value.length()-1);
             if ((start=='[') && (end==']')) {
          	     okay=true;
             } 
			 if (okay) {
				 type="list";
			 }
			 return type;
	   }
	   
	   private String isFunction(String value) {
			 String type="";
			 Integer location=0;
			 String functionName="",functionType="", end="";
			 location = value.indexOf('(');
			 if (location>0) {
                functionName=value.substring(0, location);
                functionType=this.isName(functionName);
                if (functionType.equalsIgnoreCase("name")) {
                	end = value.substring(value.length()-1,value.length());
                	if (end.equalsIgnoreCase(")")) {
                	    type="function";
                	}
                }
			 }
			 return type;
		   }
	   
	   private String isDefinedList(String value) {
			 String type="",regexType="",first="",definedName="";
			 first=value.substring(0,1);
			 if (first.equalsIgnoreCase("$")) {
				 definedName = value.substring(1,value.length());
				 regexType = this.isName(definedName);
                 if (regexType.equalsIgnoreCase("name")) {
            	    type = "predefinedlist";
                 }
             }
			 return type;
		   }
	   
	   private String isName(String value) {
			 String type="";
			 Character lowercase[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
			 Character number[]= {'0','1','2','3','4','5','6','7','8','9'};
			 Character symbol[]= {'_',':'};
			 Character uppercase[]= {'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
			 List<Character> validLower=Arrays.asList(lowercase);
			 List<Character> validUpper=Arrays.asList(uppercase);
			 List<Character> validNumber=Arrays.asList(number);
			 List<Character> validSymbol=Arrays.asList(symbol);
			 Character start,end;
			 char chr;
			 Integer count=0;
			 boolean okay=true;
			 for (int i=0;i<value.length();i++) {
				 chr = value.charAt(i);
			     if (validLower.contains(chr)) {
			    	 count++;
			     } else if (validUpper.contains(chr)) {
			    	 count++;
			     } else if ((i>0) && (validNumber.contains(chr))) {
			    	 count++;
			     } else if ((i>0) && (validSymbol.contains(chr))) {
			    	 count++;
			     }
		      }
			 if (count==value.length()) {
				 type="name";
			 }
			 return type;
		   }
	   
	   private String getName(String value) {
		   String functionName="";
		   Integer location = value.indexOf('(');
			 if (location>0) {
              functionName=value.substring(0, location);
			 }
		   return functionName;
	   }
	   
      private Boolean checkString(String part, String value, WordToken wordToken) {
    	  boolean found=false;
    	  String content="";
    	  if (part.equalsIgnoreCase("postag")) {
    		  content = wordToken.getPostag();
    	  } else if (part.equalsIgnoreCase("token")) {
    		  content = wordToken.getToken();
    	  } else if (part.equalsIgnoreCase("lemma")) {
    		  content = wordToken.getLemma();
    	  }  else if (part.equalsIgnoreCase("type")) {
        		  content = wordToken.getDependency();
          } 
    	  content = General.removeQuotes(content);
    	  value = General.removeQuotes(value);
    	  if (value.equalsIgnoreCase(content)) {
    			  found = true;
    	  }
    	  return found;
      }
      
      private Boolean checkList(String part, String value, WordToken wordToken) {
    	  boolean found=false;
    	  Integer index=0;
    	  String content="", param="";
    	  List<String> params=null;
    	  System.out.println("** Value:"+value);
    	  if (part.equalsIgnoreCase("postag")) {
    		  content = wordToken.getPostag();
    	  } else if (part.equalsIgnoreCase("token")) {
    		  content = wordToken.getToken();
    	  } else if (part.equalsIgnoreCase("lemma")) {
    		  content = wordToken.getLemma();
    	  } else if (part.equalsIgnoreCase("type")) {
    		  content = wordToken.getDependency();
    	  }  
    	  params = General.getListParameters(value);
    	  content = General.removeQuotes(content);
    	  System.out.println("** Params:"+params);
    	  while ((!found) && (index<params.size())) {
    		  param = params.get(index);
    	      param = General.removeQuotes(param);
    	      System.out.println("** Param:"+param+"  Content:"+content);
    		  if (param.equalsIgnoreCase(content)) {
    			  found = true;
    		  }
    		  index++;
    	  }
    	 return found;
      }
      
      private Boolean checkName(String part, String value, WordToken wordToken) {
    	  boolean found=false;
    	  String content="";
    	  if (part.equalsIgnoreCase("postag")) {
    		  content = wordToken.getPostag();
    		  if (value.equalsIgnoreCase(content)) {
    			  found = true;
    		  }
    	  }
    	 return found;
      }

      private Boolean checkFunction(String part, String valueType, String value, WordToken wordToken, TextBlock textBlock) {
    	  boolean found = false;
    	  String functionName = this.getName(value);
    	  found = regularFunction.doFunction(part,functionName,value,wordToken,textBlock.getSection());
    	  return found;
      }
      
      private Boolean checkPreDefinedList(String part, String valueType, String value, WordToken wordToken, TextBlock textBlock, RegexLibrary regexLibrary) {
    	  boolean found = false;
    	  String regex = null;
    	  String content = "";
    	  if (value.startsWith("$")) {
    		  value = value.substring(1, value.length());
    	  }
    	  System.out.println("** Value:"+value);
    	  if (value.equalsIgnoreCase("commonword")) {
    		  found = this.wordStorage.wordExists(value, wordToken.getToken());  
    	  } else {
    	          regex = regexLibrary.getFeatureRegex(value);
    	          if (part.equalsIgnoreCase("postag")) {
    	        	  content = wordToken.getPostag();
    	          } else if (part.equalsIgnoreCase("token")) {
    	        	  content = wordToken.getToken();
    	          } else if (part.equalsIgnoreCase("lemma")) {
    	        	  content = wordToken.getLemma();
    	          } else if (part.equalsIgnoreCase("type")) {
    	    		  content = wordToken.getDependency();
    	    	  } 
    	          if (regex != null) {
    	        	  found = true;
    	          }
    	  }
    	  return found;
      }
	   
}
