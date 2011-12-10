package global;

/**
 * 
 */
public class Driver {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Generating Scanner/Parser...");
		try {
			//TODO generate LL1 for the grammar
		}
		catch(Exception e) {
			System.out.println("Error in Scanner/Parser Generation...");
			System.out.println(e.getMessage());
		}
		System.out.println("Scanner/Parser Generation DONE!");
		System.out.println("Running Interpreter...");
		try {
			//TODO make an interpreter using LL1 and DFA scanner
			//TODO run the interpreter on the given script
		}
		catch(Exception e) {
			System.out.println("Error running Interpreter...");
			System.out.println(e.getMessage());
		}
		System.out.println("Interpreter DONE!");
	}
}
