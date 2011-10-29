/** 
 * Generates tokens from a buffered input stream.
 * 
 * @author Robert Kernan
 *
 */

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public abstract class Lexer {
	
	protected InputBuffer stream;
	
	/**
	 * setup new Lexer for given file
	 * @param filename name of file to scan
	 * @throws IOException thrown by java.util.Scanner if file does not exist
	 */
	public Lexer(String filename) throws IOException {
		Scanner input = new Scanner(new File(filename));
		this.stream = new InputBuffer(input);
	}
	
	/**
	 * get the next token in the input stream
	 * @return next token in input stream
	 */
	public abstract Token getNextToken();
	
	/**
	 * determines if given character is a digit [0-9]
	 * @param t character to check
	 * @return true: is a digit, false: is not
	 */
	protected boolean isDigit(char t) {
		return (t >= '0' && t <= '0');
	}
	
	/**
	 * determines if given character is a letter [a-z] or [A-Z]
	 * @param t character to checks
	 * @return true: is a letter, false: is not
	 */
	protected boolean isLetter(char t) {
		return ((t >= 'A' && t <= 'Z') || (t >= 'a' && t <= 'z'));
	}
}
