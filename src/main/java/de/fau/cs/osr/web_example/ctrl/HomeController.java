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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import AMOSAlchemy.IAlchemy;
import AMOSAlchemy.IAlchemyFactory;

@Controller
public class HomeController {
	
	@RequestMapping(value="/process")
	public String loadProcessPage(@RequestParam("companyName") String companyName, Model m) {
		
		IAlchemyFactory fac = IAlchemyFactory.newInstance();
		IAlchemy service = fac.createAlchemy("593ca91c29ecc4b14b7c4fa5f9f36164ac4abe6f");
		
		m.addAttribute("mainIndustry","Main industry: " + service.getCompanyMainIndustry(companyName));
		m.addAttribute("mainProduct","Main product: " + service.getCompanyMainProduct(companyName));
		m.addAttribute("companyName",companyName);
		m.addAttribute("possibleCompetitors","Possible competitors: " + service.getPossibleCompetitors(companyName));
		m.addAttribute("newsSentimentAnalisys",("News sentiment: " + service.getSentimentAnalisysOfNews(companyName)).replace("\n", "<br />"));
				
		return "process";
	}
	
	@RequestMapping(value="/")
	public String loadHomePage(Model m) {
			
		return "home";
	}
	
}
