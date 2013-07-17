package es.upm.cbgp.OWLAlphaNumericIDGenerator.Client;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.IRI;

import es.upm.cbgp.OWLAlphaNumericIDGenerator.Core.AlphanumericIDgenerator;
import es.upm.cbgp.OWLAlphaNumericIDGenerator.Core.MiniOWLAPI;

/**
 * @author Mikel Egaña Aranguren (http://mikeleganaaranguren.com/)
 *
 */
public class TerminalClient {


	/**
	 * @param Ontology file path or IRI
	 */
	public static void main(String[] args) {
		MiniOWLAPI my_owl_api = new MiniOWLAPI();
		String owl_ontology_file_or_iri = args [0]; // /home/mikel/git/OWLNumericIDGenerator/test_data/PPIO.owl
		String namespace = args [1]; // http://purl.oclc.org/PPIO#
		String fragment_alpha = args [2]; // PPIO
		int fragment_digits = Integer.parseInt(args [3]) ; // 6
		
		// Load target ontology
		
		File owl_file = new File(owl_ontology_file_or_iri);
		
		// if the passed string is a file and exists, load it as file
		if (owl_file.exists()){
			try {
				my_owl_api.loadOntology(owl_file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// otherwise load it as remote IRI
		else{
			try {
				my_owl_api.loadOntology(IRI.create(owl_ontology_file_or_iri));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Obtain fragments of the ontology entities
		ArrayList<String> URIFragments = my_owl_api.getOWLEntityURIFragments(namespace);
		
		// Generate new fragment
		AlphanumericIDgenerator gen = new  AlphanumericIDgenerator (URIFragments, fragment_alpha, fragment_digits);
		System.out.println(namespace +gen.generateNewID());
	}

}
