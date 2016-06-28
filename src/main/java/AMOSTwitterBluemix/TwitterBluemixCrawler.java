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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


/**
 * Class to access twiter4j library and to crawl tweets 
 * 
 */
public class TwitterBluemixCrawler {

	private String userName = "2a600621-3b03-480e-a4fe-d7a4c1fb4df4";
	private String password = "e9knQDBqcE";

	public TwitterBluemixCrawler(String userName, String password) {
		this.userName = userName;
		this.password = password;
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
		OkHttpClient client = new OkHttpClient();
		JSONArray jsonArray = null;
		
		try {
			Request request = new Request.Builder().url("https://" + userName + ":" + password + "@cdeservice.mybluemix.net:443/api/v1/messages/search?q=" + companyName + "&from=" + startRecord + "&size=" + numberOfRecords).build();
			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(responseString);
			jsonArray = (JSONArray)obj;
			return jsonArray;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;

	}

}
