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
import com.ibm.watson.developer_cloud.concept_insights.v2.model.Concepts;

public class AlchemyConceptsImpl {
	
	ConceptInsights service;
	AlchemyLanguageImpl alchemyLanguage;
	
	AlchemyConceptsImpl(String apiKey){
		String username = "c8adbf89-3f4d-400a-a929-11a3e2c1eb7a";
		String password = "FhyZEpyQUXXe";
		service = new ConceptInsights();
		service.setUsernameAndPassword(username,password);
		this.alchemyLanguage = new AlchemyLanguageImpl(apiKey);
	}
	
	public String getCategory(String product){
		String category = "";
		//alchemyLanguage.getCompanyRelated("Apple");
	
		Annotations annotations = service.annotateText(Graph.WIKIPEDIA, product);
		for(ScoredConcept a : annotations.getAnnotations()){
			
			Concept concept = new Concept("wikipedia", "en-latest", a.getConcept().getLabel());
			ConceptMetadata response = service.getConcept(concept);
			//System.out.println(response);
			String cat = alchemyLanguage.getCat(response.getAbstract());
			
			if(cat != null)
				category += "/" + a.getConcept().getLabel() + "-" +
						cat;
			//System.out.println(alchemyLanguage.getCat(response.getAbstract()));
		}
		
		//System.out.println(annotations);
	    
		return category;
	}
	
	
	public String getRelatedProducs(String product){
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
