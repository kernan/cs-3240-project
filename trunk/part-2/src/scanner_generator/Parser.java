package scanner_generator;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import specification_scanner.Lexer;
import specification_scanner.Token;
import specification_scanner.TokenType;

/**
 * Parser.java
 * Parses a given grammar specification and builds a state machine for scanning
 * input code from it. Scans the input code and outputs tokens.
 */
public class Parser {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
		//set debug mode...
		Options.DEBUG = true;
		
		System.out.println("============================================\n");
		System.out.println("     [Parser] GRAMMAR PARSE: START");
		System.out.println();
		
		//tokenizer
		Lexer input = new Lexer(new Scanner(new File(args[0])));
		//list of all <rexp> (as nfas)
		ArrayList<NFA_Identifier> nfa_list = new ArrayList<NFA_Identifier>();
		
		do {
			Token id = input.getNextToken();
			if(id.getType() != TokenType.EOL) {
				System.out.println("     [Parser] building identifier: " + id.getValue() + "...");
				if(id.getType() != TokenType.DEFINED) {
					throw new ParseException("Invalid syntax, new lines must begin with an id definition", -1);
				}
				RecursiveDescent rd = new RecursiveDescent(input, nfa_list);
				NFA_Identifier new_nfa = rd.descend();
				new_nfa.setName(id.getValue());
				nfa_list.add(new_nfa);
			}
		} while(input.gotoNextLine());
		
		System.out.println();
		System.out.println("     [Parser] GRAMMAR PARSE: DONE");
		System.out.println("     [Parser] NFA MERGE: START");
		System.out.println();
		
		NFA big_nfa = new NFA();
		for(int i = 0; i < nfa_list.size(); i++) {
			System.out.print("     [Parser] " + nfa_list.get(i).getName() + ": ");
			if(!nfa_list.get(i).getCharClass()) {
				System.out.println("token, merging...");
				big_nfa.merge(nfa_list.get(i).getNFA());
			}
			else {
				System.out.println("char class");
			}
		}
		big_nfa.finalize();
		
		//System.out.print(big_nfa.toString());
		
		System.out.println();
		System.out.println("     [Parser] NFA MERGE: DONE");
		System.out.println("     [Parser] NFA CONVERSION: START");
		System.out.println();
		
		DFA dfa = new DFA(big_nfa);
		
		//System.out.println(dfa.toString());
		
		System.out.println();
		System.out.println("     [Parser] NFA CONVERSION: DONE");
		System.out.println("     [Parser] CODE SCANNING: START");
		System.out.println();
		
		for(int i = 1; i < args.length; i++) {
			String file = args[i];
			
			System.out.println("     [Parser] " + file + ": ");
			
			boolean pass= true;
			boolean file_pass = true;
			
			Scanner scan = new Scanner(new File(args[i]));
			while(scan.hasNext()) {
				String t = scan.next();
				for(int j = 0; j < t.length(); j++) {
					dfa.gotoNext(t.charAt(j));
				}
				pass = dfa.atFinal();
				System.out.println("     [Parser] [" + file + "] Token: \"" + t + "\", " + pass);
				dfa.reset();
				if(!pass) {
					file_pass = false;
				}
			}
			
			if(file_pass) {
				System.out.println("     [Parser] [" + file + "] PASS");
			}
			else {
				System.out.println("     [Parser] [" + file + "] FAIL");
			}
		}
		
		System.out.println();
		System.out.println("     [Parser] CODE SCANNING: DONE");
	}
	
	/*
	 * ASCII printable characters (the alphabet)
	 */
	 public static final char[] ascii = {
             ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', 
             '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 
             'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', 
             '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 
             'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'
     };

}
