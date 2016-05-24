/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyDataNews;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Document;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Documents;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.service.BadRequestException;
/**
 * class to use AlchelmyNewsApi
 *
 */
public  class AlchemyNewsImpl implements IAlchemyNews{
	
	private AlchemyDataNews service;
	
	public AlchemyNewsImpl(String apiKey){
		service = new AlchemyDataNews();
		service.setApiKey(apiKey);
	}
	
	/**
	 * Get possible competitors from news. If others company names appear in the same context.
	 * 
	 * @param companyName - company name
	 * @return list of possible competitors name
	 */
	public  ArrayList getPossibibleCompetitorsList(String companyName) throws BadRequestException{
		  
		  
		Map<String, Object> params = new HashMap<String, Object>();

		  String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment",
		          "q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.enrichedTitle.entities.entity.type"};
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
		  ArrayList list = new ArrayList();
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
	 * @return Documents that have information about news
	 */
	public  Documents getSentimentAnalysisOfNews(String companyName, String entity) throws BadRequestException{
		Map<String, Object> params = new HashMap<String, Object>();

		String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment","q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.entities.entity.text","q.enriched.url.entities.entity.type",
		          "q.enriched.url.enrichedTitle.entities.entity.type",
		          "q.enriched.url.enrichedTitle.taxonomy"};
		params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		params.put(AlchemyDataNews.START, "now-5d");
		params.put(AlchemyDataNews.END, "now");
		params.put(AlchemyDataNews.COUNT, 20);
		params.put("q.enriched.url.title","["+companyName+"]");
		params.put("q.enriched.url.enrichedTitle.entities.entity.text", "O["+companyName.trim().replace(" ", "^")+"^"+companyName+"]");
		params.put("q.enriched.url.enrichedTitle.entities.entity.type", entity);
		//get company/product taxonomy
		//q.enriched.url.enrichedTitle.taxonomy.taxonomy_.label=technology+and+computing
		//q.enriched.url.entities.entity = |text=Ipad,type=O[Technology^Product]|
	
		DocumentsResult result =  service.getNewsDocuments(params);
		return result.getDocuments();
	}
	
	/**
	 * Get industries of a company
	 * 
	 * @param comapanyName - company name
	 * @return list of possible industries of a company
	 */
	public  ArrayList getPossibibleSubTypesList(String companyName) throws BadRequestException{
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
		  ArrayList list = new ArrayList();
		  System.out.println(d);
		  for(Document d2 : d.getDocuments()){

			  for(Entity e : d2.getSource().getEnriched().getArticle().getEnrichedTitle().getEntities()){
				  for(String st : e.getDisambiguated().getSubType())
				  list.add(st);
			  }
		  }
		  return list;
	}
	
	
}
