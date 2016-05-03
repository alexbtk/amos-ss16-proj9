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

public  class AlchemyNewsImpl implements IAlchemyNews{
	
	private AlchemyDataNews service;
	
	public AlchemyNewsImpl(String apiKey){
		service = new AlchemyDataNews();
		service.setApiKey(apiKey);
	}
	
	
	public  ArrayList getPossibibleCompetitorsList(String companyName) throws BadRequestException{
		  
		  
		  Map<String, Object> params = new HashMap<String, Object>();

		  String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment",
		          "q.enriched.url.enrichedTitle.entities.entity.text",
		          "q.enriched.url.enrichedTitle.entities.entity.type"};
		  params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		  params.put(AlchemyDataNews.START, "now-1d");
		  params.put(AlchemyDataNews.END, "now");
		  params.put(AlchemyDataNews.COUNT, 7);
		  params.put("q.enriched.url.enrichedTitle.entities.entity.type", "company");
		  params.put("q.enriched.url.enrichedTitle.entities.entity.text", companyName);
		  
		  DocumentsResult result =  service.getNewsDocuments(params);
		  Documents d = result.getDocuments();
		  ArrayList list = new ArrayList();
		  //System.out.println(d);
		  for(Document d2 : d.getDocuments()){

			  for(Entity e : d2.getSource().getEnriched().getArticle().getEnrichedTitle().getEntities()){
				  if(e.getType().toString().equals("Company") && !e.getText().toString().equals(companyName))
					  list.add(e.getText().toString());
			  }
		  }
		  return list;
	}
	
	public  Documents getSentimentAnalisysOfNews(String companyName) throws BadRequestException{
		Map<String, Object> params = new HashMap<String, Object>();

		String[] fields =
		      new String[] {"enriched.url.title", "enriched.url.url",
		          "enriched.url.enrichedTitle.docSentiment"};
		params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));
		params.put(AlchemyDataNews.START, "now-1d");
		params.put(AlchemyDataNews.END, "now");
		params.put(AlchemyDataNews.COUNT, 7);
		//params.put("q.enriched.url.title", "Apple");
		//params.put("q.enriched.url.text", "Apple");
		params.put("q.enriched.url.enrichedTitle.entities.entity.type", "company");
		params.put("q.enriched.url.enrichedTitle.entities.entity.text", companyName);

		DocumentsResult result =  service.getNewsDocuments(params);
		
		return result.getDocuments();
	}
}
