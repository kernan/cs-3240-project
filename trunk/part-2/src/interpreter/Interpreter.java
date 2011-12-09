package interpreter;

import generator.parser.LL1;
import generator.regex.DFA;
import generator.regex.NFA_Identifier;
import generator.regex.RecursiveDescent;
import global.InputBuffer;

import java.text.ParseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Interpreter.java
 * a Mine-RE interpreter that contains all actions that
 * can be used in a Mini-RE script
 */
public class Interpreter {
	
	private ArrayList<Identifier> identifiers;
	private ArrayList<DFA> terminals;
	private LL1 parser;
	private File script;
	
	/**
	 * setup interpreter with given scanner and given parser
	 * @param scanner dfa for lexical analysis of given script
	 * @param parser ll1 parser for syntactic analysis of given script
	 * @param script the script for this interpreter instance to run
	 */
	public Interpreter(ArrayList<DFA> terminals, LL1 parser, File script) {
		this.terminals = terminals;
		this.parser = parser;
		this.script = script;
		this.identifiers = new ArrayList<Identifier>();
	}
	
	/**
	 * run the script
	 */
	public void run() {
		
	}
	
	/*
	 * assignments for identifiers 
	 */
	
	/**
	 * assign a given identifier a given list value
	 * @param id identifier to assign a value to
	 * @param value value to assign to the id
	 * @return the new identifier with assigned value
	 */
	private Identifier assign(Identifier id, Object value) {
		id.setValue(value);
		return id;
	}
	
	/*
	 * regular expression generation
	 */
	
	/**
	 * generate a DFA from a given regex string
	 * @param regex regular expression to generate from
	 * @return dfa that represents the same language as the regex
	 * @throws ParseException thrown by RecursiveDescent.descend
	 */
	private DFA generateDFA(String regex) throws ParseException {
		//init the nfa generator
		RecursiveDescent dfa_generator = new RecursiveDescent(regex, new ArrayList<NFA_Identifier>());
		NFA_Identifier nfa;
		try {
			//generate the nfa
			nfa = dfa_generator.descend();
		}
		catch(ParseException pe) {
			//TODO something meaningful with the ParseException
			throw pe;
		}
		//convert to dfa
		DFA dfa = new DFA(nfa.getNFA());
		return dfa;
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
	 * find first match and replace that substring
	 * @param regex regular expression to match
	 * @param original string to match expression to
	 * @param replacement substring replacement
	 * @return new string with replaced substring
	 */
	private String replace(DFA regex, String original, String replacement) {
		for(int i = 0; i < original.length(); i++) {
			regex.reset();
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
				for(int k = match_end+1; k < original.length(); k++) {
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
			}while(!last_result.equals(result));
			
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
		InputBuffer file_reader = new InputBuffer(file);
		//convert the file to a string
		String file_buffer = new String();
		while(file_reader.peekNext() != '\n') {
			file_buffer += file_reader.getNext();
		}
		//regex match the file
		for(int i = 0; i < file_buffer.length(); i++) {
			regex.reset();
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
				InputString match = new InputString(file_buffer.substring(match_start, match_end+1));
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
	 * write given list to the given file
	 * @param list collection to write to a file
	 * @param filename file to write to
	 */
	private void writeFile(ArrayList<String> list, String filename) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		for(int i = 0; i < list.size(); i++) {
			out.write(list.get(i));
			out.write(" ");
		}
		out.close();
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
	
	public static void main(String[] args) {
		
		//Options.DEBUG = true;
		
		/*
		 * List operations
		 */
		System.out.println();
		System.out.println("LIST OPERATIONS:");
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
		
		/*
		 * DFA
		 */
		System.out.println();
		System.out.println("DFA");
		DFA testdfa = null;
		String testregex = "([^a-c] IN [a-z])*";
		//String testregex = "([a-z])*";
		System.out.println("regex = " + testregex);
		try {
			testdfa = interpreter.generateDFA(testregex);
		}
		catch(ParseException pe) {
			System.out.println("test dfa generation failed...");
			System.out.println(pe.getMessage());
			return;
		}
		String testdfa_in = "defg";
		System.out.println("string = " + testdfa_in);
		for(int i = 0; i < testdfa_in.length(); i++) {
			testdfa.gotoNext(testdfa_in.charAt(i));
			if(testdfa.atDead()) {
				break;
			}
		}
		System.out.println("dead = " + testdfa.atDead() + ", accept = " + testdfa.atFinal());
		
		System.out.println();
		testregex = "ads([a-zA-Z])*";
		//String testregex = "([a-z])*";
		System.out.println("regex = " + testregex);
		try {
			testdfa = interpreter.generateDFA(testregex);
		}
		catch(ParseException pe) {
			System.out.println("test dfa generation failed...");
			System.out.println(pe.getMessage());
			return;
		}
		testdfa_in = "adsfakjBASDajefsjdakljekjtDSJAFJDKAE";
		System.out.println("string = " + testdfa_in);
		for(int i = 0; i < testdfa_in.length(); i++) {
			testdfa.gotoNext(testdfa_in.charAt(i));
			if(testdfa.atDead()) {
				break;
			}
		}
		System.out.println("dead = " + testdfa.atDead() + ", accept = " + testdfa.atFinal());
		
		System.out.println();
		testregex = "abc([a-zA-Z])*";
		//String testregex = "([a-z])*";
		System.out.println("regex = " + testregex);
		try {
			testdfa = interpreter.generateDFA(testregex);
		}
		catch(ParseException pe) {
			System.out.println("test dfa generation failed...");
			System.out.println(pe.getMessage());
			return;
		}
		testdfa_in = "abcZabc";
		System.out.println("string = " + testdfa_in);
		for(int i = 0; i < testdfa_in.length(); i++) {
			testdfa.gotoNext(testdfa_in.charAt(i));
			if(testdfa.atDead()) {
				break;
			}
		}
		System.out.println("dead = " + testdfa.atDead() + ", accept = " + testdfa.atFinal());
		
		/*
		 * Replace
		 */
		System.out.println();
		System.out.println("REPLACE:");
		DFA d1 = null;
		try {
			d1 = interpreter.generateDFA("abc");
		}
		catch(ParseException pe) {
			System.out.println("dfa 1 generation failed...");
		}
		String o1 = "ZabcZabc";
		System.out.println();
		System.out.println("original: " + o1);
		System.out.println("replacing \'abc\' with \'ABC\'");
		String r1 = interpreter.replace(d1, o1, "ABC");
		System.out.println("replaced: " + r1);
		System.out.println("original: " + o1);
		System.out.println();
		System.out.println("original: " + r1);
		System.out.println("replacing \'abc\' with \'ABC\'");
		String r2 = interpreter.replace(d1, r1, "ABC");
		System.out.println("replaced: " + r2);
		System.out.println("original: " + r1);
		
		DFA d2 = null;
		try {
			d2 = interpreter.generateDFA("abc([A-Za-z])*");
		}
		catch(ParseException pe) {
			System.out.println("dfa 2 generation failed...");
		}
		System.out.println();
		System.out.println("original: " + o1);
		System.out.println("replacing \'abc([A-Za-z])*\' with \'replaced\'");
		String r3 = interpreter.replace(d2, o1, "replaced");
		System.out.println("replaced: " + r3);
		System.out.println("original: " + o1);
		
		/*
		 * Find
		 */
		System.out.println();
		System.out.println("FIND:");
		DFA d3 = null;
		try {
			d3 = interpreter.generateDFA("abc([A-Za-z])*");
		}
		catch(ParseException pe) {
			System.out.println("dfa 3 generation failed...");
		}
		ArrayList<InputString> fd1 = null;
		try {
			fd1 = interpreter.find(d3, "test.txt");
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("file test.txt not found");
		}
		System.out.println("f1");
		for(int i = 0; i < fd1.size(); i++) {
			System.out.println("\t" + fd1.get(i).toString());
		}
		
		System.out.println();
		DFA d4 = null;
		try {
			d4 = interpreter.generateDFA("([A-Za-z])*");
		}
		catch(ParseException pe) {
			System.out.println("dfa 4 generation failed...");
		}
		ArrayList<InputString> fd2 = null;
		try {
			fd2 = interpreter.find(d4, "test.txt");
		}
		catch(FileNotFoundException fnfe) {
			System.out.println("file test.txt not found");
		}
		System.out.println("f2");
		for(int i = 0; i < fd2.size(); i++) {
			System.out.println("\t" + fd2.get(i).toString());
		}
		
		/*
		 * REPLACE
		 */
		System.out.println("\nREPLACE:");
		
		DFA rd1 = null;
		try {
			rd1 = interpreter.generateDFA("([A-Za-z])*");
		}
		catch(ParseException pe) {
			System.out.println("dfa replace 1 generation failed...");
		}
		ArrayList<String> replace1 = interpreter.replace(rd1, "TEST", fd1);
		System.out.println("original");
		for(int i = 0; i < fd1.size(); i++) {
			System.out.println("\t" + fd1.get(i).toString());
		}
		System.out.println("rd1");
		for(int i = 0; i < replace1.size(); i++) {
			System.out.println("\t" + replace1.get(i).toString());
		}
		
		System.out.println();
		DFA rd2 = null;
		try {
			rd2 = interpreter.generateDFA("abc");
		}
		catch(ParseException pe) {
			System.out.println("dfa replace 2 generation failed...");
		}
		ArrayList<String> replace2 = interpreter.replace(rd2, "TEST", fd2);
		System.out.println("original");
		for(int i = 0; i < fd2.size(); i++) {
			System.out.println("\t" + fd2.get(i).toString());
		}
		System.out.println("rd2");
		for(int i = 0; i < replace2.size(); i++) {
			System.out.println("\t" + replace2.get(i).toString());
		}
		
		System.out.println("\nRECURSIVEREPLACE:");
		
		System.out.println();
		DFA rrd1 = null;
		try {
			rrd1 = interpreter.generateDFA("abc");
		}
		catch(ParseException pe) {
			System.out.println("dfa recursive replace 1 generation failed...");
		}
		ArrayList<String> rr1 = interpreter.recursivereplace(rrd1, "TEST", fd2);
		System.out.println("original");
		for(int i = 0; i < fd2.size(); i++) {
			System.out.println("\t" + fd2.get(i).toString());
		}
		System.out.println("rr1");
		for(int i = 0; i < rr1.size(); i++) {
			System.out.println("\t" + rr1.get(i).toString());
		}
		
		System.out.println();
		DFA rrd2 = null;
		try {
			rrd2 = interpreter.generateDFA("([a-zA-Z])*");
		}
		catch(ParseException pe) {
			System.out.println("dfa resursive replace 2 generation failed...");
		}
		ArrayList<String> rr2 = interpreter.recursivereplace(rrd2, "herp", fd2);
		System.out.println("original");
		for(int i = 0; i < fd2.size(); i++) {
			System.out.println("\t" + fd2.get(i).toString());
		}
		System.out.println("rrd2");
		for(int i = 0; i < rr2.size(); i++) {
			System.out.println("\t" + rr2.get(i).toString());
		}
		
		/*
		 * IDENTIFIER
		 */
		System.out.println("\nIDENTIFIER:");
		System.out.println("assign");
		Identifier id1, id2, id3;
		//empty identifiers
		id1 = new Identifier("id1");
		id2 = new Identifier("id2");
		id3 = new Identifier("id3");
		
		id2 = interpreter.assign(id2, fd1);
		//id3 = interpreter.assign(id3, 20);
		
		System.out.println(id1);
		System.out.println(id2);
		System.out.println(id3);
		
		//reassigning identifiers
		System.out.println("\nreassign");
		id1 = interpreter.assign(id1,  id3.getValue());
		id2 = interpreter.assign(id3, fd2);
		
		System.out.println(id1);
		System.out.println(id2);
		System.out.println(id3);
		
		/*
		 * FILE WRITE
		 */
		System.out.println("\nfile write:");
		ArrayList<String> wf = new ArrayList<String>();
		wf.add("bob");
		wf.add("bob");
		wf.add("sue");
		wf.add("nick");
		wf.add("sue");
		try {
			System.out.println("writing file...");
			interpreter.writeFile(wf, "writetest.txt");
		}
		catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
		System.out.println("\nend tests");
	}
}
