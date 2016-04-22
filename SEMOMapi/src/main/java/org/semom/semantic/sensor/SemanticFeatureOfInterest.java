package org.semom.semantic.sensor;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SemanticFeatureOfInterest {
	private String featureOfInterest;
	private OWLOntology original_ontology;
	private OWLOntology new_ontology;
	private OWLOntologyManager manager;
	private OWLIndividual foi;
	
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
	public SemanticFeatureOfInterest(String foi, OWLOntology original_ontology, OWLOntology new_ontology,
			OWLOntologyManager manager, IRI documentIRI){
		this.featureOfInterest= foi;
		this.original_ontology = original_ontology;
		this.new_ontology = new_ontology;
		this.manager = manager;
		this.foi = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+foi));
	}
	public void createClassAssertion(){		
		OWLClassAssertionAxiom axiom = null;
		for (OWLClass c : original_ontology.getClassesInSignature()) 
			if(c.toString().contains("ssn:FeatureOfInterest")){
				axiom = df.getOWLClassAssertionAxiom(c , foi);
				manager.addAxiom(new_ontology,axiom);	
			}
	}
	public void createObjectProperty(OWLIndividual semSensor){
		OWLObjectPropertyAssertionAxiom axiom = null;
		for (OWLObjectProperty obj : original_ontology.getObjectPropertiesInSignature())
			if (obj.toString().contains("ssn:featureOfInterest")){
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,semSensor, foi);
				manager.addAxiom(new_ontology,axiom);		
			}
	}
	
	public OWLIndividual getFoi(){
		return foi;
	}

}
