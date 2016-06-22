/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class Demo {

	public static void main(String[] args) {
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("<api-key>");
		service.setAlchemyConceptsImpl("<username>","<password>");
		
		AlchemyConceptsImpl alchemyConcepts = new AlchemyConceptsImpl("4998caf2-5f4c-46de-b88f-5f530af7425b", "qfKiK2XDQlxJ");

		try{
			String[] d = {"Iphone","Ipad"};
			System.out.println(StringUtils.join(service.getAvgNewsSentimentPeriod(Arrays.copyOfRange(d,1,2),"O[Product^Technology^OperatingSystem^Facility^FieldTerminology]",2,1),","));
			//System.out.println("Related concepts to abstract: " + alchemyConcepts.getRelatedDocs(alchemyConcepts.getAbstract("Microsoft")));
			
			//System.out.println("Main industry: " + service.getCompanyMainIndustry("Apple"));
			//System.out.println("Main product: " + service.getCompanyMainProduct("Apple"));
			//System.out.println("Possible competitors: " + service.getPossibleCompetitors("Apple"));
			//System.out.println("News sentiment: " + service.getSentimentAnalysisOfNews("Apple","Company", "now-5d", "now", 20));
			//System.out.println("Competitors Sentiment: Iphone-" + service.getCompetitorsProductSentiment("IPhone","<http://dbpedia.org/resource/Apple_Inc.>"));
			
			
		} catch(BadRequestException e){
			System.out.println("Could not find company...");
		}
	}

}
