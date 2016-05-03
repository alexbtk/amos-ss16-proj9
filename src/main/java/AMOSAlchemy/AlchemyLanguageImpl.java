package AMOSAlchemy;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public class AlchemyLanguageImpl implements IAlchemyLanguage{
	
	private AlchemyLanguage service;
	
	public AlchemyLanguageImpl(String apiKey){
		service = new AlchemyLanguage();
		service.setApiKey(apiKey);
	}

	//@Override
	public Taxonomies getCompanyTaxonomies(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.ENTITIES, company);
        
        return service.getTaxonomy(params); //Result from AlchemyLanguage service for taxonomies
	}
	
	//@Override
	public Entities getCompanyEntities(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.ENTITIES, company);
        
        //Keywords k = service.getKeywords(params);
        return service.getEntities(params);
        
	}
	public DocumentSentiment getSentimentForText(String text) throws BadRequestException{
		Map<String,Object> params = new HashMap<String, Object>();
		params.put(AlchemyLanguage.TEXT, text);
		return service.getSentiment(params);
	}
}
