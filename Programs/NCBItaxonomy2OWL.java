package es.upm.cbgp.ppio;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;


public class NCBItaxonomy2OWL {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws OWLOntologyStorageException 
	 * @throws OWLOntologyCreationException 
	 */
	public static void main(String[] args) throws IOException, OWLOntologyStorageException, OWLOntologyCreationException {
		String names_file_path = args [0]; // /home/mikel/UPM/PPIO/plant-pathogen-interacions-ontology.ontology/names.dmp
		String nodes_file_path = args [1]; // //home/mikel/UPM/PPIO/plant-pathogen-interacions-ontology.ontology/nodes.dmp
		String input_ontology_file_path = args [2]; // /home/mikel/UPM/PPIO/plant-pathogen-interacions-ontology.ontology/DC3000_A_A.owl
		String output_ontology_file_path = args [3]; // /home/mikel/UPM/PPIO/plant-pathogen-interacions-ontology.ontology/DC3000_A_A_taxa.owl
		String taxon_ids_file_path = args [4]; // /home/mikel/UPM/PPIO/plant-pathogen-interacions-ontology.ontology/ncbi_taxa_ids
		
		// Parse names.dmp and add content to id_name
		HashMap<String, String> id_name = new HashMap<String, String>();	
		BufferedReader in = new BufferedReader(new FileReader(names_file_path));
		String str;
		while ((str = in.readLine()) != null) {
			String [] groups = str.split("\\|");
			// if col 3 is scientific name, push id-name into hashmap
			if(groups[3].contains("scientific name")){
				id_name.put(groups[0].replaceAll("\t",""), groups[1].replaceAll("\t",""));
			}
		}
		in.close();		
//		printHashMap(id_name);
		
		// Parse nodes.dmp and add content child_id_parent_id
		HashMap<String, String> child_id_parent_id = new HashMap<String, String>();	
		BufferedReader nodes_in = new BufferedReader(new FileReader(nodes_file_path));
		String node_str;
		while ((node_str = nodes_in.readLine()) != null) {
			String [] groups = node_str.split("\\|");
			child_id_parent_id.put(groups[0].replaceAll("\t",""), groups[1].replaceAll("\t",""));
		}
		nodes_in.close();
//		printHashMap(child_id_parent_id);
		
//		Parse ncbi_taxa_ids and add content to ids_in
		ArrayList<String> ids = new ArrayList<String>();
		BufferedReader ids_in = new BufferedReader(new FileReader(taxon_ids_file_path));
		String id_str;
		while ((id_str = ids_in.readLine()) != null) {
//			System.out.println(id_str.trim());
			ids.add(id_str.trim());
		}
		ids_in.close();
		
//		Load input ontology
		File in_owl_file = new File(input_ontology_file_path);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(in_owl_file);
//		System.out.println("Loaded ontology: " + ontology); 
		
		
//		Traverse hashmaps and add organisms to ontology
		ArrayList<String> added_organisms = new ArrayList<String>();
		Iterator<String> ids_iterator = ids.iterator();
		while(ids_iterator.hasNext()) {
			String org = (String)ids_iterator.next();
			added_organisms = addOrganism (org, id_name, child_id_parent_id, added_organisms, manager, factory, ontology);
			
//			String org = obtain_hashMap_key_from_value(id_name,(String)ids_iterator.next());
//			added_organisms = addOrganism (org, id_name, child_id_parent_id, added_organisms, manager, factory, ontology);
		}
		
//		Write loaded input ontology as a different file
		File out_owl_file = new File(output_ontology_file_path);
		RDFXMLOntologyFormat rdfxmlformat = new RDFXMLOntologyFormat();
		manager.saveOntology(ontology,rdfxmlformat, IRI.create(out_owl_file.toURI())); 
	}
//	static void printHashMap(HashMap any_hash_map){
//		Set set = any_hash_map.keySet();
//		Iterator i = set.iterator();
//		while(i.hasNext()) {
//			String key = (String) i.next();
//			String value = (String) any_hash_map.get(key);
//			System.out.println(key + " -- " + value);
//		}
//	}
//	static String obtain_hashMap_key_from_value (HashMap any_hash_map, String provided_value){
//		String result = null;
//		Set set = any_hash_map.keySet();
//		Iterator i = set.iterator();
//		while(i.hasNext()) {
//			String key = (String) i.next();
//			String value = (String) any_hash_map.get(key);
//			if(value.equals(provided_value)){
//				result = key;
//			}
//		}
//		return result;
//	}
	static ArrayList<String> addOrganism (String organism_id, HashMap<String,String> id_name, HashMap<String,String> child_id_parent_id, ArrayList<String> added_organisms, OWLOntologyManager manager, OWLDataFactory factory, OWLOntology ontology) throws OWLOntologyChangeException{
//		System.out.println("---------------->" + organism_id);
		if(!added_organisms.contains(organism_id)){
			added_organisms.add(organism_id);
			String organism_parent_id = child_id_parent_id.get(organism_id); 
			OWLClass clsChild = factory.getOWLClass(IRI.create(ontology.getOntologyID().getOntologyIRI().toString() + "#NCBI_" + organism_id));
			OWLClass clsParent = factory.getOWLClass(IRI.create(ontology.getOntologyID().getOntologyIRI().toString() + "#NCBI_" + organism_parent_id));
			OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsChild, clsParent);
			AddAxiom addAxiom = new AddAxiom(ontology, axiom);
			manager.applyChange(addAxiom);
			String organism_name = id_name.get(organism_id);
			String parent_name = id_name.get(organism_parent_id);
//			System.out.println("--->");
//			System.out.println("CHILD:" + organism_name);
//			System.out.println("CHILD ID:" + organism_id);
//			System.out.println("PARENT:" + parent_name);
//			System.out.println("PARENT ID:" + organism_parent_id);
			if(parent_name != null){ 
				addLabel(parent_name, clsParent, factory, manager, ontology);
			}
			if(organism_name != null){
				addLabel(organism_name, clsChild, factory, manager, ontology);
			}
			if(!organism_parent_id.equals("1")){
				added_organisms = addOrganism (organism_parent_id, id_name, child_id_parent_id, added_organisms, manager, factory, ontology);
			}	
		}
		return added_organisms;
	}
	static void addLabel (String label, OWLClass cls, OWLDataFactory factory, OWLOntologyManager manager, OWLOntology ontology) throws OWLOntologyChangeException{
		OWLAnnotation labelAnno = factory.getOWLAnnotation(
				factory.getRDFSLabel(),
				factory.getOWLLiteral(label, "la"));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), labelAnno);
		manager.applyChange(new AddAxiom(ontology, ax));
	}
}
