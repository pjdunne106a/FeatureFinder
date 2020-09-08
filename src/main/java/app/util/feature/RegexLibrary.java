package app.util.feature;

import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.web.context.WebApplicationContext;

public class RegexLibrary {
   Map<String, Object> groupregexes;
   Map<String, Object> featureregexes;
   Map<String, Object> definedregexes;
   Map<String, Object> definedlists;
   
   public RegexLibrary(WebApplicationContext applicationContext) {
	   FeatureStorage featureStorage = new FeatureStorage(applicationContext);
	   this.groupregexes = featureStorage.readGroupRegexes();
	   this.featureregexes = featureStorage.readFeatureRegexes();
	   this.definedregexes = featureStorage.readDefinedRegexes();
	   this.definedlists = featureStorage.readDefinedLists();
   }
   
   public JSONArray getDefinedList(String name) {
	   JSONArray jsonArray = (JSONArray)this.definedlists.get(name);
	   return jsonArray;
   }
   
   public String getDefinedRegex(String name) {
	   String regex = (String)this.definedregexes.get(name);
	   return regex;
   }
   
   
   
}
