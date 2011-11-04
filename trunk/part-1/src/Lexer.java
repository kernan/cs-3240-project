/** 
 * Generates tokens from a buffered input stream.
 * 
 * @author Robert Kernan
 *
 */

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Lexer {
	
	private InputBuffer input_stream;
	private boolean peek;
	private Token current;
	
	/**
	 * setup lexer for given scanner input
	 * @param input scanner to tokenize from
	 */
	public Lexer(Scanner scanner) {
		this.input_stream = new InputBuffer(scanner);
		this.peek = false;
		this.current = null;
	}

	/**
	 * get the next token in the stream
	 * @return next token in stream
	 */
	public Token getNextToken() {
		if(peek) {
			peek = false;
			return current;
		}
		else {
			current = makeNewToken();
			return current;
		}
	}
	
	/**
	 * peek at the next token in the stream
	 * @return next token in the stream
	 */
	public Token peekNextToken() {
		if(peek) {
			current = makeNewToken();
			return current;
		}
		else {
			peek = true;
			return getNextToken();
		}
	}
	
	/**
	 * make a new token from the stream
	 * @return new token
	 */
	public Token makeNewToken() {
		char t = input_stream.getNext();
		
		Token result = null;
		
		switch(t) {
			//ignore comment lines
			case '%':
				if(input_stream.peekNext() == '%') {
					input_stream.gotoNextLine();
					result = new Token(TokenType.EOL, "\n");
				}
				else {
					result = new Token(TokenType.LITERAL, "%");
				}
				break;
			//defined name
			case '$':
				String name = new String();
				while(input_stream.peekNext() != ' ') {
					name += input_stream.getNext();
				}
				result = new Token(TokenType.DEFINED, name);
				break;
			//alternation
			case '|':
				result = new Token(TokenType.UNION, "|");
				break;
			//repetition >= 0
			case '*':
				result = new Token(TokenType.KLEENE, "*");
				break;
			//repetition > 0
			case '+':
				result = new Token(TokenType.PLUS, "+");
				break;
			//dash (used when defining a range)
			case '-':
				result = new Token(TokenType.DASH, "-");
				break;
			//caret (exclude set)
			case '^':
				result = new Token(TokenType.CARET, "^");
				break;
			//dot (wild card)
			case '.':
				result = new Token(TokenType.DOT, ".");
				break;
			//left bracket
			case '[':
				result = new Token(TokenType.LBRACKET, "[");
				break;
			//right bracket
			case ']':
				result = new Token(TokenType.RBRACKET, "]");
				break;
			//left parentheses (scope out)
			case '(':
				result = new Token(TokenType.LPAREN, "(");
				break;
			//right parentheses (scope in)
			case ')':
				result = new Token(TokenType.RPAREN, ")");
				break;
			//IN (for defining ranges)
			case 'I':
				if(input_stream.peekNext() == 'N') {
					result = new Token(TokenType.IN, "IN");
				}
				else {
					result = new Token(TokenType.LITERAL, "L");
				}
				break;
			//everything else (character literals)
			default:
				result = new Token(TokenType.LITERAL, new String() + t);
		}
		
		return result;
	}
	
	/**
	 * moves the stream to the next line of the file
	 * @return true: there is another line, false: end of file
	 */
	public boolean gotoNextLine() {
		return input_stream.gotoNextLine();
	}
}
