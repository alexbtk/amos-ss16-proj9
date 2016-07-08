/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

import java.util.List;
import java.util.Map;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemy {

	public String getProductCategories(String products) throws BadRequestException;
	public String getCompanyMainIndustry(String companyName) throws BadRequestException;
	public String getCompanyMainProduct(String companyName) throws BadRequestException;
	public String getPossibleCompetitors(String companyName) throws BadRequestException;
	public String getSentimentAnalysisOfNews(String companyName, String entity, String startTime, String endTime, int limit)throws BadRequestException;
	public List<String> getPossibleProducts(String companyName) throws BadRequestException;
	public String getCompetitorsProducts(String companyName)throws BadRequestException;
	public boolean sameCategory(String pr1, String pr2);
	public Map<String, String> getCompetitorsProductSentiment(String name,String companyResource);
	public double getNumberSentimentAnalysisOfNews(String name, String entity, String startTime, String endTime, int limit)throws BadRequestException;
	public void setAlchemyConceptsImpl(String username, String password);
	public double getAvgNewsSentiment(String companyName, String entity, String startTime, String endTime, int limit)throws BadRequestException;
	public List<String> getAvgNewsSentimentPeriod(String[] entityName, String entity, int weeks, int limit)throws BadRequestException;

}
