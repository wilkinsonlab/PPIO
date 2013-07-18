package es.upm.cbgp.OWLAlphaNumericIDGenerator.Core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;




/**
 * @author Mikel Egaña Aranguren (http://mikeleganaaranguren.com/)
 * 
 * An abstraction of the OWL API offering only the needed methods
 * 
 */
public class MiniOWLAPI {
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private boolean ontology_loaded = false;

	public MiniOWLAPI (){
		manager = OWLManager.createOWLOntologyManager();
	}

	public void loadOntology (File owl_file) throws OntologyAlreadyLoadedException, OWLOntologyCreationException{
		if (ontology_loaded){
			throw (new OntologyAlreadyLoadedException());
		}
		else{
			ontology = manager.loadOntologyFromOntologyDocument(owl_file);
			ontology_loaded = true;
		}
	}
	public void loadOntology (IRI ontology_iri) throws OntologyAlreadyLoadedException, OWLOntologyCreationException{
		if (ontology_loaded){
			throw (new OntologyAlreadyLoadedException());
		}
		else{
			ontology = manager.loadOntologyFromOntologyDocument(ontology_iri);
			ontology_loaded = true;
		}
	}

	/**
	 * @param namespace: the namespace to filter entities, e.g. "http://purl.oclc.org/PPIO#"
	 * @return an ArrayList storing the fragments of the URIs of the entities (classes, 
	 * properties, individuals) matching the namespace
	 */
	public ArrayList<String> getOWLEntityURIFragments(String namespace){
		ArrayList<String> URIFragments = new ArrayList<String> ();
		for(OWLClass cl : ontology.getClassesInSignature()){
			IRI clsIRI = cl.getIRI();
			if(clsIRI.getNamespace().equals(namespace)){
				URIFragments.add(clsIRI.getFragment().toString());
			}
		}
		for(OWLIndividual ind : ontology.getIndividualsInSignature()){
			IRI indIRI = ((OWLNamedObject)ind).getIRI();
			if(indIRI.getNamespace().equals(namespace)){
				URIFragments.add(indIRI.getFragment().toString());
			}
		}
		for(OWLObjectProperty prop : ontology.getObjectPropertiesInSignature()){
			IRI propIRI = prop.getIRI();
			if(propIRI.getNamespace().equals(namespace)){
				URIFragments.add(propIRI.getFragment().toString());
			}
		}
		for(OWLDataProperty prop : ontology.getDataPropertiesInSignature()){
			IRI propIRI = prop.getIRI();
			if(propIRI.getNamespace().equals(namespace)){
				URIFragments.add(propIRI.getFragment().toString());
			} 
		}
		for(OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()){
			IRI propIRI = prop.getIRI();
			if(propIRI.getNamespace().equals(namespace)){
				URIFragments.add(propIRI.getFragment().toString());
			}
		}
		return URIFragments;
	}	
}
