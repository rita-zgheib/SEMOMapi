package org.semom.semantic.message;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semom.semantic.sensor.SemanticSensor;

public class SemanticObservationMessage {
	
	private SemanticSensor sens;
	private int value;
	private OWLOntologyManager manager;
	private OWLOntology o;
	File intput = new File("C:/Users/rzgheib/Documents/GenericOntology.owl");
	
	//private static final IRI SEMANTIC_MESSAGE_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticMessage.owl");
	public static final IRI Ontology_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticMiddleware.owl");
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
	public SemanticObservationMessage(SemanticSensor sens, int observationValue) {
		this.sens = sens;
		this.value = observationValue;
		manager = OWLManager.createOWLOntologyManager();
		try {
			o = manager.loadOntologyFromOntologyDocument(intput);
			manager.saveOntology(o, new SystemOutDocumentTarget());
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*//create individuals
				OWLIndividual obs = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+observation_id));
				OWLIndividual sensor = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+sensor_name));
				OWLIndividual FoI = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+feature_of_Interest));
				OWLIndividual prop = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+property));
				OWLIndividual sensorOutput = df.getOWLNamedIndividual(IRI.create(documentIRI + ";sensorOutput"));
				OWLIndividual location = df.getOWLNamedIndividual(IRI.create(documentIRI + ";location"));
				//OWLIndividual sensorOutput = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+data_Value));
				//OWLIndividual location = df.getOWLNamedIndividual(IRI.create(documentIRI + ";"+room+"-"+bed));
				//OWLIndividual calssif = df.getOWLNamedIndividual(IRI.create(documentIRI + ";classification"));

				manager.addAxiom(o,df.getOWLClassAssertionAxiom(Observation , obs));
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(Sensor , sensor));
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(FeatureOfInterest , FoI));
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(Property , prop));
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(SensorOutput , sensorOutput));
				manager.addAxiom(o,df.getOWLClassAssertionAxiom(PatientLocation , location));
				
				OWLDataPropertyAssertionAxiom dataPropertyAssertion = df.getOWLDataPropertyAssertionAxiom(hasDataValue, sensorOutput, data_Value);
				OWLDataPropertyAssertionAxiom dataPropertyAssertion1 = df.getOWLDataPropertyAssertionAxiom(isClassifiedBy, sensorOutput, classification);
				OWLDataPropertyAssertionAxiom dataPropertyAssertion2 = df.getOWLDataPropertyAssertionAxiom(hasRoom, location, room);
				OWLDataPropertyAssertionAxiom dataPropertyAssertion3 = df.getOWLDataPropertyAssertionAxiom(hasBed, location, bed);
				
				AddAxiom addDataAxiomChange = new AddAxiom(o, dataPropertyAssertion);
				manager.applyChange(addDataAxiomChange);
				AddAxiom addDataAxiomChange1 = new AddAxiom(o, dataPropertyAssertion1); 
				manager.applyChange(addDataAxiomChange1);
				AddAxiom addDataAxiomChange2 = new AddAxiom(o, dataPropertyAssertion2);
				manager.applyChange(addDataAxiomChange2);
				AddAxiom addDataAxiomChange3 = new AddAxiom(o, dataPropertyAssertion3);
				manager.applyChange(addDataAxiomChange3);
				
				OWLObjectPropertyAssertionAxiom assertion = df.getOWLObjectPropertyAssertionAxiom(observedBy, obs, sensor);
				OWLObjectPropertyAssertionAxiom assertion1 = df.getOWLObjectPropertyAssertionAxiom(featureOfInterest, obs, FoI);
				OWLObjectPropertyAssertionAxiom assertion2 = df.getOWLObjectPropertyAssertionAxiom(observedProperty, obs, prop);
				OWLObjectPropertyAssertionAxiom assertion3 = df.getOWLObjectPropertyAssertionAxiom(observationResult, obs, sensorOutput);
				OWLObjectPropertyAssertionAxiom assertion4 = df.getOWLObjectPropertyAssertionAxiom(hasPatientLocation, obs, location);
				
				manager.applyChange(new AddAxiom(o, assertion));
				manager.applyChange(new AddAxiom(o, assertion1));
				manager.applyChange(new AddAxiom(o, assertion2));
				manager.applyChange(new AddAxiom(o, assertion3));
				manager.applyChange(new AddAxiom(o, assertion4));*/
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException{
		SemanticObservationMessage sem = new SemanticObservationMessage(null, 3);
		
	}
}
