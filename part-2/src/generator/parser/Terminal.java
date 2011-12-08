package generator.parser;

import java.util.ArrayList;

import global.Token;
/**
 *
 */
public class Terminal extends LL1_Token {
	private Token<LL1_TokenType> token;
	private ArrayList<Token<LL1_TokenType>> FirstSet;
	
	/**
	 * 
	 * @param token
	 */
	public Terminal(Token<LL1_TokenType> token){
		this.token = token;
		this.FirstSet = new ArrayList<Token<LL1_TokenType>>();
	}
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFirstSet(Token<LL1_TokenType> tok){
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

	public ArrayList<Token<LL1_TokenType>> getFirstSet() {
		return FirstSet;
	}

	public void setFirstSet(ArrayList<Token<LL1_TokenType>> firstSet) {
		FirstSet = firstSet;
	}
	
	
	
}
