package generator.regex;

import generator.regex.Token;
import generator.regex.TokenType;

/**
 * Lexer.java
 * Generates tokens from a given input string
 */

public class Regex_Lexer {
	
	public static final char NULL_CHAR = '\u0000';
	
	private String input;
	private int current_pos;
	private boolean peek;
	private Token current;
	
	/**
	 * setup lexer for given scanner input
	 * @param input scanner to tokenize from
	 */
	public Regex_Lexer(String input) {
		this.input = input;
		this.current_pos = 0;
		this.peek = false;
		this.current = null;
	}
	
	/**
	 * accessor for the current line being scanned
	 * @return the current position in the current line
	 */
	public int getPosition() {
		return this.current_pos;
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
			return current;
		}
		else {
			current = getNextToken();
			peek = true;
			return current;
		}
	}
	
	/**
	 * make a new token from the stream
	 * @return new token
	 */
	public Token makeNewToken() {
		char t = this.getNextChar();
		//if there isn't a token left
		if(t == NULL_CHAR) {
			//pass end of stream token
			return new Token(TokenType.EOF, null);
		}
		Token result = null;
		
		switch(t) {
			//ignore whitespace
			case '\t':
			case ' ':
				return makeNewToken();
			//handle possible line returns
			case '\n':
				result = new Token(TokenType.EOL, "\n");
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
			//escaped characters
			case '\\':
				String escaped = "\\" + this.getNextChar();
				result = new Token(TokenType.LITERAL, escaped);
				break;
			//everything else (character literals)
			default:
				result = new Token(TokenType.LITERAL, ((Character)t).toString());
		}
		
		return result;
	}
	
	/**
	 * get next character in the input
	 * @return next char in the input
	 */
	public char getNextChar() {
		//if we've reached the end of the input
		if(this.current_pos >= input.length()) {
			//return empty character
			return NULL_CHAR;
		}
		else {
			//return next character
			return this.input.charAt(current_pos++);
		}
	}
}
