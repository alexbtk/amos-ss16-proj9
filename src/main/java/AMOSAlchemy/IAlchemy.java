package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemy {
	
	public String getCompanyMainIndustry(String companyName) throws BadRequestException;
	public String getCompanyMainProduct(String companyName) throws BadRequestException;
	public String getPossibleCompetitors(String companyName) throws BadRequestException;
	public String getSentimentAnalisysOfNews(String companyName)throws BadRequestException;
	public String getPossibleProducts(String companyName) throws BadRequestException;
}
