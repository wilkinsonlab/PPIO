package es.upm.cbgp.NCBITaxonomy2OWL;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 


public class XMLResponseParser {
	/**
	 * @param uri RESTful Search URI for BioPortal web services
	 * Example taken from https://bmir-gforge.stanford.edu/gf/project/client_examples/scmsvn/?action=browse&path=%2Ftrunk%2FJava%2FListOntologies-Java%2Fsrc%2FParseXMLResponse.java&view=markup
	 */
	static String lastTaxonID = null;

	public static String getAddedLastTaxonID(){
		return lastTaxonID;
	}
	public static NCBIOWLStore parseXMLFile (String uri, NCBIOWLStore store){
		try {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			
			Document doc = docBuilder.parse(uri);
//			Document doc = docBuilder.parse(new File("/home/mikel/git/NCBITaxonomy2OWL/NCBO_output_443906.xml"));
//			Document doc = docBuilder.parse(new File("/home/mikel/git/NCBITaxonomy2OWL/NCBO_output_33013.xml"));
			
			
			
			Node classBean = doc.getElementsByTagName("data").item(0).getChildNodes().item(1);
//			System.out.println("CLASSBEAN " + classBean);

			NodeList children = classBean.getChildNodes();
//			System.out.println("child id " + retrieveNode(children, "id").getTextContent());
//			System.out.println("child fullId " + retrieveNode(children, "fullId").getTextContent());
			String childFullID = retrieveNode(children, "fullId").getTextContent();
//			System.out.println("child label " + retrieveNode(children, "label").getTextContent());
			String childLabel = retrieveNode(children, "label").getTextContent();
			
			NodeList relation_children = (retrieveNode(children, "relations").getChildNodes());
			for(int s=0; s<relation_children.getLength(); s++) {
				Node relation_child = relation_children.item(s);
				if(relation_child.getNodeName().equals("entry") && retrieveNode(relation_child.getChildNodes(), "string").getTextContent().equals("rdfs:subClassOf")){
					Node list = retrieveNode(relation_child.getChildNodes(), "list");
					Node parent_class_bean = retrieveNode(list.getChildNodes(),"classBean");
					NodeList children_parent_class_bean = parent_class_bean.getChildNodes();
//					System.out.println("parent id " + retrieveNode(children_parent_class_bean, "id").getTextContent());
					store.addTaxon(childFullID, retrieveNode(children_parent_class_bean, "fullId").getTextContent());
					store.addLabel(childFullID, childLabel);
					lastTaxonID = retrieveNode(children_parent_class_bean, "id").getTextContent();
//					System.out.println("parent full id " + retrieveNode(children_parent_class_bean, "fullId").getTextContent());	
				}
			}
		}
		catch (SAXParseException err) {
			System.out.println ("**PARSING ERROR" + ", line "+err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
		}
		catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();
		}
		catch (Throwable t) {
			t.printStackTrace ();
		}
		return store;
	}
	private static Node retrieveNode (NodeList children, String node_name){
		Node node = null;
		for(int s=0; s<children.getLength(); s++) {
			if(children.item(s).getNodeName().equals(node_name)){
				node = children.item(s);
			}
		}
		return node;
	} 
}
