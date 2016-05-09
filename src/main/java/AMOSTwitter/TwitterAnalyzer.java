package AMOSTwitter;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import AMOSAlchemy.IAlchemyLanguage;
import twitter4j.Status;

public class TwitterAnalyzer {

	public TwitterAnalyzer(){
		
	}
	
	public double getAverageSentimetForTweets(List<Status> tweets, IAlchemyLanguage languageService){
		DocumentSentiment sentiment;
		List<Double> sentimentValues = new ArrayList<Double>();
		Double sumSentiment = 1.0;
		for (Status tweet : tweets) {
			try {
			sentiment = languageService.getSentimentForText(tweet.getText());
			sentimentValues.add(sentiment.getSentiment().getScore());
			} catch (Exception e){
				// Exception handling is for pussies
			}
        }
		System.out.println(sentimentValues.size());
		
		for (Double value : sentimentValues){
			sumSentiment = sumSentiment + value;
		}
		
		
		
		return sumSentiment/(sentimentValues.size());
	}
	
}
