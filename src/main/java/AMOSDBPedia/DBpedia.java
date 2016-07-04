/*!
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-05-25
*/
package AMOSDBPedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
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
								"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
								"SELECT * " +
								"WHERE { " 
								+ " { " +
								    resource + " prop:name ?n . " + " } UNION { " +
								    resource + " rdfs:label  ?n . " + " } " +
								    " FILTER(LANG(?n) = 'en')" +
								" } ";
		
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
            	if(res.trim() != "Inc.")
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
	
	public static String getResourceLocationCountry(String resource){

		resource = resource.replace("[", "");
		resource = resource.replace("]", "");
		resource = "<" + resource + ">";

		String queryString = 	"PREFIX prop: <http://dbpedia.org/property/> " +
				"SELECT * " +
				"WHERE { " +
				resource + " prop:locationCountry ?n . " +
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
	
	public static String getCompanyLocationCountry(String name){

		List<String> resources = getResourceByName(name);
		String companyLocationCountry = null;

		//Try resources until company is found
		for(String r : resources){
			if(resourceIsCompany(r)){
				companyLocationCountry = getResourceLocationCountry(r);
				break;
			}
		}

		return companyLocationCountry;
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
	
	/**
	 * Run queries and return ?n variable
	 * @param queryString - String that is a query
	 * @return list of resources or labels
	 */
	public static List<String> runQuery(String queryString){
		// TOdo: generalize from ?n to multiple variables
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
	
	
	/**
	 * Get resources based on simple query: ?concept1 ?property ?concept2
	 * 
	 * @param resource - concept1
	 * @param property - property of the concept1 that are concept2
	 * @param propertyResult - concept2
	 * @return	list of resources
	 */
	public static List<String> getResourcesQuery(String resource, String propriety, String propertyResult){
		// help function
		// get the resources based on a property(not null)
		// resource,result - <http://dbpedia.org/resource/someresource> or null
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
	

	/**
	 * Get company's industries
	 * 
	 * @param name - company name
	 * @return	list of resources
	 */
	public static List<String> getCompanyIndustriesResources(String name){
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
	
	/**
	 * Get names and resources of companies industries
	 * 
	 * @param companyName 
	 * @return return a hashmap with a entry <name,resurce>
	 */
	public static HashMap<String,String> getCompanyIndustriesNames(String companyName){
		List<String> list = getCompanyIndustriesResources(companyName);
		HashMap<String,String> map = new HashMap<String,String>();
		for(String l : list){
			map.put(getResourceName(l), l);
		}
		return map;
	}
	
	/**
	 * Get companies from a industry
	 * 
	 * @param resource - industry resource
	 * @return return a hashmap with a entry <companyName,resurce>
	 */
	public static HashMap<String,String> getIndustryCompaniesNames(String resource){
		List<String> list  = getResourcesQuery(null,"dbpedia-owl:industry",resource);
		HashMap<String,String> map = new HashMap<String,String>();
		for(String l : list){
			map.put(getResourceName(l), l);
		}
		return map;
	}
	
	/**
	 * Get company competitors using the types and categories
	 * 
	 * @param name - company name
	 * @return	list of resources
	 */
	public static List<String> getCompanyCompetitorsResources(String name){
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
	
	/**
	 * Get competitors name
	 * 
	 * @param company
	 * @return Return competitors Names
	 */
	public static List getCompanyCompetitorsName(String company){
		List<String> competitorsResources = getCompanyCompetitorsResources(company);
		List<String> competitorsNames = new ArrayList();
		for(String resource : competitorsResources){
			String name = getResourceName(resource);
			if(name != null)
				competitorsNames.add(name);
		}
		return competitorsNames;
	}
	
	/**
	 * Check if resource is a product
	 * 
	 * @param resource - string resource <http://linktoresource>
	 * @return	true if resource is a product property of another resource
	 */
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
	
	/**
	 * Get product categories
	 * 
	 * @param product name
	 * @return	list of resources
	 */
	public static List<String> getProductCategoryResources(String name){
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
	
	/**
	 * Get product types
	 * 
	 * @param name - product name
	 * @return	list of resources
	 */
	public static List<String> getProductTypesResources(String name){
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
	
	/**
	 * Check if the two types are from same category or refer to same idea of type 
	 * 
	 * @param type1 - name of the first type
	 * @param type2 - name of the second type
	 * @return	true if the type are the same
	 */
	public static boolean equalType(String type1, String type2){
		// ToDo: find a way to determine witch types are good as criteria.
		return type1.equals(type2);
	}
	
	/**
	 * Check if the two product are from same category 
	 * 
	 * @param prod1 - name of the first product
	 * @param prod2 - name of the second product
	 * @return	true if the products are from same category
	 */
	public static boolean sameCategory(String prod1, String prod2, int epsilon){
		// Check types
		if(prod1 == prod2)
			return false;
		List<String> types1 = getProductTypesResources(prod1);
		List<String> types2 = getProductTypesResources(prod2);
		int sameTypes = 0;
		if(types1 != null && types2 != null)
			for(String type1: types1)
				for(String type2: types2)
					if(equalType(type1,type2)){
						++sameTypes;
					}
		// Check categories
		List<String> cats1 = getProductCategoryResources(prod1);
		List<String> cats2 = getProductCategoryResources(prod2);
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
	
	/**
	 * Check if the two product are from same company 
	 * 
	 * @param prod1 - name of the first product
	 * @param prod2 - name of the second product
	 * @return	true if the products are from same company
	 */
	public static boolean isSameCompanyProducts(String prod1, String prod2, String companyResource){
		//Todo: need also the correct company
		String queryString = "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
				"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " + 
				"SELECT ?n " +
				"WHERE { " +
				  "?n a dbpedia-owl:Company."+
				  "?n  dbpedia-owl:product "+prod1+"."+
				  "?n  dbpedia-owl:product "+prod2+"."+
				  "FILTER(?n = "+companyResource+" )"+
				 // write others filters or product type
				"} LIMIT 1";
		
		return !(runQuery(queryString).isEmpty());
	}
	
	/**
	 * Get product that are in the same category with current product
	 * 
	 * @param name - name of the product
	 * @param companyResource
	 * @return	map of resources and categories that are related to product
	 */
	public static Map<String, String> getRelatedProductResources(String name, String companyResource){
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
		
		Map<String, String> relatedproduct = new HashMap<String, String>();
		//Try resources until product is found
		for(String resource : resources){
			if(resourceIsProduct("<"+resource+">")){
				List<String> productCategories = getProductCategoryResources(resource);
				for(String cat : productCategories){
					if(true){//if cat has nothing to do with company
						// get products in the same category
						List<String> possibleProducts = getResourcesQuery(null,"<http://purl.org/dc/terms/subject>","<" + cat + ">");
						//Sometimes get an error, need more time between queries
						for(String s: possibleProducts){
							s = s.replace("[", "");
							s = s.replace("]", "");
							if(resourceIsProduct("<"+s+">"))
								if(sameCategory(s,resource,2) && !isSameCompanyProducts("<"+s+">","<"+resource+">",companyResource))
									relatedproduct.put(s,cat);
						}
					}
				}
				break;
			}
		}
		return relatedproduct;
	}
	
	
	/**
	 * Get competitors name
	 * 
	 * @param product - product name
	 * @param companyResource 
	 * @return Return competitors Names
	 */
	public static List getProductCompetitorsName(String name,String companyResource){
		
		List<String> resources = getResourceByName(name);
		if(resources == null){
			resources = getResourceByLabel(name);
		}else{
			resources.addAll(getResourceByLabel(name));
		}
		for(String resource : resources){
			if(resourceIsProduct('<'+resource+'>')){
				Map<String, String> competitorsResources = getRelatedProductResources(resource,companyResource);
				List<String> competitorsNames = new ArrayList();
				for(Map.Entry<String, String> entry : competitorsResources.entrySet()){
					String competitor = getResourceName(entry.getKey());
					if(name != null)
						competitorsNames.add(competitor);
				}
				return competitorsNames;
			}		
		}
		return null;
	}
	
	/**
	 * Get coordonates of a company
	 * 
	 * @param companyName - the name os a company wirtten as in DBpedia references
	 * 
	 * @return A map with key - location name, and with value lat-long
	 */
	public static Map getCompanyLocationCoordonates(String companyName){
		List<String> resources = getResourceByName(companyName);
		if(resources == null){
			resources = getResourceByLabel(companyName);
		}else{
			resources.addAll(getResourceByLabel(companyName));
		}
		if(resources.size() == 0)
			return null;
		System.out.println(resources.get(0));
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		String queryString =  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		"PREFIX dbo: <http://dbpedia.org/ontology/> "+
		"PREFIX prop: <http://dbpedia.org/property/> "+
		"PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> "+
		"SELECT  ?lat ?long ?name " +
        "WHERE { { <"+resources.get(0)+"> prop:location ?city .} "+
		"UNION { <"+resources.get(0)+"> dbo:locationCity ?city . } "+
		//" ?city  <http://www.georss.org/georss/point> ?point. "+
        " ?city rdfs:label ?name. "+
		"?city geo:long ?long."+
		" ?city geo:lat ?lat. "+
		"FILTER(lang(?name) = 'en')"+
       "}";
		
		
		Query query = QueryFactory.create(queryString);
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	
            	QuerySolution qs = rs.next();
            	//check for uniqueness
            	map.put(qs.getLiteral("name").getString(), qs.getLiteral("lat").getString()+", "+qs.getLiteral("long").getString());
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return map;
	}
	
	/**
	 * Get 5 Competitors using product categories and employees number
	 * 
	 * @param companyName - company name
	 * @return list of competitors name
	 */
	public static List<String> getCompetitorsFromProducts(String companyName){
		List<String> resources = getResourceByName(companyName);
		if(resources == null){
			resources = getResourceByLabel(companyName);
		}else{
			resources.addAll(getResourceByLabel(companyName));
		}
		if(resources.size() == 0)
			return null;
		System.out.println(resources.get(0));
		String resource = "<" + resources.get(0) + ">";
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
		
		String queryString =  " PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "+
  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
  "SELECT DISTINCT ?company "+//, COUNT(DISTINCT ?cat1) AS ?nrcatPR, COUNT(DISTINCT ?cat11) AS ?nrcatSE, COUNT(DISTINCT ?cat12) AS ?nrcatDEV "+ 
  "WHERE { "+
	  resource +"  dbpedia-owl:industry ?industry. "+
	  "?company  dbpedia-owl:industry ?industry ; rdf:type <http://dbpedia.org/ontology/Company>. "+    
	
	  "OPTIONAL{ "+
		  "?company dbpedia-owl:numberOfEmployees ?numEmC. "+
		  resource +" dbpedia-owl:numberOfEmployees ?numEmMC. "+
		  "FILTER( "+
			  "(?numEmMC > 100000 && ?numEmC > 100000) || "+
			  "(?numEmMC < 100000 && ?numEmMC > 50000 && ?numEmC > 50000 && ?numEmC < 100000) || "+
			  "(?numEmMC < 50000 && ?numEmC < 50000) "+
		  ") "+
	  "} "+
	          
	  "OPTIONAL{ "+
		  "?company dbpedia-owl:product ?prod1. "+
		  resource + "dbpedia-owl:product ?prod2. "+
		  "?prod1 <http://purl.org/dc/terms/subject> ?cat1. "+
		  "?prod2 <http://purl.org/dc/terms/subject> ?cat1. "+
	  "} "+
	      
	  "OPTIONAL{ "+
		  "?company dbpedia-owl:service ?prod11. "+
		  resource+" dbpedia-owl:service ?prod21. "+
		  "?prod11 <http://purl.org/dc/terms/subject> ?cat11. "+
		  "?prod21 <http://purl.org/dc/terms/subject> ?cat11. "+
	  "} "+
	  "OPTIONAL{ "+
		  "?prod12 dbpedia-owl:developer  ?company. "+
		  "?prod22 dbpedia-owl:developer  "+resource+". "+
		  "?prod12 <http://purl.org/dc/terms/subject> ?cat12. "+
		  "?prod22 <http://purl.org/dc/terms/subject> ?cat12. "+
	  "}"+
	
	  "FILTER NOT EXISTS{  ?owner dbpedia-owl:service ?company. FILTER(?owner = "+resource+" ) } "+
	  "FILTER NOT EXISTS{  ?parentC dbpedia-owl:parentCompany ?company. FILTER(?parentC = "+resource+")} "+
	  "FILTER NOT EXISTS{  ?parentCC dbpedia-owl:parentCompany "+resource+". FILTER(?parentCC = ?company) } "+
	
	   
	  "FILTER (?company != "+resource+
	  		" && ?prod1!=?prod2 "+
	  ") "+
  "} ORDER BY DESC(?numEmC) DESC(?nrcatDEV) DESC(?nrcatPR) DESC(?nrcatSE) LIMIT 5";
		
		Query query = QueryFactory.create(queryString,Syntax.syntaxARQ);
		List<String> competitors = new ArrayList<String>();
		
		try ( QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query) ) {
			// Set the DBpedia specific timeout.
            ((QueryEngineHTTP)qexec).addParam("timeout", "10000") ;

            // Execute.
            ResultSet rs = qexec.execSelect();
            while(rs.hasNext()){
            	QuerySolution qs = rs.next();
            	String res = qs.getResource("company").toString();            	
	            competitors.add(getResourceName(res));            	
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return competitors;
	}
	
	public static void main(String args[]){
		List<String> resources = getResourceByName("Foxconn");

		//Try resources until company is found
		for(String r : resources){
			System.out.println(r);
			if(resourceIsCompany(r)){
				System.out.println("Is company!");
			}
		}
		System.out.println(getCompanyLocationCountry("Foxconn"));
	}
}


