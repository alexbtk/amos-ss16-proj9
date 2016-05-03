package AMOSAlchemy;

import java.util.ArrayList;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Documents;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemyNews {
	public  ArrayList getPossibibleCompetitorsList(String companyName) throws BadRequestException;
	public  Documents getSentimentAnalisysOfNews(String companyName) throws BadRequestException;
}
