
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class Parser {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		//tokenizer
		Lexer input = new Lexer(new Scanner(new File(args[0])));
		//list of all <rexp> (as nfas)
		ArrayList<NFA_Identifier> nfa_list = new ArrayList<NFA_Identifier>();
		
		do {
			Token id = input.getNextToken();
			System.out.println("[Parser] got ID: " + id.getValue());
			if(id.getType() != TokenType.DEFINED) {
				throw new ParseException("Invalid syntax, new lines must begin with a id definition", -1);
			}
			RecursiveDescent rd = new RecursiveDescent(input, nfa_list);
			NFA_Identifier new_nfa = rd.descend();
			new_nfa.setName(id.getValue());
			nfa_list.add(new_nfa);
		} while(input.gotoNextLine());
		
		System.out.println("[Parser] merging...");
		
		NFA big_nfa = new NFA();
		for(int i = 0; i < nfa_list.size(); i++) {
			System.out.println("\t" + i + ": " + nfa_list.get(i).getCharClass());
			if(!nfa_list.get(i).getCharClass()) {
				System.out.println("[Parser] merging with #" + i);
				big_nfa.merge(nfa_list.get(i).getNFA());
			}
		}
	}
}
