/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSDBPedia;

import java.util.List;

public class Demo {
	
	public static void main(String[] args){
		List<String> res = DBpedia.getCompanies("Apple");
		for(String s : res)
			System.out.println(s);
		
		System.out.println();
		System.out.println(DBpedia.getResourceByLabel(res.get(0)));
		System.out.println(DBpedia.getCompanyAbstract(res.get(0)));
		System.out.println(DBpedia.getCompanyHomepage(res.get(0)));

		System.out.println(DBpedia.getCompanyIndustriesResources(res.get(0)));
		System.out.println();
		List<String> products = DBpedia.getCompanyProducts(res.get(0));
		for(String s : products)
			System.out.println(s);
		System.out.println();
		System.out.println(DBpedia.resourceIsProduct("<http://dbpedia.org/resource/IPhone>"));
		System.out.println(DBpedia.getCompanyCompetitorsName("Microsoft"));
		System.out.println(DBpedia.getProductCompetitorsName("IPhone","<http://dbpedia.org/resource/Apple_Inc.>"));
	
		System.out.println(DBpedia.getCompanyLocationCoordonates("Apple Inc."));
		
	}
	
}
