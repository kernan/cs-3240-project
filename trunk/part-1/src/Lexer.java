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
	public Token getNextToken() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenType peekNextToken() {
		char a = stream.peekNext();
		return makeToken(a).getType();
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private Token makeToken(char c) {
		switch(c) {
			TokenType type;
			case '|':
				type = TokenType.OR;
				break;
			case '*':
				type = TokenType.MULTIPLY;
				break;
			case '+':
				type = TokenType.PLUS;
				break;
			case '$':
				type = TokenType.DOLLAR;
				break;
			case '(':
				type = TokenType.LPAREN;
				break;
			case ')':
				type = TokenType.RPAREN;
				break;
			case '[':
				type = TokenType.LBRACKET;
				break;
			case ']':
				type = TokenType.RBRACKET;
				break;
			default:
				type = TokenType.IDENTIFIER;
		}
	}
}
