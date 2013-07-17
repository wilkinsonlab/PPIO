package es.upm.cbgp.OWLAlphaNumericIDGenerator.Core;

import java.util.ArrayList;
import java.util.Collections;

public class AlphanumericIDgenerator {
	private static ArrayList<String> frags;
	private static String alpha;
	private static int digits;
	public AlphanumericIDgenerator (ArrayList<String> Fragments, String fragment_alpha, int fragment_digits){
		this.frags = Fragments;
		this.alpha = fragment_alpha;
		this.digits = fragment_digits;
	}
	/**
	 * @return new fragment
	 */
	public String generateNewID(){
		// ontology is empty
		if(frags.isEmpty()){
			return alpha + "_" + generate_final_number(1);	
		}
		
		// ontology has already entities with this type of ID
		else{
			// Get the highest number
			ArrayList<Integer> numbers = new ArrayList<Integer> ();

			// Decompose fragments into alphabet and number
			for(String frag : frags){
				int current_number = Integer.parseInt(frag.split("_")[1]);
				numbers.add(current_number);
			}
			
			// Sort the numbers array and obtain biggest
			Collections.sort(numbers);
			int new_number = (numbers.get(numbers.size()-1))+1;
			
			return alpha + "_" + generate_final_number(new_number);
		}
	}
	
	// Add zeros
	private static String generate_final_number (int generic_number){
		int number_length = (String.valueOf(generic_number)).length();
		String final_number = String.valueOf(generic_number);
		for(int i=0 ; i <= digits - number_length ;i++) {
			final_number = "0" + final_number;
		}
		return final_number;
	}
}
