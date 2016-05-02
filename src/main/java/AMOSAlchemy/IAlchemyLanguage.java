package AMOSAlchemy;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemyLanguage {
	public Taxonomies getCompanyTaxonomies(String company, String companyUrl) throws BadRequestException;
	public Entities getCompanyEntities(String company, String companyUrl) throws BadRequestException;
}
