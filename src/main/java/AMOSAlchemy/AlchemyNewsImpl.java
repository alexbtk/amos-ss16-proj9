/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyDataNews;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Document;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Documents;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.service.BadRequestException;

import AMOSCache.AMOSCache;
import AMOSTwitterBluemix.TwitterBluemixPost;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * class to use AlchelmyNewsApi
 *
 */
public  class AlchemyNewsImpl implements IAlchemyNews{
	
	private AlchemyDataNews service;
	private String myApiKey;
	private AMOSCache cache;
	
	public AlchemyNewsImpl(String apiKey){
		service = new AlchemyDataNews();
		service.setApiKey(apiKey);
		myApiKey = apiKey;
		this.cache = AMOSCache.getInstance();
	}
	
	/**
	 * Get possible competitors from news. If others company names appear in the same context.
	 * 
	 * @param companyName - company name
	 * @return list of possible competitors name
	 */
	public List<String> getPossibibleCompetitorsList(String companyName) throws BadRequestException{
		  
		  
		Map<String, Object> params = new HashMap<String, Object>();

		  String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment",
		          "q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.enrichedTitle.entities.entity.type",
		          "q.enriched.url.enrichedTitle.entities.entity.disambiguated.geo"};
		  params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		  params.put(AlchemyDataNews.START, "now-10d");
		  params.put(AlchemyDataNews.END, "now");
		  params.put(AlchemyDataNews.COUNT, 7);
		 // params.put("q.enriched.url.enrichedTitle.entities.entity.type", "company");
		  //params.put("q.enriched.url.enrichedTitle.entities.entity.text", companyName);
		  params.put("q.enriched.url.entities.entity.type", "company");
		  params.put("q.enriched.url.entities.entity.text", companyName);
		  
		  DocumentsResult result =  service.getNewsDocuments(params);
		  Documents d = result.getDocuments();
		  List<String> list = new ArrayList<String>();
		  for(Document d2 : d.getDocuments()){

			  for(Entity e : d2.getSource().getEnriched().getArticle().getEnrichedTitle().getEntities()){
				  if(e.getType().toString().equals("Company") && !e.getText().toString().equals(companyName))
					  list.add(e.getText().toString());
			  }
		  }
		  return list;
	}
	

	
	/**
	 * Get news that have information about company and get the sentiment analysis
	 * 
	 * @param companyName - company name
	 * @param entity - entity(company,product)
	 * @param days - interval of time
	 * @return Documents that have information about news
	 */
	public  Documents getSentimentAnalysisOfNews(String companyName, String entity, String startTime, String endTime, int count) throws BadRequestException{
		Map<String, Object> params = new HashMap<String, Object>();

		String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment","q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.entities.entity.text","q.enriched.url.entities.entity.type",
		          "q.enriched.url.enrichedTitle.entities.entity.type",
		          "q.enriched.url.relations.relation.location",
		          "q.enriched.url.enrichedTitle.relations.relation.location.entities.entity.disambiguated.geo",
		          "q.enriched.url.enrichedTitle.relations.relation.location",
		          "q.enriched.url.enrichedTitle.taxonomy"};
		params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		params.put(AlchemyDataNews.START, startTime);
		params.put(AlchemyDataNews.END, endTime);
		params.put(AlchemyDataNews.COUNT, count);
		params.put("q.enriched.url.title","["+companyName+"]");
		params.put("q.enriched.url.enrichedTitle.entities.entity.text", "O["+companyName.trim().replace(" ", "^")+"^"+companyName+"]");
		params.put("q.enriched.url.enrichedTitle.entities.entity.type", entity);
		//get company/product taxonomy
		//q.enriched.url.enrichedTitle.taxonomy.taxonomy_.label=technology+and+computing
		//q.enriched.url.entities.entity = |text=Ipad,type=O[Technology^Product]|
	
		DocumentsResult result =  service.getNewsDocuments(params);
		return result.getDocuments();
	}
	
	public Documents getSentimentAnalysisOfNewsByRegion(String name, String entity) throws BadRequestException{
		Map<String, Object> params = new HashMap<String, Object>();

		String[] fields =
		      new String[] {//"enriched.url.title", "enriched.url.url",
		          //"enriched.url.enrichedTitle.docSentiment",
		         // "q.enriched.url.enrichedTitle.entities.entity.text",
		          "enriched.url.title"};
		params.put(AlchemyDataNews.RETURN, "enriched.url.title,enriched.url.entities");
		params.put(AlchemyDataNews.START, "now-30d");
		params.put(AlchemyDataNews.END, "now");
		//params.put(AlchemyDataNews.COUNT, 20);
		params.put("q.enriched.url.title","Apple");
		//params.put("q.enriched.url.enrichedTitle.entities.entity.text", "O["+name.trim().replace(" ", "^")+"^"+name+"]");
		//params.put("q.enriched.url.enrichedTitle.entities.entity.type", entity);
		//params.put("q.enriched.url.entities.entity.type", "O[City^Country]");
		
		//get company/product taxonomy
		//q.enriched.url.enrichedTitle.taxonomy.taxonomy_.label=technology+and+computing
		//q.enriched.url.entities.entity = |text=Ipad,type=O[Technology^Product]|
	
		DocumentsResult result =  service.getNewsDocuments(params);//.getDocuments().getDocuments().get(0).getSource().getEnriched().getArticle();
		//http://gateway-a.watsonplatform.net/calls/data/GetNews?apikey=593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f&
		//outputMode=json&start=now-30d&end=now&q.enriched.url.title=Apple&return=q.enriched.url.entities
		return result.getDocuments();
	}
	
	/**
	 * Get industries of a company
	 * 
	 * @param comapanyName - company name
	 * @return list of possible industries of a company
	 */
	public List<String> getPossibibleSubTypesList(String companyName) throws BadRequestException{
		 Map<String, Object> params = new HashMap<String, Object>();

		  String[] fields =
		      new String[] {"q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.enrichedTitle.entities.entity.disambiguated.subType",
		          "q.enriched.url.enrichedTitle.entities.entity.type"};
		  params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		  params.put(AlchemyDataNews.START, "now-10d");
		  params.put(AlchemyDataNews.END, "now");
		  params.put(AlchemyDataNews.COUNT, 1);
		  params.put("q.enriched.url.enrichedTitle.entities.entity.type", "Company");
		  params.put("q.enriched.url.enrichedTitle.entities.entity.text", companyName);
		  
		  DocumentsResult result =  service.getNewsDocuments(params);
		  Documents d = result.getDocuments();
		  List<String> list = new ArrayList<String>();
		  System.out.println(d);
		  for(Document d2 : d.getDocuments()){

			  for(Entity e : d2.getSource().getEnriched().getArticle().getEnrichedTitle().getEntities()){
				  for(String st : e.getDisambiguated().getSubType())
				  list.add(st);
			  }
		  }
		  return list;
	}
	
	/**
	 * Get recent developments of a company.
	 */
	public  Map<String,String> getRecentDevelopmentList(String companyName) throws BadRequestException{
		Object r = cache.getCurrentMethodCache(companyName);
		
		if(r != null)
			return (Map<String,String>) r;
		
		Map<String,String> li = new HashMap<String,String>();
		OkHttpClient client = new OkHttpClient();
		String queryString = "https://gateway-a.watsonplatform.net/calls/data/GetNews?"+ 	
				"apikey="+myApiKey+
				"&outputMode=json"+
				"&start=now-10d"+  
				"&end=now"+
				"&q.enriched.url.enrichedTitle.relations.relation.subject.entities.entity.text=O["
				+companyName.trim()+"^"+companyName.trim().replace(" ", "^")+"]"+				
				"&q.enriched.url.enrichedTitle.relations.relation.subject.entities.entity.type=Company"+
				"&return=q.enriched.url.enrichedTitle.relations,q.enriched.url.relations,q.enriched.url.url";
		JSONObject jsonResult = null;
		JSONArray docsArray = null;
		try {
			Request request = new Request.Builder().url(queryString).build();
			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(responseString);
			jsonResult = (JSONObject)obj;
			if(!(jsonResult.get("status")).equals("OK"))
				return li;
			JSONObject val = (JSONObject)jsonResult.get("result");
			docsArray = (JSONArray)val.get("docs");
			
			for (int i = 0; i < docsArray.size(); i++){
				JSONObject doc = (JSONObject)docsArray.get(i);
				val = (JSONObject)doc.get("source");
				val = (JSONObject)val.get("enriched");
				val = (JSONObject)val.get("url");
				String urlNews =  (String) val.get("url");
				val = (JSONObject)val.get("enrichedTitle");
				JSONArray relArray = (JSONArray)val.get("relations");
				for(int j=0;j<relArray.size();++j){
					val	= (JSONObject)relArray.get(j);
					val = (JSONObject)val.get("subject");
					JSONArray entitiesArray = (JSONArray)val.get("entities");
					boolean ok = false;
					for(int k=0;k<entitiesArray.size();++k){
						val	= (JSONObject)entitiesArray.get(k);
						if(((String)val.get("text")).contains(companyName) || companyName.contains(((String)val.get("text")))){
							ok = true;
							break;
						}
					}
					if(ok){
						val	= (JSONObject)relArray.get(j);				
						li.put(urlNews, ((String)val.get("sentence")));
						break;
					}
				}
				
			}
		
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(li.size() > 0)
			cache.putCurrentMethodCache(companyName, li);
		
		return li;
	}
	
}
