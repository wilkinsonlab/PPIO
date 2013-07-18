package es.upm.cbgp.OWLAlphaNumericIDGenerator.Core;

/**
 * @author Mikel Egaña Aranguren (http://mikeleganaaranguren.com/)
 * 
 * The OWL API only complains if the same ontology is reloaded; I want the core to only load one ontology
 */
public class OntologyAlreadyLoadedException extends Exception {

	public OntologyAlreadyLoadedException() {
		// TODO Auto-generated constructor stub
	}

	public OntologyAlreadyLoadedException(String message) {
		super("Ontology already loaded!");
		// TODO Auto-generated constructor stub
	}

	public OntologyAlreadyLoadedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public OntologyAlreadyLoadedException(String message, Throwable cause) {
		super("Ontology already loaded!", cause);
		// TODO Auto-generated constructor stub
	}

	public OntologyAlreadyLoadedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super("Ontology already loaded!", cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
