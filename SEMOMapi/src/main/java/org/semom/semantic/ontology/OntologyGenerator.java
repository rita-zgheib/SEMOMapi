package org.semom.semantic.ontology;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.PriorityCollection;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OntologyGenerator { 

	OWLOntologyManager manager;
	OWLOntology o;


//	 private static final Logger LOG = LoggerFactory.getLogger(CopyOfOntologyGenerator.class);
	 public static final IRI EXAMPLE_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticMiddleware.owl");
//	 public static final IRI EXAMPLE_SAVE_IRI  = IRI.create("file:materializedOntologies/semanticMiddleware129.owl");
	 OWLDataFactory df = OWLManager.getOWLDataFactory();

	 
	public OntologyGenerator() throws OWLOntologyStorageException, IOException, OWLOntologyCreationException {	
		//create Ontology manager		
		manager = OWLManager.createOWLOntologyManager();
		System.out.println("OWL manager created.");
		
		// map the ontology IRI to a physical IRI (files for example)
		//File output = File.createTempFile("saved_bedsore", "owl");
		File output = new File("C:/Users/rzgheib/Documents/GenericOntologySpace/GenericOntology.owl");
		IRI documentIRI = IRI.create(output);
		// Set up a mapping, which maps the ontology to the document IRI
		SimpleIRIMapper mapper = new SimpleIRIMapper(EXAMPLE_IRI, documentIRI);
		PriorityCollection<OWLOntologyIRIMapper> iriMappers = manager.getIRIMappers();
		iriMappers.add(mapper);
		// set up a mapper to read local copies of ontologies
		File localFolder = new File("materializedOntologies");
		// the manager will look up an ontology IRI by checking localFolder first for a local copy
		iriMappers.add(new AutoIRIMapper(localFolder, true));
		
		// Now create the ontology using the ontology IRI (not the physical URI)
		o = manager.createOntology(EXAMPLE_IRI);
		// save the ontology to its physical location - documentIRI
		manager.saveOntology(o);
		System.out.println("OWL ontology created.");
	 		
		//OWL classes definition
		OWLClass SemanticSensor = df.getOWLClass(IRI.create(documentIRI + ";SemanticSensor")); // a voir avec Remi ou jing
		OWLClass Sensor = df.getOWLClass(IRI.create(documentIRI + ";ssn:sensor"));
		OWLClass Observation = df.getOWLClass(IRI.create(documentIRI + ";ssn:Observation"));
		OWLClass FeatureOfInterest = df.getOWLClass(IRI.create(documentIRI + ";ssn:FeatureOfInterest"));
		OWLClass Property = df.getOWLClass(IRI.create(documentIRI + ";ssn:Property"));
		OWLClass SensorOutput = df.getOWLClass(IRI.create(documentIRI + ";ssn:SensorOutput"));
		OWLClass PatientLocation = df.getOWLClass(IRI.create(documentIRI + ";PatientLocation"));	
		
		// Axioms definition
		OWLAxiom axiom = df.getOWLSubClassOfAxiom(SemanticSensor, Observation);
		OWLAxiom axiom1 = df.getOWLSubClassOfAxiom(SemanticSensor, Sensor);
		OWLAxiom axiom2 = df.getOWLSubClassOfAxiom(SemanticSensor, FeatureOfInterest);
		OWLAxiom axiom3 = df.getOWLSubClassOfAxiom(SemanticSensor, SensorOutput);
		OWLAxiom axiom4 = df.getOWLSubClassOfAxiom(SemanticSensor, Property);
		OWLAxiom axiom5 = df.getOWLSubClassOfAxiom(SemanticSensor, PatientLocation);
		
		manager.applyChange(new AddAxiom(o, axiom));
		manager.applyChange(new AddAxiom(o, axiom1));
		manager.applyChange(new AddAxiom(o, axiom2));
		manager.applyChange(new AddAxiom(o, axiom3));
		manager.applyChange(new AddAxiom(o, axiom4));
		manager.applyChange(new AddAxiom(o, axiom5));
		
		//object property definition
		OWLObjectProperty featureOfInterest = df.getOWLObjectProperty(IRI.create(documentIRI + "ssn:featureOfInterest"));
		OWLObjectProperty observedProperty = df.getOWLObjectProperty(IRI.create(documentIRI + "ssn:observedProperty"));
		OWLObjectProperty observedBy = df.getOWLObjectProperty(IRI.create(documentIRI + ";ssn:observedBy"));
		OWLObjectProperty observationResult = df.getOWLObjectProperty(IRI.create(documentIRI + ";ssn:observationResult"));
		OWLObjectProperty hasPatientLocation = df.getOWLObjectProperty(IRI.create(documentIRI + ";hasPatientLocation"));
		
									
		// Expressions definition on the observation message
		OWLClassExpression exp1 = df.getOWLObjectAllValuesFrom(featureOfInterest, FeatureOfInterest);
		OWLClassExpression exp2 = df.getOWLObjectAllValuesFrom(observedProperty, Property);
		OWLClassExpression exp3 = df.getOWLObjectAllValuesFrom(observedBy, Sensor);	
		OWLClassExpression exp4 = df.getOWLObjectAllValuesFrom(observationResult, SensorOutput);
		OWLClassExpression exp5 = df.getOWLObjectAllValuesFrom(hasPatientLocation, PatientLocation);
		
			
		// create restrictions
		OWLSubClassOfAxiom ax1 =	df.getOWLSubClassOfAxiom(Observation, exp1);
		OWLSubClassOfAxiom ax2 =	df.getOWLSubClassOfAxiom(Observation, exp2);
		OWLSubClassOfAxiom ax3 =	df.getOWLSubClassOfAxiom(Observation, exp3);
		OWLSubClassOfAxiom ax4 =	df.getOWLSubClassOfAxiom(Observation, exp4);
		OWLSubClassOfAxiom ax5 =	df.getOWLSubClassOfAxiom(Observation, exp5);
			
		// Data property for sensorOutput
		OWLDataProperty hasDataValue = df.getOWLDataProperty(IRI.create(documentIRI + ";DUL:hasDataValue"));	
		OWLDataRange dataValue = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(), OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(0));
		OWLClassExpression exp6 = df.getOWLDataSomeValuesFrom(hasDataValue, dataValue);
		OWLSubClassOfAxiom ax6 = df.getOWLSubClassOfAxiom(SensorOutput, exp6);
		
		OWLDataProperty isClassifiedBy = df.getOWLDataProperty(IRI.create(documentIRI + ";DUL:isClassifiedBy"));
		OWLClassExpression exp7 = df.getOWLDataSomeValuesFrom(isClassifiedBy, df.getRDFPlainLiteral());
		OWLSubClassOfAxiom ax7 = df.getOWLSubClassOfAxiom(SensorOutput, exp7);

		
		// Data property for patient location
		OWLDataProperty hasRoom = df.getOWLDataProperty(IRI.create(documentIRI + ";hasRoom"));	
		OWLDataRange roomNb = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(), OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(0));
		OWLClassExpression exp8 = df.getOWLDataSomeValuesFrom(hasRoom, roomNb);
		OWLSubClassOfAxiom ax8 = df.getOWLSubClassOfAxiom(PatientLocation, exp8);
		
		OWLDataProperty hasBed = df.getOWLDataProperty(IRI.create(documentIRI + ";hasBed"));	
		OWLDataRange bedNb = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(), OWLFacet.MIN_INCLUSIVE, df.getOWLLiteral(0));
		OWLClassExpression exp9 = df.getOWLDataSomeValuesFrom(hasBed, bedNb);
		OWLSubClassOfAxiom ax9 = df.getOWLSubClassOfAxiom(PatientLocation, exp9);
					
		manager.applyChange(new AddAxiom(o, ax1));
		manager.applyChange(new AddAxiom(o, ax2));
		manager.applyChange(new AddAxiom(o, ax3));
		manager.applyChange(new AddAxiom(o, ax4));
		manager.applyChange(new AddAxiom(o, ax5));

		manager.applyChange(new AddAxiom(o, ax6));
		manager.applyChange(new AddAxiom(o, ax7)); 
		manager.applyChange(new AddAxiom(o, ax8));
		manager.applyChange(new AddAxiom(o, ax9));
				
		manager.saveOntology(o, documentIRI);		
		manager.saveOntology(o, new SystemOutDocumentTarget());

	}

	public OWLOntology getOntology(){
		return o;
		
	}
	
	public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException, IOException{
		OntologyGenerator newMsg = new OntologyGenerator();
	//	newMsg.toSemanticTopic("bedsoreRisk");
	}
}
