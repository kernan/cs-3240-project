
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
			if(id.getType() != TokenType.DEFINED) {
				throw new ParseException("Invalid syntax, new lines must begin with a id definition", -1);
			}
			RecursiveDescent rd = new RecursiveDescent(input, nfa_list);
			NFA_Identifier new_nfa = rd.descend();
			new_nfa.setName(id.getValue());
			nfa_list.add(new_nfa);
		} while(input.gotoNextLine());
		
		NFA big_nfa = new NFA();
		for(int i = 0; i < nfa_list.size(); i++) {
			if(nfa_list.get(i).getCharClass()) {
				big_nfa.merge(nfa_list.get(i).getNFA());
			}
		}
	}
}