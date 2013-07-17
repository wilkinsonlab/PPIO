package es.upm.cbgp.OWLAlphaNumericIDGenerator.Tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import es.upm.cbgp.OWLAlphaNumericIDGenerator.Core.MiniOWLAPI;
import es.upm.cbgp.OWLAlphaNumericIDGenerator.Core.OntologyAlreadyLoadedException;

public class MiniOWLAPITests {
	private static final String owl_ontology_file_or_iri = "/home/mikel/UPM/PPIO/PPIO/Ontologies/PPIO/PPIO.owl";
	
    @Rule  
    public ExpectedException thrownOntologyAlreadyLoadedException = ExpectedException.none();

	@Test
	public void doubleLoadShouldFail() throws OntologyAlreadyLoadedException {
		thrownOntologyAlreadyLoadedException.expect(OntologyAlreadyLoadedException.class);
		MiniOWLAPI my_owl_api = new MiniOWLAPI();
		File owl_file = new File(owl_ontology_file_or_iri);
		try {
			my_owl_api.loadOntology(owl_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			my_owl_api.loadOntology(owl_file);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

}
