package AMOSAlchemy;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

public class AlchemyToneAnalyzerImpl implements IAlchemyToneAnalyzer{
	
	private ToneAnalyzer service;
	
	AlchemyToneAnalyzerImpl(String username, String password){
		service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
		service.setUsernameAndPassword(username, password);
	}
	
	public String getToneAsString(String text){
		if(service != null){
			ToneAnalysis tone = service.getTone(text);
			return tone.toString();
		}
		else
			return null;
	}
	
	public static void main(String[] args) {
		
		String username = null, password = null;
		
		for(int i = 0; i < args.length; i++){
			if(i == 0)
				username = args[i];
			else if(i == 1)
				password = args[i];
		}
		
		if(username != null && password != null){
			IAlchemyFactory fac = IAlchemyFactory.newInstance();
			IAlchemyToneAnalyzer service = fac.createAlchemyToneAnalyzer(username, password);

			String text =
					"I know the times are difficult! Our sales have been "
							+ "disappointing for the past three quarters for our data analytics "
							+ "product suite. We have a competitive data analytics product "
							+ "suite in the industry. But we need to do our job selling it! "
							+ "We need to acknowledge and fix our sales challenges. "
							+ "We can’t blame the economy for our lack of execution! "
							+ "We are missing critical sales opportunities. "
							+ "Our product is in no way inferior to the competitor products. "
							+ "Our clients are hungry for analytical tools to improve their "
							+ "business outcomes. Economy has nothing to do with it.";

			System.out.println(service.getToneAsString(text));
		}
	  }
}
