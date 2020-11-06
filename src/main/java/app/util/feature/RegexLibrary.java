package app.util.feature;

import java.util.ArrayList;
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
   Map<String, Object> groupregexes;
   Map<String, Object> featureregexes;
   Map<String, Object> definedregexes;
   Map<String, Object> definedlists;
   Map<String, Object> grouplists;
   
   public RegexLibrary(WebApplicationContext applicationContext) {
	   FeatureStorage featureStorage = new FeatureStorage(applicationContext);
	   this.groupregexes = featureStorage.readGroupRegexes();
	   this.featureregexes = featureStorage.readFeatureRegexes();
	   this.definedregexes = featureStorage.readDefinedRegexes();
	   this.definedlists = featureStorage.readDefinedLists();
	   this.grouplists = featureStorage.readGroupsList();
   }
   
   public JSONArray getDefinedList(String name) {
	   JSONArray jsonArray = (JSONArray)this.definedlists.get(name);
	   return jsonArray;
   }
   
   public String getDefinedRegex(String name) {
	   String regex = (String)this.definedregexes.get(name);
	   return regex;
   }
   
   public Map<String, Object> getGroupNames() {
	  return this.grouplists;
   }
   
   public String getFeatureRegex(String featurename) {
	  String featureRegex="";
	  String itemStr="";
	  JSONObject jsonObject=null;
	  if (featurename!=null) {
		  jsonObject = (JSONObject)featureregexes.get(featurename);
		  itemStr = (String)jsonObject.get("regex");
		  featureRegex = itemStr;
	  }
	  return featureRegex;
   }
   
   public String getFeatureRegex(String group, String name) {
	   JSONArray jsonArray = (JSONArray)this.grouplists.get(group);
	   JSONObject jsonObject=null;
	   String regexName="",jsonStr="";
	   Integer index=0;
	   Boolean found = false;
	   while ((!found) && (index<jsonArray.size())) {
		   regexName = (String)jsonArray.get(index);
		   System.out.println("**********Regex Name:"+regexName+"  " + index+"  Name:"+name);
		   if (regexName.equalsIgnoreCase(name)) {
	             found = true;
	             jsonObject = (JSONObject)featureregexes.get(regexName);
	   		     jsonStr = (String)jsonObject.get("regex");
		   }
		   index = index + 1;
	   }	   
	  return jsonStr;
   }
   
   public String getGroupList() {
	   Integer count = 0, index = 0, number = 0;
	   Set<String> list = this.grouplists.keySet();
	   List<String> jsonList = new ArrayList<>(list);
	   String jsonStr="", regexStr="", itemStr="", key="", exprStr="[";
	   JSONArray jsonArray = null;
	   JSONObject jsonItem=null, jsonRegex=null;
	   number = jsonList.size();
	   for (int k=0; k<number; k++)  {
		   key = jsonList.get(k);
		   jsonItem = (JSONObject)grouplists.get(key);
		   if (jsonItem != null) {
		    	regexStr = jsonItem.toString();
		    	exprStr = exprStr + regexStr;
		   }
		   count++;
		   if (count<number) {
			   exprStr = exprStr + ",";
		   }
	   }
	   exprStr = exprStr + "]";
	   return exprStr;
   }
   
   public String getFeatureList(DocumentDatabase documentDatabase) {
	   Integer count = 0, index = 0, number=0, size=0;
	   Set<String> list = this.featureregexes.keySet();
	   List<String> jsonList = new ArrayList<>(list);
	   String jsonStr="", regexStr="", itemStr="", key="", exprStr="[";
	   String[] functionList=null, parts=null;
	   JSONArray jsonArray = null;
	   JSONObject jsonItem=null, jsonRegex=null;
	   functionList = RegularFunction.FUNCTION_FEATURES;
	   for (String functionStr:functionList) {
		   parts = functionStr.split(":");
		   jsonStr = "{\"name\":\""+parts[0]+"\",\"description\":\""+parts[1]+"\",\"type\":\""+parts[2]+"\",\"group\":\""+parts[3]+"\",\"contents\":\""+parts[4]+"\"}";
		   exprStr = exprStr + jsonStr + ",";
	   }
	   number = jsonList.size();
	   exprStr = exprStr.substring(0, exprStr.length()-1);
	   List<FeatureDocument> featureDocuments = documentDatabase.getDocuments("regex");
	   size = featureDocuments.size();
	   for (FeatureDocument featureDocument:featureDocuments)  {
		   exprStr = exprStr + "," + featureDocument.toString();
	   }
	   exprStr = exprStr + "]";
	   return exprStr;
   }
   
}
