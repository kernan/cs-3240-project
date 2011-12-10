package generator.parser;

import java.io.FileNotFoundException;

import global.InputBuffer;
import global.Lexer;
import global.Token;

/**
 * Grammar_Lexer.java
 * has capability to scan and generate tokens for a specification grammar
 */
public class Grammar_Lexer extends Lexer<Token<LL1_TokenType>> {
	
	private InputBuffer input_stream;
	
	/**
	 * initialize a grammar lexer for a given file
	 * @param filename file to scan
	 */
	public Grammar_Lexer(String filename) throws FileNotFoundException {
		super();
		try {
			this.input_stream = new InputBuffer(filename);
		}
		catch(FileNotFoundException fnfe) {
			//TODO proper error handling
			throw fnfe;
		}
	}
	
	/**
	 * generate the next token in the file
	 * @return next token
	 */
	protected Token<LL1_TokenType> makeNewToken() {
		Token<LL1_TokenType> result = null;
		
		//check if there is any input left
		if(!this.input_stream.hasNext()) {
			//if not, return end of file
			return new Token<LL1_TokenType>(LL1_TokenType.EOF, null);
		}
		
		char t = this.input_stream.getNext();
		
		switch(t) {
			//TODO handle special token types
			//ignore spaces
			case ' ':
			case '\t':
				return makeNewToken();
			//move buffer on new lines
			case '\n':
				result = new Token<LL1_TokenType>(LL1_TokenType.EOL, "\n");
				input_stream.gotoNextLine();
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
				}
				break;
			//generate non-terminals
			case '<':
				String non_term = new String();
				while(this.input_stream.peekNext() != '>') {
					if(this.input_stream.peekNext() == '\n') {
						//TODO handle error situation
					}
					non_term += this.input_stream.getNext();
				}
				this.input_stream.getNext();
				result = new Token<LL1_TokenType>(LL1_TokenType.NON_TERMINAL, non_term);
				break;
			//generate terminals
			default:
				//TODO handle cases where there might not be spaces?
				String terminal = new String();
				terminal += t;
				while(!this.isWhitespace(this.input_stream.peekNext()) && this.input_stream.peekNext() != '\n') {
					terminal += this.input_stream.getNext();
				}
				result = new Token<LL1_TokenType>(LL1_TokenType.TERMINAL, terminal);
		}
		
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
