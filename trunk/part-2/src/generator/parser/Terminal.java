package generator.parser;

import java.util.ArrayList;

import global.Token;
/**
 *
 */
public class Terminal extends LL1_Token {

	public Terminal(Token<LL1_TokenType> token) {
		super(token);
		this.addToFirstSet(this);
	}
	
	
	
}
