package AMOSAlchemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Concept;
import com.ibm.watson.developer_cloud.concept_insights.v2.ConceptInsights;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Annotation;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Annotations;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.ConceptMetadata;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Graph;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.RequestedFields;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.ScoredConcept;
import com.ibm.watson.developer_cloud.service.BadRequestException;
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Concepts;
/**
 * Class to use ConceptInsights
 *
 */
public class AlchemyConceptsImpl {
	
	ConceptInsights service;
	
	AlchemyConceptsImpl(){
		String username = "c8adbf89-3f4d-400a-a929-11a3e2c1eb7a";
		String password = "FhyZEpyQUXXe";
		service = new ConceptInsights();
		service.setUsernameAndPassword(username,password);
	}
	
	/**
	 * Get product abstract from concept
	 * 
	 * @param product - product name
	 * @return Abstract
	 */
	public String getAbstract(String product){
		Annotations annotations = service.annotateText(Graph.WIKIPEDIA, product);
		for(ScoredConcept a : annotations.getAnnotations()){
			
			Concept concept = new Concept(Graph.WIKIPEDIA, a.getConcept().getLabel());
			try{
				ConceptMetadata response = service.getConcept(concept);
				return response.getAbstract();
			}
			catch(Exception e){//can not catch the exception for Samsung Galaxy text
				System.out.println("Could not find abstract...");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Get related products using GraphRelatedConcepts
	 * 
	 * @param product - product name
	 * @return products that have the same concept
	 */
	public String getRelatedProducs(String product){
		// Just a possibility of getting Related content
		// ToDo, use abstract instead of name of product
		String category = "";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(ConceptInsights.LIMIT, 2);
		params.put(ConceptInsights.LEVEL, 1);
		params.put(ConceptInsights.CONCEPT_FIELDS, "{}");
		final RequestedFields fs = new RequestedFields();
		fs.include("abstract");
		params.put("concept_fields", fs);

		Concept watson = new Concept(Graph.WIKIPEDIA, product);
		Concept business = new Concept(Graph.WIKIPEDIA, "Smartphone");
		final List<Concept> concepts = new ArrayList<Concept>();
		concepts.add(watson);
		//concepts.add(business);

		Concepts related_concepts = service.getGraphRelatedConcepts(Graph.WIKIPEDIA, concepts, params);
		System.out.println(related_concepts);
	    
		return category;
	}
	

}