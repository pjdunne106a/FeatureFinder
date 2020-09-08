package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;

public class FeatureStorage {
	private static final String DEFINED_REGEX_FILE = "definedregexes.jsonl";
	private static final String DEFINED_LIST_FILE = "definedlists.jsonl";
	private static final String FEATURE_REGEX_FILE = "featureregexes.jsonl";
	private static final String GROUP_REGEX_FILE = "featuregroups.jsonl";
	private WebApplicationContext applicationContext;

	public FeatureStorage(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public Map<String, Object> readDefinedRegexes() {
		Map<String, Object> definedMap = new HashMap<>();
		this.readResource(DEFINED_REGEX_FILE, definedMap);
		return definedMap;
	}
	
	public Map<String, Object> readDefinedLists() {
		Map<String, Object> definedMap = new HashMap<>();
		this.readResource(DEFINED_LIST_FILE, definedMap);
		return definedMap;
	}
	
	public Map<String, Object> readFeatureRegexes() {
		Map<String, Object> featureMap = new HashMap<>();
		this.readResource(FEATURE_REGEX_FILE, featureMap);
		return featureMap;
	}
	
	public Map<String, Object> readGroupRegexes() {
		Map<String, Object> featureMap = new HashMap<>();
		this.readResource(DEFINED_LIST_FILE, featureMap);
		return featureMap;
	}
	
	public Map<String, Object> readResource(String filename, Map<String, Object> resourceMap) {
		BufferedReader bufferedReader=null;
		JSONParser parser = new JSONParser();
		InputStream inputStream=null;
		InputStreamReader inputStreamReader = null;
		Map<String, Object> featureMap = new HashMap<>();
		List<String> lines = new ArrayList<>();
		Resource resource=null;
		String line="";
		try {
			 resource = applicationContext.getResource("classpath:"+filename);
			 inputStream = resource.getInputStream();
			 inputStreamReader = new InputStreamReader(inputStream);
			 bufferedReader = new BufferedReader(inputStreamReader);
			 line = bufferedReader.readLine();
			 while (line!=null) {
				 lines.add(line);
				 line = bufferedReader.readLine();
			 }
			 lines.forEach(item -> this.addToMap(parser, featureMap, item));
			 bufferedReader.close();
			 inputStreamReader.close();
             System.out.println("Lines"+lines.size());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return featureMap;
	}

	private void addToMap(JSONParser jsonParser, Map<String, Object> featureMap, String jsonLine) {
		JSONObject jsonObject = null;
		try {
			 jsonObject = (JSONObject)jsonParser.parse(jsonLine);
   		     String name = (String)jsonObject.get("name");
		     featureMap.put(name, jsonObject.get("regex"));
	       } catch (Exception exception) {
		        exception.printStackTrace();
	       }
	}
}
