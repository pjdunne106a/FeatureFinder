package app.util.feature;

import java.util.ArrayList;
import java.util.List;

public class General {

	public static String removeQuotes(String content) {
		String newContent = content;
		if ((newContent.startsWith("'")) || (newContent.startsWith("\""))) {
			  newContent = newContent.substring(1,newContent.length());
		  }
		if ((newContent.endsWith("'")) || (newContent.endsWith("\""))) {
			  newContent = newContent.substring(0,newContent.length()-1);
		  }
	   return newContent;
	}
	
	public static List<String> getListParameters(String value) {
  		List<String> parameters = new ArrayList<>();
  		boolean skip=false;
  		String listParameters = value;
  		String parameter="";
  		String part="";
  		if (listParameters.startsWith("[")) {
  		  listParameters = listParameters.substring(1,listParameters.length());
  	    }
  	    if (listParameters.endsWith("]")) {
  		  listParameters = listParameters.substring(0,listParameters.length()-1);
  	    }
  		if (listParameters.length()>0) {
  		   skip = false;
  		   for (int i=0; i<listParameters.length();i++) {
  			   part = listParameters.substring(i,i+1);
  			   if ((part.equalsIgnoreCase(",") && (!skip))) {
  				   if (part.length()>0) {
  				       parameters.add(parameter);
  				       parameter="";
  				   }
  			   } else {
  				       parameter = parameter + part;
  				       if ((part.equalsIgnoreCase("'")) || (part.equalsIgnoreCase("\""))) {
  				    	   skip = !skip;
  				       }
  			   }
  		   }
  		   if (parameter.length()>0) {
  			   parameters.add(parameter);
  		   }
  		}
  		return parameters;
  	}
	
}
