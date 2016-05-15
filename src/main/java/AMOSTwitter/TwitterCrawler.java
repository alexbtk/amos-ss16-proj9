package AMOSTwitter;

import java.util.List;

import javax.security.auth.login.Configuration;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Class to access twiter4j library and to crawl tweets 
 * 
 */
public class TwitterCrawler {

	private final AccessToken accessToken;
	private final Twitter twitter;

	public TwitterCrawler(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		twitter = new TwitterFactory().getInstance();
		accessToken = new AccessToken(token, tokenSecret);
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		twitter.setOAuthAccessToken(accessToken);

		// accessToken = new AccessToken(token, tokenSecret);

		// twitter = new TwitterFactory().getInstance(accessToken);
		// twitter.setOAuthConsumer(consumerKey, consumerSecret);
	}
	
	/**
	 * Get posts/tweets concerning a specific company name
	 * 
	 * @param companyName	company name to search tweets for
	 * @return	list of tweets (with additional attributes) about searched company
	 */
	public List<Status> crawlPosts(String companyName) {
		try {
			Query query = new Query("#" + companyName);
			QueryResult result;
			result = twitter.search(query);
			List<Status> tweets = result.getTweets();
			return tweets;

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			return null;
		}

	}

}
