package global;


public abstract class Lexer<E> {
	
	public static final char NULL_CHAR = '\u0000';
	
	protected boolean peek;
	protected Token<E> current;
	
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
	 */
	public abstract Token<E> makeNewToken();
	
	/**
	 * consume the next token in the stream
	 * @return next token in the stream
	 */
	public Token<E> getNextToken() {
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
	public Token<E> peekNextToken() {
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
