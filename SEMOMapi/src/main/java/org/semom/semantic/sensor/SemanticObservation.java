package org.semom.semantic.sensor;

import java.util.Random;

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

public class SemanticObservation {
	private OWLOntology original_ontology;
	private OWLOntology new_ontology;
	private OWLOntologyManager manager;
	private OWLIndividual observ;
	private Random rand = new Random();
	
	OWLDataFactory df = OWLManager.getOWLDataFactory();	
	public SemanticObservation(OWLOntology original_ontology, OWLOntology new_ontology,
			OWLOntologyManager manager, IRI documentIRI) {
		this.original_ontology = original_ontology;
		this.new_ontology = new_ontology;
		this.manager = manager;
		this.observ = df.getOWLNamedIndividual(IRI.create(documentIRI + ";Observation"+rand.nextInt(50)));
		createClassAssertion();
	}
	private void createClassAssertion(){		
		OWLClassAssertionAxiom axiom = null;
		for (OWLClass c : original_ontology.getClassesInSignature()) 
			if(c.toString().contains("ssn:Observation")){
				axiom = df.getOWLClassAssertionAxiom(c , observ);
				manager.addAxiom(new_ontology,axiom);	
			}
	}
	public void createObjectProperty(OWLIndividual sensor, OWLIndividual property, 
			OWLIndividual foi, OWLIndividual sensorOutput ){
		OWLObjectPropertyAssertionAxiom axiom = null;
		for (OWLObjectProperty obj : original_ontology.getObjectPropertiesInSignature()){
			if(obj.toString().contains("ssn:observedProperty"))	{
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,this.observ, property);
				manager.addAxiom(new_ontology,axiom);		
			}
			if(obj.toString().contains("ssn:observedBy"))	{
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,this.observ, sensor);
				manager.addAxiom(new_ontology,axiom);		
			}
			if(obj.toString().contains("ssn:featureOfInterest"))	{
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,this.observ, foi);
				manager.addAxiom(new_ontology,axiom);		
			}
			if(obj.toString().contains("ssn:observationResult"))	{
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,this.observ, sensorOutput);
				manager.addAxiom(new_ontology,axiom);		
			}
		//return axiom;
		}
	}
	public OWLIndividual getObservationInd(){
		return this.observ;
	}

}
