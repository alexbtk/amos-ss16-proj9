package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public class AlchemyImpl implements IAlchemy{

	private IAlchemyLanguage alchemyLanguage;
	
	public AlchemyImpl(String apiKey){
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
		
	}
	
	@Override
	public String getCompanyMainIndustry(String companyName) throws BadRequestException{
		//Assumes that company has a .com website
		String companyUrl = "http://" + companyName.toLowerCase() + ".com";
		return alchemyLanguage.getCompanyTaxonomy(companyName, companyUrl);
	}

	@Override
	public String getCompanyMainProduct(String companyName) throws BadRequestException{
		// TODO Auto-generated method stub
		return null;
	}

}
