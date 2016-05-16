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

	private AlchemyLanguageImpl alchemyLanguage;
	private AlchemyNewsImpl alchemyNews;
	private AlchemyConceptsImpl alchemyConcepts;
	
	public AlchemyImpl(String apiKey){
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
		this.alchemyNews = new AlchemyNewsImpl(apiKey);
		this.alchemyConcepts = new AlchemyConceptsImpl();//put here pass and username
	}
	
	@Override
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

	@Override
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
		
        if(result == null)
        	return "No main product found...";
        else
        	return result.getText();
	}
	
	/**
	 * Get possible competitors
	 * 
	 * @param companyName
	 * @return list of competitors
	 */
	@Override
	public String getPossibleCompetitors(String companyName) throws BadRequestException{
		
		ArrayList list = alchemyNews.getPossibibleCompetitorsList(companyName);
		String possibleCompetitors = "";
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()){
				 possibleCompetitors += "/" + itr.next();
		}
		return possibleCompetitors;		
	}
	
	/**
	 * Get possible products list using company web site
	 * 
	 * @param companyName
	 * @return list of product founded on web site
	 */
	public ArrayList<String> getPossibleProducts(String companyName) throws BadRequestException{
			
		String possibleProducts = "";
		List<String> res = DBpedia.getCompanies(companyName);
		String companyUrl = AMOSDBPedia.DBpedia.getCompanyHomepage(res.get(0));
		System.out.println(companyUrl);
		Entities e = alchemyLanguage.getCompanyEntities(res.get(0), companyUrl);
		ArrayList<String> list = new ArrayList<String>();		
        for(Entity i : e.getEntities()){
        	String type = i.getType();
        	if(type.equals("Product") || type.equals("Technology")
        			//|| type.equals("FieldTerminology") 
        			||	type.equals("OperatingSystem") ||	type.equals("Facility")
        			){
        		possibleProducts += "/" + i.getText();
        		list.add(i.getText());
        	}
        }
        return list;		
	}
	
	/**
	 * Check if two products are from same category
	 * 
	 * @param pr1 - product2 name
	 * @param pr2 - product1 name
	 * @return true if product are from same category
	 */
	public boolean sameCategory(String pr1, String pr2){
		if(pr1==null || pr2 == null)return false;
		//use DBpedia to get product category/type
		String pr1Cat = getProductCategories(pr1);
		String pr2Cat = getProductCategories(pr2);
		System.out.println(pr1+"-"+pr1Cat+"|"+pr2+"-"+pr2Cat);
		if(pr1Cat == null)return false;
		if(pr2Cat == null)return false;
		if(pr1Cat.toLowerCase().trim().equals(pr2Cat.toLowerCase().trim()))
			return true;
		return false;
	}
	
	/**
	 * Get possible competitors from news
	 * 
	 * @param companyName
	 * @return possible competitors list
	 */
	public String getCompetitorsProducts(String companyName)throws BadRequestException{
		String result = "";
		ArrayList list = new ArrayList();//alchemyNews.getPossibibleCompetitorsList(companyName);
		list.add("Microsoft");
		list.add("Lenovo");// for testing
		//use DBpedia to get competitors
		List<String> res = DBpedia.getCompanies(companyName);
		List<String> products = getPossibleProducts(companyName);
		products.addAll(DBpedia.getCompanyProducts(res.get(0)));
		ArrayList<ArrayList<String>> competitorProducts = new ArrayList<ArrayList<String>>();		
		for(String s : products){
			ArrayList<String> l = new ArrayList<String>();
			l.add(s);
			competitorProducts.add(l);
		}
		String possibleCompetitors = "";
		Iterator<String> itr = list.iterator();
		while(itr.hasNext()){
			String competitor = itr.next();
			List<String> resC = DBpedia.getCompanies(competitor);
			if(!resC.equals(null) && !resC.isEmpty()){
				List<String> productsC = getPossibleProducts(competitor);
				productsC.addAll(DBpedia.getCompanyProducts(resC.get(0)));
				if(productsC != null && !productsC.equals(null) && !productsC.isEmpty())
					for(String s : productsC){
						for(ArrayList<String> al : competitorProducts){
							String product = al.get(0);
							if(sameCategory(product,s))
								al.add(s);
						}
					}
			}
		}
		return result;
	}
	
	/**
	 * Get product category using product abstract and keywords.
	 * 
	 * @param product - product name
	 * @return product category
	 */
	public String getProductCategories(String products) throws BadRequestException{
		return alchemyLanguage.getRelationObject(alchemyConcepts.getAbstract(products));
	}
	
	/**
	 * Get new with sentiment analysis about a company
	 * 
	 * @param companyName
	 * @return list of news
	 */
	public String getSentimentAnalysisOfNews(String companyName)throws BadRequestException{
		Documents docs = alchemyNews.getSentimentAnalysisOfNews(companyName);
		String positive = "", negative = "";
		Integer total = 0, poz = 0, neg = 0;
		for(Document d : docs.getDocuments()){
			
			String  sentiment = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getType().toString();
			String  score = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getScore().toString();
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
