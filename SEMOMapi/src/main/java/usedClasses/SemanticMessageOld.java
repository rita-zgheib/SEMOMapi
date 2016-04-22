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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class SemanticMessageOld {
	
	private String Observation_id;
	private String sensor_name;
	private String feature_of_Interest;
	private String property;
	// ou pour après je peux créer un hashmap s'appelant Observation result qui contient datavalue et classification!!! à revoir
	private int dataValue;   //sensor output
	private String classification; //example celsius
	private int roomNb;
	private int badNb;
	File output; 
	
	public SemanticMessageOld() {
	}
	public SemanticMessageOld(String observation_id, String sensor_name,
			String feature_of_Interest, String property, int dataValue,
			String classification, int roomNb, int badNb) {
		super();
		Observation_id = observation_id;
		this.sensor_name = sensor_name;
		this.feature_of_Interest = feature_of_Interest;
		this.property = property;
		this.dataValue = dataValue;
		this.classification = classification;
		this.roomNb = roomNb;
		this.badNb = badNb;
	}
	
	public OWLOntology toOwl() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException{
		//OWLOntology o = m.loadOntologyFromOntologyDocument(pizza_iri);
		output = File.createTempFile("savedBedsore", "owl");
		System.out.println("creating OWL manager...");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		System.out.println("OWL manager created.");
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		IRI documentIRI = IRI.create(output);
		System.out.println("creating OWL ontology...");
		OWLOntology o = manager.createOntology(documentIRI);
		System.out.println("OWL ontology created.");
		
		OWLClass Observation = df.getOWLClass(IRI.create(documentIRI + "#ssn:Observation"));
		OWLClass FeatureOfInterest = df.getOWLClass(IRI.create(documentIRI + "#ssn:FeatureOfInterest"));
		OWLClass ObservedProperty = df.getOWLClass(IRI.create(documentIRI + "#ssn:ObservedProperty"));
		OWLClass ObservationResult = df.getOWLClass(IRI.create(documentIRI + "#ssn:ObservationResult"));
		OWLClass SensorOutput = df.getOWLClass(IRI.create(documentIRI + "#ssn:SensorOutput"));
		OWLClass observedBy = df.getOWLClass(IRI.create(documentIRI + "#ssn:observedBy"));
		OWLClass room = df.getOWLClass(IRI.create(documentIRI + "#room"));
		OWLClass bed = df.getOWLClass(IRI.create(documentIRI + "#bed"));
		
		//object property definition
		OWLObjectProperty hasDataValue = df.getOWLObjectProperty(IRI.create(documentIRI + "#DUL:hasDataValue"));
		OWLObjectProperty isClassifiedBy = df.getOWLObjectProperty(IRI.create(documentIRI + "#DUL:isClassifiedBy"));
		OWLObjectProperty hasPatientLocation = df.getOWLObjectProperty(IRI.create(documentIRI + "#hasPatientLocation"));
		
		OWLAxiom axiom = df.getOWLSubClassOfAxiom(Observation, FeatureOfInterest);
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		manager.applyChange(addAxiom);
		manager.saveOntology(o);
		
		Set<OWLSubClassOfAxiom> axiomres = o.getSubClassAxiomsForSubClass(Observation);
		System.out.println("axioms are: " + axiomres);
		manager.saveOntology(o, new SystemOutDocumentTarget());

		return o;	
	}
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
		SemanticMessageOld ob = new SemanticMessageOld();
		OWLOntology result = ob.toOwl();
		Set<OWLAxiom> axSet= result.getAxioms();
		System.out.println("axioms are: " + axSet);
		
	}
}
