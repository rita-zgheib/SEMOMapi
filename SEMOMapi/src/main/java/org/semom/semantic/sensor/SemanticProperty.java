package org.semom.semantic.sensor;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SemanticProperty {
	private String property;
	private OWLOntology original_ontology;
	private OWLOntology new_ontology;
	private OWLOntologyManager manager;
	private OWLIndividual prop;
	
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
	public SemanticProperty(String property, OWLOntology original_ontology, OWLOntology new_ontology,
			OWLOntologyManager manager, IRI documentIRI){
	this.property= property;
	this.original_ontology = original_ontology;
	this.new_ontology = new_ontology;
	this.manager = manager;
	this.prop = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+property));
	}
	//public OWLClassAssertionAxiom createClassAssertion(OWLDataFactory df,IRI documentIRI )
	public void createClassAssertion(){		
		OWLClassAssertionAxiom axiom = null;
		for (OWLClass c : original_ontology.getClassesInSignature()) 
			if(c.toString().contains("ssn:Property")){
				axiom = df.getOWLClassAssertionAxiom(c , prop);
				manager.addAxiom(new_ontology,axiom);	
			}
		//return axiom;
	}
	//public OWLObjectPropertyAssertionAxiom createObjectProperty(OWLIndividual semSensor, OWLIndividual prop){
	public void createObjectProperty(OWLIndividual semSensor){
		OWLObjectPropertyAssertionAxiom axiom = null;
		for (OWLObjectProperty obj : original_ontology.getObjectPropertiesInSignature())
			if(obj.toString().contains("ssn:observedProperty"))	{
				axiom = df.getOWLObjectPropertyAssertionAxiom(obj,semSensor, prop);
				manager.addAxiom(new_ontology,axiom);		
			}
		//return axiom;
	}
	
	public OWLIndividual getProp(){
		return prop;
	}
}
