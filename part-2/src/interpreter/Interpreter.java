package interpreter;

import generator.parser.LL1;
import generator.regex.DFA;
import global.InputBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


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
	
	private String replace(DFA regex, String original, String replacement) {
		regex.reset();
		for(int i = 0; i < original.length(); i++) {
			int match_start, match_end;
			match_start = i;
			match_end = -1;
			//look for a match
			for(int j = i; j < original.length(); j++) {
				regex.gotoNext(original.charAt(j));
				if(regex.atFinal()) {
					//save the current index
					match_end = j;
				}
				//if at a dead state
				else if(regex.atDead()) {
					//go to another start index
					break;
				}
			}
			//a match was found
			if(match_end != -1) {
				String result = new String();
				//replace the substring
				for(int k = 0; k < match_start; k++) {
					result += original.charAt(k);
				}
				for(int k = 0; k < replacement.length(); k++) {
					result += replacement.charAt(k);
				}
				for(int k = match_end; k < original.length(); k++) {
					result += original.charAt(k);
				}
				return result;
			}
		}
		//nothing was replaced
		return original;
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input file to read and match with
	 * @return list of replaced strings
	 */
	private ArrayList<String> replace(DFA regex, String replacement, ArrayList<InputString> input) {
		ArrayList<String> output = new ArrayList<String>();
		for(int i = 0; i < input.size(); i++) {
			output.add(replace(regex, input.get(i).getString(), replacement));
		}
		return output;
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * replaces until it cannot replace find anymore patterns
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input file to read and match with
	 * @return list of replaced strings
	 */
	private ArrayList<String> recursivereplace(DFA regex, String replacement, ArrayList<InputString> input) {
		ArrayList<String> output = new ArrayList<String>();
		String result, last_result;
		for(int i = 0; i < input.size(); i++) {
			result = input.get(i).getString();
			do {
				last_result = new String(result);
				result = replace(regex, result, replacement);
			}while(input.get(i).getString() != result);
			
			output.add(result);
		}
		return output;
	}
	
	/**
	 * finds all regex matches in a given file
	 * @param regex pattern to match
	 * @param file input to check for matched
	 * @return the list of all matching words
	 */
	private ArrayList<InputString> find(DFA regex, String file) throws FileNotFoundException {
		ArrayList<InputString> result = new ArrayList<InputString>();
		InputBuffer file_reader = new InputBuffer(new Scanner(new File(file)));
		//convert the file to a string
		String file_buffer = new String();
		while(file_reader.peekNext() != '\n') {
			file_buffer += file_reader.getNext();
		}
		//regex match the file
		regex.reset();
		for(int i = 0; i < file_buffer.length(); i++) {
			int match_start, match_end;
			match_start = i;
			match_end = -1;
			//look for matches starting at current index until the end of the file
			for(int j = i; j < file_buffer.length(); j++) {
				regex.gotoNext(file_buffer.charAt(j));
				//if we've found a match
				if(regex.atFinal()) {
					//log the match
					match_end = j;
					//keep going, want LONGEST match
				}
				//if we're deadlocked
				else if(regex.atDead()) {
					//stop looking for a match
					break;
				}
			}
			//if a match was found
			if(match_end != -1) {
				//move current position to the end of the match
				i += match_end - match_start;
				//create the match (w/ metadata)
				InputString match = new InputString(file_buffer.substring(match_start, match_end));
				ArrayList<Integer> positions = new ArrayList<Integer>();
				positions.add(match_start);
				StringFileData metadata = new StringFileData(file, positions);
				match.addMetadata(metadata);
				//add the match to the results list (no duplicates)
				boolean added = false;
				for(int k = 0; k < result.size(); k++) {
					//if there's already a match
					if(result.get(k).equals(match)) {
						//update the metadata
						result.get(k).addMetadata(match.getMetadata());
						added = true;
					}
				}
				//if it doesn't exist
				if(!added) {
					//add a new element
					result.add(match);
				}
			}
		}
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
		
		System.out.println("size: " + result.size());
		
		for(int i = 0; i < list2.size(); i++) {
			int pos = -1;
			
			for(int j = 0; j < result.size(); j++) {
				if(result.get(j).equals(list2.get(i))) {
					pos = j;
					break;
				}
			}
			if(pos == -1) {
				result.add(list2.get(i).clone());
			}
			else {
				result.get(pos).addMetadata(list2.get(i).getMetadata());
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
				if(list1.get(i).equals(list2.get(j))) {
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
	 * @param id identifier to print
	 * @return string representation of the value
	 */
	private String print(NumIdentifier id) {
		return ((Integer)id.getValue()).toString();
	}
	
	/**
	 * prints a given list
	 * @param id identifier to print
	 * @return string representation of the value
	 */
	private String print(ListIdentifier id) {
		return id.getValue().toString();
	}
	
	public static void main(String[] args) {
		//lists
		ArrayList<InputString> t1 = new ArrayList<InputString>();
		ArrayList<InputString> t2 = new ArrayList<InputString>();
		
		//input strings
		InputString s1 = new InputString("1");
		InputString s2 = new InputString("2");
		InputString s3 = s1.clone();
		
		//files
		String f1 = new String("f1.txt");
		String f2 = new String("f2.txt");
		
		//positions
		ArrayList<Integer> p1 = new ArrayList<Integer>();
		p1.add(2);
		p1.add(4);
		ArrayList<Integer> p2 = new ArrayList<Integer>();
		p2.add(1);
		ArrayList<Integer> p3 = new ArrayList<Integer>();
		p3.add(30);
		
		//metadata
		StringFileData sfd1 = new StringFileData(f1, p1);
		StringFileData sfd2 = new StringFileData(f1, p2);
		StringFileData sfd3 = new StringFileData(f2, p3);
		
		//associate metadata
		s1.addMetadata(sfd1);
		s2.addMetadata(sfd2);
		s3.addMetadata(sfd3);
		
		System.out.println("s1: " + s1.toString());
		System.out.println("s2: " + s2.toString());
		System.out.println("s3: " + s3.toString());
		
		InputString s4 = s1.clone();
		s4.addMetadata(s3.getMetadata());
		s4.addMetadata(s2.getMetadata());
		System.out.println("s4: " + s4.toString());
		s4 = null;
		
		//try list operations
		t1.add(s1);
		t1.add(s2);
		t2.add(s3);
		
		System.out.println("t1: ");
		for(int i = 0; i < t1.size(); i++) {
			System.out.println("\t" + t1.get(i).toString());
		}
		System.out.println("t2: ");
		for(int i = 0; i < t2.size(); i++) {
			System.out.println("\t" + t2.get(i).toString());
		}
		
		//generate empty Interpreter
		Interpreter interpreter = new Interpreter(null, null, null);
		
		//test UNION
		System.out.println("\nunion test: ");
		ArrayList<InputString> union_test = interpreter.union(t1, t2);
		for(int i = 0; i < union_test.size(); i++) {
			System.out.println("\t" + union_test.get(i).toString());
		}
		//make sure other lists are unchanged
		System.out.println("t1: ");
		for(int i = 0; i < t1.size(); i++) {
			System.out.println("\t" + t1.get(i).toString());
		}
		System.out.println("t2: ");
		for(int i = 0; i < t2.size(); i++) {
			System.out.println("\t" + t2.get(i).toString());
		}
		
		//test diff
		System.out.println("\ndiff test");
		ArrayList<InputString> diff_test = interpreter.diff(t1, t2);
		for(int i = 0; i < diff_test.size(); i++) {
			System.out.println("\t" + diff_test.get(i).toString());
		}
		//make sure other lists are unchanged
		System.out.println("t1: ");
		for(int i = 0; i < t1.size(); i++) {
			System.out.println("\t" + t1.get(i).toString());
		}
		System.out.println("t2: ");
		for(int i = 0; i < t2.size(); i++) {
			System.out.println("\t" + t2.get(i).toString());
		}
		
		//test inters
		System.out.println("\ninters test");
		ArrayList<InputString> inters_test = interpreter.inters(t1, t2);
		for(int i = 0; i < inters_test.size(); i++) {
			System.out.println("\t" + inters_test.get(i).toString());
		}
		//make sure other lists are unchanged
		System.out.println("t1: ");
		for(int i = 0; i < t1.size(); i++) {
			System.out.println("\t" + t1.get(i).toString());
		}
		System.out.println("t2: ");
		for(int i = 0; i < t2.size(); i++) {
			System.out.println("\t" + t2.get(i).toString());
		}
	}
}
