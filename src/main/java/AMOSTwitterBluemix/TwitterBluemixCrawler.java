/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-06-28
*/
package AMOSTwitterBluemix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import com.squareup.okhttp.Call;

import AMOSCache.AMOSCache;

import org.json.simple.parser.JSONParser;


/**
 * Class to access twiter4j library and to crawl tweets 
 * 
 */
public class TwitterBluemixCrawler {

	private String userName = "2a600621-3b03-480e-a4fe-d7a4c1fb4df4";
	private String password = "e9knQDBqcE";
	
	private AMOSCache cache;

	public TwitterBluemixCrawler(String userName, String password) {
		if (userName != null && password != null){
		this.userName = userName;
		this.password = password;
		}
		
		this.cache = AMOSCache.getInstance();
	}

	/**
	 * Get posts/tweets concerning a specific company name
	 * 
	 * @param companyName: company name to search tweets for
	 * @param startRecord: starting record
	 * @param numberOfRecords: Specifies the number of records to return. The maximum value is 500. The value defaults to 100 if this parameter is empty
	 * @return list of tweets (with additional attributes) about searched company
	 */
	public List<TwitterBluemixPost> crawlPosts(String companyName, String startRecord, String numberOfRecords) {
		Object r = cache.getCurrentMethodCache(companyName, startRecord, numberOfRecords);
		if(r != null)
			return (List<TwitterBluemixPost>) r;
		
		OkHttpClient client = new OkHttpClient();
		String credential = Credentials.basic(userName, password);
		
		JSONObject jsonResult = null;
		JSONArray postsArray = null;
		ArrayList<TwitterBluemixPost> posts = new ArrayList<TwitterBluemixPost>();
		
		try {
			Request request = new Request.Builder().header("Authorization", credential).url("https://" + userName + ":" + password + "@cdeservice.mybluemix.net:443/api/v1/messages/search?q=" + companyName + "&from=" + startRecord + "&size=" + numberOfRecords).build();
			okhttp3.Call call = client.newCall(request);
			Response response = call.execute();
			String responseString = response.body().string();
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(responseString);
			jsonResult = (JSONObject)obj;
			postsArray = (JSONArray)jsonResult.get("tweets");
			
			if(postsArray == null){
				call.cancel();
				return null;
			}
			
			for (int i = 0; i < postsArray.size(); i++){
				TwitterBluemixPost bluemixPost = new TwitterBluemixPost();
				JSONObject jsonPost = (JSONObject)postsArray.get(i);
				JSONObject cde = (JSONObject)jsonPost.get("cde");
				JSONObject cdeContent = (JSONObject)cde.get("content");
				if (cdeContent != null){
				JSONObject cdeContentSentiment = (JSONObject)cdeContent.get("sentiment");
				bluemixPost.sentiment = cdeContentSentiment.get("polarity").toString();
				}
				
				
				
				JSONObject message = (JSONObject)jsonPost.get("message");
				bluemixPost.postContent = message.get("body").toString().trim().replace("\n", "").replace("\r", "").replace("\"", "");
				bluemixPost.id = message.get("id").toString().substring(message.get("id").toString().lastIndexOf(":") + 1);
				
				JSONObject actor = (JSONObject)message.get("actor");
				bluemixPost.displayName = actor.get("displayName").toString();
				
				bluemixPost.retweetCount = message.get("retweetCount").toString();
				
				posts.add(bluemixPost);
			}
		
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(posts.size() > 0)
			cache.putCurrentMethodCache(companyName, startRecord, numberOfRecords, posts);
		
		return posts;

	}

}
