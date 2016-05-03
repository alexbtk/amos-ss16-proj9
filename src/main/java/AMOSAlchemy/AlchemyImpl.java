package AMOSAlchemy;



import java.util.ArrayList;
import java.util.Iterator;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Document;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Documents;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomy;
import com.ibm.watson.developer_cloud.service.BadRequestException;


public class AlchemyImpl implements IAlchemy{

	private IAlchemyLanguage alchemyLanguage;
	private AlchemyNewsImpl alchemyNews;
	
	public AlchemyImpl(String apiKey){
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
		this.alchemyNews = new AlchemyNewsImpl(apiKey);
	}
	
	//@Override
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

	//@Override
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
	
	//@Override
	public String getPossibleCompetitors(String companyName) throws BadRequestException{
		
		ArrayList list = alchemyNews.getPossibibleCompetitorsList(companyName);
		//System.out.println(list);
		String possibleCompetitors = "";
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()){
				 possibleCompetitors += "/" + itr.next();
		}
		return possibleCompetitors;		
	}
	
	public String getSentimentAnalisysOfNews(String companyName)throws BadRequestException{
		Documents docs = alchemyNews.getSentimentAnalisysOfNews(companyName);
		String positive = "", negative = "";
		Integer total = 0, poz = 0, neg = 0;
		//System.out.println(docs);
		for(Document d : docs.getDocuments()){
			
			String  sentiment = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getType().toString();
			String  score = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getScore().toString();
			//System.out.println(sentiment+" "+score);
			if(sentiment.equals("POSITIVE")){
				++poz;
				positive += d.getSource().getEnriched().getArticle().getTitle().toString()+"\n";
				positive += d.getSource().getEnriched().getArticle().getUrl().toString()+"\n\n";
				
			}
			if(sentiment.equals("NEGATIVE")){
				++neg;
				negative += d.getSource().getEnriched().getArticle().getTitle().toString()+"\n";
				negative += d.getSource().getEnriched().getArticle().getUrl().toString()+"\n\n";
				
			}
		}
		return "Positive articles: "+poz.toString()+"\n"+positive+" Negative Articles: "+neg.toString()+"\n"+negative;
	}

}
