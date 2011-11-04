
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
		ArrayList<NFA> nfa_list = new ArrayList<NFA>();
		
		do {
			Token id = input.getNextToken();
			if(id.getType() != TokenType.DEFINED) {
				throw new ParseException("Invalid syntax, new lines must begin with a id definition", -1);
			}
			RecursiveDescent rd = new RecursiveDescend(input, nfa_list);
			NFA new_nfa = rd.descend();
			nfa_list.add(new_nfa);
		} while(input.gotoNextLine());
	}
}