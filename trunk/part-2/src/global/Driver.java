package global;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import interpreter.Interpreter;
import generator.parser.LL1;

/**
 * 
 */
public class Driver {
	
	/**
	 * 
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		String spec_file = "minire-specification-NEW.txt";
		
		System.out.println("Generating Scanner/Parser...\n");
		LL1 parser = null;
		try {
			parser = new LL1(spec_file);
			System.out.println("==========\nPARSE TABLE\n==========");
			System.out.println(parser.getPt());
		}
		catch(ParseException pe) {
			System.out.println("Error in Scanner/Parser Generation...");
			System.out.println(pe);
			System.exit(1);
		}
		catch(IOException ioe) {
			System.out.println("Error in Scanner/Parser Generation...");
			System.out.println(ioe);
			System.exit(1);
		}
		
		System.out.println("Scanner/Parser Generation DONE!\n");
		System.out.println("Running Interpreter...\n");
		try {
			Interpreter interpreter = new Interpreter(parser);
			
			String next_file = new String();
			do {
				System.out.println("input name of script file... (\"quit\" to exit)");
				System.out.print(">>");
				next_file = scan.next();
				//terminating condition
				if(next_file.equals("quit")) {
					System.out.print("really quit? (y/n) ");
					if(scan.next().equals("y")) {
						break;
					}
				}
				else {
					try {
						interpreter.run(next_file);
					}
					catch(FileNotFoundException fnfe) {
						System.out.println("file: \"" + next_file + "\" does not exist...");
					}
				}
			}
			while(true);
			
			//interpreter.run("test/oneline/find.txt");
		}
		catch(ParseException pe) {
			System.out.println("Error running Interpreter...\n");
			System.out.println(pe);
			System.exit(1);
		}
		catch(IOException ioe) {
			System.out.println("Error in Scanner/Parser Generation...");
			System.out.println(ioe);
			System.exit(1);
		}
		
		System.out.println("\nInterpreter DONE!\n");
		System.exit(0);
	}
}
