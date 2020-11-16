package app.util.feature;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import app.util.database.DocumentDatabase;
import app.util.database.FeatureDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FeatureService {
   private DocumentDatabase documentDatabase;
   private RegexLibrary regexLibrary;
   private EnglishParser englishParser;
   private WordStorage wordStorage;
   private LanguageTree languageTree;
   private WebApplicationContext applicationContext;
	
   public void setDependencies(DocumentDatabase documentDatabase, RegexLibrary regexLibrary, EnglishParser englishParser, WordStorage wordStorage, LanguageTree languageTree, WebApplicationContext applicationContext) {
	   this.documentDatabase = documentDatabase;
	   this.regexLibrary = regexLibrary;
	   this.englishParser = englishParser;
	   this.wordStorage = wordStorage;
	   this.languageTree = languageTree;
	   this.applicationContext = applicationContext;
   }
	
   @Async("threadPoolTaskExecutor")
   public CompletableFuture <List<FeatureResult>> runFeatureGroup(Integer featuregroupid, Integer documentgroupid, String language, String granularity) throws InterruptedException {
      Integer matches = 0;
      JSONObject jsonObject = null;
	  List<FeatureResult> featureResultList = new ArrayList<>();
      List<String> documents = null;
      List<String> features = null;
      Matcher matcher = null;
      Object featureObject = null;
      JSONParser parser = null;
      RegularFunction regularFunction = null;
      Section section = null;
      String[] featureArray = null;
      String[] documentArray = null;
      String document = "", feature = "", text = "", featureRegex = "";
      FeatureDocument featureDocument = null;
      System.out.println("***Feature Group id:"+featuregroupid+"     ***Document Group id:"+documentgroupid);
      FeatureDocument featureGroupDocument = documentDatabase.getDocumentById(featuregroupid);
      FeatureDocument documentGroupDocument = documentDatabase.getDocumentById(documentgroupid);
      FeatureResult featureResult = null;
      String featureContents = featureGroupDocument.getContents();
      String documentContents = documentGroupDocument.getContents();
      String featureType="", featurePattern="";
      if ((featureContents!=null) && (documentContents!=null)) {
    	  parser = new JSONParser();
          featureContents = featureContents.replace("[","");
          featureContents = featureContents.replace("]","");
          featureContents = featureContents.replace("\'","");
          featureContents = featureContents.replace("\"","");
          featureArray = featureContents.split(",");
          
          documentContents = documentContents.replace("[","");
          documentContents = documentContents.replace("]","");
          documentContents = documentContents.replace("\'","");
          documentContents = documentContents.replace("\"","");
          documentArray = documentContents.split(",");
          if ((featureArray.length>0) && (documentArray.length>0)) {
               for (int i=0;i<documentArray.length;i++) {
            	   document = documentArray[i];
            	   featureDocument = documentDatabase.getDocument(document,"text");
            	   text = featureDocument.getContents();
            	   section = englishParser.parseTextToSentence(text);
            	   regularFunction = new RegularFunction();
            	   featureResult = new FeatureResult(document);
            	   for (int k=0;k<featureArray.length;k++) {
            		   feature = featureArray[k];
            		   featureRegex = regexLibrary.getFeatureRegex(feature);
            		   try {
            		        featureObject = parser.parse(featureRegex);
            		        jsonObject = (JSONObject)featureObject;
            		   } catch (Exception exception) {
            			   exception.printStackTrace();
            		   }
            		   featureType = jsonObject.get("type").toString();
            		   if (featureType.equalsIgnoreCase("regex")) {
            			   featurePattern = jsonObject.get("contents").toString();
            		   } else if (featureType.equalsIgnoreCase("function")) {
            			   featurePattern = "<token="+jsonObject.get("name").toString()+"()>";
            		   } else if (featureType.equalsIgnoreCase("list")) {
            			   featurePattern = "<token="+jsonObject.get("name").toString()+"()>";
            		   }
            		   System.out.println("****Feature:"+featurePattern);
            		   matcher = new Matcher(featurePattern, regularFunction, wordStorage, languageTree, regexLibrary, applicationContext);
            		   matches = matcher.matchcount(section, granularity);
            		   featureResult.addMatch(feature, matches);
            	   }
            	   featureResultList.add(featureResult);
               }
          }
      }
  // doing an artificial sleep
      Thread.sleep(20000);
      return CompletableFuture.completedFuture(featureResultList);
   }

}