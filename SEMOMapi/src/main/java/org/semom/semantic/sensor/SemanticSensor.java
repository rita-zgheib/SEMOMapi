// programmer1 uses this class to create new semantic sensor which is an object:
// attributes of this object are: property, featureOfInterest, sensorName
// and a list of Observations


package org.semom.semantic.sensor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.PriorityCollection;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semom.semantic.message.SemanticObservationMessage;

// Dans cette classe 3 méthode getSemanticMessage

public class SemanticSensor {
	//private String foi;
	//private String prop;
	private OWLIndividual semSensor;
	private SemanticFeatureOfInterest featureOfInterest;
	private SemanticProperty property;
	private List<SemanticSensorOutput> sensorOutputs;
	private List<SemanticObservation> observations;
	
	private String sensorName;
	private String classification;
	private List<Integer> sensorOutput;
	
	private OWLOntologyManager manager;
	private OWLOntology o;
	private OWLOntology original_ontology;
	
	private static final IRI SEMANTIC_SENSOR_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticSensor.owl");
	//public static final IRI Ontology_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticMiddleware.owl");
	File intput = new File("C:/Users/rita.zougheib/Documents/GenericOntologySpace/GenericOntology.owl"); //home	
	// File intput = new File("C:/Users/rzgheib/Documents/GenericOntologySpace/GenericOntology.owl");    // ISIS
	private File output;
	private IRI documentIRI;
	
	private Random rand = new Random();
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
	public SemanticSensor(String prop, String foi, String classification) {			
			this.sensorName = prop+"Sensor"+rand.nextInt(50);
		   // this.output = new File("C:/Users/rzgheib/Documents/OntologiesSpace/"+sensorName+".owl"); //isis
			this.output = new File("C:/Users/rita.zougheib/Documents/OntologiesSpace/"+sensorName+".owl");
			createOntology();
			try {
				original_ontology = manager.loadOntologyFromOntologyDocument(intput);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			this.property = new SemanticProperty(prop, original_ontology, o, manager, documentIRI) ;
			this.featureOfInterest = new SemanticFeatureOfInterest(foi, original_ontology, o, manager, documentIRI);
			this.classification = classification;
			//this.sensorOutputs = null;
			//this.observations=null;			
			createIndividuals();
	}

	public void createOntology() {
			//create Ontology manager		
			manager = OWLManager.createOWLOntologyManager();
			System.out.println("OWL manager created.");

			// map the ontology IRI to a physical IRI (files for example)
			//File output = new File("C:/Users/rzgheib/Documents/OntologiesSpace/SemanticSensor.owl");
			this.documentIRI = IRI.create(output);
			// Set up a mapping, which maps the ontology to the document IRI
			SimpleIRIMapper mapper = new SimpleIRIMapper(SEMANTIC_SENSOR_IRI, documentIRI);
			PriorityCollection<OWLOntologyIRIMapper> iriMappers = manager.getIRIMappers();
			iriMappers.add(mapper);
			// set up a mapper to read local copies of ontologies
			File localFolder = new File("materializedOntologies");
			// the manager will look up an ontology IRI by checking localFolder first for a local copy
			iriMappers.add(new AutoIRIMapper(localFolder, true));
			
			// Now create the ontology using the ontology IRI (not the physical URI)
			try {
				o = manager.createOntology(SEMANTIC_SENSOR_IRI);
				// save the ontology to its physical location - documentIRI
				manager.saveOntology(o);
			//	manager.saveOntology(o, new SystemOutDocumentTarget());
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
			
			System.out.println("OWL ontology created.");
	}
	
	public void createIndividuals() {
		
		this.semSensor = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+sensorName));
		for (OWLClass c : original_ontology.getClassesInSignature()) 
			if(c.toString().contains("SemanticSensor"))
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(c , semSensor));
		property.createClassAssertion();
		property.createObjectProperty(semSensor);
		featureOfInterest.createClassAssertion();
		featureOfInterest.createObjectProperty(semSensor);
		
		//OWLIndividual obs = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+observation_id));		
		try {
			manager.saveOntology(o);
			manager.saveOntology(o, new SystemOutDocumentTarget());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}	
	}
	
	// For each sensor output, this method return a semantic observation used for generating
	// a semantic message
	public String addSensorOutput(int value) {
		
		SemanticSensorOutput sensOutput = new SemanticSensorOutput(value, classification, 
				original_ontology, o , manager, documentIRI);
		SemanticObservation semObs = new SemanticObservation(original_ontology, o, manager, documentIRI);
		semObs.createObjectProperty(this.semSensor, property.getProp(), featureOfInterest.getFoi(), sensOutput.getSensorOutputInd());
		semObs.getObservationInd();
		try {
			manager.saveOntology(o);
			manager.saveOntology(o, new SystemOutDocumentTarget());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		return semObs.getObservationInd().toString();
	//	this.sensorOutputs.add(sensOutput);
	
	}
	
// il faut ajouter les méthodes getProperty, getFoi qui renvoie les individus correspondats à un capteur sémantique
	/*public List<SemanticObservation> getObservations() {	
		return ;
	}
	*/
	public Set<OWLClass> getOntologyClasses() {	
		return original_ontology.getClassesInSignature();
	}
	
	public OWLIndividual getsemanticSensorInd(){
		return this.semSensor;
	}
/*	
	public static void main(String[] args){
		// new semanticMessage sem et sem.getSemMsg(semanticSensor, Observation) 
		SemanticSensor sem = new SemanticSensor("Temperature", "bed", "RH");
		sem.addSensorOutput(30);
		System.out.println(sem.getOntologyClasses());
	}
	*/
}
