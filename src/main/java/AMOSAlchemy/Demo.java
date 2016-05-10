package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		//IAlchemyFactory fac = IAlchemyFactory.newInstance();
		//IAlchemy service = fac.createAlchemy("fd98eff08dde3219578ef740567a4604939f0a5f");

		try{
			/*
			System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			System.out.println("Main product: " + service.getCompanyMainProduct("Apple"));
			System.out.println("Possible competitors: " + service.getPossibleCompetitors("Apple"));
			System.out.println("News sentiment: " + service.getSentimentAnalisysOfNews("Apple"));
			*/
			//Possible products: /iPhone/iPad/MacBook/iOS/iPod
			//Main industry: /technology and computing/hardware/computer/portable computer/tablet
			AlchemyConceptsImpl s = new AlchemyConceptsImpl("fd98eff08dde3219578ef740567a4604939f0a5f");
			System.out.println(s.getCategory("iPhone iPad MacBook iOS iPod"));
			
			//System.out.println("Possible products: " + service.getPossibleProducts("Apple"));
			//System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			
		} catch(BadRequestException e){
			System.out.println("Could not find company...");
		}
	}

}
