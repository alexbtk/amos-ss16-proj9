package AMOSAlchemy;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Concepts;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.SAORelation;
import com.ibm.watson.developer_cloud.alchemy.v1.model.SAORelations;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public class AlchemyLanguageImpl implements IAlchemyLanguage{
	
	private AlchemyLanguage service;
	
	public AlchemyLanguageImpl(String apiKey){
		service = new AlchemyLanguage();
		service.setApiKey(apiKey);
	}

	@Override
	public Taxonomies getCompanyTaxonomies(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.ENTITIES, company);
        
        return service.getTaxonomy(params); //Result from AlchemyLanguage service for taxonomies
	}
	
	@Override
	public Entities getCompanyEntities(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.XPATH, companyUrl);
        params.put(AlchemyLanguage.CQUERY, companyUrl);
        params.put(AlchemyLanguage.SOURCE_TEXT, AlchemyLanguage.RAW);
        params.put(AlchemyLanguage.MAX_RETRIEVE, 200);
        //params.put(AlchemyLanguage.ENTITIES, "Product");
        
        //Keywords k = service.getKeywords(params);
        return service.getEntities(params);
        
	}
	
	public  Entities getCompanyRelated(String company) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.TEXT, company);
        params.put(AlchemyLanguage.REQUIRED_ENTITIES, "Company");
        
        System.out.println(service.getEntities(params));
         return null;        
	}
	
	public String getKeyword(String text) throws BadRequestException{
		// Get entities from text in order to analyze
		Map<String, Object> params = new HashMap<String, Object>();		
        params.put(AlchemyLanguage.TEXT, text);        
        Entities k = service.getEntities(params);
        
        Entity result = null;
        for(Entity e : k.getEntities()){        	
        	String type = e.getType();
        	if(type.equals("FieldTerminology")||type.equals("Product") 
        			|| type.equals("Technology")){
        		if(result == null)
        			result = e;
        		else if(e.getRelevance() > result.getRelevance())
        			result = e;
        	}
        }
        if(result == null)
           	return null;
        else
           	return result.getText();
	}
	
	public String getRelationObject(String text) throws BadRequestException{
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.TEXT, text);    
        SAORelations k = service.getRelations(params);
        
        for( SAORelation r : k.getRelations()){
        	// Check for 'to be' verb
        	if(r.getAction().getText().equals("is")){
        		 return getKeyword(r.getObject().getText());
        	}
        }
        return null;
	}
	
	public Concepts getCompanyConcepts(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.ENTITIES, company);
        
        return service.getConcepts(params);        
	}
	
	public DocumentSentiment getSentimentForText(String text) throws BadRequestException{
		Map<String,Object> params = new HashMap<String, Object>();
		params.put(AlchemyLanguage.TEXT, text);
		return service.getSentiment(params);
	}
}
