package generator.parser;

import global.Token;

import java.util.ArrayList;
/**
 * 
 *
 */
public class NonTerminal extends LL1_Token {
	private Token<LL1_TokenType> token;
	private ArrayList<Token<LL1_TokenType>> FirstSet;
	private ArrayList<Token<LL1_TokenType>> FollowSet;
	
	/**
	 * 
	 * @param token
	 */
	public NonTerminal(Token<LL1_TokenType> token){
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
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFollowSet(Token<LL1_TokenType> tok){
		FollowSet.add(tok);	
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
	public ArrayList<Token<LL1_TokenType>> getFollowSet() {
		return FollowSet;
	}
	public void setFollowSet(ArrayList<Token<LL1_TokenType>> followSet) {
		FollowSet = followSet;
	}
	
	
	
	
	
	
}
