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
import AMOSTwitter.TwitterAnalyzer;
import AMOSTwitter.TwitterCrawler;
import twitter4j.Status;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
		service = fac.createAlchemy("fd799d237109e0012b419b2aeeddc979af33a683");
		languageService = fac.createAlchemyLanguage("fd799d237109e0012b419b2aeeddc979af33a683");
		twitterCrawler = new TwitterCrawler("xsqQfqabFUAX3gaoFBvShR8zP", "ZMCkHyJLyiCc25MWMJtpSuni5udZOhrLuSS616sX2hWT8rLokl","729408419602571264-oCcXJu3zfIEPZUsoYR0dHVNdiZ6GXlZ", "c9O4wafJ4Sl9APDLiHVWUVBn86WXC9Ys2HzKFAe9rBxZb");
		twitterAnalyzer = new TwitterAnalyzer();
	}

	@RequestMapping(value="/process")
	public String loadProcessPage(@RequestParam("companyName") String companyName, Model m) {


		m.addAttribute("mainIndustry","Main industry: " + service.getCompanyMainIndustry(companyName));
		m.addAttribute("mainProduct","Main product: " + service.getCompanyMainProduct(companyName));
		m.addAttribute("companyName",companyName);
		m.addAttribute("possibleCompetitors","Possible competitors: " + service.getPossibleCompetitors(companyName));
		m.addAttribute("newsSentimentAnalisys",("News sentiment: " + service.getSentimentAnalisysOfNews(companyName)).replace("\n", "<br />"));
		m.addAttribute("category","Categories: " + service.getProductCategories("iPhone iPad MacBook iOS iPod"));
				
		return "process";
	}

	@RequestMapping(value="/getSentiment", method = RequestMethod.POST)
	public String loadTextSentiment(@RequestParam("Text") String companyName, Model m) {
		List<Status> posts = twitterCrawler.crawlPosts(companyName);
		Double avgSentimentValue = twitterAnalyzer.getAverageSentimetForTweets(posts, languageService);
		
		
		//IAlchemyLanguage languageService = fac.createAlchemyLanguage("593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f");
		//DocumentSentiment sentiment = languageService.getSentimentForText(socialMediaPost);
		m.addAttribute("textSentiment", avgSentimentValue.toString());

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
	
	@RequestMapping(value="/")
	public String loadHomePage(Model m) {
			
		return "home";
	}
	
}
