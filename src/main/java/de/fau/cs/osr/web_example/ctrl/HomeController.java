/**
 * Copyright 2016 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fau.cs.osr.web_example.ctrl;

import AMOSAlchemy.IAlchemyLanguage;
import AMOSAlchemy.IAlchemyToneAnalyzer;
import AMOSDBPedia.DBpedia;
import AMOSFacebook.FacebookCrawler;
import AMOSTwitter.TwitterAnalyzer;
import AMOSTwitter.TwitterCrawler;
import twitter4j.Status;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import AMOSAlchemy.IAlchemy;
import AMOSAlchemy.IAlchemyFactory;

@Controller
public class HomeController {
	
	private class AvgNewsSentimentCacheEntry{
		private long timestamp;
		private String resultString;
		
		AvgNewsSentimentCacheEntry(long timestamp, String resultString){
			this.timestamp = timestamp;
			this.resultString = resultString;
		}
		
		public long getTimestamp(){
			return timestamp;
		}
		
		public String getResultString(){
			return resultString;
		}
		
	}

	IAlchemyFactory fac;
	IAlchemy service;
	IAlchemyLanguage languageService;
	TwitterCrawler twitterCrawler;
	TwitterAnalyzer twitterAnalyzer;

	Map<String, AvgNewsSentimentCacheEntry> avgNewsSentimentCache;
	
	public HomeController(){
		fac = IAlchemyFactory.newInstance();
		twitterAnalyzer = new TwitterAnalyzer();
		avgNewsSentimentCache = new HashMap<String, AvgNewsSentimentCacheEntry>();
	}

	@RequestMapping(value="/process")
	public String loadProcessPage(@RequestParam("companyName") String companyName, @CookieValue("apiKey") String apiKey, Model m) {
		service = fac.createAlchemy(apiKey);
		
		m.addAttribute("companyName",companyName);
		m.addAttribute("category","Categories: " + service.getProductCategories("iPhone iPad MacBook iOS iPod"));
				
		return "process";
	}

	@RequestMapping(value="/getSentiment", method = RequestMethod.POST)
	public String loadTextSentiment(@RequestParam("Text") String companyName, @CookieValue("apiKey") String apiKey, @CookieValue("twitterConsumerKey") String consumerKey, @CookieValue("twitterConsumerSecret") String consumerSecret, @CookieValue("twitterToken") String token, @CookieValue("twitterTokenSecret") String tokenSecret, Model m) {
		languageService = fac.createAlchemyLanguage(apiKey);
		twitterCrawler = new TwitterCrawler(consumerKey, consumerSecret, token, tokenSecret);
		
		List<Status> posts = twitterCrawler.crawlPosts(companyName);
		Double avgSentimentValue = twitterAnalyzer.getAverageSentimetForTweets(posts, languageService);
		HashMap<Long, Double> map = twitterAnalyzer.getSentimentForEachTweet(posts, languageService);
		
		//IAlchemyLanguage languageService = fac.createAlchemyLanguage("593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f");
		//DocumentSentiment sentiment = languageService.getSentimentForText(socialMediaPost);
		m.addAttribute("textSentiment", avgSentimentValue.toString());
		m.addAttribute("postsList", posts);
		m.addAttribute("sentimentlist", map);

		return "home";
	}
	
	@RequestMapping(value="/getTone", method = RequestMethod.POST)
	public String loadTextTone(@RequestParam("Text") String text, @CookieValue("toneAnalyzerUsername") String username, @CookieValue("toneAnalyzerPassword") String password, Model m) {
		
		IAlchemyToneAnalyzer toneAnalyzer = fac.createAlchemyToneAnalyzer(username, password);
		
		String tone = toneAnalyzer.getToneAsString(text);
		
		//IAlchemyLanguage languageService = fac.createAlchemyLanguage("593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f");
		//DocumentSentiment sentiment = languageService.getSentimentForText(socialMediaPost);
		m.addAttribute("textTone", username + " " + password + " <br />" + tone);

		return "home";
	}
	
	@ResponseBody
	@RequestMapping(value="/getCompanies", method = RequestMethod.POST)
	public String loadCompanies(@RequestParam("Text") String companyName, Model m){
		List<String> list = AMOSDBPedia.DBpedia.getCompanies(companyName);
		
		if(list != null && list.size() > 0){
			String retJson = "{\"companies\":[\"" + list.get(0) + "\"";
		
			for(int i = 1; i < list.size(); i++){
				retJson += ", \"" + list.get(i) + "\"";
			}
			
			return retJson + "]}";
		}else{
			return "{\"companies\":[]}";
		}
	}
	
	/**
	 * Get the request from javascript.
	 * 
	 * @param requests - the parameters from requests, key are the name of checkbox, the value are the companyName(could be sent another info)
	 * @param m - the model
	 * @return return for each entry in the map a vector with json objects, title and content.
	 */
	@ResponseBody
	@RequestMapping(value="/qeuryRequest", method = RequestMethod.POST)
	public String queryResponse(@RequestParam Map<String, String>  requests, @CookieValue("apiKey") String apiKey, @CookieValue("twitterConsumerKey") String consumerKey, @CookieValue("twitterConsumerSecret") String consumerSecret, @CookieValue("twitterToken") String token, @CookieValue("twitterTokenSecret") String tokenSecret, Model m){
		this.service = fac.createAlchemy(apiKey);
		languageService = fac.createAlchemyLanguage(apiKey);
		twitterCrawler = new TwitterCrawler(consumerKey, consumerSecret, token, tokenSecret);
		int weeks = 1;
		List<String> answers = new ArrayList<String>();
		//Todo - catch error: incorrect companyName, noResult
		if(requests.containsKey("question1")){
			answers.add("{\"title\":\"Main industry(Alchemy)\",\"content\":\"" + 
					service.getCompanyMainIndustry(requests.get("question1"))+"\"}");
		}
		if(requests.containsKey("question3a")){
			answers.add("{\"title\":\"Company competitors(DBpedia)\",\"content\":\"" + 
					StringUtils.join(DBpedia.getCompanyCompetitorsName(requests.get("question3a")), ",")+"\"}");
		}
		if(requests.containsKey("question3b")){
			answers.add("{\"title\":\"Company competitors(Alchemy)\",\"content\":" + 
					service.getPossibleCompetitors(requests.get("question3b"))+"}");
		}
		if(requests.containsKey("question4")){
			weeks = Integer.parseInt(requests.get("timeframe"));
			answers.add("{\"title\":\"News Sentiment(Alchemy)\",\"content\":\"" + 
					(service.getSentimentAnalysisOfNews(requests.get("question4"),"Company", "now-" + (7*weeks) + "d", "now", 20)).replace("\n", "<br />")+"\"}");
		}
		if(requests.containsKey("question6")){
			List<Status> posts = twitterCrawler.crawlPosts(requests.get("question6"));
			Double avgSentimentValue = twitterAnalyzer.getAverageSentimetForTweets(posts, languageService);
			answers.add("{\"title\":\"Twiter vs News Sentiment\",\"content\":\"" + 
					"<p>News: " + service.getNumberSentimentAnalysisOfNews(requests.get("question6"),"Company", "now-1d", "now", 20)+ 
					"</p><p>Twiter:"+avgSentimentValue.toString()+"</p>\"}");
		}
		if(requests.containsKey("question7")){
			Map map = DBpedia.getCompanyLocationCoordonates(requests.get("question7"));
			Iterator it = map.entrySet().iterator();
			List location = new ArrayList<String>();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove(); 
		        location.add("{\"name\":\""+pair.getKey()+"\",\"point\":\"" + pair.getValue()+"\"}");
		    }
			answers.add("{\"title\":\"Company Location\",\"content\":[" + 
					StringUtils.join(location, ",")+"]}");
		}
		if(requests.containsKey("productsCompany")){
			List products = new ArrayList();
			List<String> DBproducts = DBpedia.getCompanyProducts(requests.get("productsCompany"));
			for(String p : DBproducts){
				products.add("\""+p+"\"");
			}
			answers.add("{\"title\":\"Company Products(DBpedia)\",\"content\":[" + 
					StringUtils.join(products, ",")+"]}");
		}
		if(requests.containsKey("productsCompetitors")){
			List products = new ArrayList();
			List<String> DBproducts = DBpedia.getCompanyProducts(requests.get("productsCompetitors"));
			String companyResourse = DBpedia.getResourceByName(requests.get("productsCompetitors")).get(0);
			for(String p : DBproducts){
				List<String> Cpr = DBpedia.getProductCompetitorsName(p,companyResourse);
				if(Cpr != null)
					for(String pC : Cpr)
						products.add("\""+pC+"\"");
			}
			answers.add("{\"title\":\"Company Competitors Products(DBpedia)\",\"content\":[" + 
					StringUtils.join(products, ",")+"]}");
		}
		if(requests.containsKey("products")){
			List products = new ArrayList();
			List<String> DBproducts = DBpedia.getCompanyProducts(requests.get("products"));
			for(String p : DBproducts){
				products.add("{\"name\":\""+p+"\"}");
			}
			answers.add("{\"title\":\"Company Products(DBpedia)\",\"content\":[" + 
					StringUtils.join(products, ",")+"]}");
		}
		if(requests.containsKey("industries")){
			HashMap<String,String> map = DBpedia.getCompanyIndustriesNames(requests.get("industries"));
			Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove(); 
		        answers.add("{\"name\":\""+pair.getKey()+"\",\"resource\":\"" + pair.getValue()+"\"}");
		    }
		}
		if(requests.containsKey("industriesCompetitors")){
			HashMap<String,String> map = DBpedia.getCompanyIndustriesNames(requests.get("industriesCompetitors"));
			Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove(); 
		        HashMap<String,String> mapC = DBpedia.getIndustryCompaniesNames("<"+pair.getValue().toString()+">");
		        Iterator itC = mapC.entrySet().iterator();
		        ArrayList<String> comp = new ArrayList<String>();
		        while (itC.hasNext()) {
		        	Map.Entry pairC = (Map.Entry)itC.next();
		        	comp.add("\""+pairC.getKey().toString()+"\"");
		        }
		        answers.add("{\"name\":\""+pair.getKey()+"\",\"comp\":[" + StringUtils.join(comp, ",")+"]}");
		    }
		}
		if(requests.containsKey("industryCompanies")){
			HashMap<String,String> map = DBpedia.getIndustryCompaniesNames(requests.get("industryCompanies"));
			Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove(); 
		        answers.add("{\"name\":\""+pair.getKey()+"\",\"resource\":\"" + pair.getValue()+"\"}");
		    }
		}
		if(requests.containsKey("avgNewsSentimentGraph")){
			if(avgNewsSentimentCache.containsKey(requests.get("avgNewsSentimentGraph"))){
				
				AvgNewsSentimentCacheEntry e = avgNewsSentimentCache.get(requests.get("avgNewsSentimentGraph"));
				long diffTime = System.currentTimeMillis() - e.getTimestamp();
				
				//if diffTime smaller than one day in milliseconds
				if(diffTime < 86400000)
					answers.add(avgNewsSentimentCache.get(requests.get("avgNewsSentimentGraph")).getResultString());
				//Cache entry too old -- create new entry
				else{
					//remove old entry
					avgNewsSentimentCache.remove(requests.get("avgNewsSentimentGraph"));
					
					String resultString = "{\"title\":\"News Sentiment Graph\",\"values\": [";
					int days = Integer.parseInt(requests.get("avgNewsSentimentGraphWeeks"));
					if(days > 0){
						double avgSentiment = this.service.getAvgNewsSentiment(requests.get("avgNewsSentimentGraph"), "Company", "now-7d", "now", 5);
						resultString += avgSentiment;

						for(int i = 1; i < days; i++){
							avgSentiment = this.service.getAvgNewsSentiment(requests.get("avgNewsSentimentGraph"), "Company", "now-" + (7*i) + "d", "now-" + (7*(i-1)) + "d", 5);
							resultString += ", " + avgSentiment;
						}

						resultString += "]}";
						answers.add(resultString);
						avgNewsSentimentCache.put(requests.get("avgNewsSentimentGraph"), new AvgNewsSentimentCacheEntry(System.currentTimeMillis(), resultString));
					}
				}
			}
			else{
				String resultString = "{\"title\":\"News Sentiment Graph\",\"values\": [";
				int days = Integer.parseInt(requests.get("avgNewsSentimentGraphWeeks"));
				if(days > 0){
					double avgSentiment = this.service.getAvgNewsSentiment(requests.get("avgNewsSentimentGraph"), "Company", "now-7d", "now", 5);
					resultString += avgSentiment;

					for(int i = 1; i < days; i++){
						avgSentiment = this.service.getAvgNewsSentiment(requests.get("avgNewsSentimentGraph"), "Company", "now-" + (7*i) + "d", "now-" + (7*(i-1)) + "d", 5);
						resultString += ", " + avgSentiment;
					}

					resultString += "]}";
					answers.add(resultString);
					avgNewsSentimentCache.put(requests.get("avgNewsSentimentGraph"), new AvgNewsSentimentCacheEntry(System.currentTimeMillis(), resultString));
				}
			}
		}
		
		return "["+StringUtils.join(answers, ",")+"]";
	}
	
	@RequestMapping(value="/getCompanyLocationMap", method = RequestMethod.POST)
	public String getLocationMap(@RequestParam String companyName, Model m) {
		Map map = DBpedia.getCompanyLocationCoordonates(companyName);
		Iterator it = map.entrySet().iterator();
		List location = new ArrayList<String>();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        it.remove(); 
	        location.add("{latLng: [" + pair.getValue() + "], name: '" + pair.getKey() + "'}"  );
	    }
	    
   
	    m.addAttribute("locations", location);
		return "locationmap";
	}
	
	@RequestMapping(value="/")
	public String loadHomePage(Model m) {
		
		return "index";
	}
	
	@RequestMapping(value="/template")
	public String loadAdminLteTemplate(Model m) {
		
		return "template";
	}

	@RequestMapping(value="/demo")
	public String loadAdminLteDemo(Model m) {
		
		return "home";

	}
	
	@RequestMapping(value="/locationmap")
	public String loadLocationMapPage(Model m) {
		
		return "locationmap";
	}
	
}
