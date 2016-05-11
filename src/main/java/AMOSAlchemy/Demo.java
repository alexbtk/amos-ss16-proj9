package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		//593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f
		//1eccdb3cfc18574d5a62e986faf05016f05fbc88
		//fd98eff08dde3219578ef740567a4604939f0a5f
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("fd799d237109e0012b419b2aeeddc979af33a683");

		try{
			
			System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			System.out.println("Main product: " + service.getCompanyMainProduct("Apple"));
			System.out.println("Possible competitors: " + service.getPossibleCompetitors("Apple"));
			System.out.println("News sentiment: " + service.getSentimentAnalisysOfNews("Apple"));
			System.out.println("Categories: " + service.getProductCategories("iPhone iPad MacBook iOS iPod"));
	
			
		} catch(BadRequestException e){
			System.out.println("Could not find company...");
		}
	}

}
