package generator.parser;

import global.Token;

/**
 *
 */
public class Terminal extends LL1_Token {

	/**
	 * 
	 * @param token
	 */
	public Terminal(Token<LL1_TokenType> token) {
		super(token);
		this.addToFirstSet(this);
	}
}
