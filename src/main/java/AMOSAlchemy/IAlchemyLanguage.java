/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSAlchemy;

import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Taxonomies;
import com.ibm.watson.developer_cloud.service.BadRequestException;

public interface IAlchemyLanguage {
	public Taxonomies getCompanyTaxonomies(String company, String companyUrl) throws BadRequestException;
	public Entities getCompanyEntities(String company, String companyUrl) throws BadRequestException;
	public double getSentimentForText(String text) throws BadRequestException;
	public String getKeyword(String text) throws BadRequestException;
	public String getRelationObject(String text) throws BadRequestException;
	
}
