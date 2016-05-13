package AMOSAlchemy;

import java.util.ArrayList;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemy {

	public String getProductCategories(String products) throws BadRequestException;
	public String getCompanyMainIndustry(String companyName) throws BadRequestException;
	public String getCompanyMainProduct(String companyName) throws BadRequestException;
	public String getPossibleCompetitors(String companyName) throws BadRequestException;
	public String getSentimentAnalisysOfNews(String companyName)throws BadRequestException;
	public ArrayList<String> getPossibleProducts(String companyName) throws BadRequestException;
	public String getCompetitorsProducts(String companyName)throws BadRequestException;
	public boolean sameCategory(String pr1, String pr2);
}
