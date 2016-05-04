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
            //ResultSetFormatter.out(System.out, rs, query);
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
		
		for(String s : resourceList){
			
			if(resourceIsCompany(s)){
				String name = getResourceName(s);
				if(name != null)
					resultList.add(name);
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
			}
		}
		
		return resultList;
	}
}
