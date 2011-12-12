package interpreter;

import generator.parser.LL1;
import generator.parser.LL1_Rule;
import generator.parser.LL1_Token;
import generator.parser.LL1_TokenType;
import generator.parser.Script_Lexer;
import generator.regex.DFA;
import generator.regex.NFA_Identifier;
import generator.regex.RecursiveDescent;
import global.InputBuffer;
import global.Token;

import java.text.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.Stack;


/**
 * Interpreter.java
 * a Mine-RE interpreter that contains all actions that
 * can be used in a Mini-RE script
 */
public class Interpreter {
	
	private ArrayList<Identifier> identifiers;
	private LL1 parser;
	
	/**
	 * setup interpreter with given scanner and given parser
	 * @param parser ll1 parser for syntactic analysis of given script
	 */
	public Interpreter(LL1 parser) {
		this.parser = parser;
		this.identifiers = new ArrayList<Identifier>();
	}
	
	//token types
	public static final String EOL = ";";
	public static final String BEGIN = "begin";
	public static final String END = "end";
	public static final String ID = "ID";
	public static final String PRINT = "print";
	public static final String FIND = "find";
	public static final String MAXFREQSTR = "maxfreqstring";
	public static final String REPLACE = "replace";
	public static final String RECURSIVEREPLACE = "recursivereplace";
	public static final String DIFF = "diff";
	public static final String INTERS = "inters";
	public static final String UNION = "union";
	public static final String REGEX = "REGEX";
	public static final String ASCII_STR = "ASCII-STR";
	public static final String LENGTH = "#";
	public static final String COMMA = ",";
	
	/**
	 * run the script
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void run(String filename) throws ParseException, IOException {
		//make script file lexer
		Script_Lexer lexer = new Script_Lexer(filename, parser.getTermList());
		//create token stack
		Stack<LL1_Token> code_stack = new Stack<LL1_Token>();
		//push end of file
		code_stack.push(new LL1_Token(new Token<LL1_TokenType>(LL1_TokenType.EOF, "EOF")));
		//push start symbol on stack
		code_stack.push(parser.getStartSymbol());
		//get token at top of stack
		LL1_Token current = code_stack.peek();
		//get next token in script
		Token<String> token = lexer.getNextToken();
		//loop until finish or error
		
		boolean length = false;
		boolean print = false;
		boolean find = false;
		boolean maxfreqstr = false;
		boolean replace = false;
		boolean recursivereplace = false;
		boolean assignment = false;
		boolean diff = false;
		boolean inters = false;
		boolean union = false;
		boolean new_line = true;
		
		boolean at_replacement = false;
		boolean at_file_in = false;
		boolean at_file_out = false;
		
		DFA curr_regex = null;
		String file_in = null;
		String file_out = null;
		String replacement = null;
		
		Stack<Identifier> temp_stk = new Stack<Identifier>();
		
		do {
			//if current is a terminal
			if(current.getToken().getType() == LL1_TokenType.TERMINAL) {
				//System.out.println("   TOKEN: type='" + token.getType() + "', val=" + token.getValue());
				//System.out.println("TERMINAL:  val='" + current.getToken().getValue() + "'");
				//System.out.println();
				
				if(current.getToken().getValue().equals("EPSILON")) {
					code_stack.pop();
				}
				//if current == token
				else if(current.getToken().getValue().equals(token.getType())) {
					//pop current off the stack
					code_stack.pop();
					//assignment statement
					if(token.getValue().equals(EOL)) {
						//System.out.print("EOL reached");
						if(assignment) {
							//System.out.print(", assigning value");
							Identifier t2 = temp_stk.pop();
							Identifier t1 = temp_stk.pop();
							t1.setValue(t2.getValue());
							assignment = false;
						}
						//System.out.print("\n");
						//reset all flags
						new_line = true;
					}
					else if(!token.getType().equals(BEGIN) && !token.getType().equals(END)) {
						//id found
						if(token.getType().equals(ID)) {
							//System.out.println("found an assignment");
							if(new_line) {
								assignment = true;
							}
							Identifier temp = new Identifier(token.getValue());
							boolean found = false;
							//find id in id list
							for(int i = 0; i < this.identifiers.size(); i++) {
								if(this.identifiers.get(i).getName().equals(token.getValue())) {
									found = true;
									temp = this.identifiers.get(i);
									break;
								}
							}
							//add if doesn't exist
							if(!found && assignment) {
								this.identifiers.add(temp);
							}
							//handle length operator
							if(length) {
								Identifier temp1 = temp;
								temp = new Identifier("ans");
								temp.setValue(((Collection<?>)temp1.getValue()).size());
								length = false;
							}
							//System.out.println("pushing id to the stack");
							
							temp_stk.push(temp);
							
							if(diff || inters || union) {
								ArrayList<InputString> l2 = (ArrayList<InputString>) temp_stk.pop().getValue();
								ArrayList<InputString> l1 = (ArrayList<InputString>) temp_stk.pop().getValue();
								ArrayList<InputString> result = null;
								if(diff) {
									result = this.diff(l1, l2);
									diff = false;
								}
								else if(inters) {
									result = this.inters(l1, l2);
									inters = false;
								}
								else {
									result = this.union(l1, l2);
									union = false;
								}
								temp = new Identifier("ans");
								temp.setValue(result);
								temp_stk.push(temp);
							}
							if(print) {
								//System.out.print(", printing value");
								System.out.println(temp_stk.pop());
								print = false;
							}
						}
						//operations
						//length
						else if(token.getType().equals(LENGTH)) {
							//System.out.println("found length");
							length = true;
						}
						//print
						else if(token.getType().equals(PRINT) || token.getType().equals(COMMA)) {
							//System.out.println("found print");
							print = true;
						}
						//find
						else if(token.getType().equals(FIND)) {
							//System.out.println("unary op find");
							find = true;
						}
						else if(token.getType().equals(MAXFREQSTR)) {
							//System.out.println("maxfreqstr = true");
							maxfreqstr = true;
						}
						//replace
						else if(token.getType().equals(REPLACE)) {
							//System.out.println("unary op replace");
							replace = true;
						}
						//recursivereplace
						else if(token.getType().equals(RECURSIVEREPLACE)) {
							//System.out.println("unary op recursivereplace");
							recursivereplace = true;
						}
						//difference
						else if(token.getType().equals(DIFF)) {
							//System.out.println("binary op diff");
							diff = true;
						}
						//intersect
						else if(token.getType().equals(INTERS)) {
							//System.out.println("binary op inters");
							inters = true;
						}
						//union
						else if(token.getType().equals(UNION)) {
							//System.out.println("binary op union");
							union = true;
						}
						//currently in find
						if(find) {
							if(token.getType().equals(REGEX)) {
								//System.out.println("find: building regex...");
								curr_regex = this.generateDFA(token.getValue());
								at_file_in = true;
							}
							else if(token.getType().equals(ASCII_STR) && at_file_in) {
								at_file_in = false;
								//System.out.println("find: found file, running find");
								file_in = token.getValue();
								Identifier new_id = new Identifier("ans");
								new_id.setValue(this.find(curr_regex, file_in));
								temp_stk.push(new_id);
								
								Identifier temp = null;
								if(diff || inters || union) {
									ArrayList<InputString> l2 = (ArrayList<InputString>) temp_stk.pop().getValue();
									ArrayList<InputString> l1 = (ArrayList<InputString>) temp_stk.pop().getValue();
									ArrayList<InputString> result = null;
									if(diff) {
										result = this.diff(l1, l2);
										diff = false;
									}
									else if(inters) {
										result = this.inters(l1, l2);
										inters = false;
									}
									else {
										result = this.union(l1, l2);
										union = false;
									}
									temp = new Identifier("ans");
									temp.setValue(result);
									temp_stk.push(temp);
								}
								find = false;
							}
						}
						if(replace || recursivereplace) {
							//get regex
							if(token.getType().equals(REGEX)) {
								//System.out.println("replace: building regex...");
								curr_regex = this.generateDFA(token.getValue());
								at_replacement = true;
							}
							//get replacement string
							else if(token.getType().equals(ASCII_STR)) {
								if(at_replacement) {
									replacement = token.getValue();
									at_replacement = false;
									at_file_in = true;
								}
								//get input file
								else if(at_file_in) {
									file_in = token.getValue();
									at_file_in = false;
									at_file_out = true;
								}
								//get output file + run function
								else if(at_file_out) {
									file_out = token.getValue();
									at_file_out = false;
									String replaced = new String();
									if(replace) {
										//System.out.println("normal replace");
										replaced = this.replace(curr_regex, replacement, file_in);
									}
									else {//recursive replace
										//System.out.println("recursive replace");
										replaced = this.recursivereplace(curr_regex, replacement, file_in);
									}
									this.writeFile(replaced, file_out);
									replace = false;
									recursivereplace = false;
								}
							}
						}
						if(maxfreqstr) {
							if(token.getType().equals(ID)) {
								Identifier t = temp_stk.pop();
								t.setValue(this.maxfreqstring(t));
								temp_stk.push(t);
								maxfreqstr = false;
							}
						}
						new_line = false;
					}
					//end some code
					
					
					token = lexer.getNextToken();
				}
				else {
					throw new ParseException("Script Parse ERROR: invalid terminal token: \'" + current.getToken().getValue() +
							"\', line: " + lexer.getLine() + ", pos: " + lexer.getPosition(), lexer.getPosition());
				}
			}
			else if(current.getToken().getType() == LL1_TokenType.NON_TERMINAL) {
				//System.out.println("NON_TERMINAL: val=" + current.getToken().getValue() + ", type=" + current.getToken().getType());
				//make sure production exists
				if(this.parser.getPt().hasProduction(current, token)) {
					//get production rule
					LL1_Rule rule = this.parser.getPt().getProduction(current, token);
					//remove current from stack
					code_stack.pop();
					//push on the stack (in reverse order)
					ArrayList<LL1_Token> rule_list = rule.getTNTList();
					for(int i = rule_list.size(); i > 0; i--) {
						code_stack.push(rule_list.get(i-1));
					}
				}
				else {
					throw new ParseException("Script Parse ERROR: invalid non-terminal token: \'" + current.getToken().getValue() +
							"\', line: " + lexer.getLine() + ", pos: " + lexer.getPosition(), lexer.getPosition());
				}
			}
			else {
				throw new ParseException("Script Parse ERROR: unrecognized token type: \'" + current.getToken().getValue() +
						"\', line: " + lexer.getLine() + ", pos: " + lexer.getPosition(), lexer.getPosition());
			}
			current = code_stack.peek();
		} while(current.getToken().getType() != LL1_TokenType.EOF);
		
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
		//generate the nfa
		nfa = dfa_generator.descend();
		//convert to dfa
		DFA dfa = new DFA(nfa.getNFA());
		return dfa;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws ParseException 
	 */
	private InputString maxfreqstring(Identifier id) throws ParseException {
		if(id.isList() && id.getValue() != null) {
			InputString result = null;
			for(Object value: ((Collection<?>)id.getValue())) {
				if(result == null) {
					result = (InputString)value;
				}
				else if(result.getNumOccurances() < ((InputString)value).getNumOccurances()) {
					result = (InputString)value;
				}
			}
			return result;
		}
		else {
			throw new ParseException("Script ERROR: id \'" + id.getName() + "\' is not of type List", 0);
		}
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input file to read and match with
	 * @return list of replaced strings
	 * @throws ParseException 
	 */
	private String replace(DFA regex, String replacement,String input_file) throws ParseException {
		Scanner in = null;
		try {
			in = new Scanner(new File(input_file));
		}
		catch(FileNotFoundException fnfe) {
			throw new ParseException("Script ERROR: file \"" + input_file + "\" does not exist", 0);
		}
		String input = in.nextLine();
		for(int i = 0; i < input.length(); i++) {
			regex.reset();
			int match_start = i;
			int match_end = -1;
			//look for a match
			for(int j = i; j < input.length(); j++) {
				regex.gotoNext(input.charAt(j));
				if(regex.atFinal()) {
					//save the current index
					match_end = j;
				}
				else if(regex.atDead()) {
					//go to another start index
					break;
				}
			}
			if(match_end != -1) {
				String new_input = new String();
				for(int k = 0; k < match_start; k++) {
					new_input += input.charAt(k);
				}
				for(int k = 0; k < replacement.length(); k++) {
					new_input += replacement.charAt(k);
				}
				for(int k = match_end+1; k < input.length(); k++) {
					new_input += input.charAt(k);
				}
				input = new_input;
				i = (match_end - match_start) - replacement.length();
			}
		}
		return input;
	}
	
	/**
	 * replace all regex matches with given replacement word in given files
	 * replaces until it cannot replace find anymore patterns
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input file to read and match with
	 * @return replaced file string
	 * @throws FileNotFoundException thrown if input file not found
	 */
	private String recursivereplace(DFA regex, String replacement, String input_file) throws ParseException {
		Scanner in = null;
		try {
			in = new Scanner(new File(input_file));
		}
		catch(FileNotFoundException fnfe) {
			throw new ParseException("Script ERROR: file \"" + input_file + "\" does not exist", 0);
		}
		String input = in.nextLine();
		return this.recursivereplace_help(regex,  replacement, input);
	}
	
	/**
	 * helper for recursive replace
	 * @param regex pattern to match
	 * @param replacement word to replace matches with
	 * @param input file to read and match with
	 * @return replaced file
	 */
	private String recursivereplace_help(DFA regex, String replacement, String input) {
		for(int i = 0; i < input.length(); i++) {
			regex.reset();
			int match_start = i;
			int match_end = -1;
			//look for a match
			for(int j = i; j < input.length(); j++) {
				regex.gotoNext(input.charAt(j));
				if(regex.atFinal()) {
					//save the current index
					match_end = j;
				}
				else if(regex.atDead()) {
					//go to another start index
					break;
				}
			}
			if(match_end != -1) {
				String new_input = new String();
				for(int k = 0; k < match_start; k++) {
					new_input += input.charAt(k);
				}
				for(int k = 0; k < replacement.length(); k++) {
					new_input += replacement.charAt(k);
				}
				for(int k = match_end+1; k < input.length(); k++) {
					new_input += input.charAt(k);
				}
				input = new_input;
				return this.recursivereplace_help(regex, replacement, input);
			}
		}
		return input;
	}
	
	/**
	 * finds all regex matches in a given file
	 * @param regex pattern to match
	 * @param file input to check for matched
	 * @return the list of all matching words
	 */
	private ArrayList<InputString> find(DFA regex, String file) throws ParseException {
		ArrayList<InputString> result = new ArrayList<InputString>();
		InputBuffer file_reader = null;
		try {
			file_reader = new InputBuffer(file);
		}
		catch(FileNotFoundException fnfe) {
			throw new ParseException("Script ERROR: file \"" + file + "\" does not exist", 0);
		}
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
						//System.out.println("duplicate");
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
	private void writeFile(String value, String filename) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(value);
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
}
