package global;

import java.text.ParseException;

/**
 * Lexer.java
 * abstract lexer with functionality to get positions and generate tokens
 * @param <E> defines the generated token type
 */
public abstract class Lexer<E> {
	
	public static final char NULL_CHAR = '\u0000';
	
	protected boolean peek;
	protected E current;
	
	/**
	 * initialize a Lexer with no current token
	 */
	public Lexer() {
		this.current = null;
		this.peek = false;
	}
	
	/**
	 * get current integer position in the stream
	 * @return current position in the stream
	 */
	public abstract int getPosition();
	
	/**
	 * generate next token from the stream
	 * @return new token from the stream
	 * @throws ParseException thrown on lexer errors
	 */
	protected abstract E makeNewToken() throws ParseException;
	
	/**
	 * consume the next token in the stream
	 * @return next token in the stream
	 * @throws ParseException thrown by makeNewToken
	 */
	public E getNextToken() throws ParseException {
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
	 * @throws ParseException thrown by makeNewToken
	 */
	public E peekNextToken() throws ParseException {
		if(peek) {
			return current;
		}
		else {
			current = makeNewToken();
			peek = true;
			return current;
		}
	}
}
