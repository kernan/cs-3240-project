package generator.parser;

import global.Token;

import java.util.ArrayList;

public class LL1_Token {
	private Token<LL1_TokenType> token;
	private ArrayList<Terminal> FirstSet;
	
	/**
	 * 
	 * @param token
	 */
	public LL1_Token(Token<LL1_TokenType> token){
		this.token = token;
		this.FirstSet = new ArrayList<Terminal>();
	}
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFirstSet(Terminal tok){
		FirstSet.add(tok);	
	}

	
	
/* Accessors and Mutators	
 */
	
	public Token<LL1_TokenType> getToken() {
		return token;
	}

	public void setToken(Token<LL1_TokenType> token) {
		this.token = token;
	}

	public ArrayList<Terminal> getFirstSet() {
		return FirstSet;
	}

	public void setFirstSet(ArrayList<Terminal> firstSet) {
		FirstSet = firstSet;
	}
	
}
