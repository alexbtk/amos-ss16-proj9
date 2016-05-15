package AMOSTwitter;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import AMOSAlchemy.IAlchemyLanguage;
import twitter4j.Status;

/**
 * Wrapper Class for alchemy language api and twitter4j
 * 
 */
public class TwitterAnalyzer {

	public TwitterAnalyzer() {

	}

	/**
	 * Method to get average sentiment value of given tweets
	 * 
	 * @param tweets	tweets in a list to analyze
	 * @param languageService	IAlchemyLanguage object to work with
	 * @return	average double sentiment value of provided tweets
	 */
	public double getAverageSentimetForTweets(List<Status> tweets, IAlchemyLanguage languageService) {
		DocumentSentiment sentiment;
		List<Double> sentimentValues = new ArrayList<Double>();
		Double sumSentiment = 0.0;
		for (Status tweet : tweets) {
			try {
				sentiment = languageService.getSentimentForText(tweet.getText());
				sentimentValues.add(sentiment.getSentiment().getScore().doubleValue());
			} catch (Exception e) {
				// Exception handling is for pussies
			}
		}
		System.out.println(sentimentValues.size());

		for (Double value : sentimentValues) {
			sumSentiment = sumSentiment + value;
		}

		return sumSentiment / (sentimentValues.size());
	}

}
