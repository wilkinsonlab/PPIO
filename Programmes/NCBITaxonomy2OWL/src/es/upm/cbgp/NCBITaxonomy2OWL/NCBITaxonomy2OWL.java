package es.upm.cbgp.NCBITaxonomy2OWL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class NCBITaxonomy2OWL {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws OWLOntologyStorageException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		String ncbi_taxon_ids_file_path = args [0];
		String API_KEY = args [1]; 
		String input_ontology_file_path = args [2]; 

		// Load ontology
		NCBIOWLStore owl_store = new NCBIOWLStore();
		owl_store.loadModel(input_ontology_file_path);
		
		// Get ids and for each, add it to the ontology, including the path to the root
		ArrayList<String> ids = new ArrayList<String>();
		BufferedReader ids_in = new BufferedReader(new FileReader(ncbi_taxon_ids_file_path));
		String id_str;
		while ((id_str = ids_in.readLine()) != null) {
			String id = id_str.trim();
			String NCBI_taxon = "obo:NCBITaxon_" + id;
			// TODO: Highly inefficient, it doesn't stop when a common ancestor is found! 
			for(;;){
				if(NCBI_taxon.equals("obo:NCBITaxon_1")){
					break;
				}
				else{
					owl_store = XMLResponseParser.parseXMLFile("http://rest.bioontology.org/bioportal/concepts/47845?conceptid="+ 
						NCBI_taxon + "&apikey=" +API_KEY, owl_store);
					NCBI_taxon = XMLResponseParser.getAddedLastTaxonID();
				}
			}
//			owl_store.addComment("http://purl.obolibrary.org/obo/NCBITaxon_" + id,"AddedByNCBI2OWL");
		}
		ids_in.close();
//		owl_store.writeModel(input_ontology_file_path);
		owl_store.writeModel();
	}
}
