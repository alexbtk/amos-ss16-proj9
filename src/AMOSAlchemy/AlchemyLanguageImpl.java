package AMOSAlchemy;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomy;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public class AlchemyLanguageImpl implements IAlchemyLanguage{
	
	private AlchemyLanguage service;
	
	public AlchemyLanguageImpl(String apiKey){
		service = new AlchemyLanguage();
		service.setApiKey(apiKey);
	}

	@Override
	public String getCompanyTaxonomy(String company, String companyUrl) throws BadRequestException{
		//Set parameters for request
		Map<String, Object> params = new HashMap<String, Object>();
        params.put(AlchemyLanguage.URL, companyUrl);
        params.put(AlchemyLanguage.ENTITIES, company);
        
        Taxonomies t = service.getTaxonomy(params); //Result from AlchemyLanguage service for taxonomies
        
        Taxonomy maxScore = null; //return result
        
        //Find taxonomy with highest score
        for(Taxonomy score : t.getTaxonomy()){
        	if(maxScore == null || score.getScore() > maxScore.getScore())
        		maxScore = score;
        }
        
		return maxScore.getLabel();
	}
}
