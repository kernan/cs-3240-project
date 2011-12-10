package generator.regex;

import generator.regex.Regex_TokenType;
import global.Lexer;
import global.Token;

/**
 * Lexer.java
 * Generates tokens from a given input string
 */
public class Regex_Lexer extends Lexer<Token<Regex_TokenType>> {
	
	private String input;
	private int current_pos;
	
	/**
	 * setup lexer for given scanner input
	 * @param input scanner to tokenize from
	 */
	public Regex_Lexer(String input) {
		super();
		this.input = input;
		this.current_pos = 0;
	}
	
	/**
	 * accessor for the current line being scanned
	 * @return the current position in the current line
	 */
	@Override
	public int getPosition() {
		if(peek) {
			return this.current_pos-1;
		}
		else {
			return this.current_pos;
		}
	}
	
	/**
	 * make a new token from the stream
	 * @return new token
	 */
	@Override
	protected Token<Regex_TokenType> makeNewToken() {
		char t = this.getNextChar();
		//if there isn't a token left
		if(t == NULL_CHAR) {
			//pass end of stream token
			return new Token<Regex_TokenType>(Regex_TokenType.EOF, null);
		}
		Token<Regex_TokenType> result = null;
		
		switch(t) {
			//ignore whitespace
			case '\t':
			case ' ':
				return makeNewToken();
			//handle possible line returns
			case '\n':
				result = new Token<Regex_TokenType>(Regex_TokenType.EOL, "\n");
				break;
			case '^':
				result = new Token<Regex_TokenType>(Regex_TokenType.CARET, "^");
				break;
			//alternation
			case '|':
				result = new Token<Regex_TokenType>(Regex_TokenType.UNION, "|");
				break;
			//repetition >= 0
			case '*':
				result = new Token<Regex_TokenType>(Regex_TokenType.KLEENE, "*");
				break;
			//repetition > 0
			case '+':
				result = new Token<Regex_TokenType>(Regex_TokenType.PLUS, "+");
				break;
			//dash (used when defining a range)
			case '-':
				result = new Token<Regex_TokenType>(Regex_TokenType.DASH, "-");
				break;
			//dot (wild card)
			case '.':
				result = new Token<Regex_TokenType>(Regex_TokenType.DOT, ".");
				break;
			//left bracket
			case '[':
				result = new Token<Regex_TokenType>(Regex_TokenType.LBRACKET, "[");
				break;
			//right bracket
			case ']':
				result = new Token<Regex_TokenType>(Regex_TokenType.RBRACKET, "]");
				break;
			//left parentheses (scope out)
			case '(':
				result = new Token<Regex_TokenType>(Regex_TokenType.LPAREN, "(");
				break;
			//right parentheses (scope in)
			case ')':
				result = new Token<Regex_TokenType>(Regex_TokenType.RPAREN, ")");
				break;
			//escaped characters
			case '\\':
				String escaped = "\\" + this.getNextChar();
				result = new Token<Regex_TokenType>(Regex_TokenType.LITERAL, escaped);
				break;
			//literal
			default:
				result = new Token<Regex_TokenType>(Regex_TokenType.LITERAL, ((Character)t).toString());
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
