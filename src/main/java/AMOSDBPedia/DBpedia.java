package AMOSDBPedia;

import java.util.ArrayList;
import java.util.List;

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
}
