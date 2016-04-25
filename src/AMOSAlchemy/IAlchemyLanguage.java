package AMOSAlchemy;

import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemyLanguage {
	public String getCompanyTaxonomy(String company, String companyUrl) throws BadRequestException;
}
