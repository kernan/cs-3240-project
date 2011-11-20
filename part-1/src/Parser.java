
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 *
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
				System.out.println("     [Parser] got ID: " + id.getValue());
				if(id.getType() != TokenType.DEFINED) {
					throw new ParseException("Invalid syntax, new lines must begin with an id definition", -1);
				}
				RecursiveDescent rd = new RecursiveDescent(input, nfa_list);
				NFA_Identifier new_nfa = rd.descend();
				new_nfa.setName(id.getValue());
				nfa_list.add(new_nfa);
				System.out.println("     [Parser] ID finished: " + id.getValue());
			}
		} while(input.gotoNextLine());
		
		System.out.println();
		System.out.println("     [Parser] GRAMMAR PARSE: DONE");
		System.out.println("     [Parser] NFA MERGE: START");
		System.out.println();
		
		System.out.println("     [Parser] merging...");
		System.out.println("     [Parser] char class?");
		NFA big_nfa = new NFA();
		for(int i = 0; i < nfa_list.size(); i++) {
			System.out.println("     [Parser]\t" + i + ": " + nfa_list.get(i).getCharClass());
			if(!nfa_list.get(i).getCharClass()) {
				System.out.println("     [Parser] merging #" + i + " with final");
				big_nfa.merge(nfa_list.get(i).getNFA());
			}
		}
		big_nfa.finalize();
		
		System.out.println();
		System.out.print(big_nfa.toString());
		
		System.out.println();
		System.out.println("     [Parser] NFA MERGE: DONE");
		System.out.println("     [Parser] NFA CONVERSION: START");
		System.out.println();
		
		DFA dfa = new DFA(big_nfa);
		System.out.println(dfa.toString());
		
		System.out.println();
		System.out.println("     [Parser] NFA CONVERSION: DONE");
		System.out.println("     [Parser] CODE PARSING: START");
		System.out.println();
		
		for(int i = 1; i < args.length; i++) {
			String file = args[i];
			System.out.print("     [Parser] " + file + ": ");
			boolean pass= true;
			
			Scanner scan = new Scanner(new File(args[i]));
			while(scan.hasNext() && pass) {
				String t = scan.next();
				for(int j = 0; j < t.length(); j++) {
					pass = dfa.gotoNext(t.charAt(j));
					if(!pass) {
						break;
					}
				}
				if(pass) {
					pass = dfa.atFinal();
				}
				dfa.reset();
			}
			
			if(pass) {
				System.out.println(" PASS");
			}
			else {
				System.out.println(" FAIL");
			}
		}
		
		System.out.println();
		System.out.println("     [Parser] CODE PARSING: DONE");
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
