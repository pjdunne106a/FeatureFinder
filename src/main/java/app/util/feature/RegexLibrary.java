package app.util.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import app.util.database.DocumentDatabase;
import app.util.database.FeatureDocument;

@Component
public class RegexLibrary {
   private Map<String, String> featureregexes;
   private DocumentDatabase documentDatabase;
   
   public RegexLibrary() {
	 
   }
   
   public void setDependencies(DocumentDatabase documentDatabase) {
	   this.documentDatabase = documentDatabase;
	   this.featureregexes = this.getFeatureRegexes();
   }
   
   public String getFeatureRegex(String featurename) {
	  String featureRegex="";
	  String itemStr="";
	  if (featurename!=null) {
		  itemStr = (String)featureregexes.get(featurename);
		  featureRegex = itemStr;
	  }
	  return featureRegex;
   }
   
   public Map<String, String> getFeatureRegexes() {
	   Map<String, String> featureRegexes = new HashMap<>();
	   String jsonStr="";
	   String[] functionList=null, parts=null; 
	   functionList = RegularFunction.FUNCTION_FEATURES;
	   for (String functionStr:functionList) {
		   parts = functionStr.split(":");
		   jsonStr = "{\"name\":\""+parts[0]+"\",\"description\":\""+parts[1]+"\",\"type\":\""+parts[2]+"\",\"group\":\""+parts[3]+"\",\"contents\":\""+parts[4]+"\"}";
		   featureRegexes.put(parts[0], jsonStr);
	   }
	   List<FeatureDocument> featureDocuments = documentDatabase.getDocuments("regex");
	   for (FeatureDocument featureDocument:featureDocuments)  {
		   featureRegexes.put(featureDocument.getName(), featureDocument.toString());
	   }
      return featureRegexes;	  
   }
   
   public String getFeatureList() {
	   String exprStr = "[";
	   for (Map.Entry<String, String> feature:featureregexes.entrySet()) {
	        exprStr = exprStr + feature.getValue();
	        exprStr = exprStr + ",";
	   }	   
	   exprStr = exprStr.substring(0,exprStr.length()-1);
	   exprStr = exprStr + "]";
	   return exprStr;
   }
   
}
