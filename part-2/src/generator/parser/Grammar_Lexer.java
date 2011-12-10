package generator.parser;

import java.io.FileNotFoundException;
import java.text.ParseException;

import global.InputBuffer;
import global.Lexer;
import global.Token;

/**
 * Grammar_Lexer.java
 * has capability to scan and generate tokens for a specification grammar
 */
public class Grammar_Lexer extends Lexer<Token<LL1_TokenType>> {
	
	private InputBuffer input_stream;
	private boolean done;
	
	/**
	 * initialize a grammar lexer for a given file
	 * @param filename file to scan
	 */
	public Grammar_Lexer(String filename) throws FileNotFoundException {
		super();
		this.done = false;
		this.input_stream = new InputBuffer(filename);
	}
	
	/**
	 * generate the next token in the file
	 * @return next token
	 * @throws ParseException thrown on lexer errors
	 */
	protected Token<LL1_TokenType> makeNewToken() throws ParseException {
		Token<LL1_TokenType> result = null;
		
		char t = this.input_stream.getNext();
		
		//check for end of file
		if(this.done) {
			return new Token<LL1_TokenType>(LL1_TokenType.EOF, "EOF");
		}
		
		//check to see if we've FINISHED the final line of the spec 
		if(!this.input_stream.hasNext() && t == '\n') {
			this.done = true;
		}
		
		switch(t) {
			//ignore spaces
			case ' ':
			case '\t':
				return makeNewToken();
			//move buffer on new lines
			case '\n':
				result = new Token<LL1_TokenType>(LL1_TokenType.EOL, "EOL");
				this.input_stream.gotoNextLine();
				break;
			//generate headers
			case '%':
				if(this.input_stream.peekNext() == '%') {
					this.input_stream.getNext();
					while(this.isWhitespace(this.input_stream.peekNext())) {
						this.input_stream.getNext();
					}
					String header = new String();
					char temp = this.input_stream.peekNext();
					while(temp != '\n' && temp != ' ' && temp != '\t') {
						header += this.input_stream.getNext();
						temp = this.input_stream.peekNext();
					}
					this.input_stream.gotoNextLine();
					result = new Token<LL1_TokenType>(LL1_TokenType.HEADER, header);
				}
				else {
					//TODO is '%' a valid character for anything else?
					throw new ParseException("Specification Lexical ERROR: symbol: \'" + t + "\' can only appear at beginning of HEADER token, line: " +
							this.getLine() + ", pos: " + this.getPosition(), this.getPosition());
				}
				break;
			//generate non-terminals
			case '<':
				String non_term = new String();
				while(this.input_stream.peekNext() != '>') {
					if(this.input_stream.peekNext() == '\n') {
						throw new ParseException("Specification Lexical ERROR: newline found before end of non-terminal: \'" +
								non_term + "\', line: " +this.getLine() + ", pos: " + this.getPosition(), this.getPosition());
					}
					non_term += this.input_stream.getNext();
				}
				this.input_stream.getNext();
				result = new Token<LL1_TokenType>(LL1_TokenType.NON_TERMINAL, non_term);
				break;
			//generate terminals
			default:
				String terminal = new String();
				terminal += t;
				while(!this.isWhitespace(this.input_stream.peekNext()) && this.input_stream.peekNext() != '\n') {
					terminal += this.input_stream.getNext();
				}
				result = new Token<LL1_TokenType>(LL1_TokenType.TERMINAL, terminal);
		}
		System.out.println("generated: type = " + result.getType() + ", val = " + result.getValue());
		return result;
	}

	/**
	 * determines if current token is whitespace
	 * @param t token to check
	 * @return true: given token is whitespace, false: it is not
	 */
	private boolean isWhitespace(char t) {
		return t == ' ' || t == '\t';
	}

	/**
	 * accessor for the current position being scanner
	 * @return current position in the current line
	 */
	public int getPosition() {
		return this.input_stream.getPosition();
	}
	
	/**
	 * accessor for the current line being scanned
	 * @return current line
	 */
	public int getLine() {
		return this.input_stream.getLine();
	}
}
