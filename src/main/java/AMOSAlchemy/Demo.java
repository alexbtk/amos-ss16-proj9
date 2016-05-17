package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("<api-key>");

		try{
			
			System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			System.out.println("Main product: " + service.getCompanyMainProduct("Apple"));
			System.out.println("Possible competitors: " + service.getPossibleCompetitors("Apple"));
			System.out.println("News sentiment: " + service.getSentimentAnalysisOfNews("Apple","Company"));
			System.out.println("Competitors Sentiment: Iphone-" + service.getCompetitorsProductSentiment("IPhone","<http://dbpedia.org/resource/Apple_Inc.>"));
			
		} catch(BadRequestException e){
			System.out.println("Could not find company...");
		}
	}

}
