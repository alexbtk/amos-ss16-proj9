/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	}
	
	public void setAlchemyConceptsImpl(String username, String password){
		this.alchemyConcepts = new AlchemyConceptsImpl(username,password);
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
	 * Get new with sentiment analysis about an entity
	 * 
	 * @param name - company or product
	 * @param entity
	 * @return list of news
	 */
	public String getSentimentAnalysisOfNews(String name, String entity, String startTime, String endTime, int count)throws BadRequestException{
		Documents docs = alchemyNews.getSentimentAnalysisOfNews(name, entity, startTime, endTime, count);
		String positive = "", negative = "";
		Integer total = 0, poz = 0, neg = 0;
		if(docs != null && docs.getDocuments() != null)
			for(Document d : docs.getDocuments()){
				
				String  sentiment = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getType().toString();
				String  score = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getScore().toString();
				if(sentiment.toLowerCase().equals("positive")){
					++poz;
					positive += d.getSource().getEnriched().getArticle().getTitle().toString()+"\n";
					positive += d.getSource().getEnriched().getArticle().getUrl().toString()+"\n\n";
					
				}
				if(sentiment.toLowerCase().equals("negative")){
					++neg;
					negative += d.getSource().getEnriched().getArticle().getTitle().toString()+"\n";
					negative += d.getSource().getEnriched().getArticle().getUrl().toString()+"\n\n";
					
				}
			}
		return "Positive articles: "+poz.toString()+"\n"+positive+" Negative Articles: "+neg.toString()+"\n"+negative;
	}
	
	/**
	 * Get new with sentiment analysis about an entity
	 * 
	 * @param name - company or product
	 * @param entity
	 * @return list of news
	 */
	public double getNumberSentimentAnalysisOfNews(String name, String entity, String startTime, String endTime, int count)throws BadRequestException{
		Documents docs = alchemyNews.getSentimentAnalysisOfNews(name,entity,startTime, endTime, count);
		System.out.println(docs);
		if(docs == null || docs.getDocuments() == null) return 0;
		String positive = "", negative = "";
		Integer total = 0, poz = 0, neg = 0;
		for(Document d : docs.getDocuments()){
			
			String  sentiment = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getType().toString();
			String  score = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getScore().toString();
			if(sentiment.toLowerCase().equals("positive")){
				++poz;				
			}
			if(sentiment.toLowerCase().equals("negative")){
				++neg;				
			}
		}
		return ((poz-neg)*1.0/(poz+neg)*1.0);
	}
	
	/**
	 * GEt the sentiment of the competitors products
	 * 
	 * @param name - product name
	 * @param companyResource
	 * @return map(product,sentiment)
	 */
	public Map<String, String> getCompetitorsProductSentiment(String name, String companyResource){
		List<String> products = DBpedia.getProductCompetitorsName(name,companyResource);
		products.add(name);
		Map<String, String> relatedproduct = new HashMap<String, String>();
		for(String product : products){
			relatedproduct.put(product, String.valueOf(getNumberSentimentAnalysisOfNews(product,"O[Product^Technology^OperatingSystem^Facility^FieldTerminology]","now-5d", "now", 20)));
		}
		return relatedproduct;
	}
	
	@Override
	public double getAvgNewsSentiment(String companyName, String entity, String startTime, String endTime, int limit)
			throws BadRequestException {
		Documents docs = alchemyNews.getSentimentAnalysisOfNews(companyName, entity, startTime, endTime, limit);
		double avgSentiment = 0.0;
		if(docs != null && docs.getDocuments() != null){
			int numDocs = docs.getDocuments().size();
			for(Document d : docs.getDocuments()){
				double sentiment = d.getSource().getEnriched().getArticle().getEnrichedTitle().getSentiment().getScore().doubleValue();
				//System.out.println(sentiment);
				avgSentiment += sentiment;
			}
			avgSentiment = avgSentiment/(double)numDocs;
		}
		return avgSentiment;
	}
	
	/**
	 * Get the sentiment of an entity over a period of time
	 * 
	 * @param entityName - an array with the names of products or companies
	 * @param entity - the company or product
	 * @param weeks - number of weeks
	 * @param limit - the limit of news for a query
	 */
	public ArrayList<String> getAvgNewsSentimentPeriod(String[] entityName, String entity, int weeks, int limit)throws BadRequestException{
		double[] rs = new double[weeks];
		double k = 0.;
		for(int j=0;j<entityName.length;++j){
			double avgSentiment = getAvgNewsSentiment(entityName[j], entity, "now-7d", "now", limit);
			rs[0] += avgSentiment;

			for(int i = 1; i < weeks; i++){
				avgSentiment = getAvgNewsSentiment(entityName[j], entity, "now-" + (7*(i+1)) + "d", "now-" + (7*(i)) + "d", limit);
				rs[i] += avgSentiment;
			}
			k=k+1;
		}
		ArrayList<String> ra =  new ArrayList<String>();
		for(int j=0;j<weeks;++j){
			rs[j] = rs[j]/k;
			ra.add(String.valueOf(rs[j]));
			//System.out.println(rs[j]);
		}
		
		return ra;
	}


}
