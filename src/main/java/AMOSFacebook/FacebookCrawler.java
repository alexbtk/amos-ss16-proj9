/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSFacebook;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;

/**
 * 
 * Class to access Facebook graph API via facebook4j library
 *
 */
public class FacebookCrawler {
	
	private final Facebook facebook;
	
	public FacebookCrawler(String appId, String appSecret){
		facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appId, appSecret);
	}
	
	/**
	 * get posts about a company name and display it in the console window
	 * problem: in nov/15 facebook deactivated this functionality
	 * only possible with but not available at the moment:
	 * https://developers.facebook.com/docs/public_feed
	 * 
	 * @param companyName name of the company to search
	 */
	
	public void crawlPublicPosts(String companyName){
		if (companyName != null && companyName.length() != 0){
			try {
				
				ResponseList<Post> results = facebook.searchPosts(companyName);
				
				for (Post post : results) {
					System.out.println(post.getMessage());
				}
			} catch (FacebookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
