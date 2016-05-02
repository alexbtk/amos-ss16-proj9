package AMOSAlchemy;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomy;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public class AlchemyImpl implements IAlchemy{

	private IAlchemyLanguage alchemyLanguage;
	
	public AlchemyImpl(String apiKey){
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
		
	}
	
	@Override
	public String getCompanyMainIndustry(String companyName) throws BadRequestException{
		//Assumes that company has a .com website
		String companyUrl = "http://" + companyName.toLowerCase() + ".com";
		Taxonomies t = alchemyLanguage.getCompanyTaxonomies(companyName, companyUrl);

        Taxonomy maxScore = null; //return result
        
        //Find taxonomy with highest score
        for(Taxonomy score : t.getTaxonomy()){
        	if(maxScore == null || score.getScore() > maxScore.getScore())
        		maxScore = score;
        }
        
		return maxScore.getLabel();
	}

	@Override
	public String getCompanyMainProduct(String companyName) throws BadRequestException{
		String companyUrl = "http://" + companyName.toLowerCase() + ".com";
		Entities e = alchemyLanguage.getCompanyEntities(companyName, companyUrl);
		
		Entity result = null;
        for(Entity i : e.getEntities()){
        	String type = i.getType();
        	if(!type.equals("Company") && !type.equals("Facility") && !type.equals("JobTitle") && !type.equals("FieldTerminology") && !type.equals("Quantity")){
        		if(result == null)
        			result = i;
        		else if(i.getRelevance() > result.getRelevance())
        			result = i;
        	}
        }
        //System.out.println(e);
		
        if(result == null)
        	return "No main product found...";
        else
        	return result.getText();
	}

}
