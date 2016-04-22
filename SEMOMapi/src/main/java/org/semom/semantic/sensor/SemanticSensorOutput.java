package org.semom.semantic.sensor;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SemanticSensorOutput {
	private OWLOntology original_ontology;
	private OWLOntology new_ontology;
	private OWLOntologyManager manager;
	private int value;
	private String classification;
	private Random rand = new Random();
	private OWLIndividual sensorOutput;
	private Set<OWLAxiom> axioms;
	
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
	public SemanticSensorOutput(int value, String classification, OWLOntology original_ontology,
			OWLOntology new_ontology, OWLOntologyManager manager, IRI documentIRI) {
		this.original_ontology = original_ontology;
		this.new_ontology = new_ontology;
		this.manager = manager;
		this.value = value;
		this.classification = classification;
		this.sensorOutput = df.getOWLNamedIndividual(IRI.create(documentIRI + ";sensorOutput"+rand.nextInt(50)));	
	
		for (OWLClass c : original_ontology.getClassesInSignature()) 
			if(c.toString().contains("ssn:SensorOutput")){
				manager.addAxiom(new_ontology,df.getOWLClassAssertionAxiom(c , sensorOutput));
				//axioms.add(df.getOWLClassAssertionAxiom(c , sensorOutput));
	//			System.out.println("axiom1: " + df.getOWLClassAssertionAxiom(c , sensorOutput));
		//		OWLAxiom ax = df.getOWLClassAssertionAxiom(c , sensorOutput);
				//System.out.println(ax);
		//		axioms.add(ax);
			}
		for (OWLDataProperty dataProp : original_ontology.getDataPropertiesInSignature()){
			if(dataProp.toString().contains("DUL:hasDataValue")){
				manager.addAxiom(new_ontology,df.getOWLDataPropertyAssertionAxiom(dataProp, sensorOutput, value));
		//		axioms.add(df.getOWLDataPropertyAssertionAxiom(dataProp, sensorOutput, value));
			}
			if(dataProp.toString().contains("DUL:isClassifiedBy")){
				System.out.println("data: "+dataProp+"sensorOutput: " +sensorOutput + "classification: " +classification);
				manager.addAxiom(new_ontology,df.getOWLDataPropertyAssertionAxiom(dataProp, sensorOutput, classification));
			//	axioms.add(df.getOWLDataPropertyAssertionAxiom(dataProp, sensorOutput, this.classification));
			}
		}
	}
	public OWLIndividual getSensorOutputInd(){
		return this.sensorOutput;
	}
	public Set<OWLAxiom> getSensorOutput(){
		return axioms;
	}
	

}
