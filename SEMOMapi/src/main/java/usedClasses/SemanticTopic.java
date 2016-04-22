package usedClasses;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.PriorityCollection;

public class SemanticTopic {
		
	File output_Topic;
	OWLOntologyManager topic_manager;
	OWLOntology ont_Topic;
	
	public static final IRI EXAMPLE_IRI_Topic  = IRI.create("http://www.semanticweb.org/ontologies/SemanticTopic.owl");
	OWLDataFactory df = OWLManager.getOWLDataFactory();
	
public SemanticTopic(String topic) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException{
		
		output_Topic = File.createTempFile("saved_Topic", "owl");
		topic_manager = OWLManager.createOWLOntologyManager();
		PriorityCollection<OWLOntologyIRIMapper> iriMappers = topic_manager.getIRIMappers();
		iriMappers.add(new AutoIRIMapper(new File("materializedOntologies/semanticTopic.owl"), true));
		System.out.println("OWL Topic manager created.");
		
		IRI TopicdocumentIRI = IRI.create(output_Topic);		
		ont_Topic = topic_manager.createOntology(EXAMPLE_IRI_Topic);
		System.out.println("OWL ontology created.");
		
		OWLClass Property = df.getOWLClass(IRI.create(TopicdocumentIRI + "#ssn:Property"));
		OWLIndividual prop = df.getOWLNamedIndividual(IRI.create(TopicdocumentIRI + "#"+topic));
		topic_manager.addAxiom(ont_Topic,df.getOWLClassAssertionAxiom(Property , prop));
		
		topic_manager.saveOntology(ont_Topic, TopicdocumentIRI);
		topic_manager.saveOntology(ont_Topic, new SystemOutDocumentTarget());
		/*
		System.out.println("here starts the test");
		Set<OWLNamedIndividual> s = ont_Topic.getIndividualsInSignature();
		for (OWLNamedIndividual i : s){
			//if (EntitySearcher.getTypes(i, ont_Topic).equals(Property))
				System.out.println("Individual " + i.toString() + " with type " + EntitySearcher.getTypes(i, ont_Topic));
			//EntitySearcher.getTypes(i, ont_Topic);
		}
		 
		System.out.println("here the end of the test");	*/
	}

	public static void main(String[] args) throws OWLOntologyStorageException, OWLOntologyCreationException, IOException{
		SemanticTopic newTopic = new SemanticTopic("bedsoreRisk/bed2");

	}
	
}
