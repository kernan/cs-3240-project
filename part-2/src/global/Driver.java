package global;

import generator.parser.LL1;

/**
 * 
 */
public class Driver {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String spec_file = "minire-specification-NEW.txt";
		
		System.out.println("Generating Scanner/Parser...\n");
		try {
			//TODO generate LL1 for the grammar
			LL1 parser = new LL1(spec_file);
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
	}
}
