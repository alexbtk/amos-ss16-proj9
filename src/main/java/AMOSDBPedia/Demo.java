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
		System.out.println(DBpedia.getCompanyCompetitors("Microsoft"));
		System.out.println(DBpedia.getRelatedProduct("http://dbpedia.org/resource/IPhone"));
	}
	
}
