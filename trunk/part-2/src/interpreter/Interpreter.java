package interpreter;

import java.io.File;
import java.util.ArrayList;
import scanner_generator.DFA;
import parser_generator.LL1;

/**
 * Interpreter.java
 * a Mine-RE interpreter that contains all actions that
 * can be used in a Mini-RE script
 */
public class Interpreter {
	
	private ArrayList<Identifier> identifiers;
	private DFA scanner;
	private LL1 parser;
	private File script;
	
	/**
	 * setup interpreter with given scanner and given parser
	 * @param scanner dfa for lexical analysis of given script
	 * @param parser ll1 parser for syntactic analysis of given script
	 * @param script the script for this interpreter instance to run
	 */
	public Interpreter(DFA scanner, LL1 parser, File script) {
		this.scanner = scanner;
		this.parser = parser;
		this.script = script;
		this.identifiers = new ArrayList<Identifier>();
	}
	
	/**
	 * assign a given identifier a given number value
	 * @param id identifier to assign a value to
	 * @param value number to assign to this identifier
	 * @return the new identifier with assigned value
	 */
	private NumIdentifier assign(Identifier id, int value) {
		return new NumIdentifier(id, value);
	}
	
	/**
	 * assign a given identifier a given list value
	 * @param id identifier to assign a value to
	 * @param value list to assign to this identifier
	 * @return the new identifier with assigned value
	 */
	private ListIdentifier assign(Identifier id, ArrayList<InputString> value) {
		return new ListIdentifier(id, value);
	}
	
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
	 * interpreter actions
	 */
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input_file file to read and match with
	 * @param output_file file to output results to
	 */
	private void replace(DFA regex, String replacement, File input_file, File output_file) {
		//TODO 
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * replaces until it cannot replace find anymore patterns
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input_file file to read and match with
	 * @param output_file file to output results to
	 */
	private void recursivereplace(DFA regex, String replacement, File input_file, File output_file) {
		//TODO
	}
	
	/**
	 * finds all regex matches in a given file
	 * @param regex pattern to match
	 * @param file input to check for matched
	 * @return the list of all matching words
	 */
	private ArrayList<InputString> find(DFA regex, File file) {
		ArrayList<InputString> result = new ArrayList<InputString>();
		//TODO open the file
		//TODO read through the file, if a word matches the pattern, add it to result
		return result;
	}
	
	/**
	 * adds two lists together
	 * @param list1 list to add to
	 * @param list2 list to add from
	 * @return list containing the union of both given lists
	 */
	private ArrayList<InputString> union(ArrayList<InputString> list1, ArrayList<InputString> list2) {
		ArrayList<InputString> result = new ArrayList<InputString>();
		//clone list1 (so we don't change it)
		for(int i = 0; i < list1.size(); i++) {
			result.add(list1.get(i).clone());
		}
		for(int i = 0; i < list2.size(); i++) {
			for(int j = 0; j < result.size(); i++) {
				//if value is already in results, add the metadata
				if(result.get(j).equals(list2.get(i))) {
					result.get(j).addMetadata(list2.get(i).getMetadata());
				}
				//otherwise, add a new item
				else {
					result.add(list2.get(i).clone());
				}
			}
		}
		return result;
	}
	
	/**
	 * removes values from list1 that are also in list2
	 * @param list1 list to remove values from
	 * @param list2 list to check for contained values
	 * @return list containing only value from list1 that are not in list2
	 */
	private ArrayList<InputString> diff(ArrayList<InputString> list1, ArrayList<InputString> list2) {
		ArrayList<InputString> result = new ArrayList<InputString>();
		for(int i = 0; i < list1.size(); i++) {
			boolean contains = false;
			for(int j = 0; j < list2.size(); j++) {
				//found in both lists, shouldn't add
				if(list1.get(1).equals(list2.get(j))) {
					contains = true;
				}
			}
			if(!contains) {
				result.add(list1.get(i).clone());
			}
		}
		
		return result;
	}
	
	/**
	 * finds values contained in both lists
	 * @param list1 list to search
	 * @param list2 list to search
	 * @return new list containing only value that appeared in both lists
	 */
	private ArrayList<InputString> inters(ArrayList<InputString> list1, ArrayList<InputString> list2) {
		ArrayList<InputString> result = new ArrayList<InputString>();
		for(int i = 0; i < list1.size(); i++) {
			for(int j = 0; j < list2.size(); j++) {
				//if value is in both lists, concatenate their metadata and add them to the new list
				if(list1.get(i).equals(list2.get(j))) {
					InputString temp = list1.get(i).clone();
					temp.addMetadata(list2.get(j).getMetadata());
					result.add(temp);
				}
			}
		}
		return result;
	}
	
	/**
	 * finds the length of the given list
	 * @param list list to get length of
	 * @return size of the list
	 */
	private int length(ArrayList<InputString> list) {
		return list.size();
	}
	
	/**
	 * prints a given number
	 * @param number value to print
	 * @return string representation of the value
	 */
	private String print(int number) {
		return ((Integer)number).toString();
	}
	
	/**
	 * prints a given list
	 * @param list value to print
	 * @return string representation of the value
	 */
	private String print(ArrayList<InputString> list) {
		return list.toString();
	}
}
