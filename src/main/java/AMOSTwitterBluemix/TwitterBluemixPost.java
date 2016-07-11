package AMOSTwitterBluemix;

public class TwitterBluemixPost {

	public String id;
	public String displayName;
	public String postContent;
	public String sentiment;
	public String retweetCount;
	
	public TwitterBluemixPost(){
		
	}
	
	public TwitterBluemixPost(String id, String displayName, String postContent, String sentiment, String retweetCount){
		this.id = id;
		this.displayName = displayName;
		this.postContent = postContent;
		this.sentiment = sentiment;
		this.retweetCount = retweetCount;
	}
	
	public static TwitterBluemixPost parse(String s){
		String[] split = s.split(",");
		
		if(split.length > 4){
			TwitterBluemixPost post = new TwitterBluemixPost(
					split[0], split[1], split[2], split[3], split[4]);
			
			return post;
		}
		else
			return null;
	}
	
	@Override
	public String toString(){
		String s =  "" + id + "," +
					"" + displayName + "," +
					"" + postContent + "," +
					"" + sentiment + "," +
					"" + retweetCount;
		return s;
	}
}
