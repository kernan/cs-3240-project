package global;

import java.io.FileNotFoundException;
import java.text.ParseException;

import generator.parser.LL1;
import generator.parser.Script_Lexer;

/**
 * 
 */
public class Driver {
	
	/**
	 * 
	 * @param args
	 * @throws ParseException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
		String spec_file = "minire-specification-NEW.txt";
		
		System.out.println("Generating Scanner/Parser...\n");
		LL1 parser = null;
		try {
			//TODO generate LL1 for the grammar
			parser = new LL1(spec_file);
			System.out.println(parser.getPt());
		}
		catch(Exception e) {
			System.out.println("Error in Scanner/Parser Generation...");
			System.out.println(e.getMessage());
		}
		System.out.println("Scanner/Parser Generation DONE!\n");
		System.out.println("Running Interpreter...\n");
		try {
			//TODO make an interpreter using LL1 and DFA scanner
			//TODO run the interpreter on the given script
		}
		catch(Exception e) {
			System.out.println("Error running Interpreter...\n");
			System.out.println(e.getMessage());
		}
		System.out.println("Interpreter DONE!\n");
		
		Script_Lexer lextest = new Script_Lexer("example/minire_test_script.txt", parser.getPt().getTerminals());
		while(!lextest.peekNextToken().getType().equals("EOF")) {
			System.out.println(lextest.getNextToken());
		}
	}
}
