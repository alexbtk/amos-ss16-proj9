/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSTwitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.ext.com.google.common.collect.ImmutableMap;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import AMOSAlchemy.IAlchemyLanguage;
import AMOSTwitterBluemix.TwitterBluemixPost;
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
		
		List<Double> sentimentValues = new ArrayList<Double>();
		Double sumSentiment = 0.0;
		for (Status tweet : tweets) {
			try {
				sentimentValues.add(languageService.getSentimentForText(tweet.getText()));
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
	
	public double getAverageSentimetForBluemixTweets(List<TwitterBluemixPost> tweets, IAlchemyLanguage languageService) {
		Map<String, Double> VAL = ImmutableMap.of("POSITIVE", 1., "NEGATIVE", -1., "NEUTRAL", 0.);
		
		List<Double> sentimentValues = new ArrayList<Double>();
		Double sumSentiment = 0.0;
		for (TwitterBluemixPost tweet : tweets) {
			try {
				if(tweet.sentiment != null)
					sentimentValues.add(VAL.get(tweet.sentiment));
				else sentimentValues.add(-1.);
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
	
	
	public HashMap<Long, Double> getSentimentForEachTweet(List<Status> tweets, IAlchemyLanguage languageService){
		
		DocumentSentiment sentiment;
		HashMap<Long, Double> map = new HashMap<Long, Double>();
		for (Status tweet: tweets){
			try {
				map.put(tweet.getId(), languageService.getSentimentForText(tweet.getText()));
			} catch (Exception e) {
				// Exception handling is for pussies
			}
		}
		
		return map;
	}
	
	public HashMap<String, Double> getSentimentForEachBluemixTweet(List<TwitterBluemixPost> tweets, IAlchemyLanguage languageService){
		Map<String, Double> VAL = ImmutableMap.of("POSITIVE", 1., "NEGATIVE", -1., "NEUTRAL", 0.);
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (TwitterBluemixPost tweet: tweets){
			try {
				if(tweet.sentiment != null)
					map.put(tweet.id, VAL.get(tweet.sentiment));
				else map.put(tweet.id, -1.);
			} catch (Exception e) {
				// Exception handling is for pussies
			}
		}
		
		return map;
	}

}
