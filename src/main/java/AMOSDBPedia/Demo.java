/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSDBPedia;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import AMOSCache.AMOSCache;

public class Demo {
	
	public static void main(String[] args){
		
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get("cacheFile.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String dump = new String(encoded, Charset.forName("UTF-8"));
		AMOSCache.getInstance().setCache(dump);
		
		Long[] times = new Long[2];
		
		for(int i = 0; i < 2; i++){
			long startTime = System.currentTimeMillis();
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
			System.out.println(DBpedia.getCompetitorsFromProducts("Apple Inc."));
			times[i] = System.currentTimeMillis()-startTime;
		}
		
		System.out.println();
		System.out.println();
		
		for(int k = 0; k < times.length; k++){
			System.out.println("#" + (k+1) + " round: " + times[k] + "ms");
		}
		
		System.out.println();
		System.out.println();
		
		System.out.println(AMOSCache.getInstance().toString());
		
	}
	
}
