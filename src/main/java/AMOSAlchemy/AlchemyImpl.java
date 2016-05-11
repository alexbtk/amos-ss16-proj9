package AMOSAlchemy;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Document;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Documents;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomy;
import com.ibm.watson.developer_cloud.service.BadRequestException;

import AMOSDBPedia.DBpedia;


public class AlchemyImpl implements IAlchemy{

	private IAlchemyLanguage alchemyLanguage;
	private AlchemyNewsImpl alchemyNews;
	private AlchemyConceptsImpl alchemyConcepts;
	
	public AlchemyImpl(String apiKey){
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
		this.alchemyNews = new AlchemyNewsImpl(apiKey);
		this.alchemyConcepts = new AlchemyConceptsImpl(apiKey);
	}
	
	//@Override
	public String getCompanyMainIndustry(String companyName) throws BadRequestException{
		String companyUrl = AMOSDBPedia.DBpedia.getCompanyHomepage(companyName);
		if(companyUrl == null)
			companyUrl = "http://" + companyName.toLowerCase() + ".com"; //Assumes that company has a .com website if no website found on DBpedia
		
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
		String companyUrl = AMOSDBPedia.DBpedia.getCompanyHomepage(companyName);
		if(companyUrl == null)
			companyUrl = "http://" + companyName.toLowerCase() + ".com"; //Assumes that company has a .com website if no website found on DBpedia
		
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
	
	public ArrayList<String> getPossibleProducts(String companyName) throws BadRequestException{
			
		String possibleProducts = "";
		String companyUrl = AMOSDBPedia.DBpedia.getCompanyHomepage(companyName);
		Entities e = alchemyLanguage.getCompanyEntities(companyName, companyUrl);
		ArrayList<String> list = new ArrayList<String>();		
        for(Entity i : e.getEntities()){
        	String type = i.getType();
        	if(type.equals("Product") || type.equals("Technology")
        			//|| type.equals("FieldTerminology")
        			){
        		possibleProducts += "/" + i.getText();
        		list.add(i.getText());
        	}
        }
        System.out.println(list);
		return list;		
	}
	
	public boolean sameCategory(String pr1, String pr2){
		if(pr1==null || pr2 == null)return false;
		if(alchemyLanguage.getCat(pr1) == null)return false;
		if(alchemyLanguage.getCat(pr2) == null)return false;
		if(alchemyLanguage.getCat(pr1).toLowerCase().trim().equals(alchemyLanguage.getCat(pr2).toLowerCase().trim()))
			return true;
		return false;
	}
	
	public String getCompetitorsProducts(String companyName)throws BadRequestException{
		String result = "";
		ArrayList list = alchemyNews.getPossibibleCompetitorsList(companyName);
		System.out.println(list);
		List<String> res = DBpedia.getCompanies(companyName);
		List<String> products = /*getPossibleProducts(companyName);*/DBpedia.getCompanyProducts(res.get(0));
		System.out.println("--");
		ArrayList<ArrayList<String>> competitorProducts = new ArrayList<ArrayList<String>>();		
		for(String s : products){
			ArrayList<String> l = new ArrayList<String>();
			l.add(s);
			competitorProducts.add(l);
		}
		//System.out.println(list);
		String possibleCompetitors = "";
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()){
			List<String> resC = DBpedia.getCompanies(itr.next());
			System.out.println(resC);
			if(!resC.equals(null) && !resC.isEmpty()){
				List<String> productsC = /*getPossibleProducts(companyName);*/DBpedia.getCompanyProducts(resC.get(0));
				System.out.println("-");
				System.out.println(productsC);
				System.out.println("-1");
				if(productsC != null && !productsC.equals(null) && !productsC.isEmpty())
					for(String s : productsC){
						System.out.println("-0");
						for(ArrayList<String> al : competitorProducts){
							String product = al.get(0);
							if(sameCategory(product,s))
								al.add(s);
						}
					}
			}
		}
		System.out.println(competitorProducts);
		return result;
	}
	
	public String getProductCategories(String products) throws BadRequestException{
		return alchemyConcepts.getCategory(products);
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
