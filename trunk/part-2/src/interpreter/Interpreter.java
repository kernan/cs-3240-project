package interpreter;

import java.util.List;
import scanner_generator.DFA;

/**
 * 
 *
 */

public class Interpreter {
	/*
	 * Interpreter functions:
	 *    begin
	 *      specifies the beginning of a script
	 *      
	 *    end
	 *      specifies the end of a script
	 *      
	 *    replace
	 *      finds a string that matches the given regular expression
	 *        found in an input file and replaces it with a given 
	 *        string literal and outputs the replacement to a given
	 *        output file
	 *      
	 *      replace(regex, str, input_file, output_file)
	 *      
	 *    recursivereplace
	 *      finds a string that matches the given regular expression
	 *        found in an input file and replaces it with a given 
	 *        string literal and outputs the replacement to a given
	 *        output file
	 *      continues scanning a word until all matches have been replaced
	 *      
	 *      recursivereplace(regex, str, input_file, output_file)
	 *      
	 *    find
	 *      finds all matches of regex pattern in given file
	 *      
	 *      list find(regex, file)
	 *      
	 *    union
	 *      finds the union of two lists
	 *      adds 2 lists together, without duplicates
	 *      
	 *      list union(list1, list2)
	 *      
	 *    inters
	 *      finds the intersection of two lists
	 *      finds strings that are in both lists
	 *      
	 *      list inters(list1, list2)
	 *      
	 *    diff
	 *      removes strings that are in two given lists simultaneously
	 *      set subtraction
	 *      
	 *      list diff(list1, list2)
	 *      
	 *    print
	 *      prints given expression (may be a number or a list)
	 *      
	 *      print(number)
	 *      
	 *      print(list)
	 *      
	 *    #
	 *      returns length of a list
	 *      
	 *      length number(list)
	 * 
	 * ID Types:
	 *    integer
	 *    string (+ locations)
	 *    string (+ locations) list
	 */
	
	/**
	 * 
	 * @param regex
	 * @param replacement
	 * @param input_file
	 * @param output_file
	 */
	public void replace(DFA regex, String replacement, String input_file, String output_file) {
		
	}
	
	/**
	 * 
	 * @param regex
	 * @param replacement
	 * @param input_file
	 * @param output_file
	 */
	public void recursivereplace(DFA regex, String replacement, String input_file, String output_file) {
		
	}
	
	/**
	 * 
	 * @param regex
	 * @param word
	 * @return
	 */
	private boolean replace(DFA regex, String word) {
		return false;
	}
	
	/**
	 * 
	 * @param regex
	 * @param file
	 * @return
	 */
	public List find(DFA regex, String file) {
		return null;
	}
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public List union(List list1, List list2) {
		return null;
	}
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public List diff(List list1, List list2) {
		return null;
	}
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public List inters(List list1, List list2) {
		return null;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public int length(List list) {
		return list.size();
	}
	
	/**
	 * 
	 * @param number
	 * @return
	 */
	public String print(int number) {
		return ((Integer)number).toString();
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public String print(List list) {
		return list.toString();
	}
}
