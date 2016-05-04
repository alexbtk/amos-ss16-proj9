package AMOSDBPedia;

import java.util.List;

public class Demo {
	
	public static void main(String[] args){
		List<String> res = DBpedia.getCompanies("Puma");
		
		for(String s : res)
			System.out.println(s);
		
	}
	
}
