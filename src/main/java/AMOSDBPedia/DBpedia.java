package AMOSDBPedia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBpedia {
	
	public static List<String> getResourceByLabel(String label){
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
							 "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
							 "SELECT $n " +
							 "WHERE { ?n rdfs:label '" + label + "'@en .	}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            List<String> result = new ArrayList<String>();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getResource("n").getURI();
            	if(res.contains("dbpedia.org/resource") && !res.contains("Category:"))
            		result.add(res);
            }
            
            return result;
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public static String getResourceLabel(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
								"SELECT * " +
								"WHERE { " +
								    resource + " rdfs:label ?n . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getLiteral("n").getString();
            	if(res.endsWith("@en")){
	            	res = res.replace("\"", "");
	            	res = res.replace("@en", "");
	                return res;
            	}
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public static List<String> getResourceByName(String name){

		String queryString = 	"PREFIX prop: <http://dbpedia.org/property/> " +
				"SELECT * " +
				"WHERE { " + 
					"?n prop:name \"" + name + "\"@en . " +
				"}";

		Query query = QueryFactory.create(queryString);

		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
			((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

			// Execute.
			ResultSet rs = qexec.execSelect();
			List<String> result = new ArrayList<String>();
			while(rs.hasNext()){
				QuerySolution qs = rs.next();
				String res = qs.getResource("n").toString();
				result.add(res);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getResourceName(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX prop: <http://dbpedia.org/property/> " +
								"SELECT * " +
								"WHERE { " +
								    resource + " prop:name ?n . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            if(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getLiteral("n").getString();
            	res = res.replace("\"", "");
            	res = res.replace("@en", "");
                return res;
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public static boolean resourceHasDisambiguations(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
								"ASK " +
								"WHERE { " +
								   resource + " dbpedia-owl:wikiPageDisambiguates $n . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            boolean result = qexec.execAsk();
            
            return result;
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return false;
	}
	
	public static boolean resourceIsCompany(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
								"ASK " +
								"WHERE { " +
								   resource + " a dbpedia-owl:Company . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            boolean result = qexec.execAsk();
            
            return result;
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return false;
	}
	
	public static List<String> getCompanies(String label){
		
		List<String> resourceList = getResourceByLabel(label);
		List<String> resultList = new ArrayList<String>();
		
		String[] companyEndings = {"Inc.", "Co.", "inc.", "co.", "Corporation", "Company"};
		
		for(String s : resourceList){
			
			if(resourceIsCompany(s)){
				String name = getResourceName(s);
				if(name != null){
					resultList.add(name);
				}
			}
			else if(resourceHasDisambiguations(s)){
				String resource = s;
				resource = resource.replace("[", "");
				resource = resource.replace("]", "");
				resource = "<" + resource + ">";
				String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
										"SELECT * " +
										"WHERE { " +
										   resource + " dbpedia-owl:wikiPageDisambiguates $n . " +
										   "?n a dbpedia-owl:Company " +
										"}";
				
				Query query = QueryFactory.create(queryString);
				
				try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
					// Set the DBpedia specific timeout.
		            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

		            // Execute.
		            ResultSet rs = qexec.execSelect();
		            while(rs.hasNext()){
		            	QuerySolution qs = rs.next();
		            	String res = qs.getResource("n").getURI();
		            	if(res.contains("dbpedia.org/resource") && !res.contains("Category:")){
		            		String name = getResourceName(res);
		            		if(name != null)
		            			resultList.add(name);
		            	}
		            }
				}
			}else if(!label.endsWith(companyEndings[0]) && 
					!label.endsWith(companyEndings[1]) && 
					!label.endsWith(companyEndings[2]) && 
					!label.endsWith(companyEndings[3]) && 
					!label.endsWith(companyEndings[4]) && 
					!label.endsWith(companyEndings[5])
					){
				
				for(String ending : companyEndings){
					List<String> comps = getCompanies(label + " " + ending); //Recursive - depth of recursion is always only 1
					if(comps.size() > 0)
						resultList.addAll(comps);
				}
				
			}
		}
		
		return resultList;
	}
	
	public static String getResourceAbstract(String resource){
		
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
								"SELECT * " +
								"WHERE { " +
								    resource + " dbpedia-owl:abstract ?n . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getLiteral("n").toString();
            	if(res.endsWith("@en")){
            		res = res.replace("@en", "");
	                return res;
            	}
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	public static String getCompanyAbstract(String name){
		
		List<String> resources = getResourceByName(name);
		String companyAbstract = null;
		
		//Try resources until company is found
		for(String r : resources){
			if(resourceIsCompany(r)){
				companyAbstract = getResourceAbstract(r);
				break;
			}
		}
		
		return companyAbstract;
	}
	
	public static String getResourceHomepage(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		String queryString = 	"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				"SELECT * " +
				"WHERE { " +
				resource + " foaf:homepage ?n . " +
				"}";

		Query query = QueryFactory.create(queryString);

		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
			((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

			// Execute.
			ResultSet rs = qexec.execSelect();
			if(rs.hasNext()){
				QuerySolution qs = rs.next();
				String res = qs.getResource("n").toString();
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getCompanyHomepage(String name){

		List<String> resources = getResourceByName(name);
		String companyHomepage = null;

		//Try resources until company is found
		for(String r : resources){
			if(resourceIsCompany(r)){
				companyHomepage = getResourceHomepage(r);
				break;
			}
		}

		return companyHomepage;
	}
	
	public static List<String> getResourceProducts(String resource){
		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";
		
		List<String> resultList = new ArrayList<String>();
		
		String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
								"SELECT * " +
								"WHERE { " +
								    resource + " dbpedia-owl:product ?n . " +
								"}";
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getResource("n").toString();
            	String name = getResourceName(res);
            	if(name != null)
            		resultList.add(name);
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return resultList;
	}
	
	public static List<String> getCompanyProducts(String name){

		List<String> resources = getResourceByName(name);
		List<String> companyProducts = null;

		//Try resources until company is found
		for(String r : resources){
			if(resourceIsCompany(r)){
				companyProducts = getResourceProducts(r);
				break;
			}
		}

		return companyProducts;
	}
	
	// Help function
	public static List<String> runQuery(String queryString){
		List<String> resultList = new ArrayList<String>();
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getResource("n").toString();
            	if(res != null)
            		resultList.add(res);
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		return resultList;
	}
	
	//help function
	//get the resources based on a property(not null)
	//resource,result - <http://dbpedia.org/resource/someresource> or null
	public static List<String> getResourcesQuery(String resource, String propriety, String propertyResult){

		String prefixes = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+        
		"PREFIX type: <http://dbpedia.org/class/yago/>"+
		"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		"PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
		"PREFIX : <http://dbpedia.org/resource/>" +
		"PREFIX dbpedia2: <http://dbpedia.org/property/>" +
		"PREFIX dbpedia: <http://dbpedia.org/>" +
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
		"PREFIX prop: <http://dbpedia.org/property/>";
		String queryString = "";
		String queryLimit = "10";
		// get all properties
		if(propriety == null){
			queryString = "SELECT ?n " +
					"WHERE { " +
				    resource +" ?n ?d . " +
				"}";
		}else if(resource!=null){
			//get the property of the resource
			queryString = "SELECT ?n " +
					"WHERE { " +
				    resource + " "+ propriety + " ?n . " +
				"} LIMIT "+queryLimit;
		}else{
			// get the resources that have that property
			queryString = "SELECT ?n " +
					"WHERE { " +
					"?n "+ propriety + " " + propertyResult + 
				"} LIMIT "+queryLimit;
		}
	
		return runQuery(prefixes+queryString);
	}
	

	
	public static List<String> getCompanyIndustrysResources(String name){
		// Correct name is needed
		List<String> resources = getResourceByName(name);
		if(resources == null){
			resources = getResourceByLabel(name);
		}else{
			resources.addAll(getResourceByLabel(name));
		}
		List<String> companyIndustries = null;

		//Try resources until company is found
		for(String resource : resources){
			if(resourceIsCompany(resource)){
				resource = resource.replace("[", "");
				resource = resource.replace("]", "");
				resource = "<" + resource + ">";
				companyIndustries = getResourcesQuery(resource,"dbpedia-owl:industry",null);
				break;
			}
		}

		return companyIndustries;
	}
	
	public static List<String> getCompanyCompetitors(String name){
		// Correct name is needed
		List<String> resources = getResourceByName(name);
		if(resources == null){
			resources = getResourceByLabel(name);
		}else{
			resources.addAll(getResourceByLabel(name));
		}
		List<String> companyIndustries = null;
		List<String> companycompetitors = new ArrayList<String>();
		String companyResource = "";
		//Try resources until company is found
		for(String resource : resources){
			if(resourceIsCompany(resource)){
				resource = resource.replace("[", "");
				resource = resource.replace("]", "");
				companyResource = resource;
				resource = "<" + resource + ">";
				companyIndustries = getResourcesQuery(resource,"dbpedia-owl:industry",null);
				//go throw all industries
				for(String industry : companyIndustries){
					industry = industry.replace("[", "");
					industry = industry.replace("]", "");
					industry = "<" + industry + ">";
					companycompetitors.addAll(getResourcesQuery(null,"dbpedia-owl:industry",industry));
				}
				break;
			}
		}
		// Delete dublicates
		Set<String> hs = new HashSet<>();
		hs.addAll(companycompetitors);
		companycompetitors.clear();
		companycompetitors.addAll(hs);
		// Delete the current company
		ListIterator listIterator = companycompetitors.listIterator();
		while(listIterator.hasNext()){
			if(listIterator.next().equals(companyResource))
			{
				listIterator.remove();
				break;
			}
		}
		return companycompetitors;
	}
	
	public static boolean resourceIsProduct(String resource){
		String queryString = 	"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
								"SELECT ?n " +
								"WHERE { " +
								  "?n  dbpedia-owl:product ?re."+
								"FILTER( ?re = "+resource+" )" +
								 // write others filters or product type
								"} LIMIT 1";
		return !(runQuery(queryString).isEmpty());		
	}
	
	public static List<String> getProductCategory(String name){
		List<String> resources = new ArrayList();
		if(name.startsWith("http://")){
			// we have a resource
			resources.add(name);
		}else{
			//we have a name
			// ToDo - do not work with name
			resources = getResourceByName(name);
			if(resources == null){
				resources = getResourceByLabel(name);
			}else{
				resources.addAll(getResourceByLabel(name));
			}
		}
		
		List<String> productCategories = null;

		//Try resources until product is found
		for(String resource : resources){
			resource = resource.replace("[", "");
			resource = resource.replace("]", "");
			resource = "<" + resource + ">";			
			if(resourceIsProduct(resource)){
				productCategories = getResourcesQuery(resource,"<http://purl.org/dc/terms/subject>",null);
				return productCategories;
			}
		}
		return null;
	}
	
	public static List<String> getProductTypes(String name){
		List<String> resources = new ArrayList();
		if(name.startsWith("http://")){
			// Have a resource.
			resources.add(name);
		}else{
			// Have a name.
			// ToDo - do not work with name
			resources = getResourceByName(name);
			if(resources == null){
				resources = getResourceByLabel(name);
			}else{
				resources.addAll(getResourceByLabel(name));
			}
		}
		
		List<String> productTypes = null;

		//Try resources until product is found
		for(String resource : resources){
			resource = resource.replace("[", "");
			resource = resource.replace("]", "");
			resource = "<" + resource + ">";
			if(resourceIsProduct(resource)){
				productTypes = getResourcesQuery(resource,"rdf:type",null);//also dbpedia2:wordnet_type possible
				return productTypes;
			}
		}
		return null;
	}
	
	public static boolean equalType(String type1, String type2){
		// ToDo: find a way to determine witch types are good as criteria.
		return type1.equals(type2);
	}
	
	public static boolean sameCategory(String prod1, String prod2, int epsilon){
		// Check types
		List<String> types1 = getProductTypes(prod1);
		List<String> types2 = getProductTypes(prod2);
		int sameTypes = 0;
		if(types1 != null && types2 != null)
			for(String type1: types1)
				for(String type2: types2)
					if(equalType(type1,type2)){
						++sameTypes;
					}
		// Check categories
		List<String> cats1 = getProductCategory(prod1);
		List<String> cats2 = getProductCategory(prod2);
		int sameCats = 0;
		if(cats1 != null && cats2 != null)
			for(String cat1: cats1)
				for(String cat2: cats2)
					if(equalType(cat1,cat2)){
						++sameCats;
					}
		// if we have same categories and type 
		if(sameTypes >= epsilon && sameCats > 0)
			return true;
		return false;
	}
	
	public static boolean isSameCompanyProducts(String prod1, String prod2){
		// Todo
		return false;
	}
	
	public static List<String> getRelatedProduct(String name){
		List<String> resources = new ArrayList();
		if(name.startsWith("http://")){
			// Have a resource.
			resources.add(name);
		}else{
			// Have a name.
			resources = getResourceByName(name);
			if(resources == null){
				resources = getResourceByLabel(name);
			}else{
				resources.addAll(getResourceByLabel(name));
			}
		}
		
		List<String> relatedproduct = new ArrayList();

		//Try resources until product is found
		for(String resource : resources){
			if(resourceIsProduct("<"+resource+">")){
				List<String> productCategories = getProductCategory(resource);
				for(String cat : productCategories){
					if(true){//if cat has nothing to do with company
						cat = "<" + cat + ">";
						// get products in the same category
						List<String> possibleProducts = getResourcesQuery(null,"<http://purl.org/dc/terms/subject>",cat);
						//Sometimes get an error, need more time between queries
						for(String s: possibleProducts){
							s = s.replace("[", "");
							s = s.replace("]", "");
							if(resourceIsProduct("<"+s+">"))
								if(sameCategory(s,resource,3))
									relatedproduct.add(s+"-"+cat);
						}
					}
				}
				break;
			}
		}
		return relatedproduct;
	}
	
}
