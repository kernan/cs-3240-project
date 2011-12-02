package interpreter;

import java.io.File;
import java.util.ArrayList;
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
	 * replace all regex matches with given replacement word in given files
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input_file file to read and match with
	 * @param output_file file to output results to
	 */
	public void replace(DFA regex, String replacement, File input_file, File output_file) {
		
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * replaces until it cannot replace find anymore patterns
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input_file file to read and match with
	 * @param output_file file to output results to
	 */
	public void recursivereplace(DFA regex, String replacement, File input_file, File output_file) {
		
	}
	
	/**
	 * finds all regex matches in a given file
	 * @param regex pattern to match
	 * @param file input to check for matched
	 * @return the list of all matching words
	 */
	public ArrayList find(DFA regex, File file) {
		ArrayList result = new ArrayList();
		//TODO
		return result;
	}
	
	/**
	 * adds two lists together
	 * @param list1 list to add to
	 * @param list2 list to add from
	 * @return list containing the union of both given lists
	 */
	public ArrayList union(ArrayList list1, ArrayList list2) {
		ArrayList result = new ArrayList();
		//TODO
		return result;
	}
	
	/**
	 * removes values from list1 that are also in list2
	 * @param list1 list to remove values from
	 * @param list2 list to check for contained values
	 * @return list containing only value from list1 that are not in list2
	 */
	public ArrayList diff(ArrayList list1, ArrayList list2) {
		ArrayList result = new ArrayList();
		//TODO
		return result;
	}
	
	/**
	 * finds values contained in both lists
	 * @param list1 list to search
	 * @param list2 list to search
	 * @return new list containing only value that appeared in both lists
	 */
	public ArrayList inters(ArrayList list1, ArrayList list2) {
		ArrayList result = new ArrayList();
		//TODO
		return result;
	}
	
	/**
	 * finds the length of the given list
	 * @param list list to get length of
	 * @return size of the list
	 */
	public int length(ArrayList list) {
		return list.size();
	}
	
	/**
	 * prints a given number
	 * @param number value to print
	 * @return string representation of the value
	 */
	public String print(int number) {
		return ((Integer)number).toString();
	}
	
	/**
	 * prints a given list
	 * @param list value to print
	 * @return string representation of the value
	 */
	public String print(ArrayList list) {
		return list.toString();
	}
}
