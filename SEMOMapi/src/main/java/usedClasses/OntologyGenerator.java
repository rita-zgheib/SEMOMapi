package usedClasses;

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
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OntologyGenerator { 

	File output;
	File output_Msg;
	File output_Topic;
	OWLOntologyManager manager;
	OWLOntologyManager topic_manager;
	OWLOntology o;
	OWLOntology ont_result;
	OWLOntology ont_Topic;

	 private static final Logger LOG = LoggerFactory.getLogger(OntologyGenerator.class);
	 public static final IRI EXAMPLE_IRI  = IRI.create("http://www.semanticweb.org/ontologies/semanticObservation.owl");
	 public static final IRI EXAMPLE_IRI_Obs  = IRI.create("http://www.semanticweb.org/ontologies/semanticObservationMsg.owl");
	 public static final IRI EXAMPLE_IRI_Topic  = IRI.create("http://www.semanticweb.org/ontologies/SemanticTopic.owl");
	// public static final IRI EXAMPLE_SAVE_IRI  = IRI.create("file:materializedOntologies/semanticObservation129.owl");
	 OWLDataFactory df = OWLManager.getOWLDataFactory();

	 
	public OntologyGenerator(String observation_id, String sensor_name,
			String feature_of_Interest, String property, int data_Value,
			String classification, int room, int bed) throws OWLOntologyStorageException, IOException, OWLOntologyCreationException {	
		//create the base ontology		
		output = File.createTempFile("saved_bedsore", "owl");
		output_Msg = File.createTempFile("saved_Obs_Msg", "owl");
		manager = OWLManager.createOWLOntologyManager();
		PriorityCollection<OWLOntologyIRIMapper> iriMappers = manager.getIRIMappers();
		iriMappers.add(new AutoIRIMapper(new File("materializedOntologies/semanticObservation129"), true));
		System.out.println("OWL manager created.");
		
		IRI documentIRI = IRI.create(output);
		IRI documentIRI_Msg = IRI.create(output_Msg);
		//System.out.println("creating OWL ontology...");
		o = manager.createOntology(EXAMPLE_IRI);
		ont_result = manager.createOntology(EXAMPLE_IRI_Obs);
		System.out.println("OWL ontology created.");
		 /*OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	     OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	     OWLReasoner reasoner = reasonerFactory.createReasoner(o, config);
	     reasoner.precomputeInferences();
	     boolean consistent = reasoner.isConsistent();
	     System.out.println("Consistent: " + consistent);*/
		
		
		//OWL classes definition
		OWLClass SensorOntology = df.getOWLClass(IRI.create(documentIRI + ";SensorOntology"));
		OWLClass Sensor = df.getOWLClass(IRI.create(documentIRI + ";ssn:sensor"));
		OWLClass Observation = df.getOWLClass(IRI.create(documentIRI + ";ssn:Observation"));
		OWLClass FeatureOfInterest = df.getOWLClass(IRI.create(documentIRI + ";ssn:FeatureOfInterest"));
		OWLClass Property = df.getOWLClass(IRI.create(documentIRI + ";ssn:Property"));
		OWLClass SensorOutput = df.getOWLClass(IRI.create(documentIRI + ";ssn:SensorOutput"));
		OWLClass PatientLocation = df.getOWLClass(IRI.create(documentIRI + ";PatientLocation"));	
		
		// Axioms definition
		OWLAxiom axiom = df.getOWLSubClassOfAxiom(SensorOntology, Observation);
		OWLAxiom axiom1 = df.getOWLSubClassOfAxiom(SensorOntology, Sensor);
		OWLAxiom axiom2 = df.getOWLSubClassOfAxiom(SensorOntology, FeatureOfInterest);
		OWLAxiom axiom3 = df.getOWLSubClassOfAxiom(SensorOntology, SensorOutput);
		OWLAxiom axiom4 = df.getOWLSubClassOfAxiom(SensorOntology, Property);
		OWLAxiom axiom5 = df.getOWLSubClassOfAxiom(SensorOntology, PatientLocation);
		
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
		
		
		//create individuals
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
		manager.applyChange(new AddAxiom(o, assertion4));
		
	/*	
		System.out.println("here starts the test");
		Set<OWLNamedIndividual> s = o.getIndividualsInSignature();
		for (OWLNamedIndividual i : s){
			System.out.println("Individual " + i.toString() + " with type " + EntitySearcher.getTypes(i, o));
			EntitySearcher.getTypes(i, o);
		}
		 
		System.out.println("here the end of the test");*/
		
		manager.saveOntology(o, documentIRI);

		
		//create the observation msg ontology
		
		manager.addAxiom(ont_result,df.getOWLClassAssertionAxiom(Observation , obs));
		manager.addAxiom(ont_result,df.getOWLClassAssertionAxiom(Property , prop));
		manager.addAxiom(ont_result,df.getOWLClassAssertionAxiom(SensorOutput , sensorOutput));
		manager.addAxiom(ont_result,df.getOWLClassAssertionAxiom(PatientLocation , location));
		manager.applyChange(new AddAxiom(ont_result, assertion));
		manager.applyChange(new AddAxiom(ont_result, assertion1));
		manager.applyChange(new AddAxiom(ont_result, assertion2));
		manager.applyChange(new AddAxiom(ont_result, assertion3));
		manager.applyChange(new AddAxiom(ont_result, assertion4));
		manager.applyChange(new AddAxiom(ont_result, dataPropertyAssertion));
		manager.applyChange(new AddAxiom(ont_result, dataPropertyAssertion1));
		manager.applyChange(new AddAxiom(ont_result, dataPropertyAssertion2));
		manager.applyChange(new AddAxiom(ont_result, dataPropertyAssertion3));
		
		manager.saveOntology(ont_result, documentIRI_Msg);
	/*	
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(Observation,true);
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        System.out.println("Instances of Observation: ");
        for (OWLNamedIndividual ind : individuals) {
            System.out.println("    " + ind);
            ind.getIndividualsInSignature();
        }*/
		//Set<OWLSubClassOfAxiom> axiomres = o.getSubClassAxiomsForSubClass(Observation);
		//System.out.println("axioms are: " + axiomres);
		
		
		manager.saveOntology(o, new SystemOutDocumentTarget());
		//manager.saveOntology(ont_result, new SystemOutDocumentTarget());
	}
	
	public OWLOntology getOntology(){
		return ont_result;
		
	}
	
	public OWLIndividual toSemanticTopic(String topic) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException{
		
		output_Topic = File.createTempFile("saved_Topic", "owl");
		topic_manager = OWLManager.createOWLOntologyManager();
		PriorityCollection<OWLOntologyIRIMapper> iriMappers = topic_manager.getIRIMappers();
		iriMappers.add(new AutoIRIMapper(new File("materializedOntologies/semanticTopic129.owl"), true));
		System.out.println("OWL Topic manager created.");
		
		IRI TopicdocumentIRI = IRI.create(output_Topic);
		
		ont_Topic = topic_manager.createOntology(EXAMPLE_IRI_Topic);
		System.out.println("OWL ontology created.");
		
		OWLClass Property = df.getOWLClass(IRI.create(TopicdocumentIRI + ";ssn:Property"));
		OWLIndividual prop = df.getOWLNamedIndividual(IRI.create(TopicdocumentIRI + ";"+topic));
		topic_manager.addAxiom(ont_Topic,df.getOWLClassAssertionAxiom(Property , prop));
		System.out.println("topic: " + prop + "length:" +prop.toString().getBytes().length);
		topic_manager.saveOntology(ont_Topic, TopicdocumentIRI);
		topic_manager.saveOntology(ont_Topic, new SystemOutDocumentTarget());
		return prop;
		
	}
	
	public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException, IOException{
		OntologyGenerator newMsg = new OntologyGenerator("BedsoreObservation1", "Bedsoresensor", 
				"BackElder", "bedsoreRisk", 12, "RHouX", 2, 3);
	//	newMsg.toSemanticTopic("bedsoreRisk");
	}
}
