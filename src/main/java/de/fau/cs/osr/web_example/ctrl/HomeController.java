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

	IAlchemyFactory fac;
	IAlchemy service;
	IAlchemyLanguage languageService;
	TwitterCrawler twitterCrawler;
	TwitterAnalyzer twitterAnalyzer;

	
	public HomeController(){
		fac = IAlchemyFactory.newInstance();
		twitterAnalyzer = new TwitterAnalyzer();
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
		
		List answers = new ArrayList();
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
			answers.add("{\"title\":\"Company competitors(Alchemy)\",\"content\":\"" + 
					service.getPossibleCompetitors(requests.get("question3b"))+"\"}");
		}
		if(requests.containsKey("question4")){
			answers.add("{\"title\":\"News Sentiment(Alchemy)\",\"content\":\"" + 
					(service.getSentimentAnalysisOfNews(requests.get("question4"),"Company")).replace("\n", "<br />")+"\"}");
		}
		if(requests.containsKey("question6")){
			List<Status> posts = twitterCrawler.crawlPosts(requests.get("question6"));
			Double avgSentimentValue = twitterAnalyzer.getAverageSentimetForTweets(posts, languageService);
			answers.add("{\"title\":\"Twiter vs News Sentiment\",\"content\":\"" + 
					"<p>News: " + service.getNumberSentimentAnalysisOfNews(requests.get("question6"),"Company")+ 
					"</p><p>Twiter:"+avgSentimentValue.toString()+"</p>\"}");
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
		if(requests.containsKey("industryCompanies")){
			HashMap<String,String> map = DBpedia.getIndustryCompaniesNames(requests.get("industryCompanies"));
			Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove(); 
		        answers.add("{\"name\":\""+pair.getKey()+"\",\"resource\":\"" + pair.getValue()+"\"}");
		    }
		}
		
		return "["+StringUtils.join(answers, ",")+"]";
	}
	
	@RequestMapping(value="/")
	public String loadHomePage(Model m) {
		
		return "home";
	}
	
}
